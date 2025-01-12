package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/maps/testrun.properties")
class TestDriveScrollExtensionTest5 : VisionTest() {

    @Test
    @Order(10)
    fun withScrollDown_withScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                    withScrollRight("Restaurants") {
                        flickAndGoRight(repeat = 2)
                        tap("More")
                    }
                }.expectation {
                    withScrollDown {
                        exist("Car wash")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollUp {
                        exist("Dessert")
                    }
                }
            }
            case(3) {
                expectation {
                    withScrollDown {
                        exist("Gas")
                    }
                }
            }
            case(4) {
                expectation {
                    withScrollUp {
                        exist("Coffee")
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
    fun selectWithScrollDown_selectWithScrollUp() {

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
    fun canSelectWithScrollDown_canSelectWithScrollUp() {

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

}