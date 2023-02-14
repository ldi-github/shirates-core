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
 * @param appNameOrAppId
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 */
fun TestDrive?.launchApp(
    appNameOrAppId: String = testContext.appIconName,
    fallBackToTapAppIcon: Boolean = true
): TestElement {

    val testElement = getTestElement()

    val command = "launchApp"
    val subject = Selector(appNameOrAppId).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        val packageOrBundleId = AppNameUtility.getPackageOrBundleId(appNameOrAppId = appNameOrAppId)
        if (packageOrBundleId.isBlank()) {
            if (fallBackToTapAppIcon) {
                TestDriver.tapAppIconCore(appIconName = appNameOrAppId)
                return@execOperateCommand
            } else {
                throw IllegalArgumentException(
                    message(
                        id = "failedToGetPackageOrBundleId",
                        arg1 = "appNameOrAppId=$appNameOrAppId"
                    )
                )
            }
        }

        if (testProfile.udid.isBlank()) {
            throw IllegalStateException("$command($appNameOrAppId) failed. testProfile.udid is blank")
        }

        if (isAndroid) {
            TestDriver.launchAppCore(packageOrBundleId = packageOrBundleId)
        } else if (isiOS) {
            if (isSimulator) {
                TestDriver.launchAppCore(packageOrBundleId = packageOrBundleId)
            } else {
                TestDriver.tapAppIconCore(appNameOrAppId)
                SyncUtility.doUntilTrue {
                    invalidateCache()
                    isApp(appNameOrAppId = appNameOrAppId)
                }
            }
        }
    }

    return TestDriver.lastElement
}