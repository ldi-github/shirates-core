# Branch function (ifImageExist, ifImageExistNot)

You can use special branch functions for image.

## functions

| function        | description                                                          |
|:----------------|:---------------------------------------------------------------------|
| ifImageExist    | The code block is executed when specified image is on the screen     |
| ifImageExistNot | The code block is executed when specified image is not on the screen |

### IfImageExist1.kt

(`kotlin/tutorial/basic/IfImageExist1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifImageExist
import shirates.core.driver.branchextension.ifImageExistNot
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.scrollToBottom
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/androidSettings/testrun.properties")
class IfImageExist1 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            ImageSetupHelper.setupImageAndroidSettingsTopScreen()
        }
    }

    @Test
    @Order(10)
    fun ifImageExistTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifImageExist("[Network & internet Icon].png") {
                        OK("ifImageExist called")
                    }.ifElse {
                        NG()
                    }

                    ifImageExistNot("[System Icon].png") {
                        OK("ifImageExistNot called")
                    }.ifElse {
                        NG()
                    }
                }
            }
            case(2) {
                action {
                    it.scrollToBottom()
                }.expectation {
                    ifImageExist("[Network & internet Icon].png") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }

                    ifImageExistNot("[System Icon].png") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
                }
            }
        }
    }

}
```

### Link

- [index](../../../index.md)

