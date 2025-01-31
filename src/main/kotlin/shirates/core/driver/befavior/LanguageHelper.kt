package shirates.core.driver.befavior

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.testProfile

object LanguageHelper : TestDrive {

    /**
     * setLanguageAndLocale
     */
    fun setLanguageAndLocale(
        language: String,
        locale: String,
        ocrLanguage: String = language,
    ) {
        testProfile.language = language
        testProfile.locale = locale
        TestDriver.createAppiumDriver(profile = testProfile)
        if (TestMode.isVisionTest) {
            PropertiesManager.visionOCRLanguage = ocrLanguage
        }
    }
}