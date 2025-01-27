package shirates.core.vision.uitest.ios.driver

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.appiumDriver
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.driver.toTestElement
import shirates.core.vision.driver.commandextension.enableCache
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class DirectAccessModeTestIos : VisionTest() {

    @Test
    @Order(10)
    fun enableCacheTest() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.screenIs("[iOS Settings Top Screen]")
                            .tap("[General]")
                            .screenIs("[General Screen]")
                    }.action {
                        it.tap("[About]")
                    }.expectation {
                        it.screenIs("[About Screen]")
                            .exist("[Name]")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun disableCacheTest() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.switchScreen("[iOS Settings Top Screen]")
                            .tap("[General]")
                            .switchScreen("[General Screen]")
                    }.action {
                        it.tap("[About]")
                    }.expectation {
                        it.switchScreen("[About Screen]")
                            .exist("[Name]")
                    }
                }
            }
        }
    }

    @Test
    @Order(25)
    fun appiumDriverTest() {

        scenario {
            case(1) {
                condition {
                    appiumDriver.findElement(By.id("General")).click()
                }.action {
                    appiumDriver.findElement(By.id("About")).click()
                }.expectation {
                    appiumDriver.findElement(By.id("Name"))
                        .toTestElement().isFound.thisIsTrue("<Name > exists")
                }
            }
        }

    }

    /**
     * @DisableCache is not supported on VisionTest
     */
//    @Test
//    @DisableCache
//    @Order(30)
//    fun disableCacheTest2() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.switchScreen("[iOS Settings Top Screen]")
//                        .tap("[General]")
//                        .switchScreen("[General Screen]")
//                }.action {
//                    it.tap("[About]")
//                }.expectation {
//                    it.switchScreen("[About Screen]")
//                        .exist("[Name]")
//                }
//            }
//        }
//    }

    @Test
    @Order(50)
    fun useCacheArgument() {

        enableCache()

        printUseCache("testMethod")

        testDriveScope {
            scenario(useCache = true) {
                printUseCache("scenario")

                case(1, useCache = false) {
                    printUseCache("case")

                    condition(useCache = true) {
                        printUseCache("condition")

                    }.action(useCache = false) {
                        printUseCache("action")

                    }.expectation(useCache = true) {
                        printUseCache("expectation")

                    }
                }
            }
        }
    }

    private fun printUseCache(funcName: String) {

        println("($funcName) testContext.useCache=${testContext.useCache}")
    }
}