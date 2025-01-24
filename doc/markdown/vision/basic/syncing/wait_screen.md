# waitScreen, waitScreenOf (Shirates/Vision)

You can use **waitScreen** function to wait until specified screen is displayed.

To specify multiple candidates of screen names, use **waitScreenOf** instead.

Calling without `waitSeconds` argument uses default duration of `waitSecondsOnIsScreen`.

See Also [Parameters](../parameter/parameters.md)

## waitScreen

### WaitScreen1.kt

(`kotlin/tutorial/basic/WaitScreen1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class WaitScreen1 : UITest() {

    @Test
    @Order(10)
    fun waitScreen() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp()
                }.action {
                    it.waitScreen("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun waitScreen_ERROR() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp()
                }.action {
                    it.waitScreen("[Network & internet Screen]")
                }
            }
        }
    }

...

}
```

## waitScreenOf

### WaitScreen1.kt

(`kotlin/tutorial/basic/WaitScreen1.kt`)

```kotlin
    @Test
    @Order(30)
    fun waitScreenOf() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp()
                }.action {
                    it.waitScreenOf(
                        "[Android Settings Top Screen]",
                        "[Network & internet Screen]",
                        "[Connected devices Screen]"
                    )
                    output("screenName=${it.screenName}")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun waitScreenOf_ERROR() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp("Chrome")
                }.action {
                    it.waitScreenOf(
                        "[Android Settings Top Screen]",
                        "[Network & internet Screen]",
                        "[Connected devices Screen]"
                    )
                }
            }
        }
    }
```

### Link

- [index](../../../index.md)
