# memo (Vision)

これらの関数を使用してメモを読み取り/書き込みすることができます。

メモはメモリ内の一時的な記憶領域です。

## 関数

| 関数                   | 説明                         |
|:---------------------|:---------------------------|
| writeMemo(key, text) | 指定したキーとテキストでメモを書き込みます      |
| readMemo(key)        | 指定したキーでメモを読み取ります           |
| memoTextAs(key)      | 要素のtextの値を指定したキーでメモに書き込みます |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### Memo1.kt

(`src/test/kotlin/tutorial/basic/Memo1.kt`)

```kotlin
    @Order(10)
    @Test
    fun writeMemo_readMemo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .tap("ストレージ")
                        .waitForDisplay("GB")
                }.action {
                    writeMemo("システム", it.detect("システム").rightText().text)
                    writeMemo("アプリ", it.detect("アプリ").rightText().text)
                }.expectation {
                    readMemo("システム").printInfo()
                    readMemo("アプリ").printInfo()
                }
            }
        }
    }

    @Order(20)
    @Test
    fun memoTextAs_readMemo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[ネットワークとインターネット画面]")
                }.action {
                    it.detect("SIM").belowText().memoTextAs("SIM")
                    it.detect("VPN").belowText().memoTextAs("VPN")
                }.expectation {
                    it.readMemo("SIM").thisIs("T-Mobile")
                    it.readMemo("VPN").thisIs("なし")
                }
            }
        }

    }
```

### Link

- [index](../../../../index_ja.md)

