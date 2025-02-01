package shirates.core.hand.uitest

import org.junit.jupiter.api.Test
import shirates.core.driver.befavior.LanguageHelper
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.driver.waitForDisplay
import shirates.core.vision.testcode.VisionTest

class AndroidLanguageTest : VisionTest() {

    @Test
    fun setLanguageAndLocale() {

        scenario {
            case(1) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "ja", locale = "JP")
                    it.waitForDisplay("設定")
                }.expectation {
                    it.exist("設定")
                }
            }
            case(2) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "en", locale = "US")
                    it.waitForDisplay("Settings")
                }.expectation {
                    it.exist("Settings")
                }
            }
        }
    }

}