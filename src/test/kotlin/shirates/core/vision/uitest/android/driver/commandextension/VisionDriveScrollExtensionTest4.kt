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
class VisionDriveScrollExtensionTest4 : VisionTest() {

    @Test
    @Order(10)
    fun withScrollRight_withScrollLeft() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    withScrollRight("Restaurants") {
                        exist("More")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollLeft("More") {
                        exist("Restaurants")
                    }
                }
            }
            case(3) {
                expectation {
                    withScrollRight("Restaurants") {
                        exist("More")
                    }
                }
            }
            case(4) {
                expectation {
                    withScrollLeft("More") {
                        exist("Restaurants")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existWithScrollRight_existWithScrollLeft() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.detect("Restaurants")
                        .existWithScrollRight("More")
                }
            }
            case(2) {
                expectation {
                    it.detect("More")
                        .existWithScrollLeft("Restaurants")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun selectWithScrollRight_selectWithScrollLeft() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.detect("Restaurants").onLine {
                        it.detectWithScrollRight("More")
                            .isFound.thisIsTrue("More found")
                    }
                }
            }
            case(2) {
                expectation {
                    it.detect("More").onLine {
                        it.detectWithScrollLeft("Restaurants")
                            .isFound.thisIsTrue("Restaurants found")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun canSelectWithScrollRight_canSelectWithScrollLeft() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.detect("Restaurants").onLine {
                        it.canDetectWithScrollRight("More")
                            .thisIsTrue("More found")
                    }
                }
            }
            case(2) {
                expectation {
                    it.detect("More").onLine {
                        it.canDetectWithScrollLeft("Restaurants")
                            .thisIsTrue("Restaurants found")
                    }
                }
            }
        }
    }

}