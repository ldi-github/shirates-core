# manual (Classic)

In general, you can not automate all of your manual test cases because of reasons. Instead, you can describe manual
procedure using **manual** function.

## Example

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

- [index](../../../index.md)

