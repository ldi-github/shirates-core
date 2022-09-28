package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object MapsMacro : TestDrive {

    @Macro("[Maps Top Screen]")
    fun mapsTopScreen() {

        if (it.isScreen("[Maps Top Screen]")) {
            return
        }

        it.terminateApp("com.google.android.apps.maps")
            .tapAppIcon("Maps")
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
    }
}