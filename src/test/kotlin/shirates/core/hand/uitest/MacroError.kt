package shirates.core.hand.uitest

import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object MacroError {

    @Macro(macroName = "[Null Pointer Exception]")
    fun nullPointerException() {

        var p: Int? = null
        p!!
    }
}