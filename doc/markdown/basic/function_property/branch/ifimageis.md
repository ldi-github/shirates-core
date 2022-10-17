# Branch function (ifImageIs, ifImageIsNot)

You can use special branch functions for image.

## functions

| function     | description                                                                             |
|:-------------|:----------------------------------------------------------------------------------------|
| ifImageIs    | The code block is executed when specified image matches the image of the element        |
| ifImageIsNot | The code block is executed when specified image does not match the image of the element |

### IfImageExist1.kt

(`kotlin/tutorial/basic/IfImageIs1.kt`)

```kotlin
@Test
@Order(0)
fun setupImage() {

    TestSetupHelper.setupImageAndroidSettingsTopScreen()
}

@Test
@Order(10)
fun ifImageIsTest() {

    ImageFileRepository.setup(screenDirectory = TestLog.testResults.resolve("images"))

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.select("[Network & internet Icon]")
                    .ifImageIs("[Network & internet Icon].png") {
                        OK("ifImageIs called")
                    }.ifElse {
                        NG()
                    }
                it.select("[Network & internet Icon]")
                    .ifImageIsNot("[Network & internet Icon].png") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
            }
        }
        case(2) {
            expectation {
                it.select("[Network & internet Icon]")
                    .ifImageIs("[App Icon].png") {
                        NG()
                    }.ifElse {
                        OK("ifElse called")
                    }
                it.select("[Network & internet Icon]")
                    .ifImageIsNot("[App Icon].png") {
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

- [index](../../../index.md)

