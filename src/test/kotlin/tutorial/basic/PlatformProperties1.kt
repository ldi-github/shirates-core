package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.platformName
import shirates.core.driver.platformVersion
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class PlatformProperties1 : UITest() {

    @Test
    @Order(10)
    fun platformProperties() {

        scenario {
            case(1) {
                expectation {
                    platformName
                        .thisIs("android")

                    platformVersion
                        .thisIs("12")

                    isAndroid
                        .thisIsTrue()

                    isiOS
                        .thisIsFalse()
                }
            }
        }
    }

}