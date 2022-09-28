package macro.ios

import shirates.core.driver.TestDriver.it
import shirates.core.driver.commandextension.pressHome
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object iOSMacro {

    @Macro("[iOS Home Screen]")
    fun iOSHomeScreen() {

        it.pressHome()
            .pressHome()
    }

}