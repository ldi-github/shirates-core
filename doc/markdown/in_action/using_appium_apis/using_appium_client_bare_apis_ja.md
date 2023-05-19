# Appium ClientのAPIを使用する

これらのプロパティを使用するとUiAutomator2ドライバーやiOSドライバーのAPIにアクセスできます。
共通のインターフェースはappiumDriverに実装されていますが、androidDriver/iosDriverを使用すればドライバーに固有のインターフェースを利用できます。
詳細はAppium Clientのドキュメントを参照してください。

## プロパティ

| プロパティ         | type                                                                                                                      | 説明                     |
|:--------------|:--------------------------------------------------------------------------------------------------------------------------|:-----------------------|
| appiumDriver  | [AppiumDriver](https://www.javadoc.io/doc/io.appium/java-client/latest/io/appium/java_client/AppiumDriver.html)           | Appium driverの実装       |
| androidDriver | [AndroidDriver](https://www.javadoc.io/doc/io.appium/java-client/latest/io/appium/java_client/android/AndroidDriver.html) | UiAutomator2 driverの実装 |
| iosDriver     | [IOSDriver](https://www.javadoc.io/doc/io.appium/java-client/latest/io/appium/java_client/ios/IOSDriver.html)             | iOS driverの実装          |

## 例

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

- [index](../../index_ja.md)
