# Screen assertion

You can assert screen using **screenIs** function.

You can use **isScreen** function for getting whether the screen is displayed or not.

## functions

| function   | description                                     |
|:-----------|-------------------------------------------------|
| screenIs   | Assert that the screen is displayed             |
| isScreen   | Returns true if the screen is displayed         |
| screenIsOf | Assert that any of the screens is displayed     |
| isScreenOf | Returns true if any of the screens is displayed |

## Screen nickname file

In advance of using those functions, you must
define [screen nickname file](../../selector_and_nickname/nickname/screen_nickname.md)

## Example

### ScreenIsAndIsScreen1.kt

```kotlin
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
fun isScreen_true() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.isScreen("[Android Settings Top Screen]")
                    .ifTrue {
                        OK("This is [Android Settings Top Screen]")
                    }
            }
        }
    }
}

@Test
@Order(40)
fun isScreen_false() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.isScreen("[System Screen]")
                    .ifFalse {
                        OK("This is not [System Screen]")
                    }
            }
        }
    }
}
```

## Example

### ScreenIsOfAndIsScreenOf1.kt

```kotlin
@Test
@Order(10)
fun screenIsOf_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.screenIsOf("[Android Settings Top Screen]")
                    .screenIsOf("[Android Settings Top Screen]", "[Network & internet]", "[System Screen]")
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
                it.screenIsOf("[Network & internet]", "[System Screen]")
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
                it.isScreenOf("[Android Settings Top Screen]", "[Network & internet]", "[System Screen]")
                    .ifTrue {
                        OK("This is of [Android Settings Top Screen],[Network & internet],[System Screen]")
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
                it.isScreenOf("[Network & internet]", "[System Screen]")
                    .ifFalse {
                        OK("This is not of [Network & internet],[System Screen]")
                    }
            }
        }
    }
}
```

### Link

- [index](../../../index.md)

