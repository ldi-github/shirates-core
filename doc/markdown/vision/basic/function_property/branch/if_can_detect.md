# Branch function (ifCanDetect, ifCanDetectNot) (Vision)

You can use special branch functions for element.

## functions

| function       | description                                                         |
|:---------------|:--------------------------------------------------------------------|
| ifCanDetect    | The code block is executed when specified text is on the screen     |
| ifCanDetectNot | The code block is executed when specified text is not on the screen |

## Sample code

[Getting samples](../../../getting_samples.md)

### IfCanSelect1.kt

(`src/test/kotlin/tutorial/basic/IfCanDetect1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifCanDetectTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifCanDetect("Network & internet") {
                        OK("ifCanDetect called")
                    }.ifElse {
                        NG()
                    }

                    ifCanDetectNot("System") {
                        OK("ifCanDetectNot called")
                    }.ifElse {
                        NG()
                    }
                }
            }
            case(2) {
                action {
                    it.scrollToBottom()
                }.expectation {
                    ifCanDetect("Network & internet") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }

                    ifCanDetectNot("System") {
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

- [index](../../../../index.md)

