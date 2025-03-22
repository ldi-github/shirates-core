package macro.android

import ifCanDetect
import shirates.core.driver.TestDrive
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.visionScope

@MacroObject
object PlayStoreMacro : TestDrive {

    @Macro("[Play Store Screen]")
    fun playStoreScreen() {

        if (TestMode.isClassicTest) {
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
        } else {
            visionScope {
                it.ifCanDetect("Meet the Search tab") {
                    it.tapCenterOfScreen()
                }

                if (it.isScreen("[Play Store Screen]")) {
                    return@visionScope
                }

                it.launchApp("Play Store")
                it.ifCanDetect("Not now") {
                    it.tap()
                }
                it.ifCanDetect("Meet the Search tab") {
                    val e = vision.rootElement
                    it.tap(x = e.bounds.centerX, y = e.bounds.height / 4)
                }

                it.screenIs("[Play Store Screen]")
            }
        }
    }

}