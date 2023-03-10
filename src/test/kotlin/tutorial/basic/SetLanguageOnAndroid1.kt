package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.befavior.LanguageSettingsHelper
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SetLanguageOnAndroid1 : UITest() {

    @Test
    @Order(10)
    fun setLanguage_getLanguage_removeLanguage1() {

        scenario {
            case(1) {
                condition {
                    LanguageSettingsHelper.gotoLocaleSettings()
                }.action {
                    s1 = LanguageSettingsHelper.setLanguage(language = "English", region = "Canada")
                }.expectation {
                    s1.thisIs("English (Canada)")
                }
            }
            case(2) {
                action {
                    s1 = LanguageSettingsHelper.getLanguage()
                }.expectation {
                    s1.thisIs("English (Canada)")
                }
            }
            case(3) {
                action {
                    LanguageSettingsHelper.removeLanguage(language = "English", region = "Canada")
                }.expectation {
                    it.dontExist("English (Canada)")
                }
            }
            case(4) {
                action {
                    LanguageSettingsHelper.setLanguage(language = "English", region = "United States")
                    s1 = LanguageSettingsHelper.getLanguage()
                }.expectation {
                    s1.thisIs("English (United States)")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun setLanguage_getLanguage_removeLanguage2() {

        scenario {
            case(1) {
                condition {
                    LanguageSettingsHelper.gotoLocaleSettings()
                }.action {
                    s1 = LanguageSettingsHelper.setLanguage(language = "日本語", region = "日本")
                }.expectation {
                    s1.thisIs("日本語 (日本)")
                }
            }
            case(2) {
                action {
                    s1 = LanguageSettingsHelper.getLanguage()
                }.expectation {
                    s1.thisIs("日本語 (日本)")
                }
            }
            case(3) {
                action {
                    LanguageSettingsHelper.removeLanguage(language = "日本語", region = "日本")
                }.expectation {
                    it.dontExist("日本語 (日本)")
                }
            }
            case(4) {
                action {
                    LanguageSettingsHelper.setLanguage(language = "English", region = "United States")
                    s1 = LanguageSettingsHelper.getLanguage()
                }.expectation {
                    s1.thisIs("English (United States)")
                }
            }
        }
    }

}