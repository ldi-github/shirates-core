package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.findWebElement
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsEmpty
import shirates.core.driver.testDrive
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveFindWebElementTest : VisionTest() {

    @Test
    @Order(10)
    fun findWebElementTest() {

        scenario {
            case(1, "id") {
                expectation {
                    testDrive.findWebElement("#Screen Time").getAttribute("name").thisIs("Screen Time")
                }
            }
            case(2, "className") {
                expectation {
                    testDrive.findWebElement(".XCUIElementTypeNavigationBar").getAttribute("type")
                        .thisIs("XCUIElementTypeNavigationBar")
                    testDrive.findWebElement(".XCUIElementTypeStaticText&&[2]").getAttribute("label")
                        .thisIs("Apple Account")
                }
            }
            case(3, "literal") {
                expectation {
                    testDrive.findWebElement("literal=Sign in to access your iCloud data, the App Store, Apple services, and more.")
                        .getAttribute("label")
                        .thisIs("Sign in to access your iCloud data, the App Store, Apple services, and more.")
                }
            }
            case(4, "text") {
                expectation {
                    testDrive.findWebElement("Apple Account").getAttribute("label").thisIs("Apple Account")
                    testDrive.findWebElement("Apple A*").getAttribute("label")
                        .thisIs("Apple Account, Sign in to access your iCloud data, the App Store, Apple services, and more.")
                    testDrive.findWebElement("*pple Acc*").getAttribute("label")
                        .thisIs("Apple Account, Sign in to access your iCloud data, the App Store, Apple services, and more.")
                    testDrive.findWebElement("*pple Account").getAttribute("label").thisIs("Apple Account")
                    testDrive.findWebElement("textMatches=^Apple .*Account$").getAttribute("label")
                        .thisIs("Apple Account")
                }
            }
            case(5, "access") {
                expectation {
                    testDrive.findWebElement("@Apple Account").getAttribute("name").thisIs("Apple Account")
                    testDrive.findWebElement("@Apple A*").getAttribute("name").thisIs("Apple Account")
                    testDrive.findWebElement("@*pple Accoun*").getAttribute("name").thisIs("Apple Account")
                    testDrive.findWebElement("@*pple Account").getAttribute("name").thisIs("Apple Account")
                    testDrive.findWebElement("accessMatches=^Apple .*Account$").getAttribute("name")
                        .thisIs("Apple Account")
                }
            }
            case(6, "value") {
                expectation {
                    testDrive.findWebElement("value=Apple Account").getAttribute("value").thisIs("Apple Account")
                    testDrive.findWebElement("valueStartsWith=Apple A").getAttribute("value").thisIs("Apple Account")
                    testDrive.findWebElement("valueContains=pple Accoun").getAttribute("value").thisIs("Apple Account")
                    testDrive.findWebElement("valueEndsWith=le Account").getAttribute("value").thisIs("Apple Account")
                    testDrive.findWebElement("valueMatches=^Apple .*Account$").getAttribute("value")
                        .thisIs("Apple Account")
                }
            }
            case(7, "pos") {
                expectation {
                    testDrive.findWebElement(".XCUIElementTypeStaticText&&[1]").getAttribute("label").thisIs("Settings")
                    testDrive.findWebElement(".XCUIElementTypeStaticText&&[2]").getAttribute("label")
                        .thisIs("Apple Account")
                    testDrive.findWebElement(".XCUIElementTypeStaticText&&[999]").thisIsEmpty()
                }
            }
        }
    }

}