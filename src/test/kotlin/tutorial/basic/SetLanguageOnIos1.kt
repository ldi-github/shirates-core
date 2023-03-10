package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.befavior.LanguageHelperIos
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class SetLanguageOnIos1 : UITest() {

    @Test
    @Order(10)
    fun setLanguage1() {

        scenario {
            case(1) {
                action {
                    LanguageHelperIos.setLanguage(locale = "ja-JP")
                }
            }
            case(2) {
                action {
                    LanguageHelperIos.setLanguage(locale = "en-US")
                }
            }
        }
    }

}