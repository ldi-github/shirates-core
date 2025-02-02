package shirates.core.vision.uitest.ios.misc

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver.syncCache
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.thisIs
import shirates.core.testcode.Want
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class CacheAndDirectCompatibilityTest : VisionTest() {

    @Test
    @Order(10)
    fun labelAndCell() {

        testDriveScope {
            var e1 = TestElement.emptyElement
            var e2 = TestElement.emptyElement

            scenario {
                case(1) {
                    condition {
                        syncCache(force = true)
                    }.action {
                        e1 = it.select("General", useCache = true)
                        e2 = it.select("General", useCache = false)
                    }.expectation {
                        e1.thisIs(e2)
                    }
                }
                case(2) {
                    action {
                        e1 = it.select(".XCUIElementTypeCell", useCache = true)
                        e2 = it.select(".XCUIElementTypeCell", useCache = false)
                    }.expectation {
                        e1.thisIs(e2)
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun pos() {

        testDriveScope {
            var e1 = TestElement.emptyElement
            var e2 = TestElement.emptyElement

            scenario {
                case(1) {
                    condition {
                        syncCache(force = true)
                    }.action {
                        e1 = it.select(".XCUIElementTypeCell&&pos=1", useCache = true)
                        e2 = it.select(".XCUIElementTypeCell&&pos=1", useCache = false)
                    }.expectation {
                        e1.thisIs(e2)
                    }
                }
                case(2) {
                    action {
                        e1 = it.select(".XCUIElementTypeCell&&pos=2", useCache = true)
                        e2 = it.select(".XCUIElementTypeCell&&pos=2", useCache = false)
                    }.expectation {
                        e1.thisIs(e2)
                    }
                }
            }
        }
    }

}