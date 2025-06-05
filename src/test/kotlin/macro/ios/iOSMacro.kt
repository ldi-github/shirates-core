package macro.ios

import shirates.core.driver.TestDriver.it
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.pressHome
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.vision.driver.commandextension.pressHome
import shirates.core.vision.visionScope

@MacroObject
object iOSMacro {

    @Macro("[iOS Home Screen]")
    fun iOSHomeScreen() {

        if (TestMode.isClassicTest) {
            it.pressHome()
                .pressHome()
        } else {
            it.visionScope {
                it.pressHome()
                    .pressHome()
            }
        }
    }

}