package shirates.core.uitest.ios.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.syncCache
import shirates.core.driver.commandextension.thisIs
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class CacheAndDirectCompatibilityTest : UITest() {

    @Test
    @Order(10)
    fun labelAndCell() {

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
                    e1 = it.select(".XCUIElementTypeCell&&General", useCache = true)
                    e2 = it.select(".XCUIElementTypeCell&&General", useCache = false)
                }.expectation {
                    e1.thisIs(e2)
                }
            }
        }
    }

    @Test
    @Order(20)
    fun pos() {

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