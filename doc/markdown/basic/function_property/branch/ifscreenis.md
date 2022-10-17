# Branch function (ifScreenIs, ifScreenIsNot)

You can use special branch functions for screen.

## functions

| function      | description                                                       |
|:--------------|:------------------------------------------------------------------|
| ifScreenIs    | The code block is executed when specified screen is displayed     |
| ifScreenIsNot | The code block is executed when specified screen is not displayed |

### IfScreenIs1.kt

(`kotlin/tutorial/basic/IfScreenIs1.kt`)

```kotlin
@Test
@Order(10)
fun ifScreenIsTest() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                ifScreenIs("[Android Settings Top Screen]") {
                    OK("ifScreenIs called")
                }.ifElse {
                    NG()
                }
            }
        }
        case(2) {
            action {
                it.tap("[Network & internet]")
            }.expectation {
                ifScreenIs("[Network & internet Screen]") {
                    OK("ifScreenIs called")
                }.ifElse {
                    NG()
                }
            }
        }
    }
}

@Test
@Order(20)
fun ifScreenIsNotTest() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                ifScreenIsNot("[Android Settings Top Screen]") {
                    NG()
                }.ifElse {
                    OK("ifElse called")
                }
            }
        }
        case(2) {
            action {
                it.tap("[Network & internet]")
            }.expectation {
                ifScreenIsNot("[Network & internet Screen]") {
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

