package macro.ios

import shirates.core.driver.TestDriver.it
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object SafariMacro {

    @Macro("[Safari Screen]")
    fun safariScreen() {

        it.refreshCache()

        if (it.isScreen("[Safari Screen]")) {
            return
        }

        it.launchApp("Safari")
        if (it.canSelect("Cancel")) {
            it.tap()
        }
        it.screenIs("[Safari Screen]")
    }

    @Macro("[Safari Search Screen]")
    fun safariSearchScreen() {

        it.refreshCache()

        if (it.isScreen("[Safari Search Screen]")) {
            return
        }

        safariScreen()

        if (it.isScreen("[Safari Search Screen]")) {
            return
        }

        it.tap("[Address]")
        if (it.canSelect("[Clear text]")) {
            it.tap()
        }
        it.screenIs("[Safari Search Screen]")
    }
}