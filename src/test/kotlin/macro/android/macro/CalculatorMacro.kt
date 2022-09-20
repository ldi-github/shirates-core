package macro.android.macro

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object CalculatorMacro : TestDrive {

    @Macro("[Restart Calculator]")
    fun restartCalculator() {

        it.terminateApp("com.google.android.calculator")
            .tapAppIcon("Calculator")
            .screenIs("[Calculator Main Screen]")
    }

    @Macro("[Calculator Main Screen]")
    fun calculatorMainScreen() {

        it.refreshCache()

        if (it.isScreen("[Calculator Main Screen]")) {
            return
        }

        it.restartApp()
        if (it.isScreen("[Calculator Main Screen]")) {
            return
        }

        restartCalculator()
    }
}