package macro.ios

import shirates.core.driver.TestDriver.it
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object SearchMacro {

    @Macro("[iOS Search Screen]")
    fun iosSearchScreen() {

        it.refreshCache()

        if (it.isScreen("[iOS Search Screen]")) {
            return
        }
        it.restartApp()
            .pressHome()
            .flickCenterToBottom()
            .screenIs("[iOS Search Screen]")
    }

}