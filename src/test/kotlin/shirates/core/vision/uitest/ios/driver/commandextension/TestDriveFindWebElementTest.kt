package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.findWebElement
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsEmpty
import shirates.core.driver.classic
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveFindWebElementTest : VisionTest() {

    @Test
    @Order(10)
    fun findWebElementTest() {

        scenario {
            case(1, "id") {
                expectation {
                    classic.findWebElement("#Screen Time").getAttribute("name").thisIs("Screen Time")
                }
            }
            case(2, "className") {
                expectation {
                    classic.findWebElement(".XCUIElementTypeNavigationBar").getAttribute("type")
                        .thisIs("XCUIElementTypeNavigationBar")
                    classic.findWebElement(".XCUIElementTypeStaticText&&[2]").getAttribute("label")
                        .thisIs("Apple Account")
                }
            }
            case(3, "literal") {
                expectation {
                    classic.findWebElement("literal=Sign in to access your iCloud data, the App Store, Apple services, and more.")
                        .getAttribute("label")
                        .thisIs("Sign in to access your iCloud data, the App Store, Apple services, and more.")
                }
            }
            case(4, "text") {
                expectation {
                    classic.findWebElement("Apple Account").getAttribute("label").thisIs("Apple Account")
                    classic.findWebElement("Apple A*").getAttribute("label")
                        .thisIs("Apple Account, Sign in to access your iCloud data, the App Store, Apple services, and more.")
                    classic.findWebElement("*pple Acc*").getAttribute("label")
                        .thisIs("Apple Account, Sign in to access your iCloud data, the App Store, Apple services, and more.")
                    classic.findWebElement("*pple Account").getAttribute("label").thisIs("Apple Account")
                    classic.findWebElement("textMatches=^Apple .*Account$").getAttribute("label")
                        .thisIs("Apple Account")
                }
            }
            case(5, "access") {
                expectation {
                    classic.findWebElement("@Apple Account").getAttribute("name").thisIs("Apple Account")
                    classic.findWebElement("@Apple A*").getAttribute("name").thisIs("Apple Account")
                    classic.findWebElement("@*pple Accoun*").getAttribute("name").thisIs("Apple Account")
                    classic.findWebElement("@*pple Account").getAttribute("name").thisIs("Apple Account")
                    classic.findWebElement("accessMatches=^Apple .*Account$").getAttribute("name")
                        .thisIs("Apple Account")
                }
            }
            case(6, "value") {
                expectation {
                    classic.findWebElement("value=Apple Account").getAttribute("value").thisIs("Apple Account")
                    classic.findWebElement("valueStartsWith=Apple A").getAttribute("value").thisIs("Apple Account")
                    classic.findWebElement("valueContains=pple Accoun").getAttribute("value").thisIs("Apple Account")
                    classic.findWebElement("valueEndsWith=le Account").getAttribute("value").thisIs("Apple Account")
                    classic.findWebElement("valueMatches=^Apple .*Account$").getAttribute("value")
                        .thisIs("Apple Account")
                }
            }
            case(7, "pos") {
                expectation {
                    classic.findWebElement(".XCUIElementTypeStaticText&&[1]").getAttribute("label").thisIs("Settings")
                    classic.findWebElement(".XCUIElementTypeStaticText&&[2]").getAttribute("label")
                        .thisIs("Apple Account")
                    classic.findWebElement(".XCUIElementTypeStaticText&&[999]").thisIsEmpty()
                }
            }
        }
    }

}