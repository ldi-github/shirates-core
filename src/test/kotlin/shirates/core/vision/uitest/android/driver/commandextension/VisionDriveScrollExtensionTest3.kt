package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.scanElements
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.testDrive
import shirates.core.testcode.Want
import shirates.core.vision.driver.branchextension.emulator
import shirates.core.vision.driver.branchextension.realDevice
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveScrollExtensionTest3 : VisionTest() {

    @Order(10)
    @Test
    fun doUntilScrollStop() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.doUntilScrollStop(
                        direction = ScrollDirection.Down,
                        actionFunc = {
                            it.canDetect("System")
                        }
                    )
                    it.tap()
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.doUntilScrollStop(
                        direction = ScrollDirection.Down,
                        actionFunc = {
                            it.canDetect("System2")
                        }
                    )
                }.expectation {
                    it.exist("Tips & support")
                }
            }
        }

    }

    @Order(20)
    @Test
    fun scanElements() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    TestElementCache.scanResults.clear()
                    (TestElementCache.scanResults.count() == 0).thisIsTrue()
                }.action {
                    testDrive.scanElements()
                }.expectation {
                    (TestElementCache.scanResults.count() > 0).thisIsTrue()
                    (TestElementCache.scanResults.first().element.descendants.any() { it.text == "Search settings" }).thisIsTrue()
                    emulator {
                        (TestElementCache.scanResults.last().element.descendants.any() { it.text == "About emulated device" }).thisIsTrue()
                    }.realDevice {
                        (TestElementCache.scanResults.last().element.descendants.any() { it.text == "About phone" }).thisIsTrue()
                    }
                }
            }
        }

    }

    @Test
    @Order(30)
    fun withScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it
                            .detect("Notifications").textIs("Notifications")
                            .detect("System").textIs("System")
                    }
                    withScrollUp {
                        it
                            .detect("Google").textIs("Google")
                            .detect("Display").textIs("Display")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it.exist("System")
                    }
                    withScrollUp {
                        it.exist("Storage")
                    }
                }
            }
            case(3) {
                action {
                    withScrollDown {
                        it.tap("System")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(4) {
                action {
                    it.tapImage("[←]", threshold = 0.3)
                    withScrollUp {
                        it.tap("Network & internet")
                    }
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}