package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver.androidDriver
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Drivers1 : UITest() {

    @Test
    @Order(10)
    fun findElement() {

        // AppiumDriver
        val e1 = appiumDriver.findElement(By.className("android.widget.TextView"))
        println("e1.text=${e1.text}")

        // AndroidDriver
        val e2 = androidDriver.findElement(By.className("android.widget.TextView"))
        println("e2.text=${e2.text}")
    }
}