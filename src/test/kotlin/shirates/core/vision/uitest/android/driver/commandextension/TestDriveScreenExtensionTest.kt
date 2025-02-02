package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.isScreen
import shirates.core.vision.driver.commandextension.isScreenOf
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.screenName
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveScreenExtensionTest : VisionTest() {

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