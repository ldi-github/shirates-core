# info, warn

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
                    info("info")
                    warn("warn")
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
