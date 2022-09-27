# manual

In general, you can not automate all of your manual test cases because of reasons. Instead, you can describe manual
procedure using **manual** function.

## Example

### Manual1.kt

(`kotlin/tutorial/basic/Manual1.kt`)

```kotlin
@Test
@Order(10)
fun manualTest() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.manual("Animation should be displayed on start up.")
            }
        }
    }

}
```

### Link

- [index](../../../index.md)

