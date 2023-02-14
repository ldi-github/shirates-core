package shirates.core.utility.ios

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.misc.ShellUtility

object IosAppUtility {

    /**
     * launchApp
     */
    fun launchApp(
        udid: String,
        bundleId: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {
        val args = listOf("xcrun", "simctl", "launch", udid, bundleId)
        return ShellUtility.executeCommand(args = args.toTypedArray(), log = log)
    }

    /**
     * terminateApp
     */
    fun terminateApp(
        udid: String,
        bundleId: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {
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