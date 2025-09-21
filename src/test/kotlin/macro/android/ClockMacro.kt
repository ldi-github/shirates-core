package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.vision.driver.commandextension.isScreen
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tap
import shirates.core.vision.driver.commandextension.tapLast
import shirates.core.vision.driver.isApp
import shirates.core.vision.visionScope

@MacroObject
object ClockMacro : TestDrive {

    @Macro("[Restart Clock]")
    fun restartClock() {

        it.terminateApp("com.google.android.deskclock")
            .launchApp("com.google.android.deskclock")
    }

    @Macro("[Alarms Screen]")
    fun alarmsScreen() {

        if (TestMode.isClassicTest) {
            if (isApp("[Clock]").not()) {
                restartClock()
            }
            if (isScreen("[Alarms Screen]").not()) {
                it.tap("[Alarms Tab]")
            }
            it.screenIs("[Alarms Screen]")
        } else {
            visionScope {
                if (it.isApp("[Clock]").not()) {
                    restartClock()
                }
                if (it.isScreen("[Alarms Screen]").not()) {
                    it.tapLast("Alarm")
                }
                it.screenIs("[Alarms Screen]")
            }
        }
    }

    @Macro("[Timers Screen]")
    fun timersScreen() {

        if (TestMode.isClassicTest) {
            if (it.isApp("[Clock]").not()) {
                restartClock()
            }
            if (it.isScreen("[Timers Screen]").not()) {
                it.tap("[Timers]")
            }
            it.screenIs("[Timers Screen]")
        } else {
            visionScope {
                if (it.isApp("[Clock]").not()) {
                    restartClock()
                }
                if (it.isScreen("[Timers Screen]").not()) {
                    it.tap("Timers")
                }
                it.screenIs("[Timers Screen]")
            }
        }
    }

}