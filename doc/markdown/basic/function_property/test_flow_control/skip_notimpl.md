# SKIP, NOTIMPL

You can skip or abort test using these functions.

| function      | description                              |
|:--------------|:-----------------------------------------|
| SKIP_CASE     | Skip the test case.                      |
| SKIP_SCENARIO | Skip the test scenario.                  |
| NOTIMPL       | Abort the test as it is not implemented. |

## Example

### SkipAndNotImpl1.kt

(`kotlin/tutorial/basic/SkipAndNotImpl1.kt`)

```kotlin
@Test
@Order(10)
fun skipCase() {

    scenario {
        case(1) {
            condition {
                output("platformVersion=$platformVersion")
                if (platformVersion.toInt() > 5) {
                    SKIP_CASE("case(1) skipped.")   // Skip execution of commands (log only)
                }
            }.action {
                it.tap("Settings")  // Skipped
            }.expectation {
                it.textIs("Settings")    // Skipped
            }
        }

        case(2) {
            action {
                it.tap("Settings")  // Executed
            }.expectation {
                it.textIs("Settings")    // Executed
            }
        }
    }
}

@Test
@Order(20)
fun skipScenario() {

    scenario {
        case(1) {
            condition {
                output("platformVersion=$platformVersion")
                if (platformVersion.toInt() > 5) {
                    SKIP_SCENARIO()     // Skip execution of commands (log only)
                }
            }.action {
                it.tap("Settings")  // Skipped
            }.expectation {
                it.textIs("Settings")    // Skipped
            }
        }

        case(2) {
            action {
                it.tap("Settings")  // Skipped
            }.expectation {
                it.textIs("Settings")    // Skipped
            }
        }
    }
}

@Test
@Order(30)
fun notImpl_case() {

    scenario {
        case(1) {
            action {
                it.tap("Settings")  // Executed
            }.expectation {
                it.textIs("Settings")    // Executed
            }
        }

        case(2) {
            condition {
                NOTIMPL()   // Abort this test
            }.action {
                it.tap("Settings")  // Not reached
            }.expectation {
                it.textIs("Settings")   // Not reached
            }
        }

        case(3) {
            action {
                it.tap("Settings")  // Not reached
            }.expectation {
                it.textIs("Settings")    // Not reached
                NOTIMPL("To be implement.")     // Not reached
                it.textIsNot("Connected devices")   // Not reached
            }
        }
    }
}

@Test
@Order(40)
fun notImpl_scenario() {

    scenario {
        NOTIMPL()   // Abort this scenario

        case(1) {   // Not reached
            action {
                it.tap("Settings")
            }.expectation {
                it.textIs("Settings")
            }
        }

        case(2) {   // Not reached
            action {
                it.tap("Settings")
            }.expectation {
                it.textIs("Settings")
            }
        }
    }
}
```

### Link

- [index](../../../index.md)
