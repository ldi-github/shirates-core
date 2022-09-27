# Existence assertion

You can check existence of element using these functions.

## Functions

|   group   | function                | description                                               | return value                                                  |
|:---------:|:------------------------|:----------------------------------------------------------|:--------------------------------------------------------------|
|   exist   | exist                   | Assert that the element exists in current screen          | TestElement(if exists)<br>TestNGException(if doesn't exist)   |
|   exist   | existWithScrollDown     | Assert that the element exists with scrolling down        | (same as above)                                               |
|   exist   | existWithScrollUp       | Assert that the element exists with scrolling up          | (same as above)                                               |
|   exist   | existInScanResults      | Assert that the element exists in scan results            | (same as above)                                               |
| dontExist | dontExist               | Assert that the element doesn't exist in current screen   | empty element(if doesn't exist)<br>TestNGException(if exists) |
| dontExist | dontExistWithScrollDown | Assert that the element doesn't exist with scrolling down | (same as above)                                               |
| dontExist | dontExistWithScrollUp   | Assert that the element doesn't exist with scrolling up   | (same as above)                                               |
| dontExist | dontExistInScanResults  | Assert that the element doesn't exist in scan results     | (same as above)                                               |

## Example

### ExistDontExist1.kt

(`kotlin/tutorial/basic/ExistDontExist1.kt`)

```kotlin
@Test
@Order(10)
fun exist_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.exist("Network & internet")
            }
        }
    }
}

@Test
@Order(20)
fun exist_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.exist("System")
            }
        }
    }
}

@Test
@Order(30)
fun existWithScrollDown_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.existWithScrollDown("System")
            }
        }
    }
}

@Test
@Order(40)
fun existWithScrollDown_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.existWithScrollDown("Network business")
            }
        }
    }
}

@Test
@Order(50)
fun existWithScrollUp_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
                    .flickBottomToTop()
            }.expectation {
                it.existWithScrollUp("Network & internet")
            }
        }
    }
}

@Test
@Order(60)
fun existWithScrollUp_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
                    .flickBottomToTop()
            }.expectation {
                it.existWithScrollUp("Network business")
            }
        }
    }
}

@Test
@Order(70)
fun existInScanResult_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                describe("Scans elements with scrolling down.")
                    .scanElements()
            }.expectation {
                describe("Asserts that expected elements exist in scan results.")
                    .existInScanResults("Network & internet")
                    .existInScanResults("Storage")
                    .existInScanResults("System")
            }
        }
    }
}

@Test
@Order(80)
fun existInScanResult_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.condition {
                describe("Scans elements with scrolling down.")
                    .scanElements()
            }.expectation {
                it.existInScanResults("Network & internet")
                    .existInScanResults("Storage")
                    .existInScanResults("System")
                    .existInScanResults("Network business")
            }
        }
    }
}

@Test
@Order(90)
fun dontExist_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.dontExist("System")
            }
        }
    }
}

@Test
@Order(100)
fun dontExist_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.dontExist("Network & internet")
            }
        }
    }
}

@Test
@Order(110)
fun dontExistWithScrollDown_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.dontExistWithScrollDown("Network business")
            }
        }
    }
}

@Test
@Order(120)
fun dontExistWithScrollDown_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.dontExistWithScrollDown("System")
            }
        }
    }
}

@Test
@Order(130)
fun dontExistWithScrollUp_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.condition {
                it.flickBottomToTop()
            }.expectation {
                it.dontExistWithScrollUp("Network business")
            }
        }
    }
}

@Test
@Order(140)
fun dontExistWithScrollUp_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.condition {
                it.flickBottomToTop()
            }.expectation {
                it.dontExistWithScrollUp("System")
            }
        }
    }
}

@Test
@Order(150)
fun dontExistInScanResults_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.condition {
                it.scanElements()
            }.expectation {
                it.dontExistInScanResults("Switch")
                    .dontExistInScanResults("PS5")
                    .dontExistInScanResults("XBOX")
            }
        }
    }
}

@Test
@Order(160)
fun dontExistInScanResults_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.condition {
                it.scanElements()
            }.expectation {
                it.dontExistInScanResults("Switch")
                    .dontExistInScanResults("PS5")
                    .dontExistInScanResults("XBOX")
                    .dontExistInScanResults("Network & internet")
            }
        }
    }
}
```

### Link

- [index](../../../index.md)


