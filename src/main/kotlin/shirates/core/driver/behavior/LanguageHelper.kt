package shirates.core.driver.behavior

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.testProfile
import shirates.core.vision.driver.commandextension.setOCRLanguage

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
        if (TestMode.isVisionTest) {
            vision.setOCRLanguage(ocrLanguage)
        }
        TestDriver.createAppiumDriver(profile = testProfile)
    }

}