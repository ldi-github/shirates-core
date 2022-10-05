package macro

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.tap
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object MacroObject1 : TestDrive {

    @Macro("[Network preferences Screen]")
    fun internetScreen() {

        it.tap("Network & internet")
            .tap("Internet")
            .tap("Network preferences")
    }

}