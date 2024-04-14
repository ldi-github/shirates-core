# スクリーンハンドラー (onScreen関数)

[イレギュラーハンドラー](irregular_handler_ja.md) はコマンド実行の前に呼び出され、テストセッション内でグローバルに有効になります。

**スクリーンハンドラー** は、currentScreenが変更される時に呼び出され、テストシナリオの一部で有効にすることができます。

パフォーマンス上の理由から、すべてのイレギュラーな手続きをグローバルハンドラで実装するのは良いアイデアではありません。

**onScreen関数** を使用することで、テストシナリオで必要な画面に対してイレギュラーな手続きを実装することができます。

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
class ScreenHandler1_ja : UITest() {

    @Test
    @Order(10)
    fun onScreen1() {

        onScreen("[ネットワークとインターネット]") { c ->
            printWarn("${c.screenName} が表示されました。")
            c.removeHandler()
        }
        onScreen("[System Screen]") { c ->
            printWarn("${c.screenName} が表示されました。")
            c.removeHandler()
        }

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    it.tap("[ネットワークとインターネット]")
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }

            case(2) {
                condition {
                    it.pressBack()
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    it.tapWithScrollDown("[システム]")
                }.expectation {
                    it.screenIs("[システム画面]")
                }
            }
        }
    }

}
```

## disableScreenHandler(), enableScreenHandler()

これらの関数を使用することでスクリーンハンドラーを無効化または有効化することができます。

### Link

- [index](../../index_ja.md)
