package shirates.core.driver.befavior

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.testProfile
import shirates.core.utility.getStringOrEmpty
import shirates.core.utility.ios.IosLanguageUtility

object LanguageHelperIos : TestDrive {

    /**
     * setLanguage
     */
    fun setLanguage(
        locale: String
    ) {
        if (TestMode.isNoLoadRun) {
            return
        }

        IosLanguageUtility.setAppleLocale(udid = testProfile.udid, locale = locale)
        TestDriver.createAppiumDriver()
    }

    /**
     * setLanguage
     */
    fun setLanguage() {

        if (TestMode.isNoLoadRun) {
            return
        }

        val capabilityLange = testProfile.capabilities.getStringOrEmpty("language")
        val capabilityLocale = testProfile.capabilities.getStringOrEmpty("locale")
        val targetLocale = "$capabilityLange-$capabilityLocale"

        setLanguage(locale = targetLocale)
    }

}