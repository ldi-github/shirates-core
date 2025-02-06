# 画像の検証 (Vision)

これらの関数を使用して画像を検証することができます。

### 関数

| 関数      |
|:--------|
| imageIs |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### AssertingImage1.kt

(`src/test/kotlin/tutorial/basic/AssertingImage1.kt`)

```kotlin
    @Test
    @Order(10)
    fun imageIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    v1 = it.detect("ネットワークとインターネット")
                        .leftItem()
                }.expectation {
                    v1.imageIs("[ネットワークとインターネットアイコン]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
