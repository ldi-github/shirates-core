# Screen Handler (onScreen) (Vision)

You can register callback that is called on screen changed.

## functions

| function | description                                                                    |
|:---------|:-------------------------------------------------------------------------------|
| onScreen | The code block that is registered is executed on specified screen is displayed |

## Sample code

[Getting samples](../../../getting_samples.md)

### ScreenHandler1.kt

(`src/test/kotlin/tutorial/basic/ScreenHandler1.kt`)

```kotlin
    @Test
    @Order(10)
    fun screenHandler() {

        scenario {
            case(1) {
                condition {
                    onScreen("[Android Settings Top Screen]") {
                        it.tap("Network & internet")
                    }
                    onScreen("[Network & internet Screen]") {
                        it.tap("Internet")
                    }
                }.action {
                    it.macro("[Android Settings Top Screen]")
                    /**
                     * onScreen("[Android Settings Top Screen]") is called
                     */
                    /**
                     * onScreen("[Network & internet Screen]") is called
                     */
                }.expectation {
                    it.screenIs("[Internet Screen]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)

