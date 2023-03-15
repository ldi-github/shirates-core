package macro.ios

import shirates.core.driver.TestDriver.it
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object RemindersMacro {

    @Macro("[Reminders Top Screen]")
    fun iosRemindersTopScreen() {

        it.refreshCache()

        if (it.isApp("[Reminders]").not()) {
            it.launchApp("Reminders")
        }

        if (it.isScreen("[Reminders Top Screen]")) {
            return
        }

        if (it.canSelect("Welcome to Reminders")) {
            it.tap("Continue")
                .wait()
            if (it.canSelect("Continue")) {
                it.tap()
            }
        }

        if (it.isScreen("[Reminders Top Screen]")) {
            return
        }

        if (it.canSelect("#Cancel")) {
            it.tap()
        }
        if (it.canSelect("Back")) {
            it.tap()
        }
        it.screenIs("[Reminders Top Screen]")
    }

}