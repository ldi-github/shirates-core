# detect (Vision)

You can detect text element using these functions.

[Selector expression](../../selector_and_nickname/selector_expression.md) is accepted as argument.

The function returns `VisionElement` object.

## Functions

| function              | description                                                                                                         |
|:----------------------|:--------------------------------------------------------------------------------------------------------------------|
| detect                | Finds the first element that matches the selector in current screen. Scrolling occurs within `withScroll` function. |
| detectWithScrollDown  | Finds the first element that matches the selector with scrolling down.                                              |
| detectWithScrollUp    | Finds the first element that matches the selector with scrolling up.                                                |
| detectWithScrollRight | Finds the first element that matches the selector with scrolling right.                                             |
| detectWithScrollLeft  | Finds the first element that matches the selector with scrolling left.                                              |
| detectWithoutScroll   | Finds the first element that matches the selector **without** scrolling.                                            |

## Sample code

[Getting samples](../../../getting_samples.md)

### Detect1.kt

(`kotlin/tutorial/basic/Detect1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.detectWithScrollDown
import shirates.core.vision.driver.commandextension.detectWithScrollUp
import shirates.core.vision.driver.commandextension.output
import shirates.core.vision.testcode.VisionTest

class Detect1 : VisionTest() {

    @Test
    @Order(10)
    fun detect() {

        scenario {
            case(1) {
                action {
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
                    it.detectWithScrollDown("System")
                    output(it)
                }
            }
            case(2) {
                action {
                    it.detectWithScrollUp("Settings")
                    output(it)
                }
            }
        }
    }

}
```

### Link

- [index](../../../../index.md)
