package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestElementCache
import shirates.core.driver.branchextension.emulator
import shirates.core.driver.branchextension.realDevice
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveScrollExtensionTest3 : UITest() {

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
                            it.canSelect("System")
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
                            it.canSelect("System2")
                        }
                    )
                }.expectation {
                    val lastItem = it.select("#recycler_view").descendants.last { it.id == "android:id/title" }
                    lastItem.text.thisIs("Tips & support")
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
                    it.scanElements()
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
                            .select("[Notifications]").textIs("Notifications")
                            .select("[System]").textIs("System")
                    }
                    withScrollUp {
                        it
                            .select("[Google]").textIs("Google")
                            .select("[Display]").textIs("Display")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it.exist("[System]")
                    }
                    withScrollUp {
                        it.exist("[Storage]")
                    }
                }
            }
            case(3) {
                action {
                    withScrollDown {
                        it.tap("[System]")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
            case(4) {
                action {
                    it.tap("[←]")
                    withScrollUp {
                        it.tap("[Network & internet]")
                    }
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}