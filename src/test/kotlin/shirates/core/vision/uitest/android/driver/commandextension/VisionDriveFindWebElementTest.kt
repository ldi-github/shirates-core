package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.InvalidSelectorException
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.findWebElement
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.testDrive
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionDriveFindWebElementTest : VisionTest() {

    @Test
    @Order(10)
    fun findWebElementTest() {

        scenario {
            case(1, "id") {
                expectation {
                    testDrive.findWebElement("#com.android.settings:id/homepage_title").getAttribute("text")
                        .thisIs("Settings")
                    testDrive.findWebElement("#homepage_title").getAttribute("text").thisIs("Settings")
                }
            }
            case(2, "className") {
                expectation {
                    testDrive.findWebElement(".androidx.cardview.widget.CardView").getAttribute("resource-id")
                        .thisIs("com.android.settings:id/search_bar")
                    testDrive.findWebElement(".android.widget.ImageView&&[1]").getAttribute("resource-id")
                        .thisIs("com.android.settings:id/account_avatar")
                }
            }
            case(3, "literal") {
                expectation {
                    testDrive.findWebElement("literal=Network & internet").getAttribute("text")
                        .thisIs("Network & internet")
                }
            }
            case(4, "text") {
                expectation {
                    testDrive.findWebElement("Network & internet").getAttribute("text").thisIs("Network & internet")
                    testDrive.findWebElement("Network &*").getAttribute("text").thisIs("Network & internet")
                    testDrive.findWebElement("*work & inter*").getAttribute("text").thisIs("Network & internet")
                    testDrive.findWebElement("*& internet").getAttribute("text").thisIs("Network & internet")
                    assertThatThrownBy {
                        testDrive.findWebElement("textMatches=^Net.*net$").getAttribute("text")
                            .thisIs("Network & internet")
                    }.isInstanceOf(InvalidSelectorException::class.java)
                        .hasMessageStartingWith("javax.xml.xpath.XPathExpressionException")
                }
            }
            case(5, "access") {
                expectation {
                    testDrive.findWebElement("@Profile picture, double tap to open Google Account")
                        .getAttribute("content-desc").thisIs("Profile picture, double tap to open Google Account")
                    testDrive.findWebElement("@Profile picture*").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    testDrive.findWebElement("@*file picture*").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    testDrive.findWebElement("@*to open Google Account").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    assertThatThrownBy {
                        testDrive.findWebElement("accessMatches=^Profile.*Account$").getAttribute("content-desc")
                            .thisIs("Profile picture, double tap to open Google Account")
                    }.isInstanceOf(InvalidSelectorException::class.java)
                        .hasMessageStartingWith("javax.xml.xpath.XPathExpressionException")
                }
            }
            case(6, "value") {
                expectation {
                    // Android does not have "value" attribute
                }
            }
            case(7, "pos") {
                expectation {
                    testDrive.findWebElement(".android.widget.TextView&&[999]")
                        .isEmpty.thisIsTrue()
                }
            }
            case(99) {
                expectation {
                    testDrive.findWebElement("#android:id/title&&.android.widget.TextView").getAttribute("text")
                        .thisIs("Network & internet")
                    testDrive.findWebElement("#android:id/title&&.android.widget.TextView&&[2]").getAttribute("text")
                        .thisIs("Connected devices")
                }
            }
        }
    }

}