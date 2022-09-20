# Using Appium Client bare APIs

You can access APIs of UiAutomator2 Driver or XCUITest Driver via these properties. Common interfaces are implemented in
appiumDriver, but you can use driver specific interface on androidDriver/iosDriver. For more detail see appium client
document.

## properties

| properties    | type                                                                                                                      | description                        |
|:--------------|:--------------------------------------------------------------------------------------------------------------------------|:-----------------------------------|
| appiumDriver  | [AppiumDriver](https://www.javadoc.io/doc/io.appium/java-client/latest/io/appium/java_client/AppiumDriver.html)           | Appium driver implementation       |
| androidDriver | [AndroidDriver](https://www.javadoc.io/doc/io.appium/java-client/latest/io/appium/java_client/android/AndroidDriver.html) | UiAutomator2 driver implementation |
| iosDriver     | [IOSDriver](https://www.javadoc.io/doc/io.appium/java-client/latest/io/appium/java_client/ios/IOSDriver.html)             | XCUITest driver implementation     |

## Example

### Drivers1.kt

```kotlin
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
```

### Drivers2.kt

```kotlin
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
```

### Link

- [index](../../index.md)
