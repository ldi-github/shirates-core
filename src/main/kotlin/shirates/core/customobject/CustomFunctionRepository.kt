package shirates.core.customobject

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ClassScanUtility
import shirates.core.utility.toPath
import kotlin.reflect.KClass
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

object CustomFunctionRepository {

    val classList = mutableListOf<KClass<*>>()
    val functionMap = mutableMapOf<String, CustomFunctionEntry>()

    internal var isInitialized = false

    /**
     * clear
     */
    fun clear() {

        isInitialized = false
        classList.clear()
        functionMap.clear()
    }

    /**
     * initialize
     */
    fun initialize() {

        TestLog.trace()

        if (isInitialized) {
            return
        }

        clear()
        findAndRegisterAll()

        isInitialized = true
    }

    /**
     * findAndRegisterAll
     */
    fun findAndRegisterAll() {

        val classes = ClassScanUtility.findAnnotatedClasses(
            scanDir = PropertiesManager.customObjectScanDir.toPath(),
            targetAnnotation = CustomObject::class
        )
        for (c in classes) {
            classList.add(c)

            val functions = c.functions.filter { it.hasAnnotation<CustomFunction>() }
            for (f in functions) {
                if (functionMap.containsKey(f.name)) {
                    throw TestConfigException(message(id = "customFunctionNameDuplicated", subject = f.name))
                }
                functionMap[f.name] = CustomFunctionEntry(functionName = f.name, clazz = c, function = f)
            }
        }
    }

    /**
     * hasFunction
     */
    fun hasFunction(functionName: String): Boolean {

        return functionMap.containsKey(functionName)
    }

    /**
     * getFunction
     */
    fun getFunction(functionName: String): CustomFunctionEntry {

        if (functionMap.containsKey(functionName).not()) {
            throw TestConfigException(message(id = "customFunctionNotRegistered", subject = functionName))
        }

        return functionMap[functionName]!!
    }

    /**
     * call
     */
    fun call(functionName: String, vararg args: Any?): Any? {

        TestLog.info("Calling custom function $functionName")

        val functionEntry = getFunction(functionName = functionName)
        return functionEntry.call(args = args)
    }

}