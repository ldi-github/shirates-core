# Screen assertion (Classic)

You can assert screen using **screenIs** function.

You can use **isScreen** function to getting whether the screen is displayed or not.

## functions

| function   | description                                     |
|:-----------|-------------------------------------------------|
| screenIs   | Assert that the screen is displayed             |
| isScreen   | Returns true if the screen is displayed         |
| screenIsOf | Assert that any of the screens is displayed     |
| isScreenOf | Returns true if any of the screens is displayed |

## Screen nickname file

In advance of using these functions, you must
define [Screen Nickname File](../../selector_and_nickname/nickname/screen_nickname.md)

## Example

### ScreenIsAndIsScreen1.kt

(`kotlin/tutorial/basic/ScreenIsAndIsScreen1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifFalse
import shirates.core.driver.branchextension.ifTrue
import shirates.core.driver.commandextension.isScreen
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScreenIsAndIsScreen1 : UITest() {

    @Test
    @Order(10)
    fun screenIs_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun screenIs_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun isScreen_ifTrue() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.isScreen("[Android Settings Top Screen]")
                        .ifTrue("If screen is [Android Settings Top Screen]") {
                            OK("This is [Android Settings Top Screen]")
                        }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun isScreen_ifFalse() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.isScreen("[System Screen]")
                        .ifFalse("If screen is not [System Screen]") {
                            OK("This is not [System Screen]")
                        }
                }
            }
        }
    }

}
```

## Example

### ScreenIsOfAndIsScreenOf1.kt

(`kotlin/tutorial/basic/ScreenIsOfAndIsScreenOf1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifFalse
import shirates.core.driver.branchextension.ifTrue
import shirates.core.driver.commandextension.isScreenOf
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIsOf
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScreenIsOfAndIsScreenOf1 : UITest() {

    @Test
    @Order(10)
    fun screenIsOf_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIsOf("[Android Settings Top Screen]")
                        .screenIsOf("[Android Settings Top Screen]", "[Network & internet Screen]", "[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun screenIsOf_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIsOf("[Network & internet Screen]", "[System Screen]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun isScreenOf_true() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.isScreenOf("[Android Settings Top Screen]")
                        .ifTrue {
                            OK("This is [Android Settings Top Screen]")
                        }
                    it.isScreenOf("[Android Settings Top Screen]", "[Network & internet Screen]", "[System Screen]")
                        .ifTrue {
                            OK("This is of [Android Settings Top Screen],[Network & internet Screen],[System Screen]")
                        }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun isScreenOf_false() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.isScreenOf("[Network & internet Screen]", "[System Screen]")
                        .ifFalse {
                            OK("This is not of [Network & internet Screen],[System Screen]")
                        }
                }
            }
        }
    }

}
```

### Link

- [index](../../../index.md)

