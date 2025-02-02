# Boolean value assertion (Vision)

You can assert boolean value using these functions.

## functions

| function    | description                     |
|:------------|---------------------------------|
| thisIsTrue  | Assert that this value is true  |
| thisIsFalse | Assert that this value is false |

## Sample code

[Getting samples](../../getting_samples.md)

### AssertingAnyValue1.kt

(`src/test/kotlin/tutorial/basic/AssertingAnyValue1.kt`)

```kotlin
    @Test
    @Order(40)
    fun booleanAssertion_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    true.thisIsTrue()
                    false.thisIsFalse()

                    true.thisIsTrue("The value is true")
                    false.thisIsFalse("The value is false")
                }
            }
            case(2) {
                expectation {
                    it.isApp("Settings")
                        .thisIsTrue("This app is <Settings>")
                    it.isApp("Chrome")
                        .thisIsFalse("This app is not <Chrome>")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun booleanAssertion_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    false.thisIsTrue()
                }
            }
        }
    }
```

![](_images/asserting_boolean_value.png)

### Link

- [index](../../../../index.md)

