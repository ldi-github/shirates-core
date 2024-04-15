package macro.ios

import okhttp3.internal.wait
import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object AppleMapsMacro : TestDrive {

    @Macro("[Apple Maps Top Screen]")
    fun appleMapsToScreen() {

        it.refreshCache()

        if (canSelect(".XCUIElementTypeButton&&Close")) {
            it.tap()
        }
        if (canSelect("Cancel")) {
            it.tap()
        }
        if (isScreen("[Apple Maps Top Screen]")) {
            return
        }

        it.pressHome()
            .pressHome()
            .launchApp("Maps")

        if (canSelect("Not Now")) {
            it.tap()
        }
        if (canSelect("Cancel")) {
            it.tap()
        }
        if (canSelect("Close")) {
            it.tap()
        }
        if (canSelect("Dismiss")) {
            it.tap()
        }
        if (canSelect("What’s New in Maps")) {
            it.tap("Continue")
            it.wait()
        }
        if (canSelect("Allow While Using App")) {
            it.tap()
        }
        it.screenIs("[Apple Maps Top Screen]")
    }

}