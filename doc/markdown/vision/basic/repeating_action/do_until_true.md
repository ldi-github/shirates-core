# doUntilTrue (Vision)

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

## Sample code

[Getting samples](../../getting_samples.md)

### DoUntilTrue1.kt

(`src/test/kotlin/tutorial/basic/DoUntilTrue1.kt`)

```kotlin
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
                        it.canDetect("System")
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
                        it.canDetect("System")
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
                        it.canDetect("System")
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
                        it.detect("no-exist", waitSeconds = 0.0)   // throws TestDriverException
                        false
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../index.md)
