package shirates.core.macro

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ClassScanUtility
import shirates.core.utility.toPath
import java.nio.file.Path
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

/**
 * MacroRepository
 */
object MacroRepository {

    internal var isInitialized = false

    /**
     * macroObjectEntries
     */
    val macroObjectEntries = mutableMapOf<KClass<*>, Int>()

    /**
     * macroEntries
     */
    val macroEntries = mutableMapOf<String, MacroEntry>()

    /**
     * clera
     */
    fun clear() {

        isInitialized = false
        macroObjectEntries.clear()
        macroEntries.clear()
    }

    /**
     * register
     */
    fun register(kclass: KClass<*>) {

        macroObjectEntries[kclass] = 1

        TestLog.info("Registering macro. (${kclass.qualifiedName})")

        for (f in kclass.memberFunctions) {
            val a = f.annotations.firstOrNull() { it is Macro } as Macro?
            if (a != null) {
                if (macroEntries.containsKey(a.macroName)) {
                    throw TestConfigException(message(id = "macroNameDuplicated", subject = a.macroName))
                }
                macroEntries[a.macroName] = MacroEntry(nickname = a.macroName, clazz = kclass, function = f)
            }
        }
    }

    /**
     * setup
     */
    fun setup(scanDir: Path = PropertiesManager.macroObjectScanDir.toPath()) {

        if (isInitialized) {
            return
        }

        findAndRegisterAll(scanDir = scanDir)

        isInitialized = true
    }

    /**
     * findAndRegisterAll
     */
    fun findAndRegisterAll(scanDir: Path) {

        clear()

        val targetAnnotation = MacroObject::class
        TestLog.info("Scanning macro under '$scanDir'")

        val targetClasses =
            ClassScanUtility.findAnnotatedClasses(scanDir = scanDir, targetAnnotation = targetAnnotation)
        for (c in targetClasses) {
            register(c)
        }
    }

    /**
     * hasMacro
     */
    fun hasMacro(macroName: String): Boolean {

        return macroEntries.containsKey(macroName)
    }

    /**
     * getMacroEntry
     */
    fun getMacroEntry(macroName: String): MacroEntry {

        if (macroEntries.containsKey(macroName).not()) {
            throw TestConfigException(message(id = "macroNotRegistered", subject = macroName))
        }

        return macroEntries[macroName]!!
    }

    /**
     * call
     */
    fun call(macroName: String, vararg args: Any?) {

        val macroEntry = getMacroEntry(macroName = macroName)
        macroEntry.call(args = args)
    }

}