# doUntilTrue

**doUntilTrue**関数を使用して結果がtrueになるまでアクションを繰り返すことができます。

## 引数

| 引数              | 説明                                        |
|:----------------|:------------------------------------------|
| waitSeconds     | ループを抜けるまでの最大待ち時間（秒）                       |
| intervalSeconds | actionを次回実行する際のインターバル（秒）                  |
| maxLoopCount    | ループを抜けるまでの最大ループ数                          |
| retryOnError    | true: エラー時にリトライする                         |
| throwOnFinally  | エラー発生時に例外をスローする                           |
| refreshCache    | true: 次回のループ前にrefreshCache()関数を実行する       |
| onTimeout       | waitSecondsを経過した場合に呼ばれる処理                 |
| onMaxLoop       | maxLoopCountを超過した場合に呼ばれる処理                |
| onError         | 例外が発生した場合に呼ばれる処理                          |
| action          | 繰り返すアクション。trueの場合はループを抜ける。falseの場合はリトライする |

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
                        it.scrollDown()
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
                        throwOnOverMaxLoopCount = false,
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

- [index](../../index_ja.md)
