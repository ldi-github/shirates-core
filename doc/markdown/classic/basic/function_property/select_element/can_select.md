# canSelect (Classic)

You can know whether you can select the element or not using these functions that return true or false.

## Functions

| function                   | description                                                                                                                                |
|:---------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------|
| canSelect                  | Finds the first element that matches the selector in current screen and returns true/false. Scrolling occurs within `withScroll` function. |
| canSelectWithoutScroll     | Finds the first element that matches the selector **without** scrolling and returns true/false.                                            |
| canSelectWithScrollDown    | Finds the first element that matches the selector with scrolling down and returns true/false.                                              |
| canSelectWithScrollUp      | Finds the first element that matches the selector with scrolling up and returns true/false.                                                |
| canSelectWithScrollRight   | Finds the first element that matches the selector with scrolling right and returns true/false.                                             |
| canSelectWithScrollLeft    | Finds the first element that matches the selector with scrolling left and returns true/false.                                              |
| canSelectAllWithScrollDown | Finds all elements that matches the selectors with scrolling down and returns true/false.                                                  |
| canSelectAllWithScrollUp   | Finds all elements that matches the selectors with scrolling up and returns true/false.                                                    |
| canSelectInScanResults     | Finds the first element that matches the selector in scan results and returns true/false.                                                  |
| canSelectAllInScanResults  | Finds all elements that matches the selectors in scan results and returns true/false.                                                      |

## Example 1: canSelect

### CanSelect1.kt

(`kotlin/tutorial/basic/CanSelect1.kt`)

```kotlin
    @Order(10)
    fun canSelect() {

        scenario {
            case(1) {
                action {
                    it.canSelect("Settings", log = true)
                }
            }
            case(2) {
                action {
                    it.canSelectWithScrollDown("System", log = true)
                }
            }
            case(3) {
                action {
                    it.canSelectWithScrollUp("Settings", log = true)
                }
            }
            case(4) {
                action {
                    it.canSelectAllWithScrollDown("Settings", "System", log = true)
                }
            }
            case(5) {
                action {
                    it.canSelectAllWithScrollUp("Settings", "System", log = true)
                }
            }
        }
    }
```

## Example 2: canSelectInScanElements

### CanSelect1.kt

(`kotlin/tutorial/basic/CanSelect1.kt`)

```kotlin
    @Test
    @Order(20)
    fun canSelectInScanElements() {

        scenario {
            case(1) {
                condition {
                    it.scanElements()
                }.action {
                    it.canSelectInScanResults("Settings", log = true)
                    it.canSelectInScanResults("Accessibility", log = true)
                    it.canSelectInScanResults("System", log = true)
                    it.canSelectInScanResults("Foo", log = true)
                }
            }
            case(2) {
                action {
                    it.canSelectAllInScanResults("Settings", "Accessibility", "System", log = true)
                    it.canSelectAllInScanResults("Settings", "Accessibility", "Foo", log = true)
                }
            }
        }
    }
```

### Link

- [index](../../../index.md)
