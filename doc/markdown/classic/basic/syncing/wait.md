# wait (Classic)

Sometimes, waiting for some seconds is required in particular situation.

You can use **wait** function.

Calling without `waitSeconds` argument uses default duration of `shortWaitSeconds`.
You can configure parameter `shortWaitSeconds` in testrun files.

See Also [Parameters](../parameter/parameters.md)

### Wait1.kt

(`kotlin/tutorial/basic/Wait1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.describe
import shirates.core.driver.wait
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Wait1 : UITest() {

    @Test
    @Order(10)
    fun wait1() {

        scenario {
            case(1) {
                action {
                    describe("Wait for a short time.")
                        .wait()
                }
            }

            case(2) {
                action {
                    describe("Wait for 3.0 seconds.")
                        .wait(3.0)
                }
            }
        }
    }

}
```

### Link

- [index](../../index.md)
