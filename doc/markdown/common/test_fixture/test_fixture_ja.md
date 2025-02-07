# テストフィクスチャ (Vision/Classic)

`UITest`クラスまたは`VisionTest`クラスの**テストフィクスチャ**を使用するとテスト環境のセットアップやクリーンアップを行うことができます。

## テストフィクスチャのイベント関数

| 関数                  | 呼ばれる時                                                                                | AppiumServer |
|:--------------------|:-------------------------------------------------------------------------------------|:-------------|
| beforeAll           | **AppiumServerのセットアップが完了する前**, テストクラスの最初のテスト関数が呼ばれる前                                 | 利用不可         |
| beforeAllAfterSetup | **AppiumServerのセットアップが完了した後**, before the first function of the test class is called | 利用可          |
| beforeEach          | 各テスト関数が呼ばれる前                                                                         | 利用可          |
| afterEach           | 各テスト関数が呼ばれた後                                                                         | 利用可          |
| afterAll            | 全てのテスト関数が呼ばれた後                                                                       | 利用可          |
| finally             | **AppiumServerがシャットダウンした後**                                                          | 利用不可         |

## 例

### TestFixtureEvent1.kt

(kotlin/tutorial/basic/TestFixtureEvent1.kt)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.driver
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.testcode.VisionTest

class TestFixtureEvent1 : VisionTest() {

    private fun printEvent(eventName: String) {

        println("[$eventName] --------------------------------------------------")

        try {
            driver.appiumDriver
            println("AppiumDriver: available")
        } catch (t: Throwable) {
            println("AppiumDriver: NOT available")
        }
    }

    override fun beforeAll(context: ExtensionContext?) {
        printEvent("beforeAll")
    }

    override fun beforeAllAfterSetup(context: ExtensionContext?) {
        printEvent("beforeAllAfterSetup")
    }

    override fun beforeEach(context: ExtensionContext?) {
        printEvent("beforeEach")
    }

    override fun afterEach(context: ExtensionContext?) {
        printEvent("afterEach")
    }

    override fun afterAll(context: ExtensionContext?) {
        printEvent("afterAll")
    }

    override fun finally() {
        printEvent("finally")
    }

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {}
    }

    @Test
    @Order(10)
    fun test1() {

        scenario {
            case(1) {
                expectation {
                    it.exist("ネットワークとインターネット")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun test2() {

        scenario {
            case(1) {
                expectation {
                    it.exist("接続設定")
                }
            }
        }
    }

}
```

### コンソール出力

```
...
[beforeAll] --------------------------------------------------
AppiumDriver: NOT available
...
[beforeAllAfterSetup] --------------------------------------------------
AppiumDriver: available
...
[beforeEach] --------------------------------------------------
AppiumDriver: available
...
[afterEach] --------------------------------------------------
AppiumDriver: available
...
[beforeEach] --------------------------------------------------
AppiumDriver: available
...
[afterEach] --------------------------------------------------
AppiumDriver: available
...
[afterAll] --------------------------------------------------
AppiumDriver: available
139	[00:00:25]	2025/02/07 16:39:05.030	{}	0	-	[info]	+1009	C	()	ログは次の場所に出力します。 file:////Users/wave1008/Downloads/TestResults/testConfig@a/2025-02-07_163839/TestFixtureEvent1/
Copying jar content _ReportScript.js to /Users/wave1008/Downloads/TestResults/testConfig@a/2025-02-07_163839/TestFixtureEvent1
Copying jar content _ReportStyle.css to /Users/wave1008/Downloads/TestResults/testConfig@a/2025-02-07_163839/TestFixtureEvent1
Loading: /Users/wave1008/Downloads/TestResults/testConfig@a/2025-02-07_163839/TestFixtureEvent1/TestLog(commandList)_20250207163839.log
Saved: /Users/wave1008/Downloads/TestResults/testConfig@a/2025-02-07_163839/TestFixtureEvent1/TestFixtureEvent1@a.xlsx

140	[00:00:26]	2025/02/07 16:39:05.958	{}	0	-	[info]	+928	C	()	Quitting TestDriver.
141	[00:00:26]	2025/02/07 16:39:06.091	{}	0	-	[info]	+133	C	()	テストクラスの実行が完了しました。(処理時間: 26.6 sec)
[finally] --------------------------------------------------
AppiumDriver: NOT available
Disconnected from the target VM, address: '127.0.0.1:55727', transport: 'socket'

Process finished with exit code 0
```

### Link

- [index(Vision)](../../index_ja.md)
- [index(Classic)](../../classic/index_ja.md)
