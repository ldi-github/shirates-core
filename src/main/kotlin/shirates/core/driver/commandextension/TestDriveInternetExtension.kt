package shirates.core.driver.commandextension

import io.appium.java_client.android.connection.ConnectionStateBuilder
import shirates.core.Const
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.branchextension.android
import shirates.core.logging.Message.message

/**
 * internetEnabled
 */
val TestDrive.internetEnabled: Boolean
    get() {
        if (TestMode.isNoLoadRun) {
            return true
        }

        return wiFiEnabled && mobileEnabled
    }

/**
 * wiFiEnabled
 */
val TestDrive.wiFiEnabled: Boolean
    get() {
        if (TestMode.isNoLoadRun) {
            return true
        }

        val isEnabled: Boolean
        if (isAndroid) {
            isEnabled = TestDriver.androidDriver.connection.isWiFiEnabled
        } else {
            throw NotImplementedError()
        }

        return isEnabled
    }

/**
 * mobileEnabled
 */
val TestDrive.mobileEnabled: Boolean
    get() {
        if (TestMode.isNoLoadRun) {
            return true
        }

        val isEnabled: Boolean
        if (isAndroid) {
            isEnabled = TestDriver.androidDriver.connection.isDataEnabled
        } else {
            throw NotImplementedError()
        }

        return isEnabled
    }

/**
 * internetOn
 */
fun TestDrive.internetOn(
): TestElement {

    val testElement = TestDriver.it

    val command = "internetOn"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)

    android {
        context.execOperateCommand(command = command, message = message) {
            TestDriver.androidDriver.setConnection(ConnectionStateBuilder().withDataEnabled().withWiFiEnabled().build())
            waitAllEnabled()
        }
    }

    return lastElement
}

/**
 * internetOff
 */
fun TestDrive.internetOff(
): TestElement {

    val testElement = TestDriver.it

    val command = "internetOff"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)

    android {
        context.execOperateCommand(command = command, message = message) {
            TestDriver.androidDriver.setConnection(
                ConnectionStateBuilder().withDataDisabled().withWiFiDisabled().build()
            )
        }
    }

    return lastElement
}

/**
 * wiFiOn
 */
fun TestDrive.wiFiOn(
): TestElement {

    val testElement = TestDriver.it

    val command = "wiFiOn"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)

    android {
        context.execOperateCommand(command = command, message = message) {
            TestDriver.androidDriver.setConnection(ConnectionStateBuilder().withWiFiEnabled().build())
            waitWiFiEnabled()
        }
    }

    return lastElement
}

/**
 * wiFiOff
 */
fun TestDrive.wiFiOff(
): TestElement {

    val testElement = TestDriver.it

    val command = "wiFiOff"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)

    android {
        context.execOperateCommand(command = command, message = message) {
            TestDriver.androidDriver.setConnection(ConnectionStateBuilder().withWiFiDisabled().build())
        }
    }

    return lastElement
}

/**
 * mobileOn
 */
fun TestDrive.mobileOn(
): TestElement {

    val testElement = TestDriver.it

    val command = "mobileOn"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)

    android {
        context.execOperateCommand(command = command, message = message) {
            TestDriver.androidDriver.setConnection(ConnectionStateBuilder().withDataEnabled().build())
            waitMobileEnabled()
        }
    }

    return lastElement
}

/**
 * mobileOff
 */
fun TestDrive.mobileOff(
): TestElement {

    val testElement = TestDriver.it

    val command = "mobileOff"
    val message = message(id = command)
    val context = TestDriverCommandContext(testElement)

    android {
        context.execOperateCommand(command = command, message = message) {
            TestDriver.androidDriver.setConnection(ConnectionStateBuilder().withDataDisabled().build())
        }
    }

    return lastElement
}

private fun TestDrive.waitWiFiEnabled() {

    wait(waitSeconds = Const.WAIT_SECONDS_FOR_CONNECTION_ENABLED)
}

private fun TestDrive.waitMobileEnabled() {

    wait(waitSeconds = Const.WAIT_SECONDS_FOR_CONNECTION_ENABLED)
}

private fun TestDrive.waitAllEnabled() {

    wait(waitSeconds = Const.WAIT_SECONDS_FOR_CONNECTION_ENABLED)
}
