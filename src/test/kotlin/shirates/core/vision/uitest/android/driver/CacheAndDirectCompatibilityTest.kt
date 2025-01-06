package shirates.core.vision.uitest.android.driver

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.textIs
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class CacheAndDirectCompatibilityTest : VisionTest() {

    @Test
    @Order(10)
    fun text() {

        scenario {
            case(1) {
                action {
                    v1 = it.detect("Notifications", useCache = true)
                    v2 = it.detect("Notifications", useCache = false)
                }.expectation {
                    v1.textIs(v2.text)
                }
            }
        }
    }

//    @Test
//    @Order(20)
//    fun relative() {
//
//        scenario {
//            case(1) {
//                action {
//                    v1 = it.detect("<Notifications>:leftImage", useCache = true)
//                    v2 = it.detect("Notifications", useCache = false).leftItem()
//                }.expectation {
//                    v1.textIs(v2.text)
//                }
//            }
//            case(2) {
//                action {
//                    v1 = it.detect("<Notifications>:preImage", useCache = true)
//                    v2 = it.detect("<Notifications>:preImage", useCache = false)
//                }.expectation {
//                    v1.textIs(v2.text)
//                }
//            }
//        }
//    }

//    @Test
//    @Order(30)
//    fun pos() {
//
//        scenario {
//            case(1) {
//                action {
//                    v1 = it.detect("Notification*&&pos=1", useCache = true)
//                    v2 = it.detect("Notification*&&pos=1", useCache = false)
//                }.expectation {
//                    v1.thisIs(v2)
//                }
//            }
//            case(2) {
//                action {
//                    v1 = it.detect("Notification*&&pos=2", useCache = true)
//                    v2 = it.detect("Notification*&&pos=2", useCache = false)
//                }.expectation {
//                    v1.thisIs(v2)
//                }
//            }
//        }
//    }
}