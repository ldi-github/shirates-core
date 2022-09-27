# access assertion

You can assert the value of `access` property of the element using these functions.

## functions

| function            |
|:--------------------|
| accessIs            |
| accessIsNot         |
| accessStartsWith    |
| accessStartsWithNot |
| accessContains      |
| accessContainsNot   |
| accessEndsWith      |
| accessEndsWithNot   |
| accessMatches       |
| accessMatchesNot    |
| accessIsEmpty       |
| accessIsNotEmpty    |

## Example

#### AssertingAttribute1.kt

(`kotlin/tutorial/basic/AssertingAttribute1.kt`)

```kotlin
@Test
@Order(50)
fun accessAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Network & internet Screen]")
            }.action {
                it.select("@Network & internet", log = true)
            }.expectation {
                it.accessIs("Network & internet")
                    .accessIsNot("System")
            }
        }
    }
}

@Test
@Order(60)
fun accessAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Network & internet Screen]")
            }.action {
                it.select("@Network & internet", log = true)
            }.expectation {
                it.accessIs("Connected devices")
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### Link

- [index](../../../index.md)
