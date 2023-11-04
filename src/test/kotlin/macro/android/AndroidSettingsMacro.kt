package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object AndroidSettingsMacro : TestDrive {

    @Macro("[Android Settings Top Screen]")
    fun androidSettingsTopScreen() {

        if (it.isScreen("[Android Settings Top Screen]")) {
            it.selectWithScrollUp("Network & internet")
            return
        }

        it.restartApp()
            .launchApp("Settings")
            .screenIs("[Android Settings Top Screen]")

        if (canSelect("[Account Avatar]").not()) {
            it.flickAndGoUp()
        }
    }

    @Macro("[Android Settings Search Screen]")
    fun androidSearchScreen() {

        if (it.isScreen("[Android Settings Search Screen]")) {
            return
        }

        androidSettingsTopScreen()
        it.tap("[Search settings]")
            .screenIs("[Android Settings Search Screen]")
    }

    @Macro("[Network & internet Screen]")
    fun networkAndInternetScreen() {

        if (it.isScreen("[Network & internet Screen]")) {
            it.flickTopToBottom()
            return
        }

        androidSettingsTopScreen()
        it.tap("Network & internet")
            .screenIs("[Network & internet Screen]")
    }

    @Macro("[Internet Screen]")
    fun internetScreen() {

        if (it.isScreen("[Internet Screen]")) {
            it.flickTopToBottom()
            return
        }

        androidSettingsTopScreen()
        it.tap("Network & internet")
            .screenIs("[Network & internet Screen]")
        it.tap("Internet")
            .screenIs("[Internet Screen]")
    }

    @Macro("[Connected devices Screen]")
    fun connectedDevicesScreen() {

        if (it.isScreen("[Connected devices Screen]")) {
            it.flickTopToBottom()
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollDown("Connected devices")
            .screenIs("[Connected devices Screen]")
    }

    @Macro("[Battery Screen]")
    fun batteryScreen() {

        if (it.isScreen("[Battery Screen]")) {
            it.flickTopToBottom()
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollDown("Battery")
            .screenIs("[Battery Screen]")
    }

    @Macro("[Wallpaper Screen]")
    fun wallpaperScreen() {

        if (it.isScreen("[Wallpaper Screen]")) {
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollDown("[Wallpaper & style]")
            .screenIs("[Wallpaper Screen]")
    }

    @Macro("[Accessibility Screen]")
    fun accessibilityScreen() {

        if (it.isScreen("[Accessibility Screen]")) {
            it.flickTopToBottom()
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollDown("[Accessibility]")
            .screenIs("[Accessibility Screen]")
    }

    @Macro("[System Screen]")
    fun systemScreen() {

        if (it.isScreen("[System Screen]")) {
            it.flickTopToBottom()
        }

        androidSettingsTopScreen()
        it.flickBottomToTop()
            .tapWithScrollDown("[System]")
            .screenIs("[System Screen]")
    }

    @Macro("[Developer options Screen]")
    fun developerOptionsScreen() {

        systemScreen()
        it.tapWithScrollDown("Developer options")
            .screenIs("[Developer options Screen]")
    }

}