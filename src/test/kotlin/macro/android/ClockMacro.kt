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

    @Macro("[Alarm Screen]")
    fun alarmScreen() {

        if (TestMode.isClassicTest) {
            if (isApp("[Clock]").not()) {
                restartClock()
            }
            if (isScreen("[Alarm Screen]").not()) {
                it.tap("[Alarm Tab]")
            }
            it.screenIs("[Alarm Screen]")
        } else {
            visionScope {
                if (it.isApp("[Clock]").not()) {
                    restartClock()
                }
                if (it.isScreen("[Alarm Screen]").not()) {
                    it.tapLast("Alarm")
                }
                it.screenIs("[Alarm Screen]")
            }
        }
    }

    @Macro("[Timer Screen]")
    fun timerScreen() {

        if (TestMode.isClassicTest) {
            if (it.isApp("[Clock]").not()) {
                restartClock()
            }
            if (it.isScreen("[Timer Screen]").not()) {
                it.tap("[Timer Tab]")
            }
            it.screenIs("[Timer Screen]")
        } else {
            visionScope {
                if (it.isApp("[Clock]").not()) {
                    restartClock()
                }
                if (it.isScreen("[Timer Screen]").not()) {
                    it.tap("[Timer Tab]")
                }
                it.screenIs("[Timer Screen]")
            }
        }
    }

}