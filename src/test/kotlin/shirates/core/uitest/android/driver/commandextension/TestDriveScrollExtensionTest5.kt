package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/android/maps/testrun.properties")
class TestDriveScrollExtensionTest5 : UITest() {

    @Test
    @Order(10)
    fun withScrollDown_withScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                    withScrollRight("#below_search_omnibox_container") {
                        flickAndGoRight(repeat = 2)
                        tap("More")
                    }
                }.expectation {
                    withScrollDown("#explore_modules_list_layout_recyclerView") {
                        exist("Car wash")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollUp("#explore_modules_list_layout_recyclerView") {
                        exist("Dessert")
                    }
                }
            }
            case(3) {
                expectation {
                    select("<Food & Drink>:scrollable")
                        .withScrollDown {
                            exist("Gas")
                        }
                }
            }
            case(4) {
                expectation {
                    select("<Services>:scrollable")
                        .withScrollUp {
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
                    withScrollRight("#below_search_omnibox_container") {
                        flickAndGoRight(repeat = 2)
                        tap("More")
                    }
                }.expectation {
                    it.select("Restaurants")
                        .existWithScrollDown("ATMs")
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
                    withScrollRight("#below_search_omnibox_container") {
                        flickAndGoRight(repeat = 2)
                        tap("More")
                    }
                }.expectation {
                    it.select("Restaurants")
                        .selectWithScrollDown("ATMs")
                        .isFound.thisIsTrue("ATMs found")
                }
            }
            case(2) {
                expectation {
                    selectWithScrollUp("Restaurants")
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
                    withScrollRight("#below_search_omnibox_container") {
                        flickAndGoRight(repeat = 2)
                        tap("More")
                    }
                }.expectation {
                    it.select("Restaurants")
                        .canSelectWithScrollDown("ATMs")
                        .thisIsTrue("ATMs found")
                }
            }
            case(2) {
                expectation {
                    canSelectWithScrollUp("Restaurants")
                        .thisIsTrue("Restaurants found")
                }
            }
        }
    }

}