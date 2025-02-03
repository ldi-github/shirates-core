# 画像の検証 (Vision)

これらの関数を使用して画像を検証することができます。

### 関数

| 関数           |
|:-------------|
| imageLabelIs |

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### AssertingImage1.kt

(`src/test/kotlin/tutorial/basic/AssertingImage1.kt`)

```kotlin
    @Test
    @Order(10)
    fun imageLabelIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    v1 = it.detect("Network & internet")
                        .leftItem()
                }.expectation {
                    v1.imageLabelIs("[Network & internet Icon]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
