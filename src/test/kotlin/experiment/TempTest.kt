package experiment

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.ios.IOSDriver
import org.junit.jupiter.api.Test
import org.openqa.selenium.remote.DesiredCapabilities
import java.net.URL

class TempTest {

    @Test
    fun androidDriver() {

        val caps = DesiredCapabilities()
        with(caps) {
            setCapability("appium:automationName", "UiAutomator2")
            setCapability("appium:appPackage", "com.android.settings")
            setCapability("appium:appActivity", "com.android.settings.Settings")
        }

        val androidDriver = AndroidDriver(URL("http://127.0.0.1:4723/"), caps)

        val e = androidDriver.findElement(AppiumBy.xpath("//*[@text='Network & internet']"))
        println(e.text)

        val e2 = androidDriver.findElement(AppiumBy.id("com.android.settings:id/search_bar"))
        e2.click()


        println()

    }

    @Test
    fun iosDriver() {

        val caps = DesiredCapabilities()
        with(caps) {
            setCapability("appium:automationName", "XCUITest")
            setCapability("deviceName", "iPhone 13")
            setCapability("appium:bundleId", "com.apple.Preferences")
        }

        val iosDriver = IOSDriver(URL("http://127.0.0.1:4723/"), caps)

        val e = iosDriver.findElement(AppiumBy.xpath("//*[@value='General']"))
        println(e.text)
    }
}