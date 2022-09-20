package shirates.core.unittest.customobject.testdata.data1

import shirates.core.customobject.CustomFunction
import shirates.core.customobject.CustomObject

@CustomObject
object CustomObject1 {

    @CustomFunction
    fun function1A(): String? {

        return null
    }

    @CustomFunction
    fun function1B(arg: String): String {

        return arg
    }

    @CustomFunction
    fun function1C(args: List<String>): String {

        return args.joinToString(",")
    }

    @CustomFunction
    fun function1D(vararg arg: Any?): String {

        return arg.joinToString(",")
    }

    @CustomFunction
    fun function1E(arg1: String, arg2: List<Any?>, vararg arg3: Any?): String {

        if (arg3.isEmpty()) {
            return listOf(arg1, arg2).joinToString(",")
        }

        return listOf(arg1, arg2, arg3.joinToString(",")).joinToString(",")
    }
}