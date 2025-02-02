package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.syncScreen
import shirates.core.vision.visionScope

@MacroObject
object AndroidSettingsMacro : TestDrive {

    @Macro("[Android Settings Top Screen]")
    fun androidSettingsTopScreen() {

        if (testContext.useCache) {
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
        } else {
            visionScope {
                if (it.isScreen("[Android Settings Top Screen]")) {
                    it.flickAndGoUpTurbo()
                    return@visionScope
                }

                it.restartApp()
                    .launchApp("Settings")
                    .screenIs("[Android Settings Top Screen]")

                it.flickAndGoUp()
                it.syncScreen()
            }
        }
    }

    @Macro("[Android Settings Search Screen]")
    fun androidSearchScreen() {

        if (testContext.useCache) {
            if (it.isScreen("[Android Settings Search Screen]")) {
                return
            }
            androidSettingsTopScreen()
            it.tap("[Search settings]")
                .screenIs("[Android Settings Search Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Android Settings Search Screen]")) {
                    return@visionScope
                }
                androidSettingsTopScreen()
                it.tap("Search settings")
                    .screenIs("[Android Settings Search Screen]")
            }
        }
    }

    @Macro("[Network & internet Screen]")
    fun networkAndInternetScreen() {

        if (testContext.useCache) {
            if (it.isScreen("[Network & internet Screen]")) {
                it.flickAndGoUpTurbo()
                return
            }

            androidSettingsTopScreen()
            it.tapWithScrollUp("Network & internet")
                .screenIs("[Network & internet Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Network & internet Screen]")) {
                    it.flickAndGoUpTurbo()
                    return@visionScope
                }

                androidSettingsTopScreen()
                it.tapWithScrollUp("Network & internet")
                    .screenIs("[Network & internet Screen]")
            }
        }
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

        if (testContext.useCache) {
            if (it.isScreen("[Connected devices Screen]")) {
                it.flickAndGoUpTurbo()
                return
            }

            androidSettingsTopScreen()
            it.tapWithScrollDown("Connected devices")
                .screenIs("[Connected devices Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Connected devices Screen]")) {
                    it.flickAndGoUpTurbo()
                    return@visionScope
                }

                androidSettingsTopScreen()
                it.tapWithScrollDown("Connected devices")
                    .screenIs("[Connected devices Screen]")
            }
        }
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