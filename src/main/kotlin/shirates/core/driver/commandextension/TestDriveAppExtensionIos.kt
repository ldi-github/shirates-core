package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.utility.misc.ShellUtility

internal fun TestDriveObjectIos.launchIosAppByShell(
    udid: String,
    bundleId: String,
    sync: Boolean,
    onLaunchHandler: (() -> Unit)? = testContext.onLaunchHandler,
    log: Boolean = PropertiesManager.enableShellExecLog
): TestElement {

    launchIosAppByShellCore(udid = udid, bundleId = bundleId, log = log)

    if (testContext.useCache.not()) {
        return lastElement
    }

    /**
     * Classic mode only
     */
    if (sync) {
        val isApp = TestDriver.isAppCore(appNameOrAppId = bundleId)
        if (isApp.not()) {
            testDrive.withoutScroll {
                onLaunchHandler?.invoke()
            }
            Thread.sleep(3000)
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
    sync: Boolean,
    log: Boolean = PropertiesManager.enableShellExecLog
): TestElement {

    terminateIosApp(udid = udid, bundleId = bundleId, log = log)
    return launchIosAppByShell(udid = udid, bundleId = bundleId, sync = sync, log = log)
}
