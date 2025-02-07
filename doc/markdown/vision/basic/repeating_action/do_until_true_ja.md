# doUntilTrue (Vision)

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

### サンプルコード

[サンプルの入手](../../getting_samples_ja.md)

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
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    doUntilTrue {
                        it.scrollDown()
                        it.canDetect("システム")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[システム画面]")
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
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    doUntilTrue(
                        waitSeconds = 3.0,
                        onTimeout = { c ->
                            SKIP_SCENARIO("Timeout. (waitSeconds=${c.waitSeconds})")
                        }
                    ) {
                        it.swipeCenterToTop()
                        it.canDetect("システム")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[システム画面]")
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
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    doUntilTrue(
                        maxLoopCount = 2,
                        onMaxLoop = { c ->
                            SKIP_SCENARIO("MaxLoopCount. (maxLoopCount=${c.maxLoopCount})")
                        }
                    ) {
                        it.swipeCenterToTop()
                        it.canDetect("システム")
                    }
                    it.tap()
                }.expectation {
                    it.screenIs("[システム画面]")
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

- [index](../../../index_ja.md)
