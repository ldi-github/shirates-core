package macro.ios

import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriver.it
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.visionScope

@MacroObject
object iOSSettingsMacro {

    @Macro("[iOS Settings Top Screen]")
    fun iosSettingsTopScreen() {

        if (TestMode.isClassicTest) {
            it.refreshCache()

            if (it.isScreen("[iOS Settings Top Screen]")) {
                if (it.canSelect("General").not()) {
                    it.flickAndGoUp()
                }
                return
            }

            it.restartApp("[Settings]")
                .screenIs("[iOS Settings Top Screen]")
        } else {
            it.visionScope {
                if (it.isScreen("[iOS Settings Top Screen]")) {
                    if (it.canDetect("General").not()) {
                        it.flickAndGoUp()
                    }
                    return@visionScope
                }

                it.restartApp("[Settings]")
                    .screenIs("[iOS Settings Top Screen]")

            }
        }
    }

    @Macro("[Developer Screen]")
    fun developerScreen() {

        if (TestMode.isClassicTest) {
            it.refreshCache()

            if (it.isScreen("[Developer Screen]")) {
                if (it.canSelect("Settings")) {
                    return
                }
                it.flickTopToBottom()
                if (it.canSelect("Settings")) {
                    return
                }
            }

            iosSettingsTopScreen()
            it.tapWithScrollDown("Developer")
                .screenIs("[Developer Screen]")
        } else {
            it.visionScope {
                if (it.isScreen("[Developer Screen]")) {
                    if (TestDriver.it.canSelect("Settings")) {
                        return@visionScope
                    }
                    it.flickTopToBottom()
                    if (it.canDetect("Settings")) {
                        return@visionScope
                    }
                }

                iosSettingsTopScreen()
                it.tapWithScrollDown("Developer")
                    .screenIs("[Developer Screen]")
            }
        }
    }

    @Macro("[General Screen]")
    fun settingsGeneralScreen() {

        if (TestMode.isClassicTest) {
            it.refreshCache()

            if (it.isScreen("[General Screen]")) {
                return
            }

            iosSettingsTopScreen()
            it.tapWithScrollDown("General")
                .screenIs("[General Screen]")
        } else {
            it.visionScope {
                if (it.isScreen("[General Screen]")) {
                    return@visionScope
                }

                iosSettingsTopScreen()
                it.tapWithScrollDown("General")
                    .screenIs("[General Screen]")
            }
        }
    }

    @Macro("[Keyboards Screen]")
    fun iosKeyboardScreen() {

        if (TestMode.isClassicTest) {
            it.refreshCache()

            if (it.isScreen("[Keyboards Screen]")) {
                if (it.canSelect(".XCUIElementTypeCell&&Keyboards")) {
                    return
                }
                it.flickTopToBottom()
                if (it.canSelect(".XCUIElementTypeCell&&Keyboards")) {
                    return
                }
            }

            settingsGeneralScreen()
            it.tapWithScrollDown("Keyboard")
                .screenIs("[Keyboards Screen]")
        } else {
            it.visionScope {
                if (it.isScreen("[Keyboards Screen]")) {
                    if (it.canDetect("Text Replacement")) {
                        return@visionScope
                    }
                }

                settingsGeneralScreen()
                it.tapWithScrollDown("Keyboard")
                    .screenIs("[Keyboards Screen]")
            }
        }
    }

    @Macro("[Language & Region Screen]")
    fun languageAndRegionScreen() {

        if (TestMode.isClassicTest) {
            it.refreshCache()

            if (it.isScreen("[Language & Region Screen]")) {
                if (it.canSelect(".XCUIElementTypeOther&&PREFERRED LANGUAGES")) {
                    return
                }
                it.flickTopToBottom()
                if (it.canSelect(".XCUIElementTypeOther&&PREFERRED LANGUAGES")) {
                    return
                }
            }

            settingsGeneralScreen()
            it.tap("General")
            it.tap("Language & Region")
                .screenIs("[Language & Region Screen]")
        } else {
            it.visionScope {
                if (it.isScreen("[Language & Region Screen]")) {
                    if (it.canDetect("PREFERRED LANGUAGES")) {
                        return@visionScope
                    }
                }

                settingsGeneralScreen()
                it.tap("General")
                it.tap("Language & Region")
                    .screenIs("[Language & Region Screen]")
            }
        }
    }
}