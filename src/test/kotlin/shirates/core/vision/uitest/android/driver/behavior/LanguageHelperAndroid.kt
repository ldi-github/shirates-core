package shirates.core.vision.uitest.android.driver.behavior

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.behavior.LanguageHelperAndroid
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.driver.commandextension.setOCRLanguage
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class LanguageHelperAndroid : VisionTest() {

    @Test
    fun setLanguageAndRegion() {

        scenario {
            case(1) {
                action {
                    LanguageHelperAndroid.setLanguageAndRegion(languageAndRegion = "日本語(日本)")
                }.expectation {
                    it.setOCRLanguage("ja")
                        .exist("言語")
                }
            }
            case(2) {
                action {
                    LanguageHelperAndroid.setLanguageAndRegion(languageAndRegion = "English(United States)")
                }.expectation {
                    it.setOCRLanguage("en")
                        .exist("Languages")
                }
            }
        }
    }
}