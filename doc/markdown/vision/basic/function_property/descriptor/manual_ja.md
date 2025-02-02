# manual

一般的には何らかの理由により手動テストケースの全てを自動化することはできません。

**manual**関数を使用すると手動による手続きを記述することができます。

## 例

### Manual1.kt

(`kotlin/tutorial/basic/Manual1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.manual
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Manual1 : UITest() {

    @Test
    @Order(10)
    fun manualTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.manual("Animation should be displayed on start up.")
                }
            }
        }

    }
}
```

### Link

- [index](../../../index_ja.md)

