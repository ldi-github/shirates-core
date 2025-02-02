package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.AppNameUtility
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * isAppInstalled
 */
fun VisionDrive.isAppInstalled(
    appNickname: String? = null,
    packageOrBundleId: String? = testContext.profile.packageOrBundleId
): Boolean {

    return testDrive.isAppInstalled(appNickname = appNickname, packageOrBundleId = packageOrBundleId)
}

/**
 * installApp
 */
fun VisionDrive.installApp(
    appPackageFile: String = testContext.profile.appPackageFullPath,
): VisionElement {

    val command = "installApp"
    val message = message(id = command, file = appPackageFile)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {

        TestDriver.appiumDriver.installApp(appPackageFile)

        if (isAndroid) {
            if (canDetect("送信しない||Don't*||don't*||Do not*||do not*")) {
                lastElement.tap()
            }
        }

        invalidateScreen()
        testDrive.invalidateCache()
    }

    return lastElement
}

/**
 * removeApp
 */
fun VisionDrive.removeApp(
    packageOrBundleId: String? = testContext.profile.packageOrBundleId,
): VisionElement {

    val command = "removeApp"
    val message = message(id = command, subject = packageOrBundleId)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, fireEvent = false) {

        TestDriver.appiumDriver.removeApp(packageOrBundleId)

        invalidateScreen()
        testDrive.invalidateCache()
    }

    return lastElement
}

/**
 * terminateApp
 */
fun VisionDrive.terminateApp(
    appNameOrAppId: String = testContext.appIconName
): VisionElement {

    val command = "terminateApp"
    val subject = Selector(appNameOrAppId).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(null)
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

        invalidateScreen()
        testDrive.invalidateCache()
    }

    return lastElement
}

/**
 * restartApp
 */
fun VisionDrive.restartApp(
    appNameOrAppId: String = TestDriver.testContext.appIconName
): VisionElement {

    if (TestMode.isNoLoadRun) {
        return lastElement
    }

    val command = "restartApp"
    val subject = Selector(appNameOrAppId).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(null)
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
fun VisionDrive.launchApp(
    appNameOrAppIdOrActivityName: String = testContext.appIconName,
    launchAppMethod: String = PropertiesManager.launchAppMethod,
    fallBackToTapAppIcon: Boolean = true,
    onLaunchHandler: (() -> Unit)? = testContext.onLaunchHandler,
    sync: Boolean = true
): VisionElement {

    val command = "launchApp"
    val subject = Selector(appNameOrAppIdOrActivityName).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        terminateApp(appNameOrAppIdOrActivityName)

        TestLog.info("launchAppMethod: $launchAppMethod")

        fun launchAppByShellAction() {
            launchAppByShell(
                appNameOrAppIdOrActivityName = appNameOrAppIdOrActivityName,
                fallBackToTapAppIcon = fallBackToTapAppIcon,
                onLaunchHandler = onLaunchHandler,
                sync = sync
            )
        }

        if (launchAppMethod == "shell") {
            /**
             * by shell
             */
            launchAppByShellAction()

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
            launchAppByShellAction()
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
fun VisionDrive.launchAppByShell(
    appNameOrAppIdOrActivityName: String = testContext.appIconName,
    launchAppMethod: String = PropertiesManager.launchAppMethod,
    fallBackToTapAppIcon: Boolean = true,
    onLaunchHandler: (() -> Unit)? = testContext.onLaunchHandler,
    sync: Boolean = true
): VisionElement {

    val command = "launchApp"
    val subject = Selector(appNameOrAppIdOrActivityName).toString()
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = subject) {

        TestLog.info("launchAppMethod: $launchAppMethod")

        TestDriver.launchByShell(
            appNameOrAppIdOrActivityName = appNameOrAppIdOrActivityName,
            fallBackToTapAppIcon = fallBackToTapAppIcon,
            onLaunchHandler = onLaunchHandler,
            sync = sync
        )
    }

    return lastElement
}
