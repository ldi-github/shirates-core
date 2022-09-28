package macro.android

import shirates.core.logging.TestLog
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object TestMacro {

    @Macro("[Test Macro 1]")
    fun testMacro1() {

        TestLog.info("[Test Macro 1] called.")
    }

    @Macro("[Test Macro 2]")
    fun testMacro2(arg1: String, arg2: Int) {

        TestLog.info("[Test Macro 2] called. (arg1=$arg1, arg2=$arg2)")
    }
}