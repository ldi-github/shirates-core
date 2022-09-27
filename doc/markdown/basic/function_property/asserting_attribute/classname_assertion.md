# className assertion

You can assert the value of `className` or `type` property of the element using these functions.

## functions

| function |
|:---------|
| classIs  |

## Example

### AssertingAttribute1.kt

(`kotlin/tutorial/basic/AssertingAttribute1.kt`)

```kotlin
@Test
@Order(70)
fun classNameAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.classIs("android.widget.ImageView")
                    .classIsNot("android.widget.TextView")
            }
        }
    }
}

@Test
@Order(80)
fun classNameAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it.classIs("android.widget.TextView")
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### Link

- [index](../../../index.md)

