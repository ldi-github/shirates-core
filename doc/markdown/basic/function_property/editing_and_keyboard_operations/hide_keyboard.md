# hideKeyboard

To hide software keyboard, you can use **hideKeyboard** function.

## Example

#### AndroidKeyboard1.kt

(`kotlin/tutorial/basic/AndroidKeyboard1.kt`)

```kotlin
@Test
@Order(10)
fun hideKeyboard() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Home Screen]")
                    .isKeyboardShown.thisIsFalse("Keyboard is not shown")
            }.action {
                it.tap("@Search")
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

### Link

- [index](../../../index.md)
