package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/android/maps/testrun.properties")
class TestDriveScrollExtensionTest4 : UITest() {

    @Test
    @Order(10)
    fun withScrollRight_withScrollLeft() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    withScrollRight("#below_search_omnibox_container") {
                        exist("More")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollLeft("#below_search_omnibox_container") {
                        exist("Restaurants")
                    }
                }
            }
            case(3) {
                expectation {
                    select("<#below_search_omnibox_container>:scrollable")
                        .withScrollRight {
                            exist("More")
                        }
                }
            }
            case(4) {
                expectation {
                    select("<#below_search_omnibox_container>:scrollable")
                        .withScrollLeft {
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
                    it.select("Restaurants")
                        .existWithScrollRight("More")
                }
            }
            case(2) {
                expectation {
                    existWithScrollLeft("Restaurants")
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
                    it.select("Restaurants")
                        .selectWithScrollRight("More")
                        .isFound.thisIsTrue("More found")
                }
            }
            case(2) {
                expectation {
                    selectWithScrollLeft("Restaurants")
                        .isFound.thisIsTrue("Restaurants found")
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
                    it.select("Restaurants")
                        .canSelectWithScrollRight("More")
                        .thisIsTrue("More found")
                }
            }
            case(2) {
                expectation {
                    canSelectWithScrollLeft("Restaurants")
                        .thisIsTrue("Restaurants found")
                }
            }
        }
    }

}