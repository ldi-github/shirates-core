# knownIssue

**knownIssue**関数を使用して既知の問題を説明することができます。

## 例

### KnownIssues1.kt

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.knownIssue
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.manual
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class KnownIssues1 : UITest() {

    @Test
    @Order(10)
    fun knownIssue() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.manual("Animation should be displayed on start up.")
                        .knownIssue(
                            message = "Animation is not executed smoothly.",
                            ticketUrl = "https://issues.example.com/TICKET-1234"
                        )
                }
            }
        }

    }

}
```

**message** and **ticketUrl** は必須指定です。 ticketUrlが不要な場合は`ticketUrl = ""`を設定してください。

### Link

- [index](../../../index_ja.md)
