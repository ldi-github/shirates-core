package shirates.core.macro

import shirates.core.utility.misc.ReflectionUtility
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class MacroEntry(
    val nickname: String,
    val clazz: KClass<*>,
    val function: KFunction<*>
) {

    /**
     * call
     */
    fun call(vararg args: Any?): Any? {

        val map = ReflectionUtility.getCallingMap(clazz = clazz, function = function, args = args)
        return function.callBy(map)
    }
}