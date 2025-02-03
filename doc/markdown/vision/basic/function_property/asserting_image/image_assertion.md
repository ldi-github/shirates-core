# Image assertion (Vision)

You can assert image using these functions.

## functions

| function     |
|:-------------|
| imageLabelIs |

## Sample code

[Getting samples](../../../getting_samples.md)

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

- [index](../../../../index.md)
