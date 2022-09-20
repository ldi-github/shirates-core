package macro.ios.macro

import okhttp3.internal.wait
import shirates.core.driver.TestDriver.it
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object AppleMapsMacro {

    @Macro("[Apple Maps Top Screen]")
    fun appleMapsToScreen() {

        it.refreshCache()

        if (it.isScreen("[Apple Maps Top Screen]")) {
            return
        }

        it.pressHome()
            .pressHome()
            .tapAppIcon("Maps")

        if (it.canSelect("Cancel")) {
            it.tap()
        }
        if (it.canSelect("Close")) {
            it.tap()
        }
        if (it.canSelect("What’s New in Maps")) {
            it.tap("Continue")
            it.wait()
        }
        if (it.canSelect("Allow While Using App")) {
            it.tap()
        }
        it.screenIs("[Apple Maps Top Screen]")
    }

}