package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.configuration.isValidNickname
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.storage.app
import shirates.core.utility.misc.AppNameUtility

/**
 * isAppInstalled
 */
fun TestDrive.isAppInstalled(
    appNickname: String? = null,
    packageOrBundleId: String? = testContext.profile.packageOrBundleId
): Boolean {

    val testElement = TestElement.emptyElement

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

    val testElement = TestElement.emptyElement

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

        if (isiOS) {
            wait(waitSeconds = 1.0) // for stability
        }
    }

    return lastElement
}

/**
 * removeApp
 */
fun TestDrive.removeApp(
    packageOrBundleId: String? = testContext.profile.packageOrBundleId,
): TestElement {

    val testElement = TestElement.emptyElement

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

    val testElement = TestElement.emptyElement

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

    val testElement = TestElement.emptyElement

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
    launchAppMethod: String = PropertiesManager.launchAppMethod,
    fallBackToTapAppIcon: Boolean = true,
    sync: Boolean = true,
    onLaunchHandler: (() -> Unit)? = testContext.onLaunchHandler
): TestElement {

    val testElement = TestElement.emptyElement

    val command = "launchApp"
    val subject = Selector(appNameOrAppIdOrActivityName).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        terminateApp(appNameOrAppIdOrActivityName)

        TestLog.info("launchAppMethod: $launchAppMethod")

        if (launchAppMethod == "shell") {
            /**
             * by shell
             */
            launchAppByShell(
                appNameOrAppIdOrActivityName = appNameOrAppIdOrActivityName,
                fallBackToTapAppIcon = fallBackToTapAppIcon,
                sync = sync,
                onLaunchHandler = onLaunchHandler
            )

        } else if (launchAppMethod == "tapAppIcon") {
            /**
             * by tapAppIcon
             */
            tapAppIcon(appIconName = appNameOrAppIdOrActivityName)

        } else if (launchAppMethod.startsWith("[") && launchAppMethod.endsWith("]")) {
            /**
             * by macro
             */
            macro(macroName = launchAppMethod, appNameOrAppIdOrActivityName)

        } else {
            /**
             * auto
             */
            if (appNameOrAppIdOrActivityName.split(".").count() >= 2) {
                TestDriver.launchByShell(
                    appNameOrAppIdOrActivityName = appNameOrAppIdOrActivityName,
                    fallBackToTapAppIcon = fallBackToTapAppIcon,
                    sync = sync,
                    onLaunchHandler = onLaunchHandler
                )
            } else {
                tapAppIcon(appIconName = appNameOrAppIdOrActivityName)
            }
        }

    }

    return lastElement
}

/**
 * launchAppByShell
 *
 * @param appNameOrAppIdOrActivityName
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 * or activityName com.android.settings/.Settings
 */
fun TestDrive.launchAppByShell(
    appNameOrAppIdOrActivityName: String = testContext.appIconName,
    launchAppMethod: String = PropertiesManager.launchAppMethod,
    fallBackToTapAppIcon: Boolean = true,
    sync: Boolean = true,
    onLaunchHandler: (() -> Unit)? = testContext.onLaunchHandler
): TestElement {

    val testElement = TestElement.emptyElement

    val command = "launchApp"
    val subject = Selector(appNameOrAppIdOrActivityName).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        TestLog.info("launchAppMethod: $launchAppMethod")

        TestDriver.launchByShell(
            appNameOrAppIdOrActivityName = appNameOrAppIdOrActivityName,
            fallBackToTapAppIcon = fallBackToTapAppIcon,
            sync = sync,
            onLaunchHandler = onLaunchHandler
        )
    }

    return lastElement
}


