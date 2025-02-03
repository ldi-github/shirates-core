# Text assertion (Vision)

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
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.detect("Network & internet")
                }.expectation {
                    it.textIs("Network & internet")
                        .belowTextIs("Mobile, Wi-Fi, hotspot")
                        .aboveTextIs("Network & internet")
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
                    it.macro("[Maps Top Screen]")
                }.action {
                    it.detect("Restaurants")
                }.expectation {
                    it.textIs("Restaurants")
                        .rightTextIs("Hotels")
                        .leftTextIs("Restaurants")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

