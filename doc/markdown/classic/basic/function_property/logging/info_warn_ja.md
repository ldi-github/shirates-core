# info, warn (Classic)

これらの関数を使用すると追加のメッセージを出力することができます。

| 関数   | 説明                                      |
|:-----|:----------------------------------------|
| info | **detail**のレポートでのみ出力されます                |
| warn | **simple** と **detail** の両方のレポートで出力されます |

## 例

### InfoAndWarn1.kt

(`kotlin/tutorial/basic/InfoAndWarn1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.logging.TestLog.info
import shirates.core.logging.TestLog.warn
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class InfoAndWarn1 : UITest() {

    @Test
    @Order(10)
    fun infoAndWarn1() {

        scenario {
            case(1) {
                action {
                    info("Nickname is one of key concepts of Shirates that makes test codes readable and easy to understand. You can define nicknames in nickname files for screens, elements, apps, and test data items, then use them in test codes. Messages using nickname are so user-friendly that you can read them as natural language. Especially for screen elements, nickname hides complexity of implementation of finding elements, and absorbs the difference of Android platform and iOS platform. As a result, after you wrote a test code for one platform, you can add or modify to fill gaps to another platform with a little effort.")
                    warn("Nickname is one of key concepts of Shirates that makes test codes readable and easy to understand. You can define nicknames in nickname files for screens, elements, apps, and test data items, then use them in test codes. Messages using nickname are so user-friendly that you can read them as natural language. Especially for screen elements, nickname hides complexity of implementation of finding elements, and absorbs the difference of Android platform and iOS platform. As a result, after you wrote a test code for one platform, you can add or modify to fill gaps to another platform with a little effort.")
                }
            }
        }
    }

}
```

### Html-Report(detail)

![](../../_images/info_and_warn_detail.png)

infoとwarnの両方のメッセージが出力されます。

### Html-Report(simple)

![](../../_images/info_and_warn_simple.png)

warnのメッセージのみが出力されます。

### Link

- [index](../../../index_ja.md)
