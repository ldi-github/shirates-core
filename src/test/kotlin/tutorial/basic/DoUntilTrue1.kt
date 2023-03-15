package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.doUntilTrue
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class DoUntilTrue1 : UITest() {

    @Test
    @Order(10)
    fun doUntilTrue() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.launchApp()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    doUntilTrue {
                        it.swipeCenterToTop()
                        it.canSelect("System")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun doUntilTrue_timeout() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.launchApp()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    doUntilTrue(
                        waitSeconds = 3.0,
                        onTimeoutFunc = { sc ->
                            SKIP_SCENARIO("Timeout. (waitSeconds=${sc.waitSeconds})")
                        }
                    ) {
                        it.swipeCenterToTop()
                        it.canSelect("System")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun doUntilTrue_maxLoop() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.launchApp()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    doUntilTrue(
                        maxLoopCount = 2,
                        onMaxLoopFunc = { sc ->
                            SKIP_SCENARIO("MaxLoopCount. (maxLoopCount=${sc.maxLoopCount})")
                        }
                    ) {
                        it.swipeCenterToTop()
                        it.canSelect("System")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

}