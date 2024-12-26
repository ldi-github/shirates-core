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
            it.flickAndGoUpTurbo()
            return
        }

        it.restartApp()
            .launchApp("Settings")
            .screenIs("[Android Settings Top Screen]")

        if (canSelect("[Account Avatar]").not()) {
            it.flickAndGoUp()
        }
        syncCache(force = true)
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
            it.flickAndGoUpTurbo()
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollUp("Network & internet")
            .screenIs("[Network & internet Screen]")
    }

    @Macro("[Internet Screen]")
    fun internetScreen() {

        if (it.isScreen("[Internet Screen]")) {
            it.flickAndGoUpTurbo()
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
            it.flickAndGoUpTurbo()
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollDown("Connected devices")
            .screenIs("[Connected devices Screen]")
    }

    @Macro("[Battery Screen]")
    fun batteryScreen() {

        if (it.isScreen("[Battery Screen]")) {
            it.flickAndGoUpTurbo()
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollDown("Battery")
            .screenIs("[Battery Screen]")
    }

    @Macro("[Wallpaper & style Screen]")
    fun wallpaperAndStyleScreen() {

        if (it.isScreen("[Wallpaper & style Screen]")) {
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollDown("[Wallpaper & style]")
            .screenIs("[Wallpaper & style Screen]")
    }

    @Macro("[Accessibility Screen]")
    fun accessibilityScreen() {

        if (it.isScreen("[Accessibility Screen]")) {
            it.flickAndGoUpTurbo()
            return
        }

        androidSettingsTopScreen()
        it.tapWithScrollDown("[Accessibility]")
            .screenIs("[Accessibility Screen]")
    }

    @Macro("[System Screen]")
    fun systemScreen() {

        if (it.isScreen("[System Screen]")) {
            it.flickAndGoUpTurbo()
        }

        androidSettingsTopScreen()
        it.flickAndGoDownTurbo()
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