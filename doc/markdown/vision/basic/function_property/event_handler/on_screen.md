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

<br>

You can register screen handler using `onScreen` function. The handler is going to be removed automatically after it has
been performed.

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

<br>

You can prevent from removing the registered handler by specifying `keep = true`.

```kotlin
    @Test
    @Order(20)
    fun screenHandler_keep() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    onScreen("[Network & internet Screen]") { c ->
                        it.tap("Internet")
                        c.keep = true   // The screen handler keeps registered. Not released.
                    }
                    it.tap("Network & internet")
                }.expectation {
                    it.exist("Network preferences")
                }
            }
        }
    }
```

<br>

You can register the handler permanently to prevent removint it by specifying `permanent = true`.

```kotlin
    @Test
    @Order(30)
    fun screenHandler_permanent() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    onScreen("[Network & internet Screen]", permanent = true) {
                        it.tap("Internet")
                    }
                    it.tap("Network & internet")
                }.expectation {
                    it.exist("Network preferences")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)

