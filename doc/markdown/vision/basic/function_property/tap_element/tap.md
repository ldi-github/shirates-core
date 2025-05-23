# tap (Vision)

You can tap an element on the screen using these functions.

## Functions

| function                         | description                                                                                           |
|:---------------------------------|:------------------------------------------------------------------------------------------------------|
| tap(_expression_)                | Tap the first element that matches the _expression_. Scrolling occurs within **withScroll** function. |
| tapLast(_expression_)            | Tap the last element that matches the _expression_. Scrolling occurs within **withScroll** function.  |
| tap(_x, y_)                      | Tap the coordinates (x, y).                                                                           |
| tapWithScrollDown(_expression_)  | Tap the first element that matches the _expression_ with scrolling down.                              |
| tapWithScrollUp(_expression_)    | Tap the first element that matches the _expression_ with scrolling up.                                |
| tapWithScrollRight(_expression_) | Tap the first element that matches the _expression_ with scrolling right.                             |
| tapWithScrollLeft(_expression_)  | Tap the first element that matches the _expression_ with scrolling left.                              |
| tapWithoutScroll(_expression_)   | Tap the first element that matches the _expression_ **without** scrolling.                            |
| tapCenterOfScreen                | Tap the center of the screen.                                                                         |
| tapCenterOf(_expression_)        | Tap the center of the element.                                                                        |
| tapItemUnder(_expression_)       | Tap the element below of the element of _expression_.                                                 |
| tapItemOver(_expression_)        | Tap the element above of the element of _expression_.                                                 |
| tapItemRightOf(_expression_)     | Tap the element right of the element of _expression_.                                                 |
| tapItemLeftOf(_expression_)      | Tap the element left of the element of _expression_.                                                  |
| tapTextUnder(_expression_)       | Tap the text element below of the element of _expression_.                                            |
| tapTextOver(_expression_)        | Tap the text element above of the element of _expression_.                                            |

## Key arguments

| argument        | description                                                                                                      |
|:----------------|:-----------------------------------------------------------------------------------------------------------------|
| expression      | [Selector expression](../../selector_and_nickname/selector_expression.md)                                        |
| language        | [AI-OCR language](../../switching_environment/switching_ai_ocr_language.md)                                      |
| last            | true: Finds the last element<br>false: Finds the first element(default)                                          |
| looseMatch      | true: Applies loose matching to text detection(default)<br>false: Do not apply loose matching                    |
| autoImageFilter | true: Applies image filters to improve AI-OCR recognition accuracy<br>false: Do not apply image filters(default) |

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
