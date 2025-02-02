# Using CheckStateClassifier (Vision)

Shirates/Vision recognizes check state (`[ON]`/`[OFF]`) of the image.

## Sample code

[Getting samples](../../getting_samples.md)

### AndroidSettingsVisionDemo.kt

(`src/test/kotlin/demo/vision/AndroidSettingsVisionDemo.kt`)

```kotlin
    @Test
    fun airplaneModeSwitch() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("Network & internet")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    it.detect("Airplane mode")
                        .rightItem()
                        .checkIsOFF()
                }.action {
                    it.tap()
                }.expectation {
                    it.detect("Airplane mode")
                        .rightItem()
                        .checkIsON()
                }
            }
            case(3) {
                action {
                    it.tap()
                }.expectation {
                    it.detect("Airplane mode")
                        .rightItem()
                        .checkIsOFF()
                }
            }
        }
    }
```

### Running test

1. Set `os` in `testrun.global.properties` to run as android (default is android).

```properties
## OS --------------------
#os=ios
```

2. Right-click on `airplaneModeSwitch()` and select `debug` to run test.

### TestResults

You got test results files in TestResults directory(`~/Downloads/TestResults` is default).

### _Report(Simple).html

![](_images/using_check_state_classifier_report.png)

### checkIsON, checkIsOFF

`checkIsON` and `checkIsOFF` is implemented as follows using `CheckStateClassifier`.

```kotlin
/**
 * checkIsON
 */
fun VisionElement.checkIsON(
    classifierName: String = "CheckStateClassifier",
    containedText: String = "[ON]",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "checkIsON"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkIsCore(
            containedText = containedText,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds
        )
    }
    return this
}

/**
 * checkIsOFF
 */
fun VisionElement.checkIsOFF(
    classifierName: String = "CheckStateClassifier",
    containedText: String = "[OFF]",
    waitSeconds: Double = testContext.syncWaitSeconds,
    message: String? = null,
): VisionElement {

    val command = "checkIsOFF"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        this.checkIsCore(
            containedText = containedText,
            message = assertMessage,
            classifierName = classifierName,
            waitSeconds = waitSeconds
        )
    }
    return this
}
```

### Link

- [index](../../../index.md)
