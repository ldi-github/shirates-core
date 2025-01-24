# プラットフォームプロパティ (Classic)

これらのプロパティを使用してプラットフォームの情報を取得することができます。

## プロパティ

| プロパティ           | 説明                  |
|:----------------|:--------------------|
| platformName    | "android" または "ios" |
| platformVersion | OSのメジャーバージョン        |
| isAndroid       | Androidの場合にtrue     |
| isiOS           | iOSの場合にtrue         |
| isVirtualDevice | 仮想デバイスの場合にtrue      |
| isRealDevice    | 実デバイスの場合にtrue       |

### PlatformProperties1.kt

(`kotlin/tutorial/basic/PlatformProperties1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.platformMajorVersion
import shirates.core.driver.platformName
import shirates.core.driver.platformVersion
import shirates.core.testcode.UITest

@Testrun("unitTestData/testConfig/testrun/android_14/testrun.properties")
class PlatformProperties1 : UITest() {

    @Test
    @Order(10)
    fun platformProperties() {

        scenario {
            case(1) {
                expectation {
                    platformName
                        .thisIs("android")

                    platformVersion
                        .thisIs("14")

                    platformMajorVersion
                        .thisIs(14)

                    isAndroid
                        .thisIsTrue()

                    isiOS
                        .thisIsFalse()
                }
            }
        }
    }

}
```

### Link

- [index](../../../index_ja.md)

