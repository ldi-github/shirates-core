package shirates.core.vision.uitest.android.driver.behavior

import org.junit.jupiter.api.Test
import shirates.core.driver.befavior.LanguageHelper
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.testcode.VisionTest

class AndroidLanguageTest : VisionTest() {

    @Test
    fun setLanguageAndLocale() {

        scenario {
            case(1) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "ja", locale = "JP")
                }.expectation {
                    it.exist("設定")
                }
            }
            case(2) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "en", locale = "US")
                }.expectation {
                    it.exist("Settings")
                }
            }
        }
    }

}