# hideKeyboard (Classic)

**hideKeyboard**関数を使用するとキーボードを非表示にすることができます。

## 例

#### AndroidKeyboard1.kt

(`kotlin/tutorial/basic/AndroidKeyboard1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AndroidKeyboard1 : UITest() {

    @Test
    @Order(10)
    fun hideKeyboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Home Screen]")
                        .isKeyboardShown.thisIsFalse("Keyboard is not shown")
                }.action {
                    it.tap("@Search")
                }.expectation {
                    it.keyboardIsShown()
                }
            }
            case(2) {
                action {
                    it.hideKeyboard()
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }
    }

}
```

####          

### Link

- [index](../../../index_ja.md)
