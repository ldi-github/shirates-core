# App assertion

You can assert app using these functions.

## functions

| function |
|:---------|
| appIs    |
| isApp    |

### AssertingOthers1.kt

(`kotlin/tutorial/basic/AssertingOthers1.kt`)

```kotlin
@Test
@Order(10)
fun appIs_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.appIs("Settings")
            }
        }

        case(2) {
            condition {
                it.launchApp("Chrome")
            }.expectation {
                val isApp = it.isApp("Chrome")
                output("isApp(\"Chrome\")=$isApp")
                it.appIs("Chrome")
            }
        }
    }
}

@Test
@Order(20)
fun appIs_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.appIs("Chrome")
            }
        }
    }
}
```

### Link

- [index](../../../index.md)
