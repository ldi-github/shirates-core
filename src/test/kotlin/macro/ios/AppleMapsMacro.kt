package macro.ios

import ifCanDetect
import shirates.core.driver.TestDrive
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.driver.wait
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tap
import shirates.core.vision.visionScope

@MacroObject
object AppleMapsMacro : TestDrive {

    @Macro("[Apple Maps Top Screen]")
    fun appleMapsToScreen() {

        if (TestMode.isClassicTest) {
            launchApp("Maps")
            wait(5)
            ifCanSelect("Close") {
                it.tap()
            }
            ifCanSelect("Close") {
                it.tap()
            }
            it.screenIs("[Apple Maps Top Screen]")
        } else {
            it.visionScope {
                launchApp("Maps")
                wait(5)
                it.ifCanDetect("Close") {
                    it.tap()
                }
                it.ifCanDetect("Close") {
                    it.tap()
                }
                it.screenIs("[Apple Maps Top Screen]")
            }
        }
    }

}