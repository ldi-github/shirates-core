package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveAssertionExtensionTest3 : VisionTest() {

    @Test
    fun dontExist() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExist("no exist")

                    assertThatThrownBy {
                        it.dontExist("Network & internet")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Network & internet> does not exist")
                }
            }
        }
    }

    @Test
    fun withScrollDown_dontExist() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.dontExist("no exist")
                    }
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    assertThatThrownBy {
                        withScrollDown {
                            it.dontExist("System")
                        }
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<System> does not exist")
                }
            }

        }
    }

    @Test
    fun withScrollUp_dontExist() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollUp {
                        it.dontExist("no exist")
                    }
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    assertThatThrownBy {
                        withScrollUp {
                            it.dontExist("Network & internet")
                        }
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Network & internet> does not exist")
                }
            }
        }
    }

    @Test
    fun withScrollDown_existWithoutScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown() {
                        it.existWithoutScroll("Network & internet")    // OK
                        assertThatThrownBy {
                            it.existWithoutScroll("System")    // NG
                        }.isInstanceOf(TestNGException::class.java)
                            .hasMessage("<System> exists")
                    }
                }
            }
        }
    }

    @Test
    fun withScrollUp_dontExistWithoutScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExistWithoutScroll("Display")    // OK
                        assertThatThrownBy {
                            it.dontExistWithoutScroll("System")    // NG
                        }.isInstanceOf(TestNGException::class.java)
                            .hasMessage("<System> does not exist")
                    }
                }
            }
        }
    }

//    @Test
//    fun dontExistAll() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                }.expectation {
//                    it.dontExistAll("no exist", "void", "something")
//                }
//            }
//            case(2) {
//                expectation {
//                    assertThatThrownBy {
//                        it.dontExistAll("no exist", "void", "Battery", "something")
//                    }.isInstanceOf(TestNGException::class.java)
//                        .hasMessage("<Battery> does not exist")
//                }
//            }
//        }
//    }

//    @Test
//    fun dontExistInScanResults() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                    testDrive.scanElements()
//                }.expectation {
//                    testDrive.dontExistInScanResults("no exist")
//
//                    assertThatThrownBy {
//                        testDrive.dontExistInScanResults("Battery")
//                    }.isInstanceOf(TestNGException::class.java)
//                        .hasMessage("<Battery> doest not exist in scan results")
//                }
//            }
//        }
//    }

    @Test
    fun textIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .detect("Battery")
                }.expectation {
                    it.textIs("Battery")

                    assertThat(it.hasError).isFalse()
                    assertThatThrownBy {
                        it.textIs("Battery？")
                    }.isInstanceOf(TestNGException::class.java).hasMessageContaining(
                        message(
                            id = "textIs",
                            subject = "<Battery>",
                            expected = "Battery？"
                        )
                    )
                }
            }

        }
    }

//    @Test
//    fun textIsEmpty() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                        .detect("Search settings")
//                }.expectation {
//                    it.textIsEmpty()
//                    assertThat(it.hasError).isFalse()
//
//                    assertThatThrownBy {
//                        val e2 = it.detect("Search settings")
//                        e2.textIsEmpty()
//                    }.isInstanceOf(TestNGException::class.java)
//                        .hasMessageContaining(
//                            Message.message(id = "textIsEmpty", subject = "<#search_action_bar_title>")
//                                    + " (actual=\"Search settings\""
//                        )
//                }
//            }
//
//        }
//    }

}