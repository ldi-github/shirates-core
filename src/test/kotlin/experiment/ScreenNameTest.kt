package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.screenName
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.thisIs
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class ScreenNameTest : UITest() {

    @Test
    fun getCurrentScreenName() {

        scenario {
            case(1) {
                condition {
                    it.screenName.thisIs("[Android Settings Top Screen]")
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("Network & internet")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                    it.screenName.thisIs("[Network & internet Screen]")
                }
            }
        }
    }
}