package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ifCanSelectNot
import shirates.core.driver.branchextension.ifStringIs
import shirates.core.driver.commandextension.*
import shirates.core.driver.platformVersion
import shirates.core.driver.viewport
import shirates.core.exception.TestConfigException
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.utility.getUdid
import shirates.core.utility.misc.ShellUtility

@MacroObject
object AndroidMacro : TestDrive {

    @Macro("[Android Home Screen]")
    fun androidHomeScreen() {

        it.pressHome()
            .pressHome()
    }

    @Macro("[Airplane mode On]")
    fun airplaneModeON() {

        android {
            it.pressHome()
                .pressHome()
            it.swipePointToPoint(
                startX = 20,
                startY = 10,
                endX = 20,
                endY = viewport.bottom
            )
            if ((platformVersion.toIntOrNull() ?: 0) < 12) {
                throw TestConfigException("Use android 12 or greater")
            }
            it.swipePointToPoint(
                startX = 20,
                startY = 10,
                endX = 20,
                endY = viewport.bottom
            )
            it.select("@Airplane mode")
                .text
                .ifStringIs("Off") {
                    it.tap()
                }
            it.select("@Airplane mode")
                .textIs("On")
            it.pressHome()
        }
    }

    @Macro("[Airplane mode Off]")
    fun airplaneModeOFF() {

        android {
            it.pressHome()
                .pressHome()
            it.swipePointToPoint(
                startX = 20,
                startY = 10,
                endX = 20,
                endY = viewport.bottom
            )
            if ((platformVersion.toIntOrNull() ?: 0) < 12) {
                throw TestConfigException("Use android 12 or greater")
            }
            it.swipePointToPoint(
                startX = 20,
                startY = 10,
                endX = 20,
                endY = viewport.bottom
            )

            it.select("@Airplane mode")
                .text
                .ifStringIs("On") {
                    it.tap()
                }
            it.select("@Airplane mode")
                .textIs("Off")
            it.pressHome()
        }

    }

    @Macro("setLanguage")
    fun setLanguage(language: String, locale: String) {

        if (isAndroid) {
            val udid = TestDriver.capabilities.getUdid()
            val result = ShellUtility.executeCommand(
                "adb", "-s", udid, "shell", "am", "start", "-a", "android.settings.LOCALE_SETTINGS"
            )
            println(result.resultString)
            it.refreshCache()

            val languageLocale = "$language ($locale)"

            if (it.select("1").sibling("#com.android.settings:id/label").access == languageLocale) {
                return
            }

            if (canSelect(languageLocale)) {
                it.rightImage()
                    .swipeToTop()
                return
            }

            it.tap("#add_language")
                .tap("#android:id/locale_search_menu")
                .sendKeys(language)
                .tapWithScrollDown("#android:id/locale&&" + language)
            ifCanSelectNot(languageLocale) {
                it.tapWithScrollDown("#android:id/locale&&" + locale)
            }

            it.select(languageLocale)
                .rightImage("#dragHandle")
                .swipeToTop()
        }
    }
}