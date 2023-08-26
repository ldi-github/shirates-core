# On Error Handler

You can write error handling procedure using these handlers.

| Name                 | Description                                           |   
|----------------------|-------------------------------------------------------|
| onSelectErrorHandler | Called when select function could not find an element |
| onExistErrorHandler  | Called when exist function results in NG              |
| onScreenErrorHandler | Called when screenIs function results in NG           |

### OnSelectErrorHandler1.kt

(`kotlin/tutorial/inaction/OnSelectErrorHandler1.kt`)

#### Description

1. When you try to get an element of `Airplane mode` with the select function, `onSelectErrorHandler` is called
   because the element does not exist on the screen.
   is called because the element does not exist on the screen.
2. The element of `Network & internet` is tapped as the currently displayed screen is `[Android Settings Top Screen]`.
3. select is retried, and you can get the element of `Airplane mode` this time.

```kotlin
package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.tap
import shirates.core.driver.testContext
import shirates.core.logging.printWarn
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class OnSelectErrorHandler1 : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.onSelectErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printWarn("onSelectErrorHandler is called. Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }
    }

    @Test
    @Order(10)
    fun select() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("Airplane mode")  // onSelectErrorHandler is called
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun select2() {

        /**
         * You can override the handler
         */
        testContext.onSelectErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printWarn("onSelectErrorHandler is called. Redirecting to [Battery Screen]")
                it.tap("Battery")
            }
        }

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("Battery usage")  // onSelectErrorHandler is called
                }.expectation {
                    it.screenIs("[Battery Screen]")
                }
            }
        }
    }

}
```

### OnExistErrorHandler1.kt

(`kotlin/tutorial/inaction/OnExistErrorHandler1.kt`)

```kotlin
package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.logging.printWarn
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class OnExistErrorHandler1 : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.onExistErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printWarn("onExistErrorHandler is called. Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }
    }

    @Test
    @Order(10)
    fun exist() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Airplane mode")   // onExistErrorHandler is called
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}
```

![img.png](../_images/on_exist_error_handler.png)

### OnScreenErrorHandler1.kt

(`kotlin/tutorial/inaction/OnScreenErrorHandler1.kt`)

```kotlin
package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.logging.printWarn
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class OnScreenErrorHandler1 : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.onScreenErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printWarn("onScreenErrorHandler is called. Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }
    }

    @Test
    @Order(10)
    fun screenIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")  // onScreenErrorHandler is called
                }
            }
        }
    }

}
```

![img.png](../_images/on_screen_error_handler.png)

### Link

- [index](../../index.md)
