# tempSelector (Vision)

You can register selector(nickname) on demand using `tempSelector` function.

## Sample code

[Getting samples](../../../getting_samples.md)

### TempSelector1.kt

(`src/test/kotlin/tutorial/basic/TempSelector1.kt`)

```kotlin
    @Test
    @Order(10)
    fun tempSelector1() {

        tempSelector("{First Item}", "Network & internet")

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("{First Item}")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }
```

### See also

[Nickname](../../selector_and_nickname/nickname.md)

### Link

- [index](../../../../index.md)

