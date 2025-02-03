# 分岐関数 (ifScreenIs, ifScreenIsNot) (Vision)

画面に対してこれらの分岐関数を使用することができます。

## 関数

| 関数            | 説明                              |
|:--------------|:--------------------------------|
| ifScreenIs    | 指定した画面が表示されている場合にコードブロックが実行されます |
| ifScreenIsNot | 指定した画面が表示されている場合にコードブロックが実行されます |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### IfScreenIs1.kt

(`src/test/kotlin/tutorial/basic/IfScreenIs1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifScreenIsTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifScreenIs("[Android Settings Top Screen]") {
                        OK("ifScreenIs called")
                    }.ifElse {
                        NG()
                    }
                }
            }
            case(2) {
                action {
                    it.tap("Network & internet")
                }.expectation {
                    ifScreenIs("[Network & internet Screen]") {
                        OK("ifScreenIs called")
                    }.ifElse {
                        NG()
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun ifScreenIsNotTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifScreenIsNot("[Android Settings Top Screen]") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
                }
            }
            case(2) {
                action {
                    it.tap("Network & internet")
                }.expectation {
                    ifScreenIsNot("[Network & internet Screen]") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

