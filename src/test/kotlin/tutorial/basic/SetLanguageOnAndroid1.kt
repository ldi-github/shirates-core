package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.befavior.LanguageHelper
import shirates.core.driver.commandextension.exist
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SetLanguageOnAndroid1 : UITest() {

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