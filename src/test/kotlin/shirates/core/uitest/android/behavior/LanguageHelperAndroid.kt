package shirates.core.uitest.android.behavior

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.behavior.LanguageHelperAndroid
import shirates.core.driver.commandextension.exist
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class LanguageHelperAndroid : UITest() {

    @Test
    fun setLanguageAndRegion() {

        scenario {
            case(1) {
                action {
                    LanguageHelperAndroid.setLanguageAndRegion(languageAndRegion = "日本語(日本)")
                }.expectation {
                    it.exist("@言語")
                }
            }
            case(2) {
                action {
                    LanguageHelperAndroid.setLanguageAndRegion(languageAndRegion = "English(United States)")
                }.expectation {
                    it.exist("@Languages")
                }
            }
        }
    }
}