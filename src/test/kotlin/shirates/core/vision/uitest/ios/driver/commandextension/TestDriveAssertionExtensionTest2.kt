package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.existInScanResults
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.scanElements
import shirates.core.exception.TestNGException
import shirates.core.testcode.Unstable
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveAssertionExtensionTest2 : VisionTest() {

    @Test
    @Order(60)
    fun existWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("Developer")
                }
            }
        }
    }

    @Unstable("Direct mode")
    @Test
    @Order(70)
    fun existWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.expectation {
                    it.existWithScrollUp("General")
                }
            }
        }
    }

    @Test
    @Order(80)
    fun existInScanResults() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .scanElements()
                    }.expectation {
                        it.existInScanResults("Developer")

                        assertThatThrownBy {
                            it.existInScanResults("no exist")
                        }.isInstanceOf(TestNGException::class.java)
                            .hasMessage("<no exist> exists")
                    }
                }
            }
        }
    }

    @Test
    @Order(90)
    fun existAll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.existAll(
                        "Settings",
                        "General",
                        "Accessibility",
                        "Action Button",
                        "Apple Intelligence & Siri",
                        "Camera"
                    )
                }
            }
            case(2) {
                expectation {
                    // Act, Assert
                    assertThatThrownBy {
                        it.existAll(
                            "Settings",
                            "General",
                            "Accessibility",
                            "Action Button",
                            "Apple Intelligence & Siri",
                            "Camera",
                            "no exist",
                        )
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                }
            }
            case(3) {
                expectation {
                    assertThatThrownBy {
                        it.existAll(
                            "Settings",
                            "General",
                            "Accessibility",
                            "Action Button",
                            "Apple Intelligence & Siri",
                            "Camera",
                            "no exist",
                        )
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                }
            }
        }
    }

    @Test
    @Order(100)
    fun existAllWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.existAll(
                            "Settings",
                            "General",
                            "Accessibility",
                            "Action Button",
                            "Apple Intelligence & Siri",
                            "Camera",
                            "Developer"
                        )
                    }
                }
            }
            case(2) {
                condition {
                    it.scrollToTop()
                }.expectation {
                    assertThatThrownBy {
                        withScrollDown {
                            it.existAll(
                                "Settings",
                                "General",
                                "Accessibility",
                                "Action Button",
                                "Apple Intelligence & Siri",
                                "Camera",
                                "Developer",
                                "no exist"
                            )
                        }
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                }
            }
        }
    }

}