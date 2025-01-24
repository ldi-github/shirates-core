# Screen Handler (onScreen function) (Classic)

[Irregular Handler](irregular_handler.md) is called on before commands, and it takes effects global in the test session.

**Screen handler** is called on changing current screen name, and it takes effects local in the test scenario.

It's not a good idea to implement all irregular procedures in the global handler for performance reasons.

You can use **onScreen** function to implement irregular procedures for the screens required in the test scenario .

### OnScreen1.kt

(`kotlin/tutorial/inaction/ScreenHandler1.kt`)

```kotlin
package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.eventextension.onScreen
import shirates.core.logging.printWarn
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScreenHandler1 : UITest() {

    @Test
    @Order(10)
    fun onScreen1() {

        onScreen("[Network & internet Screen]") { c ->
            printWarn("${c.screenName} is displayed.")
            c.removeHandler()
        }
        onScreen("[System Screen]") { c ->
            printWarn("${c.screenName} is displayed.")
            c.removeHandler()
        }

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("[Network & internet]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }

            case(2) {
                condition {
                    it.pressBack()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tapWithScrollDown("[System]")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

}
```

## disableScreenHandler(), enableScreenHandler()

You can disable or enable screen handler by these functions.

### Link

- [index](../../index.md)
