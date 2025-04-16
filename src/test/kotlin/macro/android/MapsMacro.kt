package macro.android

import ifCanDetect
import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.driver.waitForDisplay
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.waitForDisplay
import shirates.core.vision.visionScope

@MacroObject
object MapsMacro : TestDrive {

    @Macro("[Maps Top Screen]")
    fun mapsTopScreen() {

        if (testContext.useCache) {
            if (it.isScreen("[Maps Top Screen]")) {
                return
            }

            it.terminateApp("com.google.android.apps.maps")
                .launchApp("Maps")
                .ifCanSelect("*to send you notifications?") {
                    it.tap("Allow")
                }
                .ifCanSelect("Make it your map") {
                    it.tap("SKIP")
                }
                .waitForDisplay("#map_frame")

            if (it.isScreen("[Maps Top Screen]")) {
                return
            }

            it.tapCenterOfScreen()
            if (it.isScreen("[Maps Top Screen]")) {
                return
            }

            it.tapCenterOfScreen()
            if (it.isScreen("[Maps Top Screen]")) {
                return
            }

            it.screenIs("[Maps Top Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Maps Top Screen]")) {
                    return@visionScope
                }

                it.terminateApp("com.google.android.apps.maps")
                    .launchApp("Maps")
                    .ifCanDetect("*to send you notifications?") {
                        it.tap("Allow")
                    }
                    .ifCanDetect("Make it your map") {
                        it.tap("SKIP")
                    }
                    .waitForDisplay("Restaurants")

                if (it.canDetect("Restaurants")) {
                    return@visionScope
                }

                it.tapCenterOfScreen()
                if (it.canDetect("Restaurants")) {
                    return@visionScope
                }
                it.tapCenterOfScreen()
                if (it.canDetect("Restaurants")) {
                    return@visionScope
                }

                it.screenIs("[Maps Top Screen]")
            }
        }
    }
}