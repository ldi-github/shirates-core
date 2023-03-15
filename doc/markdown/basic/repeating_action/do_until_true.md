# doUntilTrue

You can repeat action until the action returns true using **doUntilTrue** function.

## argument

| argument        | description                                           |
|:----------------|:------------------------------------------------------|
| waitSeconds     | Maximum seconds to exit.                              |
| intervalSeconds | Interval seconds before next execution of actionFunc. |
| maxLoopCount    | Maximum loop count to exit.                           |
| refreshCache    | refreshCache() is called before next loop on true.    |
| onTimeoutFunc   | Called on waitSeconds has elapsed.                    |
| onMaxLoopFunc   | Called on over maxLoopCount.                          |
| actionFunc      | Repeated action. Returns true or false.               |

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
    fun doUntilTrue() {

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
    fun doUntilTrue_timeout() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.launchApp()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    doUntilTrue(
                        waitSeconds = 3.0,
                        onTimeoutFunc = { sc ->
                            SKIP_SCENARIO("Timeout. (waitSeconds=${sc.waitSeconds})")
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
    fun doUntilTrue_maxLoop() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                    it.launchApp()
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    doUntilTrue(
                        maxLoopCount = 2,
                        onMaxLoopFunc = { sc ->
                            SKIP_SCENARIO("MaxLoopCount. (maxLoopCount=${sc.maxLoopCount})")
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

}
```

### Link

- [index](../../index.md)
