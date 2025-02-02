# Branch function (ifImageLabelIs, ifImageLabelIsNot) (Vision)

You can use special branch functions for image label.

## functions

| function          | description                                                                                   |
|:------------------|:----------------------------------------------------------------------------------------------|
| ifImageLabelIs    | The code block is executed when specified image label matches the image of the element        |
| ifImageLabelIsNot | The code block is executed when specified image label does not match the image of the element |

## Sample code

[Getting samples](../../getting_samples.md)

### IfImageLabelIs1.kt

(`src/test/kotlin/tutorial/basic/IfImageLabelIs1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifImageLabelIsTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.detect("Network & internet")
                        .leftItem()
                        .ifImageLabelIs("[Network & internet Icon]") {
                            OK("ifImageLabelIs called")
                        }.ifElse {
                            NG()
                        }
                    it.detect("Network & internet")
                        .leftItem()
                        .ifImageLabelIsNot("[Network & internet Icon]") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                }
            }
            case(2) {
                expectation {
                    it.detect("Network & internet")
                        .leftItem()
                        .ifImageLabelIs("[App Icon]") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                    it.detect("Network & internet")
                        .leftItem()
                        .ifImageLabelIsNot("[App Icon]") {
                            OK("ifImageLabelIsNot called")
                        }.ifElse {
                            NG()
                        }
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)

