package shirates.core.driver.befavior

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.driver.testDrive
import shirates.core.driver.testProfile
import shirates.core.exception.TestDriverException
import shirates.core.utility.misc.ShellUtility

object LanguageHelperAndroid : TestDrive {

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

}