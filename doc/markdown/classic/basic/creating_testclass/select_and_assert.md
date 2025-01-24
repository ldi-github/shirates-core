# Select and assert (Classic)

You can select an element and assert its properties.

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

In the above example, **select** function finds an element where text equals to "Settings"
and returns the TestElement object. TestElement is extended by **textIs** extension function. When the text equals to
expected value, assertion log like below is output.

```
[OK]	(textIs)	<Settings> is "Settings"
```

Shirates's APIs are designed as _fluent API_, so you can chain functions as follows.

```kotlin
it.select("Settings")
    .textIs("Settings")   // OK
    .textIs("Network & internet")   // NG
```

### Link

- [index](../../index.md)
