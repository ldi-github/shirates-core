package shirates.core.uitest.android.driver.misc

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.thisIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Select_CacheAndDirectCompatibilityTest : UITest() {

    @Test
    @Order(10)
    fun text() {

        scenario {
            case(1) {
                action {
                    e1 = it.select("Notifications", useCache = true)
                    e2 = it.select("Notifications", useCache = false)
                }.expectation {
                    e1.thisIs(e2)
                }
            }
        }
    }

    @Test
    @Order(20)
    fun relative() {

        scenario {
            case(1) {
                action {
                    e1 = it.select("<Notifications>:leftImage", useCache = true)
                    e2 = it.select("<Notifications>:leftImage", useCache = false)
                }.expectation {
                    e1.thisIs(e2)
                }
            }
            case(2) {
                action {
                    e1 = it.select("<Notifications>:preImage", useCache = true)
                    e2 = it.select("<Notifications>:preImage", useCache = false)
                }.expectation {
                    e1.thisIs(e2)
                }
            }
        }
    }

    @Test
    @Order(30)
    fun pos() {

        scenario {
            case(1) {
                action {
                    e1 = it.select("Notification*&&pos=1", useCache = true)
                    e2 = it.select("Notification*&&pos=1", useCache = false)
                }.expectation {
                    e1.thisIs(e2)
                }
            }
            case(2) {
                action {
                    e1 = it.select("Notification*&&pos=2", useCache = true)
                    e2 = it.select("Notification*&&pos=2", useCache = false)
                }.expectation {
                    e1.thisIs(e2)
                }
            }
        }
    }
}