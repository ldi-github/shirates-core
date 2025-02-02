package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.appiumDriver
import shirates.core.driver.commandextension.*
import shirates.core.driver.toTestElement
import shirates.core.testcode.UITest
import shirates.core.utility.time.StopWatch

@Testrun("testConfig/android/androidSettings/testrun.properties")
class DirectAccessModeAndroid : UITest() {

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
                        .exist("[Add network]")
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
                        .exist("[Add network]")
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

    @Test
    @Order(50)
    fun performanceComparison() {

        fun process(count: Int) {
            val sw1 = StopWatch()
            val sw2 = StopWatch()
            invalidateCache()

            sw1.start()
            useCache {
                for (i in 1..count) {
                    it.select("Network & internet")  // cache mode
                }
            }
            sw1.stop()

            sw2.start()
            suppressCache {
                for (i in 1..count) {
                    it.select("Network & internet")  // direct access mode
                }
            }
            sw2.stop()

            output("$count element(s)")
            output("$sw1 cache mode")
            output("$sw2 direct access mode")
        }

        scenario {
            case(1) {
                expectation {
                    process(1)
                }
            }
            case(2) {
                expectation {
                    process(5)
                }
            }
            case(3) {
                expectation {
                    process(10)
                }
            }
            case(4) {
                expectation {
                    process(50)
                }
            }
        }
    }
}