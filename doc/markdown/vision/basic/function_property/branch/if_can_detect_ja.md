# 分岐関数 (ifCanDetect, ifCanDetectNot) (Vision)

画面要素に対してこれらの分岐関数を使用することができます。

## 関数

| 関数             | 説明                                  |
|:---------------|:------------------------------------|
| ifCanDetect    | 指定したテキストが画面上に存在する場合にコードブロックが実行されます  |
| ifCanDetectNot | 指定したテキストが画面上に存在しない場合にコードブロックが実行されます |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### IfCanSelect1.kt

(`src/test/kotlin/tutorial/basic/IfCanDetect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifCanSelectTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifCanDetect("Network & internet") {
                        OK("ifCanSelect called")
                    }.ifElse {
                        NG()
                    }

                    ifCanDetectNot("System") {
                        OK("ifCanSelectNot called")
                    }.ifElse {
                        NG()
                    }
                }
            }
            case(2) {
                action {
                    it.scrollToBottom()
                }.expectation {
                    ifCanDetect("Network & internet") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }

                    ifCanDetectNot("System") {
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

