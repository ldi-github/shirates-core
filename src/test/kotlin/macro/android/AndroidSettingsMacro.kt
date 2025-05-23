package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.TestMode
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
//                    .screenIs("[Android Settings Search Screen]")
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

        if (TestMode.isClassicTest) {
            if (it.isScreen("[Internet Screen]")) {
                it.flickAndGoUpTurbo()
                return
            }

            androidSettingsTopScreen()
            it.tap("Network & internet")
                .screenIs("[Network & internet Screen]")
            it.tap("Internet")
                .screenIs("[Internet Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Internet Screen]")) {
                    it.flickAndGoUpTurbo()
                    return@visionScope
                }

                androidSettingsTopScreen()
                it.tap("Network & internet")
                    .screenIs("[Network & internet Screen]")
                it.tap("Internet")
                    .screenIs("[Internet Screen]")
            }
        }
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

        if (TestMode.isClassicTest) {
            if (it.isScreen("[Battery Screen]")) {
                it.flickAndGoUpTurbo()
                return
            }

            androidSettingsTopScreen()
            it.tapWithScrollDown("Battery")
                .screenIs("[Battery Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Battery Screen]")) {
                    it.flickAndGoUpTurbo()
                    return@visionScope
                }

                androidSettingsTopScreen()
                it.tapWithScrollDown("Battery")
                    .screenIs("[Battery Screen]")
            }
        }
    }

    @Macro("[Wallpaper & style Screen]")
    fun wallpaperAndStyleScreen() {

        if (TestMode.isClassicTest) {
            if (it.isScreen("[Wallpaper & style Screen]")) {
                return
            }

            androidSettingsTopScreen()
            it.tapWithScrollDown("[Wallpaper & style]")
                .screenIs("[Wallpaper & style Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Wallpaper & style Screen]")) {
                    return@visionScope
                }

                androidSettingsTopScreen()
                it.tapWithScrollDown("[Wallpaper & style]")
                    .screenIs("[Wallpaper & style Screen]")
            }
        }
    }

    @Macro("[Accessibility Screen]")
    fun accessibilityScreen() {

        if (TestMode.isClassicTest) {
            if (it.isScreen("[Accessibility Screen]")) {
                it.flickAndGoUpTurbo()
                return
            }

            androidSettingsTopScreen()
            it.tapWithScrollDown("[Accessibility]")
                .screenIs("[Accessibility Screen]")
        } else {
            visionScope {
                if (it.isScreen("[Accessibility Screen]")) {
                    it.flickAndGoUpTurbo()
                    return@visionScope
                }

                androidSettingsTopScreen()
                it.tapWithScrollDown("[Accessibility]")
                    .screenIs("[Accessibility Screen]")
            }
        }
    }

    @Macro("[System Screen]")
    fun systemScreen() {

        if (TestMode.isClassicTest) {
            if (it.isScreen("[System Screen]")) {
                it.flickAndGoUpTurbo()
            }

            androidSettingsTopScreen()
            it.flickAndGoDownTurbo()
                .tapWithScrollDown("[System]")
                .screenIs("[System Screen]")
        } else {
            visionScope {
                if (it.isScreen("[System Screen]")) {
                    it.flickAndGoUpTurbo()
                }

                androidSettingsTopScreen()
                it.flickAndGoDownTurbo()
                    .tapWithScrollDown("[System]")
                    .screenIs("[System Screen]")
            }
        }
    }

    @Macro("[Developer options Screen]")
    fun developerOptionsScreen() {

        if (TestMode.isClassicTest) {
            systemScreen()
            it.tapWithScrollDown("Developer options")
                .screenIs("[Developer options Screen]")
        } else {
            visionScope {
                systemScreen()
                it.tapWithScrollDown("Developer options")
                    .screenIs("[Developer options Screen]")
            }
        }
    }

}