package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.ios.IosDeviceUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.SyncUtility

internal fun TestDriveObjectIos.launchIosAppByShell(
    udid: String,
    bundleId: String,
    sync: Boolean = true,
    onLaunchHandler: (() -> Unit)? = testContext.onLaunchHandler,
    log: Boolean = PropertiesManager.enableShellExecLog
): TestElement {

    launchIosAppByShellCore(udid = udid, bundleId = bundleId, log = log)

    var isApp = false

    if (sync) {
        SyncUtility.doUntilTrue(
            waitSeconds = testContext.waitSecondsForLaunchAppComplete
        ) { context ->
            TestLog.info("doUntilTrue(${context.count})")
            testDrive.wait(waitSeconds = 2)
            isApp = TestDriver.isAppCore(appNameOrAppId = bundleId)

            val lastMessage = TestLog.lastTestLog!!.message
            val kAXErrorServerNotFound = lastMessage.contains("kAXErrorServerNotFound") // SpringBoard is corrupted
            if (kAXErrorServerNotFound) {
                TestLog.info("SpringBoard is corrupted.")
                IosDeviceUtility.terminateSpringBoardByUdid(udid = udid, log = log)
                TestLog.info("Retrying launchApp.")
                launchIosAppByShellCore(udid = udid, bundleId = bundleId, log = log)
                testDrive.withoutScroll {
                    onLaunchHandler?.invoke()
                }
                false
            } else if (isApp) {
                TestLog.info("App launched. ($bundleId)")
                true
            } else {
                testDrive.withoutScroll {
                    onLaunchHandler?.invoke()
                }
                false
            }
        }
        if (isApp.not()) {
            throw TestDriverException("launchApp timed out. (waitSecondsForLaunchAppComplete=${testContext.waitSecondsForLaunchAppComplete}, bundleId=$bundleId)")
        }
    } else {
        Thread.sleep(3000)
    }

    return lastElement
}

internal fun launchIosAppByShellCore(
    udid: String,
    bundleId: String,
    log: Boolean = PropertiesManager.enableShellExecLog
): ShellUtility.ShellResult {
    if (udid.isBlank()) {
        throw IllegalArgumentException("udid is blank")
    }
    if (bundleId.isBlank()) {
        throw IllegalArgumentException("bundleId is blank")
    }

    val args = listOf("xcrun", "simctl", "launch", udid, bundleId)

    val r = ShellUtility.executeCommand(args = args.toTypedArray(), log = log)
    testDrive.invalidateCache()
    val message = r.waitForResultString()
    if (r.hasError) {
        throw TestDriverException(
            message = message(
                id = "failedToLaunchApp",
                arg1 = bundleId,
                submessage = message
            ),
            cause = r.error
        )
    }

    return r
}

internal fun TestDriveObjectIos.terminateIosApp(
    udid: String,
    bundleId: String,
    log: Boolean = PropertiesManager.enableShellExecLog
): TestElement {
    if (udid.isBlank()) {
        throw IllegalArgumentException("udid is blank")
    }
    if (bundleId.isBlank()) {
        throw IllegalArgumentException("bundleId is blank")
    }
    val args = listOf("xcrun", "simctl", "terminate", udid, bundleId)
    ShellUtility.executeCommand(args = args.toTypedArray(), log = log)

    invalidateCache()

    return lastElement
}

internal fun TestDriveObjectIos.restartIosApp(
    udid: String,
    bundleId: String,
    log: Boolean = PropertiesManager.enableShellExecLog
): TestElement {

    terminateIosApp(udid = udid, bundleId = bundleId, log = log)
    return launchIosAppByShell(udid = udid, bundleId = bundleId, log = log)
}
