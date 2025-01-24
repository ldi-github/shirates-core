# イレギュラーハンドラー（グローバルハンドラー） (Classic)

スマートフォンアプリにおいては画面遷移の中でイレギュラーな画面が挿入されることが頻繁にあります。

以下のような画面が表示されたりされなかったりします。

- ポップアップダイアログ（位置情報許可、ネットワークエラー、Firebase In Appメッセージ、広告）
- 新機能のチュートリアル
- 通知バルーン
- その他

![](_images/location_permissions_ja.png)

これらのイレギュラーを処理するには、条件分岐を実装する必要があります。これは相当大変な作業です。

### AnnoyingEventHandling1_ja.kt

(`kotlin/tutorial/inaction/AnnoyingEventHandling1_ja.kt`)

```kotlin
/**
 * 面倒なイベントハンドリングの例1
 */
@Test
@Order(10)
fun annoyingEventHandling1() {

    scenario {
        case(1) {
            condition {
                it.macro("[画面A]")
                    .ifCanSelect("アプリの使用時のみ") {
                        it.tap()
                    }
            }.action {
                it.tap("[ボタン1]")
                    .ifCanSelect("アプリの使用時のみ") {
                        it.tap()
                    }
            }.expectation {
                it.screenIs("[画面B]")
            }
        }
    }
}

/**
 * 面倒なイベントハンドリングの例2
 */
@Test
@Order(20)
fun annoyingEventHandling2() {

    scenario {
        case(1) {
            condition {
                it.macro("[画面B]")
                    .ifCanSelect("アプリの使用時のみ") {
                        it.tap()
                    }
            }.action {
                it.tap("[ボタン2]")
                    .ifCanSelect("アプリの使用時のみ") {
                        it.tap()
                    }
            }.expectation {
                it.screenIs("[画面C]")
            }
        }
    }
}
```

## イレギュラーハンドラー

**イレギュラーハンドラー** を使用するとイレギュラー処理を一箇所で行うことができます。

テストクラス内の全ての関数に適用するには`setEventHandlers`関数をオーバーライドして`context.irregularHandler`に処理を設定します。

**irregularHandler** はコマンドを実行する時に毎回呼ばれます。この仕組みは非常に便利であり、テストコードをシンプルにします。

### IrregularHandler1_ja.kt

(`kotlin/tutorial/inaction/IrregularHandler1_ja.kt`)

```kotlin
@Testrun("testConfig/android/androidSettings/testrun.properties")
class IrregularHandler1_ja : UITest() {

    /**
     * 注意:
     * このサンプルコードはコンセプトを説明するものです。実行することはできません。
     */

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {
            ifCanSelect("アプリの使用時のみ") {
                it.tap()
            }
        }
    }

    @Test
    @Order(10)
    fun irregularHandler1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[画面A]")
                }.action {
                    it.tap("[ボタン1]")
                }.expectation {
                    it.screenIs("[画面B]")
                }
            }
        }
    }

}
```

**注意:**

- irregularHandler
  にたくさんの処理を入れるとパフォーマンスの問題を引き起こす可能性があります。代わりに [エラーハンドラー](on_error_handler_ja.md)
  または [スクリーンハンドラー](screen_handler_ja.md) の利用を検討してください
- irregularHandlerの処理中はログ出力はデフォルトでは抑制されます

## suppressHandler

**suppressHandler** 関数を使用するとコードブロック内ではirregularHandlerの呼び出しが無効になります。

```kotlin
@Test
@Order(20)
fun suppressHandler_useHandler() {

    scenario {
        case(1) {
            condition {
                it.macro("[画面A]")
            }.action {
                /**
                 * suppressHandlerブロックの中では
                 * イレギュラーハンドラーは呼び出されません
                 */
                suppressHandler {
                    it.tap("[ボタン1]")
                }
            }.expectation {
                it.screenIs("[画面B]")
            }
        }
        case(2) {
            action {
                /**
                 * suppressHandlerブロックの中では
                 * イレギュラーハンドラーは呼び出されません
                 */
                suppressHandler {
                    it.tap("[ボタン2]")

                    /**
                     * useHandlerブロックの中では
                     * suppressHandlerブロックの中に入れ子になっていた場合でも
                     * イレギュラーハンドラーは呼び出されます
                     */
                    useHandler {
                        it.tap("[ボタン3]")
                    }
                }
            }
        }
    }
}
```

## disableHandler, enableHandler

これらの関数を使用してirregularHandlerを無効化したり有効化したりすることができます。

```kotlin
@Test
@Order(30)
fun disableHandler_EnableHandler() {

    scenario {
        case(1) {
            condition {
                it.macro("[画面A]")
            }.action {
                disableHandler()    // イレギュラーハンドラーの呼び出しは無効になります
                it.tap("[ボタン1]")
                ifCanSelect("アプリの使用時のみ") {
                    it.tap()
                }
                enableHandler()     // イレギュラーハンドラーの呼び出しは再び有効になります
            }.expectation {
                it.screenIs("[画面B]")
            }
        }
    }
}
```

### Link

- [index](../../index_ja.md)
