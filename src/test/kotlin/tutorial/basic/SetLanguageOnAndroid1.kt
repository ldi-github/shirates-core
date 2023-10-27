package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.befavior.LanguageHelperAndroid
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.rightLabel
import shirates.core.driver.commandextension.textIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SetLanguageOnAndroid1 : UITest() {

    @Test
    @Order(10)
    fun setLanguage_getLanguage_removeLanguage1() {

        scenario {
            case(1) {
                action {
                    LanguageHelperAndroid.setLanguage(language = "日本語", region = "日本")
                }.expectation {
                    it.exist("@言語")
                    it.exist("1")
                        .rightLabel().textIs("日本語 (日本)")
                }
            }
            case(2) {
                action {
                    LanguageHelperAndroid.setLanguage(language = "English", region = "United States")
                }.expectation {
                    it.exist("@Languages")
                    it.exist("1")
                        .rightLabel().textIs("English (United States)")
                }
            }
        }
    }

}