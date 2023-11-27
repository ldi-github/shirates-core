# Branch function (ifImageIs, ifImageIsNot)

You can use special branch functions for image.

## functions

| function     | description                                                                             |
|:-------------|:----------------------------------------------------------------------------------------|
| ifImageIs    | The code block is executed when specified image matches the image of the element        |
| ifImageIsNot | The code block is executed when specified image does not match the image of the element |

### IfImageIs1.kt

(`kotlin/tutorial/basic/IfImageIs1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifImageIs
import shirates.core.driver.branchextension.ifImageIsNot
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.select
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/androidSettings/testrun.properties")
class IfImageIs1 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        ImageSetupHelper.setupImageAndroidSettingsTopScreen()
    }

    @Test
    @Order(10)
    fun ifImageIsTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.select("[Network & internet Icon]")
                        .ifImageIs("[Network & internet Icon].png") {
                            OK("ifImageIs called")
                        }.ifElse {
                            NG()
                        }
                    it.select("[Network & internet Icon]")
                        .ifImageIsNot("[Network & internet Icon].png") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                }
            }
            case(2) {
                expectation {
                    it.select("[Network & internet Icon]")
                        .ifImageIs("[App Icon].png") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                    it.select("[Network & internet Icon]")
                        .ifImageIsNot("[App Icon].png") {
                            OK("ifImageIsNot called")
                        }.ifElse {
                            NG()
                        }
                }
            }
        }
    }

}
```

### Link

- [index](../../../index.md)

