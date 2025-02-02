# Text assertion (Vision)

You can assert the value of `text` using these functions.

## functions

| function    |
|:------------|
| textIs      |
| rightTextIs |
| leftTextIs  |
| belowTextIs |
| aboveTextIs |

## Sample code

[Getting samples](../../getting_samples.md)

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

- [index](../../../../index.md)

