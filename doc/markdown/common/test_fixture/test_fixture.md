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

## Sample code

[Getting samples](../../getting_samples.md)

### TestFixtureEvent1.kt

(kotlin/tutorial/basic/TestFixtureEvent1.kt)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.driver
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.testcode.VisionTest

class TestFixtureEvent1 : VisionTest() {

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

        printEvent("setEventHandlers")
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
[setEventHandlers] --------------------------------------------------
AppiumDriver: available
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
[setEventHandlers] --------------------------------------------------
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
262	[00:01:25]	2025/04/24 03:57:54.611	{}	0	-	[info]	+1030	C	()	Logging to file:////Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-24_035628/TestFixtureEvent1/
Copying jar content _ReportScript.js to /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-24_035628/TestFixtureEvent1
Copying jar content _ReportStyle.css to /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-24_035628/TestFixtureEvent1
Loading: /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-24_035628/TestFixtureEvent1/TestLog(commandList)_20250424035628.log
Saved: /Users/wave1008/Downloads/TestResults/testConfig@a/2025-04-24_035628/TestFixtureEvent1/TestFixtureEvent1@a.xlsx

263	[00:01:27]	2025/04/24 03:57:55.762	{}	0	-	[info]	+1151	C	()	Quitting TestDriver.
264	[00:01:27]	2025/04/24 03:57:56.369	{}	0	-	[info]	+607	C	()	Test class executed. (duration: 87.8 sec)
[finally] --------------------------------------------------
AppiumDriver: NOT available
Disconnected from the target VM, address: '127.0.0.1:57962', transport: 'socket'

Process finished with exit code 0
```

### Link

- [index(Vision)](../../index.md)
- [index(Classic)](../../classic/index.md)

