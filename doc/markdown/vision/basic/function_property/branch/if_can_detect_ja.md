# 分岐関数 (ifCanDetect, ifCanDetectNot) (Vision)

画面要素に対してこれらの分岐関数を使用することができます。

## 関数

| 関数             | 説明                                  |
|:---------------|:------------------------------------|
| ifCanDetect    | 指定したテキストが画面上に存在する場合にコードブロックが実行されます  |
| ifCanDetectNot | 指定したテキストが画面上に存在しない場合にコードブロックが実行されます |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### IfCanDetect1.kt

(`src/test/kotlin/tutorial/basic/IfCanDetect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifCanDetectTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    ifCanDetect("ネットワークとインターネット") {
                        OK("ifCanDetect called")
                    }.ifElse {
                        NG()
                    }

                    ifCanDetectNot("システム") {
                        OK("ifCanDetectNot called")
                    }.ifElse {
                        NG()
                    }
                }
            }
            case(2) {
                action {
                    it.scrollToBottom()
                }.expectation {
                    ifCanDetect("ネットワークとインターネット") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }

                    ifCanDetectNot("システム") {
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

