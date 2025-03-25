# tempSelector, tempValue (Vision)

You can register and get value on demand with these functions for temporary use.

## functions

| function                             | description                         |
|:-------------------------------------|-------------------------------------|
| tempSelector(_nickname, expression_) | Register _expression_ as _nickname_ |
| tempValue(_nickname_)                | Get value by _nickname_             |

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
    
    @Test
    @Order(20)
    fun tempValue1() {

        scenario {
            case(1) {
                condition {
                    tempSelector("{nickname1}", "value1")
                }.expectation {
                    tempValue("{nickname1}").thisIs("value1")
                }
            }
            case(2) {
                condition {
                    tempSelector("{nickname1}", "value2")
                }.expectation {
                    tempValue("{nickname1}").thisIs("value2")
                }
            }
        }
    }    
```

### See also

[Nickname](../../selector_and_nickname/nickname.md)

### Link

- [index](../../../../index.md)

