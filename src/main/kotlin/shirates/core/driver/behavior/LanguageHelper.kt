package shirates.core.driver.behavior

import shirates.core.driver.*
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
            visionDrive.setOCRLanguage(ocrLanguage)
        }
        TestDriver.createAppiumDriver(profile = testProfile)
    }

}