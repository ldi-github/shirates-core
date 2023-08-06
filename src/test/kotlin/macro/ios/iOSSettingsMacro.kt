package macro.ios

import shirates.core.driver.TestDriver.it
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object iOSSettingsMacro {

    @Macro("[iOS Settings Top Screen]")
    fun iosSettingsTopScreen() {

        it.refreshCache()

        if (it.isScreen("[iOS Settings Top Screen]")) {
            if (it.canSelect("General").not()) {
                it.flickAndGoUp()
            }
            return
        }

        it.restartApp("[Settings]")
            .screenIs("[iOS Settings Top Screen]")
    }

    @Macro("[Developer Screen]")
    fun developerScreen() {

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
    }

    @Macro("[General Screen]")
    fun settingsGeneralScreen() {

        it.refreshCache()

        if (it.isScreen("[General Screen]")) {
            return
        }

        iosSettingsTopScreen()
        it.tapWithScrollDown("General")
            .screenIs("[General Screen]")
    }

    @Macro("[Keyboards Screen]")
    fun iosKeyboardScreen() {

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
    }

    @Macro("[Language & Region Screen]")
    fun languageAndRegionScreen() {

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
    }
}