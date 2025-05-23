package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.behavior.LanguageHelper
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.launchApp
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class SetLanguageOnIos1 : UITest() {

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