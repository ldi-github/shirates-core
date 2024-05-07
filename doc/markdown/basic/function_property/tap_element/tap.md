# tap

You can tap an element on the screen using these functions.

## Functions

| function           | description                                                                                                     |
|:-------------------|:----------------------------------------------------------------------------------------------------------------|
| tap(expression)    | Tap the first element that matches the selector in current screen. Scrolling occurs within withScroll function. |
| tap(x, y)          | Tap the coordinates (x, y).                                                                                     |
| tapWithoutScroll   | Tap the first element that matches the selector **without** scrolling.                                          |
| tapWithScrollDown  | Tap the first element that matches the selector with scrolling down.                                            |
| tapWithScrollUp    | Tap the first element that matches the selector with scrolling up.                                              |
| tapWithScrollRight | Tap the first element that matches the selector with scrolling right.                                           |
| tapWithScrollLeft  | Tap the first element that matches the selector with scrolling left.                                            |
| tapCenterOfScreen  | Tap the center of the screen.                                                                                   |
| tapCenterOf        | Tap the center of the element.                                                                                  |

## Example

### Tap1.kt

(`kotlin/tutorial/basic/Tap1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.tapWithScrollDown
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Tap1 : UITest() {

    @Test
    fun tap() {

        scenario {
            case(1) {
                action {
                    it.tap("Network & internet")
                        .tap("Internet")
                    it.tap("@Navigate up")
                        .tap("@Navigate up")
                }
            }
            case(2) {
                action {
                    it.tapWithScrollDown("Display")
                        .tapWithScrollDown("Colors")
                    it.tap("@Navigate up")
                        .tap("@Navigate up")
                }
            }
        }
    }

    @Test
    fun tapByCoordinates() {

        scenario {
            case(1) {
                action {
                    val e = select("Network & internet")
                    it.tap(x = e.bounds.centerX, y = e.bounds.centerY)
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}
```

### Link

- [index](../../../index.md)
