# Detect and assert (Shirates/Vision)

You can detect an element by text and assert its properties.

### DetectAndAssert1.kt

(`kotlin/tutorial_vision/basic/DetectAndAssert1.kt`)

```kotlin
package tutorial_vision.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.textIs
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/vision/android/androidSettings/testrun.properties")
class DetectAndAssert1 : VisionTest() {

    @Test
    @Order(10)
    fun selectAndAssert1_OK() {

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
    fun selectAndAssert2_NG() {

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
finds an rectangle where text equals to "Settings"
and returns the VisionElement object. VisionElement is extended by **textIs** extension function. When the text equals
to
expected value, assertion log like below is output.

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

- [index](../../vision-index.md)
