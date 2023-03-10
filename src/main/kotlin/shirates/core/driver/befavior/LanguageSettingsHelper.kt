package shirates.core.driver.befavior

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.testDrive
import shirates.core.driver.testProfile
import shirates.core.exception.TestDriverException
import shirates.core.utility.getStringOrEmpty
import shirates.core.utility.ios.IosLanguageUtility
import shirates.core.utility.misc.ShellUtility

object LanguageSettingsHelper : TestDrive {

    /**
     * gotoLocaleSettings
     */
    fun gotoLocaleSettings() {

        val udid = testProfile.udid
        val args = "adb -s $udid shell am start -a android.settings.LOCALE_SETTINGS".split(" ").toTypedArray()
        ShellUtility.executeCommand(args = args)
    }

    /**
     * getLanguage
     */
    fun getLanguage(): String {

        testDrive.syncCache(true)

        if (it.canSelect("#add_language").not()) {
            gotoLocaleSettings()
        }

        if (it.canSelect("#label")) {
            return it.contentDesc
        }
        return ""
    }

    /**
     * addLanguage
     */
    fun addLanguage(language: String, region: String) {

        gotoLocaleSettings()

        val languageAndRegion = "$language ($region)"

        if (canSelect(languageAndRegion)) {
            return
        }

        tap("#add_language")
        tap("#android:id/locale_search_menu")
        sendKeys(language)
        hideKeyboard()
        if (canSelect("#android:id/locale&&$language")) {
            it.tap()
        } else {
            throw TestDriverException("Language not found. ($language)")
        }

        if (canSelect(languageAndRegion)) {
            return
        }

        if (canSelect(region)) {
            it.tap()
        } else {
            throw TestDriverException("Region not found. ($region)")
        }
    }

    /**
     * removeLanguage
     */
    fun removeLanguage(language: String, region: String) {

        gotoLocaleSettings()

        val languageAndRegion = "$language ($region)"

        if (canSelect(languageAndRegion).not()) {
            return
        }

        if (canSelect("<#com.android.settings:id/action_bar>:inner(-1)").not()) {
            return
        }
        it.tap()
        it.tap("#android:id/title")
        it.tap(languageAndRegion)
        if (testProfile.platformVersion.toInt() >= 12) {
            it.tap("<#com.android.settings:id/action_bar>:inner(-1)")
        } else {
            it.tap("<#com.android.settings:id/action_bar>:inner(-2)")
        }
        it.tap("#android:id/button1")

        if (it.canSelect(languageAndRegion)) {
            throw TestDriverException("Failed to remove $languageAndRegion")
        }
    }

    /**
     * setLanguage
     */
    fun setLanguage(language: String, region: String): String {

        gotoLocaleSettings()

        val languageAndRegion = "$language ($region)"

        if (getLanguage() == languageAndRegion) {
            return languageAndRegion
        }

        if (canSelect("#com.android.settings:id/label&&$language").not()) {
            addLanguage(language = language, region = region)
        }

        if (getLanguage() == languageAndRegion) {
            return languageAndRegion
        }

        it.select("<$languageAndRegion>:right(#dragHandle)")
            .swipeVerticalTo(endY = 0)

        val currentLanguageAndRegion = it.select("#com.android.settings:id/label&&[1]").text
        if (currentLanguageAndRegion != languageAndRegion) {
            throw TestDriverException("Failed to set to $languageAndRegion")
        }

        return currentLanguageAndRegion
    }

//    /**
//     * setLanguageOnAndroid
//     */
//    fun setLanguageOnAndroid() {
//
//        val locale = testContext.profile.capabilities.getStringOrEmpty("locale")
//
//        it.pressHome()
//            .pressHome()
//
//        val clock = it.select("#clock")
//        val isJapaneseClock = clock.text.contains("日")
//        val hasMissMatch = locale == "JP" && isJapaneseClock.not() || locale != "JP" && isJapaneseClock
//        if (hasMissMatch) {
//            gotoLocaleSettings()
//
//            it.waitForDisplay("#add_language")
//
//            if (locale == "JP") {
//                ifCanSelectNot("*日本語*") {
//                    it.tap("#add_language")
//                        .tap("#android:id/locale_search_menu")
//                        .sendKeys("ja")
//                    ifCanSelect("*日本語*") {
//                        it.tap()
//                    }
//                }
//                it.select("<*日本語*>:right(#dragHandle)")
//                    .swipeVerticalTo(endY = 0)
//            } else {
//                ifCanSelectNot("*United States*") {
//                    it.tap("#add_language")
//                    ifCanSelect("*United States*") {
//                        it.tap()
//                    }.ifElse {
//                        it.tap("#android:id/locale_search_menu")
//                            .sendKeys("English (United")
//                        ifCanSelect("*United States*") {
//                            it.tap()
//                        }
//                    }
//                }
//                it.select("<*United States*>:right(#dragHandle)")
//                    .swipeVerticalTo(endY = 0)
//            }
//        }
//        it.pressHome()
//    }

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
                IosLanguageUtility.setAppleLocale(udid = testProfile.udid, targetLocale)
            }
        }
    }

}