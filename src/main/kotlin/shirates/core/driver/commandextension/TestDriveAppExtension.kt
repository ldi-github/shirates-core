package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.configuration.isValidNickname
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.storage.app
import shirates.core.utility.misc.AppNameUtility
import shirates.core.utility.sync.SyncUtility

/**
 * isAppInstalled
 */
fun TestDrive.isAppInstalled(
    appNickname: String? = null,
    packageOrBundleId: String? = testContext.profile.packageOrBundleId
): Boolean {

    val testElement = getThisOrRootElement()

    var id = packageOrBundleId
    if (appNickname?.isValidNickname() == true) {
        id = app(datasetName = appNickname, attributeName = "packageOrBundleId", throwsException = false)
            .ifBlank { packageOrBundleId }
    }

    val context = TestDriverCommandContext(testElement)
    var isInstalled = false
    context.execBooleanCommand(arg1 = id, fireEvent = false) {

        isInstalled = TestDriver.appiumDriver.isAppInstalled(id)
        if (isInstalled) {
            TestLog.trace("app is installed ($id)")
        } else {
            TestLog.trace("app is not installed ($id)")
        }
    }

    return isInstalled
}

/**
 * installApp
 */
fun TestDrive.installApp(
    appPackageFile: String = testContext.profile.appPackageFullPath,
): TestElement {

    val testElement = getThisOrRootElement()

    val command = "installApp"
    val message = message(id = command, file = appPackageFile)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {

        TestDriver.appiumDriver.installApp(appPackageFile)

        if (isAndroid) {
            if (canSelect("送信しない||Don't*||don't*||Do not*||do not*")) {
                lastElement.tap()
            }
        }

        TestDriver.invalidateCache()
    }

    return lastElement
}

/**
 * removeApp
 */
fun TestDrive.removeApp(
    packageOrBundleId: String? = testContext.profile.packageOrBundleId,
): TestElement {

    val testElement = getThisOrRootElement()

    val command = "removeApp"
    val message = message(id = command, subject = packageOrBundleId)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {

        TestDriver.appiumDriver.removeApp(packageOrBundleId)

        TestDriver.invalidateCache()
    }

    return lastElement
}

/**
 * terminateApp
 */
fun TestDrive.terminateApp(
    appNameOrAppId: String = testContext.appIconName
): TestElement {

    val testElement = getThisOrRootElement()

    val command = "terminateApp"
    val subject = Selector(appNameOrAppId).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        try {
            val packageOrBundleId = AppNameUtility.getPackageOrBundleId(appNameOrAppIdOrActivityName = appNameOrAppId)
            TestDriver.appiumDriver.terminateApp(packageOrBundleId = packageOrBundleId)
        } catch (t: Throwable) {
            if (t.message?.contains("is still running after") == true) {
                // 'com.android.settings' is still running after 500ms timeout
                throw TestDriverException(t.message ?: t.toString(), t)
            } else {
                throw t
            }
        }

        TestDriver.invalidateCache()
    }

    return lastElement
}

/**
 * restartApp
 */
fun TestDrive.restartApp(
    appNameOrAppId: String = TestDriver.testContext.appIconName
): TestElement {

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

    val testElement = getThisOrRootElement()

    val command = "restartApp"
    val subject = Selector(appNameOrAppId).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {
        terminateApp(appNameOrAppId)
        launchApp(appNameOrAppId)
    }

    return lastElement
}

/**
 * launchApp
 *
 * @param appNameOrAppIdOrActivityName
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 * or activityName com.android.settings/.Settings
 */
fun TestDrive.launchApp(
    appNameOrAppIdOrActivityName: String = testContext.appIconName,
    fallBackToTapAppIcon: Boolean = true
): TestElement {

    val testElement = getThisOrRootElement()

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

        if (isAndroid) {
            TestDriver.launchAppCore(packageOrBundleIdOrActivity = packageOrBundleId)
        } else if (TestMode.isiOS) {
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

    return lastElement
}

