# Keyboard assertion

You can assert showing status of keyboard using these functions.

## functions

| function           |
|:-------------------|
| keyboardIsShown    |
| keyboardIsNotShown |
| isKeyboardShown    |

## Example

### AssertingOthers1.kt

```kotlin
@Test
@Order(30)
fun keyboardIsShown_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
                output("isKeyboardShown=$isKeyboardShown")
            }.expectation {
                it.keyboardIsNotShown()
            }
        }

        case(2) {
            action {
                it.tap("[Search settings]")
            }.expectation {
                it.keyboardIsShown()
            }
        }
    }
}

@Test
@Order(40)
fun keyboardIsShown_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.keyboardIsNotShown()
                    .keyboardIsShown()
            }
        }
    }
}
```

### Link

- [index](../../../index.md)


