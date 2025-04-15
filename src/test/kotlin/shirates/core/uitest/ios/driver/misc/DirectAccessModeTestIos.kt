package shirates.core.uitest.ios.driver.misc

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

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class DirectAccessModeTestIos : UITest() {

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
                    Thread.sleep(500)
                }.action {
                    appiumDriver.findElement(By.id("About")).click()
                    Thread.sleep(500)
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
    @Order(50)
    fun useCacheArgument() {

        disableCache()
        
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