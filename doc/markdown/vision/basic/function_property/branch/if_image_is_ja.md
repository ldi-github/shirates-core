# 分岐関数 (ifImageIs, ifImageIsNot) (Vision)

画像に対してこれらの分岐関数を使用することができます。

## 関数

| 関数           | 説明                                   |
|:-------------|:-------------------------------------|
| ifImageIs    | 指定した画像に要素の画像がマッチする場合にコードブロックが実行されます  |
| ifImageIsNot | 指定した画像に要素の画像がマッチしない場合にコードブロックが実行されます |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### IfImageIs1.kt

(`kotlin/tutorial/basic/IfImageIs1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifImageIsTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.detect("ネットワークとインターネット")
                        .leftItem()
                        .ifImageIs("[ネットワークとインターネットアイコン]") {
                            OK("ifImageIs called")
                        }.ifElse {
                            NG()
                        }
                    it.detect("ネットワークとインターネット")
                        .leftItem()
                        .ifImageIsNot("[ネットワークとインターネットアイコン]") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                }
            }
            case(2) {
                expectation {
                    it.detect("ネットワークとインターネット")
                        .leftItem()
                        .ifImageIs("[アプリアイコン]") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                    it.detect("ネットワークとインターネット")
                        .leftItem()
                        .ifImageIsNot("[アプリアイコン]") {
                            OK("ifImageIsNot called")
                        }.ifElse {
                            NG()
                        }
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

