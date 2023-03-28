package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveFindWebElementTest : UITest() {

    @Test
    @Order(10)
    fun findWebElementTest() {

        scenario {
            case(1, "id") {
                expectation {
                    it.findWebElement("#android:id/title").getAttribute("text").thisIs("Network & internet")
                }
            }
            case(2, "className") {
                expectation {
                    it.findWebElement(".android.widget.ScrollView").getAttribute("resource-id")
                        .thisIs("com.android.settings:id/main_content_scrollable_container")
                    it.findWebElement(".android.widget.ImageView&&[1]").getAttribute("resource-id")
                        .thisIs("com.android.settings:id/account_avatar")
                }
            }
            case(3, "literal") {
                expectation {
                    it.findWebElement("literal=Network & internet").getAttribute("text").thisIs("Network & internet")
                }
            }
            case(4, "text") {
                expectation {
                    it.findWebElement("Network & internet").getAttribute("text").thisIs("Network & internet")
                    it.findWebElement("Network &*").getAttribute("text").thisIs("Network & internet")
                    it.findWebElement("*work & inter*").getAttribute("text").thisIs("Network & internet")
                    it.findWebElement("*& internet").getAttribute("text").thisIs("Network & internet")
                    it.findWebElement("textMatches=^Net.*net$").getAttribute("text").thisIs("Network & internet")
                }
            }
            case(5, "access") {
                expectation {
                    it.findWebElement("@Profile picture, double tap to open Google Account")
                        .getAttribute("content-desc").thisIs("Profile picture, double tap to open Google Account")
                    it.findWebElement("@Profile picture*").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    it.findWebElement("@*file picture*").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    it.findWebElement("@*to open Google Account").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                    it.findWebElement("accessMatches=^Profile.*Account$").getAttribute("content-desc")
                        .thisIs("Profile picture, double tap to open Google Account")
                }
            }
            case(6, "value") {
                expectation {
                    it.findWebElement("Network & internet").getAttribute("text").thisIs("Network & internet")
                    it.findWebElement("Network &*").getAttribute("text").thisIs("Network & internet")
                    it.findWebElement("*work & inter*").getAttribute("text").thisIs("Network & internet")
                    it.findWebElement("*& internet").getAttribute("text").thisIs("Network & internet")
                    it.findWebElement("textMatches=^Net.*net$").getAttribute("text").thisIs("Network & internet")
                }
            }
            case(7, "pos") {
                expectation {
                    assertThatThrownBy {
                        it.findWebElement(".android.widget.TextView&&[999]")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Element not found. (.android.widget.TextView&&[999])")
                }
            }
            case(99) {
                expectation {
                    it.findWebElement("#android:id/title&&.android.widget.TextView").getAttribute("text")
                        .thisIs("Network & internet")
                    it.findWebElement("#android:id/title&&.android.widget.TextView&&[2]").getAttribute("text")
                        .thisIs("Connected devices")
                }
            }
        }
    }


}