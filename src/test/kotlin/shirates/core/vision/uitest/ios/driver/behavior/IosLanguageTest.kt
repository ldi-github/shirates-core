package shirates.core.vision.uitest.ios.driver.behavior

import org.junit.jupiter.api.Test
import shirates.core.driver.befavior.LanguageHelper
import shirates.core.testcode.ios
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.driver.commandextension.launchApp
import shirates.core.vision.testcode.VisionTest

@ios
class IosLanguageTest : VisionTest() {

    @Test
    fun setLanguageAndLocale() {

        scenario {
            case(1) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "ja", locale = "JP")
                    it.launchApp("com.apple.Preferences")
                }.expectation {
                    it.exist("設定")
                }
            }
            case(2) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "en", locale = "US")
                    it.launchApp("com.apple.Preferences")
                }.expectation {
                    it.exist("Settings")
                }
            }
        }
    }

}