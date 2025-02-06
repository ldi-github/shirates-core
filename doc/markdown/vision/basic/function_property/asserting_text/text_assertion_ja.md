# テキストの検証 (Vision)

これらの関数を使用してテキストの値の検証を行うことができます。

## 関数

| 関数          |
|:------------|
| textIs      |
| rightTextIs |
| leftTextIs  |
| belowTextIs |
| aboveTextIs |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### AssertingText1.kt

(`src/test/kotlin/tutorial/basic/AssertingText1.kt`)

```kotlin
    @Test
    @Order(10)
    fun belowTextIs_aboveTextIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    it.detect("ネットワークとインターネット")
                }.expectation {
                    it.textIs("ネットワークとインターネット")
                        .belowTextIs("モバイル、Wi-Fi、アクセスポイント")
                        .aboveTextIs("ネットワークとインターネット")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun rightTextIs_leftTextIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[マップトップ画面]")
                }.action {
                    it.detect("レストラン")
                }.expectation {
                    it.textIs("レストラン")
                        .rightTextIs("ホテル")
                        .leftTextIs("レストラン")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

