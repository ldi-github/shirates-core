package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.isScreen
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.terminateApp
import shirates.core.driver.testContext
import shirates.core.driver.waitForDisplay
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.vision.driver.commandextension.isScreen
import shirates.core.vision.driver.commandextension.launchApp
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.terminateApp
import shirates.core.vision.driver.waitForDisplay
import shirates.core.vision.visionScope

@MacroObject
object FilesMacro : TestDrive {

    @Macro("[Restart Files]")
    fun restartFiles() {

        it.terminateApp("com.google.android.documentsui")
            .launchApp("Files")
    }

    @Macro("[Files Top Screen]")
    fun mapsTopScreen() {

        if (testContext.useCache) {
            if (it.isScreen("[Files Top Screen]")) {
                return
            }

            it.terminateApp("com.google.android.documentsui")
                .launchApp("Files")
                .waitForDisplay("Downloads")

            if (it.isScreen("[Files Top Screen]")) {
                return
            }

            restartFiles()

            it.screenIs("[Files Top Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Files Top Screen]")) {
                    return@visionScope
                }

                it.terminateApp("com.google.android.documentsui")
                    .launchApp("Files")
                    .waitForDisplay("Downloads")

                if (it.isScreen("[Files Top Screen]")) {
                    return@visionScope
                }

                restartFiles()

                it.screenIs("[Files Top Screen]")
            }
        }
    }
}