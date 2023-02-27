package shirates.core.utility.ios

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.commandextension.wait
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.SyncUtility

object IosAppUtility {

    /**
     * launchApp
     */
    fun launchApp(
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

        var r: ShellUtility.ShellResult
        fun launchAction(): ShellUtility.ShellResult {
            r = ShellUtility.executeCommand(args = args.toTypedArray(), log = log)
            val message = r.waitForResultString()
            if (r.hasError) {
                throw TestDriverException(
                    message = Message.message(
                        id = "failedToLaunchApp",
                        arg1 = bundleId,
                        submessage = message
                    ),
                    cause = r.error
                )
            }
            return r
        }
        r = launchAction()

        var isApp = false

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
                r = launchAction()
                false
            } else if (isApp) {
                TestLog.info("App launched. ($bundleId)")
                true
            } else {
                false
            }
        }
        if (isApp.not()) {
            throw TestDriverException("launchApp timed out. (waitSecondsForLaunchAppComplete=${testContext.waitSecondsForLaunchAppComplete}, bundleId=$bundleId)")
        }

        return r
    }

    /**
     * terminateApp
     */
    fun terminateApp(
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
        val args = listOf("xcrun", "simctl", "terminate", udid, bundleId)
        return ShellUtility.executeCommand(args = args.toTypedArray(), log = log)
    }

    /**
     * restartApp
     */
    fun restartApp(
        udid: String,
        bundleId: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        terminateApp(udid = udid, bundleId = bundleId, log = log)
        return launchApp(udid = udid, bundleId = bundleId, log = log)
    }

}