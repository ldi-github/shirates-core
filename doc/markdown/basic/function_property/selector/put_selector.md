# putSelector

You can register selector on demand using `putSelector` function.

## Example 1

### PutSelector1.kt

(`kotlin/tutorial/basic/PutSelector1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.putSelector
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class PutSelector1 : UITest() {

    @Test
    @Order(10)
    fun putSelector() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    putSelector("[First Item]", "Network & internet")
                }.action {
                    it.tap("[First Item]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}
```

### Link

- [index](../../../index.md)

