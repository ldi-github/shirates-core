# attribute assertion

You can assert the value of any attribute of the element using these functions.

## functions

| function    |
|:------------|
| attributeIs |

## Example

### AssertingAttribute1.kt

```kotlin
@Test
@Order(90)
fun attributeAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.attributeIs("package", "com.android.settings")
            }
        }
    }
}

@Test
@Order(100)
fun attributeAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.attributeIs("package", "com.google.android.calculator")
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### Link

- [index](../../../index.md)

