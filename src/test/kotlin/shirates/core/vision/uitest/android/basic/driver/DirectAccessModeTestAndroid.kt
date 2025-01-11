package shirates.core.vision.uitest.android.basic.driver

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.toTestElement
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.wait
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class DirectAccessModeTestAndroid : VisionTest() {

    @Test
    @Order(10)
    fun enableCacheTest() {

        enableCache()   // This should not be specified if cache mode is default

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                        .tap("[Network & internet]")
                        .screenIs("[Network & internet Screen]")
                }.action {
                    it.tap("[Internet]")
                }.expectation {
                    it.screenIs("[Internet Screen]")
                        .exist("[Add network]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun disableCacheTest() {

//        disableCache()   // This should not be specified if vision mode is default

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                        .tap("[Network & internet]")
                        .switchScreen("[Network & internet Screen]")
                }.action {
                    it.tap("Internet")
                }.expectation {
                    it.switchScreen("[Internet Screen]")
                        .existWithScrollDown("[Add network]")
                }
            }
        }
    }

    @Test
    @Order(25)
    fun appiumDriverTest() {

//        disableCache()   // This should not be specified if vision mode is default

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    appiumDriver.findElement(By.xpath("//*[@text='Network & internet']")).click()
                    wait()
                }.action {
                    appiumDriver.findElement(By.xpath("//*[@text='Internet']")).click()
                    wait()
                }.expectation {
                    appiumDriver.findElement(By.xpath("//*[@text='Add network']"))
                        .toTestElement().isFound.thisIsTrue("<Add network> exists.")
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
//                    it.screenIs("[Android Settings Top Screen]")
//                        .tap("Network & internet")
//                        .switchScreen("[Network & internet Screen]")
//                }.action {
//                    it.tap("Internet")
//                }.expectation {
//                    it.switchScreen("[Internet Screen]")
//                        .existWithScrollDown("Add network")
//                }
//            }
//        }
//    }

    @Test
    @Order(40)
    fun suppressCacheTest() {

        scenario {
            case(1) {
                expectation {
                    useCache {
                        it.exist("Network & internet")  // cache mode
                    }
                    it.exist("Network & internet")  // direct access mode
                }
            }
        }
    }

}