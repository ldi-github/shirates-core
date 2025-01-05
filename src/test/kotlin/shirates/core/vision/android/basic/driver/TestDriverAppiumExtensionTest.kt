//package shirates.core.vision.android.basic.driver
//
//import org.junit.jupiter.api.Test
//import shirates.core.configuration.Testrun
//import shirates.core.driver.TestDriver.appiumDriver
//import shirates.core.driver.commandextension.thisIs
//import shirates.core.driver.implicitWaitMilliseconds
//import shirates.core.testcode.UITest
//import shirates.core.vision.testcode.VisionTest
//
//@Testrun("testConfig/android/androidSettings/testrun.properties")
//class TestDriverAppiumExtensionTest : VisionTest() {
//
//    @Test
//    fun implicitWaitMillisecondsTest() {
//
//        scenario {
//            case(1) {
//                expectation {
//                    implicitWaitMilliseconds(99) {
//                        appiumDriver.manage().timeouts().implicitWaitTimeout.toMillis().thisIs(99L)
//                    }
//                }
//            }
//        }
//
//    }
//}