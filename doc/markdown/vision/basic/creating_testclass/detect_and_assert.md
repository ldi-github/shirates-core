# Detect and assert

You can detect text in the screen by **AI-OCR text recognition** and assert its value.

### Sample code

[Getting samples](../../getting_samples.md)

### DetectAndAssert1.kt

(`kotlin/tutorial_vision/basic/DetectAndAssert1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.textIs
import shirates.core.vision.testcode.VisionTest

class DetectAndAssert1 : VisionTest() {

    @Test
    @Order(10)
    fun detectAndAssert1_OK() {

        scenario {
            case(1) {
                expectation {
                    it.detect("Settings")
                        .textIs("Settings")   // OK
                }
            }
        }
    }

    @Test
    @Order(20)
    fun detectAndAssert2_NG() {

        scenario {
            case(1) {
                expectation {
                    it.detect("Settings")
                        .textIs("Network & internet")   // NG
                }
            }
        }
    }

}
```

In the above example, **detect** function recognizes texts of the screenshot,
finds the rectangle where text equals to "Settings"
and returns the `VisionElement` object. `VisionElement` is extended by **textIs** extension function. When the text
equals to expected value, assertion log like below is output.

```
[OK]	(textIs)	<Settings> is "Settings"
```

Shirates's APIs are designed as _fluent API_, so you can chain functions as follows.

```kotlin
it.detect("Settings")
    .textIs("Settings")   // OK
    .textIs("Network & internet")   // NG
```

### Link

- [index](../../../index.md)
