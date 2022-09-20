package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SetLanguageTest : UITest() {

    @Test
    fun test1() {

        scenario {
            case(1) {
                action {
                    it.macro("setLanguage", "English", "Australia")
                }.expectation {
                    it.exist("@Languages")
                }
            }
            case(1) {
                action {
                    it.macro("setLanguage", "日本語", "日本")
                }.expectation {
                    it.exist("@言語")
                }
            }
            case(2) {
                action {
                    it.macro("setLanguage", "English", "United States")
                }.expectation {
                    it.exist("@Languages")
                }
            }
        }
    }

    @Test
    fun test2() {

        scenario {
            case(1) {
                condition {
                    it.macro("setLanguage", "English", "United States")
                }.expectation {
                    it.exist("@Languages")
                }
            }
        }
    }
}