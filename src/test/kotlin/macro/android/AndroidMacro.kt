package macro.android

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ifCanSelectNot
import shirates.core.driver.commandextension.*
import shirates.core.driver.platformMajorVersion
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject
import shirates.core.utility.appium.getCapabilityRelaxed
import shirates.core.utility.misc.ShellUtility
import shirates.core.vision.visionScope

@MacroObject
object AndroidMacro : TestDrive {

    @Macro("[Android Home Screen]")
    fun androidHomeScreen() {

        if (TestMode.isClassicTest) {
            it.pressHome()
                .pressHome()
        } else {
            visionScope {
                AndroidMacro.it.pressHome()
                    .pressHome()
            }
        }
    }

    @Macro("[Airplane mode On]")
    fun airplaneModeON() {

        android {
            it.pressHome()
                .pressHome()
            it.flickTopToBottom(startMarginRatio = 0.0)
                .flickTopToBottom()

            if (platformMajorVersion == 11) {
                it.select("#quick_settings_container")
                    .flickCenterToLeft()
            }

            if (canSelect("@Airplane mode")) {
                if (it.isChecked.not()) {
                    it.tap()
                }
                it.select("@Airplane mode")
                    .checkIsON()
            }
        }
    }

    @Macro("[Airplane mode Off]")
    fun airplaneModeOFF() {

        android {
            it.pressHome()
                .pressHome()
            it.flickTopToBottom(startMarginRatio = 0.0)
                .flickTopToBottom()

            if (platformMajorVersion == 11) {
                it.select("#quick_settings_container")
                    .flickCenterToLeft()
            }

            if (canSelect("@Airplane mode")) {
                if (it.isChecked) {
                    it.tap()
                }
                it.select("@Airplane mode")
                    .checkIsOFF()
            }
        }
    }

    @Macro("setLanguage")
    fun setLanguage(language: String, locale: String) {

        if (isAndroid) {
            val udid = TestDriver.capabilities.getCapabilityRelaxed("udid")
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