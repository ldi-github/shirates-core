package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.befavior.LanguageHelperAndroid
import shirates.core.driver.commandextension.cellOf
import shirates.core.driver.commandextension.dontExist
import shirates.core.driver.commandextension.exist
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
                    it.cellOf("1") {
                        exist("日本語 (日本)")
                        dontExist("English (United States)")
                    }
                }
            }
            case(2) {
                action {
                    LanguageHelperAndroid.setLanguage(language = "English", region = "United States")
                }.expectation {
                    it.exist("@Languages")
                    it.cellOf("1") {
                        exist("English (United States)")
                        dontExist("日本語 (日本)")
                    }
                }
            }
        }
    }

}