package macro.ios

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.driver.wait
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object AppleMapsMacro : TestDrive {

    @Macro("[Apple Maps Top Screen]")
    fun appleMapsToScreen() {

        launchApp("Maps")
        wait(5)
        ifCanSelect("Close") {
            it.tap()
        }
        ifCanSelect("Close") {
            it.tap()
        }
        it.screenIs("[Apple Maps Top Screen]")
    }

}