# スクリーンハンドラー (onScreen) (Vision)

画面が変化した時に呼び出されるコールバックを登録することができます。

## 関数

| 関数       | 説明                               |
|:---------|:---------------------------------|
| onScreen | 登録したコードブロックが指定した画面が表示された時に実行されます |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### ScreenHandler1.kt

(`src/test/kotlin/tutorial/basic/ScreenHandler1.kt`)

```kotlin
    @Test
    @Order(10)
    fun screenHandler() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    onScreen("[ネットワークとインターネット画面]") {
                        it.tap("インターネット", last = true)
                    }.onScreen("[インターネット画面]") {
                        it.tap("AndroidWifi")
                    }.tap("ネットワークとインターネット")
                    /**
                     * onScreen("[ネットワークとインターネット画面]") が呼ばれます
                     */
                    /**
                     * onScreen("[インターネット画面]") が呼ばれます
                     */
                }.expectation {
                    it.exist("ネットワークの詳細")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

