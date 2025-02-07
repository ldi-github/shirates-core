package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.behavior.LanguageHelper
import shirates.core.vision.driver.waitForDisplay
import shirates.core.vision.testcode.VisionTest

@Testrun(testrunFile = "unitTestConfig/android/androidSettings_ja/testrun.properties")
class LaunchInJapaneseTest : VisionTest() {

    @Test
    fun launch() {

        scenario {
            case(1) {
                condition {
                    LanguageHelper.setLanguageAndLocale("ja", "JP")
                    it.waitForDisplay("設定")
                }
            }
        }
    }
}