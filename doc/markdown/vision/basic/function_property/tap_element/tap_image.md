# tapImage (Vision)

You can tap an image on the screen using these functions.

## Functions

| function | description                                                                  |
|:---------|:-----------------------------------------------------------------------------|
| tapImage | Finds an image that matches the template image in current screen and tap it. |

## Sample code

[Getting samples](../../../getting_samples.md)

### TapImage1.kt

(`src/test/kotlin/tutorial/basic/TapImage1.kt`)

```kotlin
    @Test
    fun tapImage() {

        scenario {
            case(1) {
                action {
                    it.tapImage("[Network & internet Icon]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    withScrollDown {
                        it.tapImage("[System Icon]")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
