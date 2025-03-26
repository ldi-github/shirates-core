# clipboard

これらの関数を使用するとクリップボードの読み取り/書き込みを行うことができます。

## 関数

| 関数               | 説明                     |
|:-----------------|:-----------------------|
| clearClipboard   | クリップボードをクリアします         |
| readClipboard    | クリップボードからテキストを読み取ります   |
| writeToClipboard | クリップボードへテキストを書き込みます    |
| clipboardText    | 要素のtextをクリップボードへ書き込みます |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### Clipboard1.kt

(`src/test/kotlin/tutorial/basic/Clipboard1.kt`)

```kotlin
    @Test
    @Order(10)
    fun element_clipboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    it.detect("設定")
                        .clipboardText()
                }.expectation {
                    readClipboard()
                        .thisIs("設定")
                }
            }
            case(2) {
                condition {
                    it.exist("ネットワークとインターネット")
                }.action {
                    it.clipboardText()
                }.expectation {
                    readClipboard()
                        .thisIs("ネットワークとインターネット")
                }
            }
        }

    }

    @Test
    @Order(20)
    fun string_clipboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    "String1".writeToClipboard()
                }.expectation {
                    readClipboard()
                        .thisIs("String1")
                }
            }
        }

    }
```

### Link

- [index](../../../../index_ja.md)


