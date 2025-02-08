package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.exception.TestDriverException
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/maps/testrun.properties")
class VisionDriveDetectExtensionTest6 : VisionTest() {

    @Test
    @Order(10)
    fun withScrollRightCanDetectAll_withScrollLeftCanDetectAll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    withScrollRight("Restaurants") {
                        it.canDetectAll("Restaurants", "Gas", "Coffee", "More")
                            .thisIsTrue()
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollLeft("More") {
                        it.canDetectAll("Restaurants", "Gas", "Coffee", "More")
                            .thisIsFalse()
                    }
                }
            }
            case(3) {
                condition {
                    withScrollRight("Restaurants") {
                        it.flickAndGoRight(repeat = 2)
                    }
                }.expectation {
                    withScrollLeft("More") {
                        it.canDetectAll("More", "Coffee", "Gas", "Restaurants")
                            .thisIsTrue()
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existWithScrollDown_existWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                    withScrollRight("Restaurants") {
                        flickAndGoRight(repeat = 2)
                        tap("More")
                    }
                }.expectation {
                    it.existWithScrollDown("ATMs")
                }
            }
            case(2) {
                expectation {
                    existWithScrollUp("Restaurants")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun detectWithScrollDown_detectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                    withScrollRight("Restaurants") {
                        flickAndGoRight(repeat = 2)
                        tap("More")
                    }
                }.expectation {
                    it.detectWithScrollDown("ATMs")
                        .isFound.thisIsTrue("ATMs found")
                }
            }
            case(2) {
                expectation {
                    detectWithScrollUp("Restaurants")
                        .isFound.thisIsTrue("Restaurants found")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun canDetectWithScrollDown_canDetectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                    withScrollRight("Restaurants") {
                        flickAndGoRight(repeat = 2)
                        tap("More")
                    }
                }.expectation {
                    it.detect("Restaurants")
                        .canDetectWithScrollDown("ATMs")
                        .thisIsTrue("ATMs found")
                }
            }
            case(2) {
                expectation {
                    canDetectWithScrollUp("Restaurants")
                        .thisIsTrue("Restaurants found")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun detectWithoutScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.action {
                    withScrollRight("Restaurants") {
                        v1 = detectWithoutScroll("More", throwsException = false)
                    }
                }.expectation {
                    v1.isFound.thisIsFalse("<More> not found")
                }
            }
            case(2) {
                action {
                    assertThatThrownBy {
                        withScrollRight("Restaurants") {
                            v1 = detectWithoutScroll("More", throwsException = true)
                        }
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Element not found.(selector=<More>, expression=<More>)")
                }
            }
            case(3) {
                action {
                    withScrollRight("Restaurants") {
                        v1 = detect("More")
                    }
                }.expectation {
                    v1.isFound.thisIsTrue("<More> found")
                }
            }
        }
    }

}