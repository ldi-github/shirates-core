# clear

You can clear value of textbox using **clear** function.

## Example

### ValueAndClear1.kt

```kotlin
@Test
@Order(10)
fun valueAndClear() {

    scenario {
        case(1) {
            condition {
                it.screenIs("[Android Settings Top Screen]")
                    .tap("[Search settings]")
            }.action {
                it.value("app")
            }.expectation {
                it.textIs("app")
            }
        }
        case(2) {
            action {
                it.clear()
            }.expectation {
                it.textIs("Search settings")
            }
        }
    }

}
```

### Link

- [index](../../../index.md)

