package shirates.core.driver.befavior

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.branchextension.ifCanSelectNot
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.driver.testProfile
import shirates.core.utility.getStringOrEmpty
import shirates.core.utility.ios.IosDeviceUtility
import shirates.core.utility.misc.ShellUtility

object LanguageSettingsHelper : TestDrive {

    /**
     * setLanguageOnAndroid
     */
    fun setLanguageOnAndroid() {

        val locale = testContext.profile.capabilities.getStringOrEmpty("locale")

        it.pressHome()
            .pressHome()

        val clock = it.select("#clock")
        val isJapaneseClock = clock.text.contains("日")
        val hasMissMatch = locale == "JP" && isJapaneseClock.not() || locale != "JP" && isJapaneseClock
        if (hasMissMatch) {
            val udid = testProfile.udid
            val args =
                "adb -s $udid shell am start -a android.settings.LOCALE_SETTINGS".split(" ").toTypedArray()
            ShellUtility.executeCommand(args = args)

            if (locale == "JP") {
                ifCanSelectNot("*日本語*") {
                    it.tap("#add_language")
                        .tap("#android:id/locale_search_menu")
                        .sendKeys("ja")
                    ifCanSelect("*日本語*") {
                        it.tap()
                    }
                }
                it.select("<*日本語*>:right(#dragHandle)")
                    .swipeVerticalTo(endY = 0)
            } else {
                ifCanSelectNot("English (United States)") {
                    it.tap("#add_language")
                        .tap("#android:id/locale_search_menu")
                        .sendKeys("English (United")
                    ifCanSelect("English (United States)") {
                        it.tap()
                    }
                }
                it.select("<English (United States)>:right(#dragHandle)")
                    .swipeVerticalTo(endY = 0)
            }
        }
        it.pressHome()
    }

    /**
     * setLanguageOnIos
     */
    fun setLanguageOnIos() {

        val capabilityLange = testProfile.capabilities.getStringOrEmpty("language")
        val capabilityLocale = testProfile.capabilities.getStringOrEmpty("locale")
        val targetLocale = "$capabilityLange-$capabilityLocale"

        it.pressHome()
        val searchElement = it.select("<#spotlight-pill>:descendant(.XCUIElementTypeStaticText)")
        if (searchElement.isFound) {
            val deviceLocale = if (it.label == "検索") "ja-JP" else "en-US"
            if (deviceLocale != targetLocale) {
                IosDeviceUtility.setAppleLocale(udid = testProfile.udid, targetLocale)
            }
        }
    }

}