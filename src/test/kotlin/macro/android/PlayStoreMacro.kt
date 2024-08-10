package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object PlayStoreMacro : TestDrive {

    @Macro("[Play Store Screen]")
    fun playStoreScreen() {

        it.refreshCache()

        ifCanSelect("Meet the Search tab") {
            tapCenterOfScreen()
        }

        if (it.isScreen("[Play Store Screen]")) {
            return
        }

        it.launchApp("Play Store")
        ifCanSelect("Meet the Search tab") {
            tapCenterOfScreen()
        }

        it.screenIs("[Play Store Screen]")
    }

}