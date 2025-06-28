# canDetect (Vision)

You can know whether you can select the element or not using these functions that return true or false.

## Functions

| function                 | description                                                                                                                                |
|:-------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------|
| canDetect                | Finds the first element that matches the selector in current screen and returns true/false. Scrolling occurs within `withScroll` function. |
| canDetectWithScrollDown  | Finds the first element that matches the selector with scrolling down and returns true/false.                                              |
| canDetectWithScrollUp    | Finds the first element that matches the selector with scrolling up and returns true/false.                                                |
| canDetectWithScrollRight | Finds the first element that matches the selector with scrolling right and returns true/false.                                             |
| canDetectWithScrollLeft  | Finds the first element that matches the selector with scrolling left and returns true/false.                                              |
| canDetectWithoutScroll   | Finds the first element that matches the selector **without** scrolling and returns true/false.                                            |
| canDetectAll             | Finds all elements that matches the selector in current screen and returns true/false. Scrolling occurs within `withScroll` function.      |

## Key arguments

| argument        | description                                                                                             |
|:----------------|:--------------------------------------------------------------------------------------------------------|
| expression      | [Selector expression](../../selector_and_nickname/selector_expression.md)                               |
| language        | [AI-OCR language](../../switching_environment/switching_ai_ocr_language.md)                             |
| last            | true: Finds the last element<br>false: Finds the first element(default)                                 |
| looseMatch      | true: Applies loose matching to text detection<br>false: Do not apply loose matching(default)           |
| autoImageFilter | true: Applies image filters to improve AI-OCR recognition accuracy<br>false: Do not apply image filters |

## Sample code

[Getting samples](../../../getting_samples.md)

### CanDetect1.kt

(`src/test/kotlin/tutorial/basic/CanDetect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun canDetect() {

        scenario {
            case(1) {
                expectation {
                    it.canDetect("Settings")
                        .thisIsTrue("<Settings> found.")
                }
            }
            case(2) {
                expectation {
                    it.canDetectWithScrollDown("System")
                        .thisIsTrue("<System> found with scroll down.")
                }
            }
            case(3) {
                expectation {
                    it.canDetectWithScrollUp("Settings")
                        .thisIsTrue("<Settings> found with scroll up.")
                }
            }
            case(4) {
                expectation {
                    withScrollDown {
                        it.canDetectAll("Settings", "System")
                            .thisIsTrue("<Settings> found with scroll down.")
                    }
                }
            }
            case(5) {
                expectation {
                    withScrollUp {
                        it.canDetectAll("System", "Settings")
                            .thisIsTrue("<Settings> found with scroll up.")
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
