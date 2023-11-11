# tapAppIcon

You can tap app icon and launch app using **tapAppIcon** function.

## function

| function   | description                                |
|:-----------|:-------------------------------------------|
| tapAppIcon | Tap app icon on home or app launcher menu. |

## Example

### TapAppIcon1.kt

(`kotlin/tutorial/basic/TapAppIcon1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TapAppIcon1 : UITest() {

    @Test
    fun tapAppIcon() {

        scenario {
            case(1) {
                action {
                    it.launchApp("Chrome")
                }.expectation {
                    it.appIs("[Chrome]")
                }
            }
            case(2) {
                action {
                    it.launchApp("Play Store")
                }.expectation {
                    it.appIs("[Play Store]")
                }
            }
        }
    }

}
```

### Link

- [index](../../../index.md)
