package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.findWebElement
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsEmpty
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveFindWebElementTest : UITest() {

    @Test
    @Order(10)
    fun findWebElementTest() {

        scenario {
            case(1, "id") {
                expectation {
                    it.findWebElement("#Screen Time").getAttribute("name").thisIs("Screen Time")
                }
            }
            case(2, "className") {
                expectation {
                    it.findWebElement(".XCUIElementTypeNavigationBar").getAttribute("type")
                        .thisIs("XCUIElementTypeNavigationBar")
                    it.findWebElement(".XCUIElementTypeStaticText&&[2]").getAttribute("label")
                        .thisIs("Apple Account")
                }
            }
            case(3, "literal") {
                expectation {
                    it.findWebElement("literal=Sign in to access your iCloud data, the App Store, Apple services, and more.")
                        .getAttribute("label")
                        .thisIs("Sign in to access your iCloud data, the App Store, Apple services, and more.")
                }
            }
            case(4, "text") {
                expectation {
                    it.findWebElement("Apple Account").getAttribute("label").thisIs("Apple Account")
                    it.findWebElement("Apple A*").getAttribute("label")
                        .thisIs("Apple Account, Sign in to access your iCloud data, the App Store, Apple services, and more.")
                    it.findWebElement("*pple Acc*").getAttribute("label")
                        .thisIs("Apple Account, Sign in to access your iCloud data, the App Store, Apple services, and more.")
                    it.findWebElement("*pple Account").getAttribute("label").thisIs("Apple Account")
                    it.findWebElement("textMatches=^Apple .*Account$").getAttribute("label").thisIs("Apple Account")
                }
            }
            case(5, "access") {
                expectation {
                    it.findWebElement("@Apple Account").getAttribute("name").thisIs("Apple Account")
                    it.findWebElement("@Apple A*").getAttribute("name").thisIs("Apple Account")
                    it.findWebElement("@*pple Accoun*").getAttribute("name").thisIs("Apple Account")
                    it.findWebElement("@*pple Account").getAttribute("name").thisIs("Apple Account")
                    it.findWebElement("accessMatches=^Apple .*Account$").getAttribute("name").thisIs("Apple Account")
                }
            }
            case(6, "value") {
                expectation {
                    it.findWebElement("value=Apple Account").getAttribute("value").thisIs("Apple Account")
                    it.findWebElement("valueStartsWith=Apple A").getAttribute("value").thisIs("Apple Account")
                    it.findWebElement("valueContains=pple Accoun").getAttribute("value").thisIs("Apple Account")
                    it.findWebElement("valueEndsWith=le Account").getAttribute("value").thisIs("Apple Account")
                    it.findWebElement("valueMatches=^Apple .*Account$").getAttribute("value").thisIs("Apple Account")
                }
            }
            case(7, "pos") {
                expectation {
                    it.findWebElement(".XCUIElementTypeStaticText&&[1]").getAttribute("label").thisIs("Settings")
                    it.findWebElement(".XCUIElementTypeStaticText&&[2]").getAttribute("label").thisIs("Apple Account")
                    it.findWebElement(".XCUIElementTypeStaticText&&[999]").thisIsEmpty()
                }
            }
        }
    }

}