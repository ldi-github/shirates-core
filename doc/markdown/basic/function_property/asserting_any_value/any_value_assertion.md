# Any value assertion

You can assert any value using these functions.

## functions

| function    | description                                  |
|:------------|----------------------------------------------|
| thisIs      | Assert that this value is expected value     |
| thisIsNot   | Assert that this value is not expected value |
| thisIsTrue  | Assert that this value is true               |
| thisIsFalse | Assert that this value is false              |

## Example

### AssertingAnyValue1.kt

(`kotlin/tutorial/basic/AssertingAnyValue1.kt`)

```kotlin
@Test
@Order(10)
fun stringAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                "string1"
                    .thisIs("string1")
                    .thisIsNot("string2")

                    .thisStartsWith("s")
                    .thisStartsWithNot("t")

                    .thisContains("ring")
                    .thisContainsNot("square")

                    .thisEndsWith("ring1")
                    .thisEndsWithNot("ring2")

                    .thisMatches("^str.*")
                    .thisMatchesNot("^tex.*")
            }
        }

        case(2) {
            expectation {
                "".thisIsEmpty()
                "hoge".thisIsNotEmpty()

                " ".thisIsBlank()
                "hoge".thisIsNotBlank()
            }
        }

    }
}

@Test
@Order(20)
fun stringAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                "string1"
                    .thisContains("square")
            }
        }
    }
}

@Test
@Order(30)
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
@Order(40)
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

### Link

- [index](../../../index.md)

