package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveScreenExtensionTest : UITest() {

    @Test
    fun screenName() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.screenName
                        .thisIs("[Android Settings Top Screen]")
                }
            }
        }
    }

    @Test
    fun isScreen() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.isScreen("[Android Settings Top Screen]")
                        .thisIsTrue()
                    it.isScreen("[Notifications Screen]")
                        .thisIsFalse()
                }
            }
        }
    }

    @Test
    fun isScreenOf() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.isScreenOf("[Android Settings Top Screen]", "[Notifications Screen]")
                        .thisIsTrue()
                    it.isScreenOf("[Network & internet Screen]", "[Notifications Screen]")
                        .thisIsFalse()
                }
            }
        }
    }

}