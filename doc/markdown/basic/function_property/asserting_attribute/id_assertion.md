# id assertion

You can assert the value of `id` or `name` property of the element using these functions.

## functions

| function |
|:---------|
| idIs     |

## Example

### AssertingAttribute1.kt

(`kotlin/tutorial/basic/AssertingAttribute1.kt`)

```kotlin
@Test
@Order(30)
fun idAssertion_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it
                    .idIs("account_avatar")
                    .idIs("com.android.settings:id/account_avatar")
            }
        }
    }
}

@Test
@Order(40)
fun idAssertion_NG() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("#account_avatar", log = true)
            }.expectation {
                it
                    // OK. expected is converted to "com.android.settings:id/account_avatar"
                    .idIs("account_avatar")

                    // OK. expected is converted to "com.android.settings:id/account_avatar"
                    .idIs("account_avatar", auto = true)

                    // NG. expected is "account_avatar"
                    .idIs("account_avatar", auto = false)
            }
        }
    }
}
```

#### Note

`log = true` is specified for demonstration. This should not be specified in production code. Default is false.

### Link

- [index](../../../index.md)

