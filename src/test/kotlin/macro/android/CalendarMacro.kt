package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object CalendarMacro : TestDrive {

    @Macro("[Restart Calendar]")
    fun restartCalendar() {

        it.terminateApp("com.google.android.calendar")
            .launchApp("Calendar")
        skipWelcomeScreen()
    }

    @Macro("[Skip Welcome Screen")
    fun skipWelcomeScreen() {

        ifScreenIs("[Welcome Screen]") {
            it.tap("[>]")
        }
        ifScreenIs("[Tips Screen]") {
            it.tap("[Got it]")
        }
    }

    @Macro("[Calendar Week Screen]")
    fun calendarWeekScreen() {

        it.restartApp()

        if (isScreen("[Calendar Week Screen]")) {
            return
        }
        ifScreenIs("[Welcome Screen]") {
            skipWelcomeScreen()
        }
        if (it.canSelect("@Show Calendar List and Settings drawer").not()) {
            restartCalendar()
        }

        if (it.canSelect("It's now easier to block spam")) {
            it.tap("Got it")
        }
        it.tap("@Show Calendar List and Settings drawer")
            .tap("Week")

        it.screenIsOf("[Calendar Week Screen]")
    }
}