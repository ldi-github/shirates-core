# memo (Vision)

これらの関数を使用してメモを読み取り/書き込みすることができます。

メモはメモリ内の一時的な記憶領域です。

## 関数

| 関数                   | 説明                         |
|:---------------------|:---------------------------|
| writeMemo(key, text) | 指定したキーとテキストでメモを書き込みます      |
| readMemo(key)        | 指定したキーでメモを読み取ります           |
| memoTextAs(key)      | 要素のtextの値を指定したキーでメモに書き込みます |

### サンプルコード

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
                    it.macro("[Android Settings Top Screen]")
                        .tap("Storage")
                        .waitForDisplay("GB")
                }.action {
                    writeMemo("System", it.detect("System").rightText().text)
                    writeMemo("Apps", it.detect("Apps").rightText().text)
                }.expectation {
                    readMemo("System").printInfo()
                    readMemo("Apps").printInfo()
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
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.detect("SIMs").belowText().memoTextAs("SIMs")
                    it.detect("VPN").belowText().memoTextAs("VPN")
                }.expectation {
                    it.readMemo("SIMs").thisIs("T-Mobile")
                    it.readMemo("VPN").thisIs("None")
                }
            }
        }

    }
```

### Link

- [index](../../../../index_ja.md)

