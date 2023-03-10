package shirates.core.utility.ios

import shirates.core.driver.TestDriveObjectIos
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.terminateIosApp
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ShellUtility

object IosLanguageUtility {

    /**
     * setAppleLanguages
     */
    fun setAppleLanguages(
        udid: String,
        vararg langs: String
    ) {
        if (TestMode.isRealDevice) {
            TestLog.warn("setAppleLanguages is not supported on real device.")
            return
        }

        val args = mutableListOf(
            "xcrun", "simctl", "spawn", udid, "defaults", "write", "-globalDomain", "AppleLanguages", "-array"
        )
        for (lang in langs) {
            args.add(lang)
        }

        TestLog.info(args.joinToString(" "))
        ShellUtility.executeCommand(args = args.toTypedArray())
    }

    /**
     * setAppleLocale
     */
    fun setAppleLocale(
        udid: String,
        locale: String,
        setAppleLanguages: Boolean = true,
        restartDevice: Boolean = true
    ) {
        if (TestMode.isRealDevice) {
            TestLog.warn("setAppleLocale is not supported on real device.")
            return
        }
        if (setAppleLanguages) {
            setAppleLanguages(udid = udid, locale)
        }

        TestDriveObjectIos.terminateIosApp(udid = udid, bundleId = "com.apple.Preferences")

        val args = mutableListOf(
            "xcrun", "simctl", "spawn", udid, "defaults", "write", "-globalDomain", "AppleLocale", "-string", locale
        )

        TestLog.info(args.joinToString(" "))
        ShellUtility.executeCommand(args = args.toTypedArray())

        if (restartDevice) {
            IosDeviceUtility.terminateSpringBoardByUdid(udid = udid)
        }

        IosDeviceUtility.waitSimulatorStatus(udid = udid)
    }

}