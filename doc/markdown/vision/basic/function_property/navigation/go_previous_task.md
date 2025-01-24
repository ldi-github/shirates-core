# goPreviousApp (Shirates/Vision)

You can go back to previous app using **goPreviousApps** function.

## Example

### NavigationOnAndroid1.kt

(`kotlin/tutorial/basic/NavigationOnAndroid1.kt`)

```kotlin
package tutorial.basic

import goPreviousApp
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class NavigationOnAndroid1 : UITest() {

    @Test
    fun goPreviousAppTest() {

        scenario {
            case(1) {
                condition {
                    it.launchApp("[Settings]")
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
            case(2) {
                action {
                    it.launchApp("[Maps]")
                }.expectation {
                    it.appIs("[Maps]")
                }
            }
            case(3) {
                action {
                    it.goPreviousApp()
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
        }
    }

}
```

### NavigationOnIos1.kt

(`kotlin/tutorial/basic/NavigationOnIos1.kt`)

```kotlin
package tutorial.basic

import goPreviousApp
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class NavigationOnIos1 : UITest() {

    @Test
    fun goLeftTaskTest() {

        scenario {
            case(1) {
                condition {
                    it.launchApp("[Settings]")
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
            case(2) {
                action {
                    it.launchApp("[Safari]")
                }.expectation {
                    it.appIs("[Safari]")
                }
            }
            case(3) {
                action {
                    it.goPreviousApp()
                }.expectation {
                    it.appIs("[Settings]")
                }
            }
        }
    }
}
```

### Link

- [index](../../../../index.md)

