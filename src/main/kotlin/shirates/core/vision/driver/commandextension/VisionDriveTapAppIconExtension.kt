package shirates.core.vision.driver.commandextension

import shirates.core.customobject.CustomFunctionRepository
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isNoLoadRun
import shirates.core.driver.TestMode.isiOS
import shirates.core.logging.Message.message
import shirates.core.storage.appIconName
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.behavior.TapHelper
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.wait
import shirates.spec.utilily.hasBracket

/**
 * tapAppIcon
 */
fun VisionDrive.tapAppIcon(
    appIconName: String = testContext.appIconName,
    tapAppIconMethod: TapAppIconMethod = testContext.tapAppIconMethod
): VisionElement {

    val command = "tapAppIcon"
    val subject = if (isNoLoadRun && appIconName == testContext.appIconName) "" else "<${appIconName}>"
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = appIconName) {

        pressHome()

//        if (isiOS) {
//            if (rootElement.name.isNotBlank()) {
//                pressHome()
//            }
//        }

        val iconLabel =
            if (appIconName.hasBracket()) appIconName(datasetName = appIconName)
            else appIconName

        // launch app by macro
        if (testContext.tapAppIconMacro.isNotBlank()) {
            macro(macroName = testContext.tapAppIconMacro, iconLabel)
            return@execOperateCommand
        }

        // launch by custom function
        val functionName = "tapAppIcon"
        if (CustomFunctionRepository.hasFunction(functionName)) {
            val tapped = CustomFunctionRepository.call(functionName = functionName, iconLabel)
            if (tapped == true) {
                return@execOperateCommand
            }
        }

        screenshot()

        /**
         * Tap the element on the screen if it can be detected.
         */
        if (canDetect(iconLabel)) {
            lastElement.tap()
                .wait()
            return@execOperateCommand
        }

        /**
         * Manipulate launcher
         */
        when (tapAppIconMethod) {

            TapAppIconMethod.googlePixel -> {
                TapHelper.tapAppIconAsGooglePixel(appIconName = appIconName)
            }

            TapAppIconMethod.swipeLeftInHome -> {
                TapHelper.swipeLeftAndTapAppIcon(appIconName = appIconName)
            }

            else -> {
                if (isAndroid) {
                    if (drive.deviceManufacturer == "Google" || drive.deviceModel.contains("Android SDK")) {
                        TapHelper.tapAppIconAsGooglePixel(appIconName = appIconName)
                    } else {
                        TapHelper.swipeLeftAndTapAppIcon(appIconName = appIconName)
                    }
                } else if (isiOS) {
                    TapHelper.tapAppIconAsIos(appIconName = appIconName)
                }
            }
        }
    }

    return lastElement
}
