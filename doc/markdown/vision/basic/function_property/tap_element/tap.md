# tap (Vision)

You can tap an element on the screen using these functions.

## Functions

| function               | description                                                                                                     |
|:-----------------------|:----------------------------------------------------------------------------------------------------------------|
| tap(expression)        | Tap the first element that matches the selector in current screen. Scrolling occurs within withScroll function. |
| tap(x, y)              | Tap the coordinates (x, y).                                                                                     |
| tapWithScrollDown      | Tap the first element that matches the selector with scrolling down.                                            |
| tapWithScrollUp        | Tap the first element that matches the selector with scrolling up.                                              |
| tapWithScrollRight     | Tap the first element that matches the selector with scrolling right.                                           |
| tapWithScrollLeft      | Tap the first element that matches the selector with scrolling left.                                            |
| tapWithoutScroll       | Tap the first element that matches the selector **without** scrolling.                                          |
| tapCenterOfScreen      | Tap the center of the screen.                                                                                   |
| tapCenterOf            | Tap the center of the element.                                                                                  |
| tapBelowOf(expression) | Tap the element below of the element of expression.                                                             |
| tapAboveOf(expression) | Tap the element above of the element of expression.                                                             |
| tapRightOf(expression) | Tap the element right of the element of expression.                                                             |
| tapLeftOf(expression)  | Tap the element left of the element of expression.                                                              |

## Sample code

[Getting samples](../../../getting_samples.md)

### Tap1.kt

(`src/test/kotlin/tutorial/basic/Tap1.kt`)

```kotlin
    @Test
    fun tap() {

        scenario {
            case(1) {
                action {
                    it.tap("Network & internet")
                        .tap("Internet")
                    it.pressBack()
                        .pressBack()
                }
            }
            case(2) {
                action {
                    it.tapWithScrollDown("Display")
                        .tapWithScrollDown("Colors")
                    it.pressBack()
                        .pressBack()
                }
            }
        }
    }

    @Test
    fun tapByCoordinates() {

        scenario {
            case(1) {
                action {
                    val v = detect("Network & internet")
                    it.tap(x = v.bounds.centerX, y = v.bounds.centerY)
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
