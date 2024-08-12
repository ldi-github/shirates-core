import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.interactions.Pause
import org.openqa.selenium.interactions.PointerInput
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.invalidateCache
import shirates.core.driver.commandextension.suppressCache
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.android.AdbUtility
import java.time.Duration

/**
 * showTask
 */
internal fun TestDrive.showTask(
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = TestDriver.it

    val command = "showTask"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        if (isAndroid) {
            TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.APP_SWITCH))
        } else {
            throw NotImplementedError("Not implemented in iOS")

//            val b = viewBounds
//            val finger = PointerInput(PointerInput.Kind.TOUCH, "finger")
//            val sequence = org.openqa.selenium.interactions.Sequence(finger, 0)
//
//            val x1 = b.centerX
//            val y1 = b.bottom
//            sequence.addAction(
//                finger.createPointerMove(
//                    Duration.ofMillis(0),
//                    PointerInput.Origin.viewport(),
//                    x1,
//                    y1
//                )
//            )
//            sequence.addAction(
//                finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg())
//            )
//            sequence.addAction(
//                Pause(finger, Duration.ofMillis(1000))
//            )
//
//            val x2 = x1
//            val y2 = b.centerY
//            sequence.addAction(
//                finger.createPointerMove(
//                    Duration.ofMillis((0.3 * 1000).toLong()),
//                    PointerInput.Origin.viewport(), x2, y2
//                )
//            )
//
//            sequence.addAction(
//                finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg())
//            )
//            try {
//                driver.appiumDriver.perform(mutableListOf(sequence))
//            } catch (t: InvalidElementStateException) {
//                TestLog.trace(t.message ?: t.stackTraceToString())
//                //  https://github.com/appium/java-client/issues/2045
//            }
        }
        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    return TestElement.emptyElement
}

/**
 * goPreviousApp
 */
fun TestDrive.goPreviousApp(
    waitSeconds: Double = testContext.shortWaitSeconds
): TestElement {

    val testElement = TestDriver.it

    val command = "goPreviousApp"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        if (isAndroid) {
            val canUseGesture = AdbUtility.isOverlayEnabled(
                name = "com.android.internal.systemui.navbar.gestural",
                udid = testProfile.udid
            )
            if (canUseGesture) {
                goPreviousAppWithGestureAndroid()
            } else {
                goPreviousAppWithAppSwitch()
            }
        } else if (isiOS) {
            goPreviousAppWithGestureIos()
        } else {
            throw NotImplementedError()
        }

        invalidateCache()
        wait(waitSeconds = waitSeconds)
    }

    return lastElement

}

internal fun TestDrive.goPreviousAppWithAppSwitch(): TestElement {

    suppressCache {
        val b = rootElement.bounds
        TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.APP_SWITCH))
        wait()
        TestDriver.androidDriver.pressKey(KeyEvent(AndroidKey.APP_SWITCH))
    }
    invalidateCache()
    return lastElement
}

internal fun TestDrive.goPreviousAppWithGestureAndroid() {
    val b = rootElement.bounds
    val finger = PointerInput(PointerInput.Kind.TOUCH, "finger")
    val sequence = org.openqa.selenium.interactions.Sequence(finger, 0)

    val x1 = b.centerX / 2
    val y1 = b.bottom - 10
    sequence.addAction(
        finger.createPointerMove(
            Duration.ofMillis(0),
            PointerInput.Origin.viewport(),
            x1,
            y1
        )
    )
    sequence.addAction(
        finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg())
    )
    sequence.addAction(
        Pause(finger, Duration.ofMillis(0))
    )

    val x2 = b.right
    val y2 = y1
    sequence.addAction(
        finger.createPointerMove(
            Duration.ofMillis((0.3 * 1000).toLong()),
            PointerInput.Origin.viewport(), x2, y2
        )
    )

    sequence.addAction(
        finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg())
    )
    try {
        driver.appiumDriver.perform(mutableListOf(sequence))
    } catch (t: InvalidElementStateException) {
        TestLog.trace(t.message ?: t.stackTraceToString())
        //  https://github.com/appium/java-client/issues/2045
    }

}

internal fun TestDrive.goPreviousAppWithGestureIos() {

    val b = rootElement.bounds
    val finger = PointerInput(PointerInput.Kind.TOUCH, "finger")
    val sequence = org.openqa.selenium.interactions.Sequence(finger, 0)

    val x1 = b.centerX
    val y1 = b.bottom - 1
    sequence.addAction(
        finger.createPointerMove(
            Duration.ofMillis(0),
            PointerInput.Origin.viewport(),
            x1,
            y1
        )
    )
    sequence.addAction(
        finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg())
    )
    sequence.addAction(
        Pause(finger, Duration.ofMillis(0))
    )

    val x2 = x1
    val y2 = y1 - 50
    sequence.addAction(
        finger.createPointerMove(
            Duration.ofMillis((0.3 * 1000).toLong()),
            PointerInput.Origin.viewport(), x2, y2
        )
    )

    val x3 = b.right
    val y3 = y2
    sequence.addAction(
        finger.createPointerMove(
            Duration.ofMillis((0.3 * 1000).toLong()),
            PointerInput.Origin.viewport(), x3, y3
        )
    )

    sequence.addAction(
        finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg())
    )
    try {
        driver.appiumDriver.perform(mutableListOf(sequence))
    } catch (t: InvalidElementStateException) {
        TestLog.trace(t.message ?: t.stackTraceToString())
        //  https://github.com/appium/java-client/issues/2045
    }

}
