package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveAssertionExtensionTest3 : UITest() {

    @Test
    @Order(110)
    fun existAllInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.existAllInScanResults(
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
                        it.existAllInScanResults(
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
                    it.dontExistWithScrollDown("no exist")
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    assertThatThrownBy {
                        it.dontExistWithScrollDown("Developer")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Developer> does not exist (scroll down)")
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
                    it.dontExistWithScrollUp("no exist")
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    assertThatThrownBy {
                        it.dontExistWithScrollUp("General")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<General> does not exist (scroll up)")
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