package shirates.core.utility.misc

import org.junit.platform.commons.util.ReflectionUtils
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties

/**
 * ReflectionUtility
 */
object ReflectionUtility {

    /**
     * getString
     */
    fun getString(obj: Any, propertyName: String): String {

        val prop = obj::class.memberProperties.firstOrNull() { it.name == propertyName }
            ?: throw IllegalArgumentException("property not found. (propertyName=$propertyName)")
        val value = prop.getter.call(obj)?.toString() ?: ""
        return value
    }

    /**
     * getStringOrNull
     */
    fun getStringOrNull(obj: Any, propertyName: String): String? {

        val prop = obj::class.memberProperties.firstOrNull() { it.name == propertyName }
            ?: throw IllegalArgumentException("property not found. (propertyName=$propertyName)")
        val value = prop.getter.call(obj)?.toString()
        return value
    }

    /**
     * setValue
     */
    fun setValue(obj: Any, propertyName: String, value: Any?) {

        val pinfo = obj::class.memberProperties.filterIsInstance<KMutableProperty<*>>()
            .firstOrNull() { it.name == propertyName }
        if (pinfo != null) {
            pinfo.setter.call(obj, value)
        } else {
            TestLog.warn(message(id = "notFound", subject = "Property", value = propertyName))
        }
    }

    /**
     * getPrivateFieldValue
     */
    fun getPrivateFieldValue(obj: Any, fieldName: String): Any {

        val f = obj.javaClass.getDeclaredField(fieldName)
        f.isAccessible = true
        val value = f.get(obj)
        return value
    }

    /**
     * getPID
     */
    fun getPID(process: Process): Int {

        val value = getPrivateFieldValue(process, "pid")
        return value as Int
    }

    /**
     * findAllClassesInPackage
     */
    fun findAllClassesInPackage(packageName: String): List<Class<*>> {

        val classes = ReflectionUtils.findAllClassesInPackage(packageName, { _ -> true }, { _ -> true })
        return classes
    }

    /**
     * getCallingMap
     */
    fun getCallingMap(
        clazz: KClass<*>,
        function: KFunction<*>,
        vararg args: Any?
    ): MutableMap<KParameter, Any?> {

        val map = mutableMapOf<KParameter, Any?>()

        val fifo = mutableListOf<Any?>()
        for (a in args) {
            fifo.add(a)
        }

        val list = mutableListOf<Any?>()
        for (p in function.parameters) {
            if (p.type.toString() == clazz.qualifiedName) {
                list.add(clazz.objectInstance)
            } else {
                if (p.isVararg) {
                    list.add(fifo.toTypedArray())
                    fifo.clear()
                } else {
                    if (fifo.any()) {
                        val value = fifo[0]
                        list.add(value)
                        fifo.removeAt(0)
                    } else {
                        list.add(null)
                    }
                }
            }
        }

        for (p in function.parameters) {
            val value = list[p.index]
            if (value != null || !p.isOptional) {
                map[p] = value
            }
        }
        return map
    }
}