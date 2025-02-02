package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.appiumDriver
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.driver.toTestElement
import shirates.core.testcode.UITest
import shirates.core.utility.time.StopWatch

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class DirectAccessModeIos : UITest() {

    @Test
    @Order(10)
    fun enableCacheTest() {

//        enableCache()   // This should not be specified if cache mode is default

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

    @Test
    @Order(20)
    fun disableCacheTest() {

        disableCache()

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

    @Test
    @Order(25)
    fun appiumDriverTest() {

        disableCache()

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

    @Test
    @DisableCache
    @Order(30)
    fun disableCacheTest2() {

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

    @Test
    @Order(40)
    fun performanceComparison() {

        fun process(count: Int) {
            val sw1 = StopWatch()
            val sw2 = StopWatch()
            invalidateCache()

            sw1.start()
            useCache {
                for (i in 1..count) {
                    it.select("General")  // cache mode
                }
            }
            sw1.stop()

            sw2.start()
            suppressCache {
                for (i in 1..count) {
                    it.select("General")  // direct access mode
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

    @Test
    @Order(50)
    fun useCacheArgument() {

        // useCache argument can be specified with these functions

        printUseCache("testMethod")

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

    private fun printUseCache(funcName: String) {

        println("($funcName) testContext.useCache=${testContext.useCache}")
    }
}