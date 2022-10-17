# Branch function (ifCanSelect, ifCanSelectNot)

You can use special branch functions for element.

## functions

| function       | description                                                            |
|:---------------|:-----------------------------------------------------------------------|
| ifCanSelect    | The code block is executed when specified element is on the screen     |
| ifCanSelectNot | The code block is executed when specified element is not on the screen |

### IfCanSelect1.kt

(`kotlin/tutorial/basic/IfCanSelect1.kt`)

```kotlin
@Test
@Order(10)
fun ifCanSelectTest() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                ifCanSelect("[Network & internet]") {
                    OK("ifCanSelect called")
                }.ifElse {
                    NG()
                }

                ifCanSelectNot("[System]") {
                    OK("ifCanSelectNot called")
                }.ifElse {
                    NG()
                }
            }
        }
        case(2) {
            action {
                it.scrollToBottom()
            }.expectation {
                ifCanSelect("[Network & internet]") {
                    NG()
                }.ifElse {
                    OK("ifElse called")
                }

                ifCanSelectNot("[System]") {
                    NG()
                }.ifElse {
                    OK("ifElse called")
                }
            }
        }
    }
}
```

### Link

- [index](../../../index.md)

