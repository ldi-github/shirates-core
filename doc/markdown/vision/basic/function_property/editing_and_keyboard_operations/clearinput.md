# clearInput (Vision)

You can clear value of input widget using **clearInput** function.

## Sample code

[Getting samples](../../../getting_samples.md)

### AndroidSendKeys1.kt

(`src/test/kotlin/tutorial/basic/AndroidSendKeys1.kt`)

```kotlin
    @Test
    @Order(10)
    fun sendKeys_clearInput() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                }.action {
                    it.sendKeys("clock")
                }.expectation {
                    it.textIs("clock")
                }
            }
            case(2) {
                action {
                    it.clearInput()
                }.expectation {
                    it.textIs("Search settings")
                }
            }
        }
    }
```

### iOSSendKeys1.kt

(`src/test/kotlin/tutorial/basic/iOSSendKeys1.kt`)

```kotlin
    @Test
    @Order(10)
    fun sendKeys_clearInput() {

        scenario {
            case(1) {
                condition {
                    it.launchApp("[Maps]")
                        .waitForDisplay("Search Maps")
                        .tap()
                }.action {
                    it.sendKeys("safari")
                }.expectation {
                    it.textIs("safari")
                }
            }
            case(2) {
                action {
                    it.clearInput()
                }.expectation {
                    it.textIs("Search Maps")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)

