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

<br>
onScreen関数を使用してスクリーンハンドラーを登録します。登録されたハンドラーは実行後に自動的に登録が解除されます。

```kotlin
    @Test
    @Order(10)
    fun screenHandler() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    onScreen("[Network & internet Screen]") {
                        it.tap("Internet")
                    }.onScreen("[Internet Screen]") {
                        it.tap("AndroidWifi")
                    }
                    it.tap("Network & internet")
                    /**
                     * onScreen("[Network & internet Screen]") is called
                     */
                    /**
                     * onScreen("[Internet Screen]") is called
                     */
                }.expectation {
                    it.exist("Network details")
                }
            }
        }
    }
```

<br>
自動的に解除されないようにするにはハンドラー内で `keep = true` を指定します。

```kotlin
    @Test
    @Order(20)
    fun screenHandler_keep() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    onScreen("[ネットワークとインターネット画面]") { c ->
                        it.tap("インターネット", last = true)
                        c.keep = true   // スクリーンハンドラーは登録されたままになります。登録解除されません。
                    }
                    it.tap("ネットワークとインターネット")
                }.expectation {
                    it.exist("ネットワーク設定")
                }
            }
        }
    }
```

<br>

ハンドラーを自動的に解除されないように登録するには `permanent = true` を指定します。

```kotlin
    @Test
    @Order(30)
    fun screenHandler_permanent() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    onScreen("[ネットワークとインターネット画面]", permanent = true) {
                        it.tap("インターネット", last = true)
                    }
                    it.tap("ネットワークとインターネット")
                }.expectation {
                    it.exist("ネットワーク設定")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

