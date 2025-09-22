package shirates.core.driver.behavior

import shirates.core.driver.*
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ShellUtility
import shirates.core.vision.driver.commandextension.invalidateScreen

object LanguageHelperAndroid : TestDrive {

    /**
     * gotoLocaleSettings
     */
    fun gotoLocaleSettings() {

        if (TestMode.isNoLoadRun) {
            return
        }

        if (TestMode.isVisionTest) {
            vision.invalidateScreen()
        }

        val udid = testContext.profile.udid
        val args = "adb -s $udid shell am start -a android.settings.LOCALE_SETTINGS".split(" ").toTypedArray()
        ShellUtility.executeCommand(args = args)
        invalidateCache()
        if (TestMode.isVisionTest) {
            vision.invalidateScreen()
        }
    }

    /**
     * getLanguage
     */
    fun getLanguage(): String {

        if (TestMode.isNoLoadRun) {
            return ""
        }

        if (TestMode.isVisionTest) {
            vision.invalidateScreen()
        }

        classic.syncCache(true)

        if (it.canSelect("#add_language").not()) {
            gotoLocaleSettings()
        }

        val language = getLanguageInLocaleSettingsScreen()
        return language
    }

    private fun getLanguageInLocaleSettingsScreen(): String {

        if (platformMajorVersion >= 16) {
            if (it.canSelect("#number")) {
                val rightText = it.right("#android:id/title").text
                return rightText
            }
        } else {
            if (it.canSelect("#label")) {
                return it.contentDesc
            }
        }
        return ""
    }

    /**
     * addLanguage
     */
    fun addLanguage(language: String, region: String) {

        if (TestMode.isNoLoadRun) {
            return
        }

        if (TestMode.isVisionTest) {
            vision.invalidateScreen()
        }

        gotoLocaleSettings()

        val languageAndRegion = "$language ($region)"

        if (canSelect(languageAndRegion)) {
            return
        }

        if (platformMajorVersion >= 16) {
            tap("#settingslib_button")
            tap("#locale_search_menu")
            sendKeys(language)
            hideKeyboard()
            if (canSelect("#android:id/title&&${languageAndRegion}")) {
                it.tap()
                return
            }
            if (canSelect("#android:id/title&&*${language}*")) {
                it.tap()
                return
            }
            if (canSelect("#android:id/title&&*${region}*")) {
                it.tap()
            } else {
                throw TestDriverException("Region not found. ($region)")
            }
        } else {
            tap("#add_language")
            tap("#android:id/locale_search_menu")
            sendKeys(language)
            hideKeyboard()
            if (canSelect("#android:id/locale&&${languageAndRegion}")) {
                it.tap()
                return
            }
            if (canSelect("#android:id/locale&&*${language}*")) {
                it.tap()
                return
            }
            if (canSelect("#android:id/locale&&*${region}*")) {
                it.tap()
            } else {
                throw TestDriverException("Region not found. ($region)")
            }
        }
    }

    /**
     * removeLanguage
     */
    fun removeLanguage(language: String, region: String) {

        if (TestMode.isNoLoadRun) {
            return
        }

        if (TestMode.isVisionTest) {
            vision.invalidateScreen()
        }

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
        if (testContext.profile.platformVersion.toInt() >= 12) {
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
     * setLanguageAndRegion
     */
    fun setLanguageAndRegion(languageAndRegion: String) {

        val lr = languageAndRegion.replace("（", "(").replace("）", ")")
            .removeSuffix(")")
            .split("(")
        if (lr.size != 2) {
            TestLog.warn("languageAndRegion is not valid. ($languageAndRegion)")
            return
        }
        val language = lr[0]
        val region = lr[1]

        setLanguageAndRegion(language = language, region = region)
    }

    /**
     * setLanguageAndRegion
     */
    fun setLanguageAndRegion(language: String, region: String): String {

        val languageAndRegion = "$language ($region)"

        if (TestMode.isNoLoadRun) {
            return languageAndRegion
        }

        if (TestMode.isVisionTest) {
            vision.invalidateScreen()
        }

        gotoLocaleSettings()

        if (getLanguage() == languageAndRegion) {
            return languageAndRegion
        }

        if (it.canSelect(languageAndRegion).not()) {
            addLanguage(language = language, region = region)
        }

        if (getLanguage() == languageAndRegion) {
            return languageAndRegion
        }

        if (platformMajorVersion >= 16) {
            it.select(languageAndRegion).right("#settingslib_menu_button")
                .tap()
                .tap("Move to top")
                .tap("#android:id/button1")
        } else {
            it.select("<$languageAndRegion>:right(#dragHandle)")
                .swipeVerticalTo(endY = 0)
                .tap("#android:id/button1")
        }
        val currentLanguage = getLanguageInLocaleSettingsScreen()
        return currentLanguage
    }

}