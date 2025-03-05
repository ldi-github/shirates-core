package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.InvalidSelectorException
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.findWebElement
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.classic
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionDriveFindWebElementTest : VisionTest() {

    @Test
    @Order(10)
    fun findWebElementTest() {

        scenario {
            case(1, "id") {
                expectation {
                    classic.findWebElement("#com.android.settings:id/homepage_title").getAttribute("text")
                        .thisIs("Settings")
                    classic.findWebElement("#homepage_title").getAttribute("text").thisIs("Settings")
                }
            }
            case(2, "className") {
                expectation {
                    classic.findWebElement(".androidx.cardview.widget.CardView").getAttribute("resource-id")
                        .thisIs("com.android.settings:id/search_bar")
                    classic.findWebElement(".android.widget.ImageView&&[1]").getAttribute("resource-id")
                        .thisIs("com.android.settings:id/account_avatar")
                }
            }
            case(3, "literal") {
                expectation {
                    classic.findWebElement("literal=Network & internet").getAttribute("text")
                        .thisIs("Network & internet")
                }
            }
            case(4, "text") {
                expectation {
                    classic.findWebElement("Network & internet").getAttribute("text").thisIs("Network & internet")
                    classic.findWebElement("Network &*").getAttribute("text").thisIs("Network & internet")
                    classic.findWebElement("*work & inter*").getAttribute("text").thisIs("Network & internet")
                    classic.findWebElement("*& internet").getAttribute("text").thisIs("Network & internet")
                    assertThatThrownBy {
                        classic.findWebElement("textMatches=^Net.*net$").getAttribute("text")
                            .thisIs("Network & internet")
                    }.isInstanceOf(InvalidSelectorException::class.java)
                        .hasMessageStartingWith("javax.xml.xpath.XPathExpressionException")
                }
            }
            case(5, "access") {
                expectation {
                    classic.findWebElement("@Profile picture, double tap to open Google Account")
                        .getAttribute("content-desc").thisIs("Profile picture, double tap to open Google Account")
                    classic.findWebElement("@Profile picture*").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    classic.findWebElement("@*file picture*").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    classic.findWebElement("@*to open Google Account").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    assertThatThrownBy {
                        classic.findWebElement("accessMatches=^Profile.*Account$").getAttribute("content-desc")
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
                    classic.findWebElement(".android.widget.TextView&&[999]")
                        .isEmpty.thisIsTrue()
                }
            }
            case(99) {
                expectation {
                    classic.findWebElement("#android:id/title&&.android.widget.TextView").getAttribute("text")
                        .thisIs("Network & internet")
                    classic.findWebElement("#android:id/title&&.android.widget.TextView&&[2]").getAttribute("text")
                        .thisIs("Connected devices")
                }
            }
        }
    }

}