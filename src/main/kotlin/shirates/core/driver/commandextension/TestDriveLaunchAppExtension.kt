package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.logging.Message.message
import shirates.core.utility.misc.AppNameUtility
import shirates.core.utility.sync.SyncUtility

/**
 * launchApp
 *
 * @param appNameOrAppIdOrActivityName
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 * or activityName com.android.settings/.Settings
 */
fun TestDrive?.launchApp(
    appNameOrAppIdOrActivityName: String = testContext.appIconName,
    fallBackToTapAppIcon: Boolean = true
): TestElement {

    val testElement = getTestElement()

    val command = "launchApp"
    val subject = Selector(appNameOrAppIdOrActivityName).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        if (appNameOrAppIdOrActivityName.contains("/")) {
            val activityName = appNameOrAppIdOrActivityName
            TestDriver.launchAppCore(packageOrBundleIdOrActivity = activityName)
            return@execOperateCommand
        }

        val packageOrBundleId =
            AppNameUtility.getPackageOrBundleId(appNameOrAppIdOrActivityName = appNameOrAppIdOrActivityName)
        if (packageOrBundleId.isBlank()) {
            if (fallBackToTapAppIcon) {
                TestDriver.tapAppIconCore(appIconName = appNameOrAppIdOrActivityName)
                return@execOperateCommand
            } else {
                throw IllegalArgumentException(
                    message(
                        id = "failedToGetPackageOrBundleId",
                        arg1 = "appNameOrAppId=$appNameOrAppIdOrActivityName"
                    )
                )
            }
        }

        if (testProfile.udid.isBlank()) {
            throw IllegalStateException("$command($appNameOrAppIdOrActivityName) failed. testProfile.udid is blank")
        }

        if (isAndroid) {
            TestDriver.launchAppCore(packageOrBundleIdOrActivity = packageOrBundleId)
        } else if (isiOS) {
            if (isSimulator) {
                TestDriver.launchAppCore(packageOrBundleIdOrActivity = packageOrBundleId)
            } else {
                TestDriver.tapAppIconCore(appNameOrAppIdOrActivityName)
                SyncUtility.doUntilTrue {
                    invalidateCache()
                    isApp(appNameOrAppId = appNameOrAppIdOrActivityName)
                }
            }
        }
    }

    return TestDriver.lastElement
}