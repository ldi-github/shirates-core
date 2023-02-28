# Package assertion

You can assert package of current app using these functions.

## functions

| function  |
|:----------|
| packageIs |

## Example

### AssertingOthers1.kt

(`kotlin/tutorial/basic/AssertingOthers1.kt`)

```kotlin
@Test
@Order(50)
fun packageIs_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.packageIs("com.android.settings")
            }
        }

        case(2) {
            action {
                it.launchApp("Chrome")
            }.expectation {
                it.packageIs("com.android.chrome")
            }
        }
    }
}

@Test
@Order(60)
fun packageIs_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.packageIs("com.android.chrome")
            }
        }
    }
}
```

### Link

- [index](../../../index.md)
