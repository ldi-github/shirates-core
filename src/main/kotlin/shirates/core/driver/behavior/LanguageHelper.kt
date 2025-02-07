package shirates.core.driver.behavior

import shirates.core.configuration.PropertiesManager
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
        if (TestMode.isAndroid && TestMode.isEmulator && PropertiesManager.androidLanguageAndRegion.isNotBlank()) {
            LanguageHelperAndroid.setLanguageAndRegion(PropertiesManager.androidLanguageAndRegion)
        } else {
            TestDriver.createAppiumDriver(profile = testProfile)
        }
    }

}