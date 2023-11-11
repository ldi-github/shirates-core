package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.tap
import shirates.core.driver.platformMajorVersion
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AdaptingToChangesInMessage1 : UITest() {

    /**
     * Note:
     * This sample code explains concepts and does not work.
     */

    @Test
    @Order(10)
    fun original() {

        scenario {
            case(1) {
                action {
                    it.tap("Allow only while using the app")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun branch() {

        scenario {
            case(1) {
                action {
                    if (platformMajorVersion < 11) {
                        it.tap("Allow only while using the app")
                    } else {
                        it.tap("While using the app")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun selector() {

        scenario {
            case(1) {
                action {
                    it.tap("Allow only while using the app||While using the app")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun nickname() {

        scenario {
            case(1) {
                action {
                    it.tap("[While using the app]")
                }
            }
        }
    }
}