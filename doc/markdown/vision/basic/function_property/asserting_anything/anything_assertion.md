# Anything assertion(verify function) (Vision)

You can assert anything using `verify` function.

### Implementing verification logic for anything

```kotlin
it.verify("The packageName is \"com.android.settings\"") {
    if (rootElement.packageName == "com.android.settings") {
        OK()
    } else {
        NG()
    }
}
```

You can implement verification logic for anything in `verify` function.
The result of the verification is notified to the verify function by calling OK function/NG function.

### Combination of existing validation functions

```kotlin
it.verify("The app is Settings and the screen is [Android Settings Top Screen]") {
    it.appIs("Settings")
    it.screenIs("[Android Settings Top Screen]")
}
```

Calling OK function/NG function are not necessary if existing verification function is used within the verify function.

### Example of output

```
141	[00:00:18]	2024/04/12 02:46:17.832	{ok1-1}	0	-	[EXPECTATION]	+196	C	()	expectation
142	[00:00:18]	2024/04/12 02:46:17.835	{ok1-1}	0	-	[OK]	+3	C	(verify)	The packageName is "com.android.settings"
143	[00:00:18]	2024/04/12 02:46:17.840	{ok1-1}	0	-	[OK]	+5	C	(verify)	The app is Settings and the screen is [Android Settings Top Screen]
```

## Example

### AssertingAnything1.kt

(`kotlin/tutorial/basic/AssertingAnything1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElementCache.rootElement
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.verify
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AssertingAnything1 : UITest() {

    @Test
    @Order(10)
    fun ok() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The packageName is \"com.android.settings\"") {
                        if (rootElement.packageName == "com.android.settings") {
                            OK()
                        } else {
                            NG()
                        }
                    }
                    it.verify("The app is 'Settings' and the screen is [Android Settings Top Screen]") {
                        it.appIs("Settings")
                        it.screenIs("[Android Settings Top Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun ng() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The app is 'Settings2' and the screen is [Android Settings Top Screen]") {
                        it.appIs("Settings2")
                        it.screenIs("[Android Settings Top Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun notImplemented() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The app is 'Settings' and the screen is [Android Settings Top Screen]") {
                    }
                }
            }
        }
    }
}
```

### Link

- [index](../../../../index.md)

