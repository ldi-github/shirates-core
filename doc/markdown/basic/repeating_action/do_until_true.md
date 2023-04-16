# doUntilTrue

You can repeat action until the action returns true using **doUntilTrue** function.

## argument

| argument        | description                                                |
|:----------------|:-----------------------------------------------------------|
| waitSeconds     | Maximum seconds to exit.                                   |
| intervalSeconds | Interval seconds before next execution of action func.     |
| maxLoopCount    | Maximum loop count to break.                               |
| retryOnError    | Retry on error.                                            |
| throwOnFinally  | Throw if error on finally.                                 |
| refreshCache    | refreshCache() is called before next loop on true.         |
| onTimeout       | Function called on waitSeconds has elapsed.                |
| onMaxLoop       | Function called on over maxLoopCount.                      |
| onError         | Function called on exception thrown.                       |
| action          | Repeated action. Returns true(break loop) or false(retry). |

### DoUntilTrue1.kt

(`kotlin/tutorial/basic/DoUntilTrue1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.doUntilTrue
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class DoUntilTrue1 : UITest() {

    @Test
    @Order(10)
    fun doUntilTrue_action() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.launchApp()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    doUntilTrue {
                        it.swipeCenterToTop()
                        it.canSelect("System")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun doUntilTrue_onTimeout() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.launchApp()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    doUntilTrue(
                        waitSeconds = 3.0,
                        onTimeout = { c ->
                            SKIP_SCENARIO("Timeout. (waitSeconds=${c.waitSeconds})")
                        }
                    ) {
                        it.swipeCenterToTop()
                        it.canSelect("System")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun doUntilTrue_onMaxLoop() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.launchApp()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    doUntilTrue(
                        maxLoopCount = 2,
                        onMaxLoop = { c ->
                            SKIP_SCENARIO("MaxLoopCount. (maxLoopCount=${c.maxLoopCount})")
                        }
                    ) {
                        it.swipeCenterToTop()
                        it.canSelect("System")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun doUntilTrue_onError() {

        scenario {
            case(1) {
                expectation {
                    doUntilTrue(
                        onError = { c ->
                            output("${c.error} (${c.count})")
                            c.cancelRetry = c.count >= 3
                        },
                    ) {
                        it.select("#no-exist", waitSeconds = 0.0)   // throws TestDriverException
                        false
                    }
                }
            }
            case(2) {
                expectation {
                    doUntilTrue(
                        onError = { c ->
                            output("${c.error} (${c.count})")
                            c.cancelRetry = true
                        }
                    ) {
                        it.select("#no-exist", waitSeconds = 0.0)   // throws TestDriverException
                        false
                    }
                }
            }
        }
    }

}
```

### Link

- [index](../../index.md)
