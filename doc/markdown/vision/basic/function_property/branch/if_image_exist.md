# Branch function (ifImageExist, ifImageExistNot) (Vision)

You can use special branch functions for image.

## functions

| function        | description                                                                        |
|:----------------|:-----------------------------------------------------------------------------------|
| ifImageExist    | The code block is executed when an image with specified label is on the screen     |
| ifImageExistNot | The code block is executed when an image with specified label is not on the screen |

## Sample code

[Getting samples](../../getting_samples.md)

### IfImageExist1.kt

(`src/test/kotlin/tutorial/basic/IfImageExist1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifImageExistTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    ifImageExist("[Network & internet Icon]") {
                        OK("ifImageExist called")
                    }.ifElse {
                        NG()
                    }

                    ifImageExistNot("[System Icon]") {
                        OK("ifImageExistNot called")
                    }.ifElse {
                        NG()
                    }
                }
            }
            case(2) {
                action {
                    it.scrollToBottom()
                }.expectation {
                    ifImageExist("[Network & internet Icon]") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }

                    ifImageExistNot("[System Icon]") {
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

