# clearInput

You can clear value of input widget using **clearInput** function.

## Example

### AndroidSendKeys1.kt

(`kotlin/tutorial/basic/AndroidSendKeys1.kt`)

```kotlin
@Test
@Order(20)
fun clearInput() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Search Screen]")
                    .textIs("Search settings")
                    .sendKeys("clock")
                    .textIs("clock")
            }.action {
                it.clearInput()
            }.expectation {
                it.textIs("Search settings")
            }
        }
    }
}
```

### Link

- [index](../../../index.md)

