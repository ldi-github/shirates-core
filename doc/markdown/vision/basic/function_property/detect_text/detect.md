# detect (Shirates/Vision)

You can select an element using these functions.

[Selector expression](../../selector_and_nickname/selector_expression.md) is accepted as argument.

The function returns `TestElement` object.

## Functions

| function              | description                                                                                                         |
|:----------------------|:--------------------------------------------------------------------------------------------------------------------|
| select                | Finds the first element that matches the selector in current screen. Scrolling occurs within `withScroll` function. |
| selectWithoutScroll   | Finds the first element that matches the selector **without** scrolling.                                            |
| selectWithScrollDown  | Finds the first element that matches the selector with scrolling down.                                              |
| selectWithScrollUp    | Finds the first element that matches the selector with scrolling up.                                                |
| selectWithScrollRight | Finds the first element that matches the selector with scrolling right.                                             |
| selectWithScrollLeft  | Finds the first element that matches the selector with scrolling left.                                              |
| selectInScanResults   | Finds the first element that matches the selector in scan results.                                                  |

## Example 1: select

### Select1.kt

(`kotlin/tutorial/basic/Select1.kt`)

```kotlin
    @Test
    @Order(10)
    fun select() {

        scenario {
            case(1) {
                action {
                    it.select("Settings", log = true)
                    output(it)
                }
            }
            case(2) {
                action {
                    it.selectWithScrollDown("System", log = true)
                    output(it)
                }
            }
            case(3) {
                action {
                    it.selectWithScrollUp("Settings", log = true)
                    output(it)
                }
            }
        }
    }
```

## Example 2: scanElements

### Select1.kt

(`kotlin/tutorial/basic/Select1.kt`)

```kotlin
    @Test
    @Order(20)
    fun selectInScanElements() {

        scenario {
            case(1) {
                action {
                    it.scanElements()
                        .selectInScanResults("Settings", log = true)
                        .selectInScanResults("Accessibility", log = true)
                        .selectInScanResults("System", log = true)
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
