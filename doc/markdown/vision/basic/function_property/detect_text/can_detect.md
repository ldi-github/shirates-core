# canDetect (Shirates/Vision)

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

## Example 1: canDetect

### CanDetect1.kt

(`kotlin/tutorial/basic/canDetect1.kt`)

```kotlin
    @Order(10)
    fun canDetect() {

        scenario {
            case(1) {
                action {
                    it.canDetect("Settings", log = true)
                }
            }
            case(2) {
                action {
                    it.canDetectWithScrollDown("System", log = true)
                }
            }
            case(3) {
                action {
                    it.canDetectWithScrollUp("Settings", log = true)
                }
            }
            case(4) {
                action {
                    it.canDetectAllWithScrollDown("Settings", "System", log = true)
                }
            }
            case(5) {
                action {
                    it.canDetectAllWithScrollUp("Settings", "System", log = true)
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
