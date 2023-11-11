# 要素の選択と検証

要素を選択してそのプロパティを検証することができます。

### SelectAndAssert1.kt

(`kotlin/tutorial/basic/SelectAndAssert1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.textIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SelectAndAssert1 : UITest() {

    @Test
    @Order(10)
    fun selectAndAssert1_OK() {

        scenario {
            case(1) {
                expectation {
                    it.select("Settings")
                        .textIs("Settings")   // OK
                }
            }
        }
    }

    @Test
    @Order(20)
    fun selectAndAssert2_NG() {

        scenario {
            case(1) {
                expectation {
                    it.select("Settings")
                        .textIs("Network & internet")   // NG
                }
            }
        }
    }

}
```

上記の例では、 **select** 関数はtextが"Settings"である要素を検索し、最初に見つかったTestElementオブジェクトを返します。
TestElement は **textIs**
拡張関数で機能拡張されます。
text が期待値と一致する場合は、検証のログが以下のように出力されます。

```
[OK]	(textIs)	<Settings> is "Settings"
```

Shirates のAPIs は _fluent API_ としてデザインされているので、以下のように関数の呼び出しをチェーンすることができます。

```kotlin
it.select("Settings")
    .textIs("Settings")   // OK
    .textIs("Network & internet")   // NG
```

### Link

- [index](../../index_ja.md)
