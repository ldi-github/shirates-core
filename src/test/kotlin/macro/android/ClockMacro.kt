package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object ClockMacro : TestDrive {

    @Macro("[Restart Clock]")
    fun restartClock() {

        it.terminateApp("com.google.android.deskclock")
            .launchApp("Clock")
    }

    @Macro("[Alarm Screen]")
    fun alarmScreen() {

        if (isApp("[Clock]").not()) {
            restartClock()
        }
        if (isScreen("[Alarm Screen]").not()) {
            it.tap("[Alarm Tab]")
        }
        it.screenIs("[Alarm Screen]")
    }

    @Macro("[Timer Screen]")
    fun timerScreen() {

        if (isApp("[Clock]").not()) {
            restartClock()
        }
        if (isScreen("[Timer Screen]").not()) {
            it.tap("[Timer Tab]")
        }
        it.screenIs("[Timer Screen]")
    }

}