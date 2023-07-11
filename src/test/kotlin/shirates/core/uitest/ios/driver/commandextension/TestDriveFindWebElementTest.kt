package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveFindWebElementTest : UITest() {

    @Test
    @Order(10)
    fun findWebElementTest() {

        scenario {
            case(1, "id") {
                expectation {
                    it.findWebElement("#SCREEN_TIME").getAttribute("name").thisIs("SCREEN_TIME")
                }
            }
            case(2, "className") {
                expectation {
                    it.findWebElement(".XCUIElementTypeNavigationBar").getAttribute("type")
                        .thisIs("XCUIElementTypeNavigationBar")
                    it.findWebElement(".XCUIElementTypeStaticText&&[2]").getAttribute("label")
                        .thisIs("Sign in to your iPhone")
                }
            }
            case(3, "literal") {
                expectation {
                    it.findWebElement("literal=Set up iCloud, the App Store, and more.").getAttribute("label")
                        .thisIs("Set up iCloud, the App Store, and more.")
                }
            }
            case(4, "text") {
                expectation {
                    it.findWebElement("Sign in to your iPhone").getAttribute("label").thisIs("Sign in to your iPhone")
                    it.findWebElement("Sign in to*").getAttribute("label").thisIs("Sign in to your iPhone")
                    it.findWebElement("*ign in to your iPho*").getAttribute("label").thisIs("Sign in to your iPhone")
                    it.findWebElement("*ign in to your iPhone").getAttribute("label").thisIs("Sign in to your iPhone")
                    it.findWebElement("textMatches=^Sign.*iPhone$").getAttribute("label")
                        .thisIs("Sign in to your iPhone")
                }
            }
            case(5, "access") {
                expectation {
                    it.findWebElement("@Sign in to your iPhone").getAttribute("name").thisIs("Sign in to your iPhone")
                    it.findWebElement("@Sign in to*").getAttribute("name").thisIs("Sign in to your iPhone")
                    it.findWebElement("@*ign in to your iPho*").getAttribute("name").thisIs("Sign in to your iPhone")
                    it.findWebElement("@*ign in to your iPhone").getAttribute("name").thisIs("Sign in to your iPhone")
                    it.findWebElement("accessMatches=^Sign.*iPhone$").getAttribute("name")
                        .thisIs("Sign in to your iPhone")
                }
            }
            case(6, "value") {
                expectation {
                    it.findWebElement("value=Sign in to your iPhone").getAttribute("value")
                        .thisIs("Sign in to your iPhone")
                    it.findWebElement("valueStartsWith=Sign in to").getAttribute("value")
                        .thisIs("Sign in to your iPhone")
                    it.findWebElement("valueContains=ign in to your iPho").getAttribute("value")
                        .thisIs("Sign in to your iPhone")
                    it.findWebElement("valueEndsWith=ign in to your iPhone").getAttribute("value")
                        .thisIs("Sign in to your iPhone")
                    it.findWebElement("valueMatches=^Sign.*iPhone$").getAttribute("value")
                        .thisIs("Sign in to your iPhone")
                }
            }
            case(7, "pos") {
                expectation {
                    it.findWebElement(".XCUIElementTypeStaticText&&[1]").getAttribute("label").thisIs("Settings")
                    it.findWebElement(".XCUIElementTypeStaticText&&[2]").getAttribute("label")
                        .thisIs("Sign in to your iPhone")
                    it.findWebElement(".XCUIElementTypeStaticText&&[999]").thisIsEmpty()
                }
            }
            case(99) {
                expectation {
                    it.findWebElement("#VPN&&[2]").getAttribute("type")
                        .thisIs("XCUIElementTypeStaticText")
                }
            }
        }
    }

}