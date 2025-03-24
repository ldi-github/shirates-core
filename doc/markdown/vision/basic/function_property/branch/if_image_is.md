# Branch function (ifImageIs, ifImageIsNot) (Vision)

You can use special branch functions for image label.

## functions

| function     | description                                                                                   |
|:-------------|:----------------------------------------------------------------------------------------------|
| ifImageIs    | The code block is executed when specified image label matches the image of the element        |
| ifImageIsNot | The code block is executed when specified image label does not match the image of the element |

## Sample code

[Getting samples](../../../getting_samples.md)

### IfImageIs1.kt

(`src/test/kotlin/tutorial/basic/IfImageIs1.kt`)

```kotlin
    @Test
    @Order(10)
    fun ifImageIsTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.detect("Network & internet")
                        .leftItem()
                        .ifImageIs("[Network & internet Icon]") {
                            OK("ifImageIs called")
                        }.ifElse {
                            NG()
                        }
                    it.detect("Network & internet")
                        .leftItem()
                        .ifImageIsNot("[Network & internet Icon]") {
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
                        .ifImageIs("[App Icon]") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                    it.detect("Network & internet")
                        .leftItem()
                        .ifImageIsNot("[App Icon]") {
                            OK("ifImageIsNot called")
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

