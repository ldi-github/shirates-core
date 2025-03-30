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
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    onScreen("[Network & internet Screen]") {
                        it.tap("Internet")
                    }.onScreen("[Internet Screen]") {
                        it.tap("AndroidWifi")
                    }.tap("Network & internet")
                    /**
                     * onScreen("[Network & internet Screen]") is called
                     */
                    /**
                     * onScreen("[Internet Screen]") is called
                     */
                }.expectation {
                    it.exist("Network details")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)

