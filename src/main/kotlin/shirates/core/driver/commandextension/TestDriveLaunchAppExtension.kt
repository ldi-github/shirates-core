package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.logging.Message
import shirates.core.logging.TestLog
import shirates.core.utility.android.AdbUtility
import shirates.core.utility.misc.AppNameUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.SyncUtility

/**
 * launchApp
 *
 * @param appNameOrAppId
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 */
fun TestDrive?.launchApp(
    appNameOrAppId: String = testContext.appIconName
): TestElement {

    val testElement = getTestElement()

    val command = "launchApp"
    val subject = Selector(appNameOrAppId).toString()
    val message = Message.message(id = command, subject = subject)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        val packageOrBundleId = AppNameUtility.getPackageOrBundleId(appNameOrAppId = appNameOrAppId)
        if (packageOrBundleId.isBlank()) {
            tapAppIcon(appNameOrAppId)
            return@execOperateCommand
        }

        if (isAndroid) {
            AdbUtility.startApp(udid = testProfile.udid, packageName = packageOrBundleId)
            SyncUtility.doUntilTrue {
                invalidateCache()
                isApp(appNameOrAppId = appNameOrAppId)
            }
        } else if (isiOS) {
            if (isSimulator) {
                SyncUtility.doUntilTrue() {
                    TestLog.info("Launch with xcrun simctl launch ${testProfile.udid} $packageOrBundleId")
                    ShellUtility.executeCommand(
                        "xcrun",
                        "simctl",
                        "launch",
                        testProfile.udid,
                        packageOrBundleId
                    )
                    wait(waitSeconds = 4)
                    invalidateCache()
                    isApp(appNameOrAppId = appNameOrAppId)
                }
            } else {
                tapAppIcon(appNameOrAppId)
                SyncUtility.doUntilTrue {
                    invalidateCache()
                    isApp(appNameOrAppId = appNameOrAppId)
                }
            }
        }
    }

    return TestDriver.lastElement
}