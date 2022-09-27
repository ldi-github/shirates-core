# Screen branch function

## functions

| function | description                                   |
|:---------|:----------------------------------------------|
| onScreen | This function is executed on screen matched.  |

## Example

### OnScreen1.kt

(`kotlin/tutorial/basic/OnScreen1.kt`)

```kotlin
@Test
@Order(10)
fun onScreen1() {

    scenario {
        case(1) {
            condition {
                it.macro("[System Screen]")
            }.expectation {
                onScreen("[Android Settings Top Screen]") {
                    it.screenIs("[Android Settings Top Screen]")
                }.onScreen("[System Screen]") {
                    it.screenIs("[System Screen]")
                }.not {
                    describe("Screen is $screenName")
                }
            }
        }
    }
}
```

### Link

- [index](../../../index.md)

