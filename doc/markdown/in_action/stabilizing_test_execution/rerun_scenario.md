# Rerun scenario

Rerunning test is simple and powerful solution to resolve flaky test. shirates-core's built in **rerunning scenario**
mechanism makes
test results stable.

## Rerun scenario procedure

1. Shut down Appium Server process
2. Reboot device
3. Start Appium Server process
4. Connect to Appium Server
5. Run scenario again

## When rerun scenario executed automatically?

Rerun is executed in these situation.

| situation                                         | description                                                     |
|---------------------------------------------------|-----------------------------------------------------------------|
| HTTP connection time out ("Read timed out")       | Device is not responding.                                       |
| AppiumProxy.getSource() time out.                 | Appium session is corrupted.                                    |
| Could not start a new session. Response code 500. | Appium session failed.                                          |
| Terminating app time out. (Android)               | ex. 'com.android.settings' is still running after 500ms timeout |

## Requesting rerun scenario manually

You can handle error and request rerun scenario with throwing **RerunScenarioException**.

```kotlin
throw RerunScenarioException("App is not responding")
```

## Example

### RerunScenario1.kt

(`kotlin/tutorial/basic/RerunScenario1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.RerunScenarioException
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class RerunScenario1 : UITest() {

    @Test
    @Order(10)
    fun rerunScenario() {

        var count = 0

        scenario {
            case(1) {
                action {
                    count++
                    output("count=$count")
                    if (count == 1) {
                        throw RerunScenarioException("Request to rerun scenario")
                    }
                }.expectation {
                    count.thisIs(2, "count is 2")
                }
            }
        }
    }
}
```

```

```

### Link

- [Causes of flaky test](causes_of_flaky_test.md)


- [index](../../index.md)
