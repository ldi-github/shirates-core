package shirates.core.uitest.android.basic.driver

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.commandextension.*
import shirates.core.driver.toTestElement
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class DirectAccessModeTestAndroid : UITest() {

    @Test
    @Order(10)
    fun enableCacheTest() {

//        enableCache()   // This should not be specified if cache mode is default

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

        disableCache()

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                        .tap("[Network & internet]")
                        .switchScreen("[Network & internet Screen]")
                }.action {
                    it.tap("[Internet]")
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

        disableCache()

        scenario {
            case(1) {
                condition {
                    appiumDriver.findElement(By.xpath("//*[@text='Network & internet']")).click()
                }.action {
                    appiumDriver.findElement(By.xpath("//*[@text='Internet']")).click()
                }.expectation {
                    appiumDriver.findElement(By.xpath("//*[@text='Add network']"))
                        .toTestElement().isFound.thisIsTrue("<Add network> exists.")
                }
            }
        }

    }

    @Test
    @DisableCache
    @Order(30)
    fun disableCacheTest2() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                        .tap("[Network & internet]")
                        .switchScreen("[Network & internet Screen]")
                }.action {
                    it.tap("[Internet]")
                }.expectation {
                    it.switchScreen("[Internet Screen]")
                        .existWithScrollDown("[Add network]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun suppressCacheTest() {

        scenario {
            case(1) {
                expectation {
                    it.exist("Network & internet")  // cache mode
                    suppressCache {
                        it.exist("Network & internet")  // direct access mode
                    }
                }
            }
        }
    }

}