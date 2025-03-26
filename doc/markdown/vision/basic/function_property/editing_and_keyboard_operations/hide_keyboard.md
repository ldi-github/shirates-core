# hideKeyboard (Vision)

To hide software keyboard, you can use **hideKeyboard** function.

## Sample code

[Getting samples](../../../getting_samples.md)

### AndroidKeyboard1.kt

(`src/test/kotlin/tutorial/basic/AndroidKeyboard1.kt`)

```kotlin
    @Test
    @Order(10)
    fun hideKeyboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .isKeyboardShown.thisIsFalse("Keyboard is not shown")
                }.action {
                    it.tap("Search settings")
                }.expectation {
                    it.keyboardIsShown()
                }
            }
            case(2) {
                action {
                    it.hideKeyboard()
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }
    }
```

####                        

### iOSKeyboard1.kt

(`src/test/kotlin/tutorial/basic/iOSKeyboard1.kt`)

```kotlin
    @Test
    @Order(10)
    fun hideKeyboard() {

        scenario {
            case(1) {
                condition {
                    it.pressHome()
                        .isKeyboardShown.thisIsFalse("Keyboard is not shown")
                }.action {
                    it.swipeCenterToBottom()
                }.expectation {
                    it.keyboardIsShown()
                }
            }
            case(2) {
                action {
                    it.hideKeyboard()
                }.expectation {
                    it.keyboardIsNotShown()
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
