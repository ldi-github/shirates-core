# Branch function (ifImageExist, ifImageExistNot)

You can use special branch functions for image.

## functions

| function        | description                                                          |
|:----------------|:---------------------------------------------------------------------|
| ifImageExist    | The code block is executed when specified image is on the screen     |
| ifImageExistNot | The code block is executed when specified image is not on the screen |

### IfImageExist1.kt

(`kotlin/tutorial/basic/IfImageExist1.kt`)

```kotlin
@Test
@Order(0)
fun setupImage() {

    TestSetupHelper.setupImageAndroidSettingsTopScreen()
}

@Test
@Order(10)
fun ifImageExistTest() {

    ImageFileRepository.setup(screenDirectory = TestLog.testResults.resolve("images"))

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                ifImageExist("[Network & internet Icon].png") {
                    OK("ifImageExist called")
                }.ifElse {
                    NG()
                }

                ifImageExistNot("[System Icon].png") {
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
                ifImageExist("[Network & internet Icon].png") {
                    NG()
                }.ifElse {
                    OK("ifElse called")
                }

                ifImageExistNot("[System Icon].png") {
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

