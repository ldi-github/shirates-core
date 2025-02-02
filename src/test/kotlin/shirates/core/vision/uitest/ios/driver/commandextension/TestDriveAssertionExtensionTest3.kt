package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExistInScanResults
import shirates.core.driver.commandextension.existAllInScanResults
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.scanElements
import shirates.core.driver.testDrive
import shirates.core.exception.TestNGException
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveAssertionExtensionTest3 : VisionTest() {

    @Test
    @Order(110)
    fun existAllInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                    testDrive.scanElements()
                }.expectation {
                    testDrive.existAllInScanResults(
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
            case(2) {
                expectation {
                    assertThatThrownBy {
                        testDrive.existAllInScanResults(
                            "Settings",
                            "General",
                            "Accessibility",
                            "Action Button",
                            "Apple Intelligence & Siri",
                            "Camera",
                            "Developer",
                            "no exist",
                        )
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                }
            }
        }
    }

    @Test
    @Order(120)
    fun dontExist() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.dontExist("no exist")

                    assertThatThrownBy {
                        it.dontExist("General")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<General> does not exist")
                }
            }
        }
    }

    @Test
    @Order(130)
    fun dontExistWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
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
                            it.dontExist("Developer")
                        }
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Developer> does not exist")
                }
            }
        }
    }

    @Test
    @Order(140)
    fun dontExistWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
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
                            it.dontExist("General")
                        }
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<General> does not exist")
                }
            }
        }
    }

    @Test
    @Order(150)
    fun dontExistAll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.dontExistAll("no exist", "void", "something")

                    assertThatThrownBy {
                        it.dontExistAll("no exist", "void", "General", "something")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<General> does not exist")
                }
            }
        }
    }

    @Test
    @Order(160)
    fun dontExistInScanResults() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[iOS Settings Top Screen]")
                            .scanElements()
                    }.expectation {
                        it.dontExistInScanResults("no exist")

                        assertThatThrownBy {
                            it.dontExistInScanResults("Developer")
                        }.isInstanceOf(TestNGException::class.java)
                            .hasMessage("<Developer> doest not exist in scan results")
                    }
                }
            }
        }
    }
}