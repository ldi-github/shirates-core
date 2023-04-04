# Test fixture

You can set up/clean up test environment using **test fixture** of `UITest`.

## Test fixture event functions

| function            | called on                                                                                    | AppiumServer  |
|:--------------------|:---------------------------------------------------------------------------------------------|:--------------|
| beforeAll           | **Before AppiumServer setup is done**, before the first function of the test class is called | NOT available |
| beforeAllAfterSetup | **After AppiumServer setup is done**, before the first function of the test class is called  | available     |
| beforeEach          | Before each function is called                                                               | available     |
| afterEach           | After each function is called                                                                | available     |
| afterAll            | After all functions are called                                                               | available     |
| finally             | **After AppiumServer is shutdown**                                                           | NOT available |

## Example

### TestFixtureEvent1.kt

(kotlin/tutorial/basic/TestFixtureEvent1.kt)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.commandextension.exist
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestFixtureEvent1 : UITest() {

    private fun printEvent(eventName: String) {

        println("[$eventName] --------------------------------------------------")

        try {
            driver.appiumDriver
            println("AppiumDriver: available")
        } catch (t: Throwable) {
            println("AppiumDriver: NOT available")
        }
    }

    override fun beforeAll(context: ExtensionContext?) {
        printEvent("beforeAll")
    }

    override fun beforeAllAfterSetup(context: ExtensionContext?) {
        printEvent("beforeAllAfterSetup")
    }

    override fun beforeEach(context: ExtensionContext?) {
        printEvent("beforeEach")
    }

    override fun afterEach(context: ExtensionContext?) {
        printEvent("afterEach")
    }

    override fun afterAll(context: ExtensionContext?) {
        printEvent("afterAll")
    }

    override fun finally() {
        printEvent("finally")
    }

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {}
    }

    @Test
    @Order(10)
    fun test1() {

        scenario {
            case(1) {
                expectation {
                    it.exist("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun test2() {

        scenario {
            case(1) {
                expectation {
                    it.exist("Connected devices")
                }
            }
        }
    }

}
```

### Console output

```
...
[beforeAll] --------------------------------------------------
AppiumDriver: NOT available
...
[beforeAllAfterSetup] --------------------------------------------------
AppiumDriver: available
...
[beforeEach] --------------------------------------------------
AppiumDriver: available
...
[afterEach] --------------------------------------------------
AppiumDriver: available
...
[beforeEach] --------------------------------------------------
AppiumDriver: available
...
[afterEach] --------------------------------------------------
AppiumDriver: available
...
[afterAll] --------------------------------------------------
AppiumDriver: available
...
202	2023/04/04 22:20:07.238	{}	[info]	()	Logging to file:////Users/wave1008/Downloads/TestResults/androidSettingsConfig/2023-04-04_221948/UITestEvent1/
Copying _ReportScript.js to /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2023-04-04_221948/UITestEvent1
Copying _ReportStyle.css to /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2023-04-04_221948/UITestEvent1
Loading: /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2023-04-04_221948/UITestEvent1/TestLog(commandList)_20230404221948.log
Saved: /Users/wave1008/Downloads/TestResults/androidSettingsConfig/2023-04-04_221948/UITestEvent1/UITestEvent1@a.xlsx

Cleaning up unclosed ZipFile for archive /Users/wave1008/Downloads/TestResults/androidSettingsConfig/TestList_androidSettingsConfig.xlsx
203	2023/04/04 22:20:07.664	{}	[info]	()	Quitting TestDriver.
204	2023/04/04 22:20:07.766	{}	[info]	()	Test class executed. (duration: 19.1 sec)
...
[finally] --------------------------------------------------
AppiumDriver: NOT available
...
Disconnected from the target VM, address: '127.0.0.1:49896', transport: 'socket'

Process finished with exit code 0
```

### Link

- [index](../../index.md)
