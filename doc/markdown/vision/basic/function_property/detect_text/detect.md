# detect (Vision)

You can detect text element using these functions.

[Selector expression](../../selector_and_nickname/selector_expression.md) is accepted as argument.

The function returns `VisionElement` object.

## Functions

| function              | description                                                                                                         |
|:----------------------|:--------------------------------------------------------------------------------------------------------------------|
| detect                | Finds the first element that matches the selector in current screen. Scrolling occurs within `withScroll` function. |
| detectLast            | Finds the last element that matches the selector in current screen. Scrolling occurs within `withScroll` function.  |
| detectWithScrollDown  | Finds the first element that matches the selector with scrolling down.                                              |
| detectWithScrollUp    | Finds the first element that matches the selector with scrolling up.                                                |
| detectWithScrollRight | Finds the first element that matches the selector with scrolling right.                                             |
| detectWithScrollLeft  | Finds the first element that matches the selector with scrolling left.                                              |
| detectWithoutScroll   | Finds the first element that matches the selector **without** scrolling.                                            |

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

### Detect1.kt

(`kotlin/tutorial/basic/Detect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun detect() {

        scenario {
            case(1) {
                action {
                    it.detect("Search settings")
                    output(it)

                    it.detect("Network & internet")
                    output(it)
                }
            }
        }
    }

    @Test
    @Order(20)
    fun detectWithScrollDown_detectWithScrollUp() {

        scenario {
            case(1) {
                action {
                    it.detectWithScrollDown("Tips & support")
                    output(it)
                }
            }
        }
    }

    @Test
    @Order(30)
    fun detect_patterns() {

        scenario {
            case(1) {
                action {
                    it.detect("Search settings")
                    output(it)

                    it.detect("*arch sett*")
                    output(it)

                    it.detect("Search*")
                    output(it)

                    it.detect("*settings")
                    output(it)

                    it.detect("Search*&&*settings")
                    output(it)
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
