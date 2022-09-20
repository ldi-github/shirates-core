package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.TestDriver.iosDriver
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class Drivers2 : UITest() {

    @Test
    @Order(10)
    fun findElement() {

        // AppiumDriver
        val e1 = appiumDriver.findElement(By.className("XCUIElementTypeStaticText"))
        println("e1.text=${e1.text}")

        // IOSDriver
        val e2 = iosDriver.findElement(By.className("XCUIElementTypeStaticText"))
        println("e2.text=${e2.text}")
    }
}