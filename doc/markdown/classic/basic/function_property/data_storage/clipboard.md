# clipboard (Classic)

You can write to and read from clipboard using these functions.

## functions

| function         | description                            |
|:-----------------|:---------------------------------------|
| clearClipboard   | Clear clipboard                        |
| readClipboard    | Read text from clipboard               |
| writeToClipboard | Write text to clipboard                |
| clipboardText    | Write text of the element to clipboard |

## Example

### Clipboard1.kt

(`kotlin/tutorial/basic/Clipboard1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.function.readClipboard
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Clipboard1 : UITest() {

    @Test
    @Order(10)
    fun element_clipboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("Settings")
                        .clipboardText()
                }.expectation {
                    readClipboard()
                        .thisIs("Settings")
                }
            }
            case(2) {
                condition {
                    it.exist("[Network & internet]")
                }.action {
                    it.clipboardText()
                }.expectation {
                    readClipboard()
                        .thisIs("Network & internet")
                }
            }
        }

    }

    @Test
    @Order(20)
    fun string_clipboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    "String1".writeToClipboard()
                }.expectation {
                    readClipboard()
                        .thisIs("String1")
                }
            }
        }

    }

}
```

### Link

- [index](../../../index.md)


