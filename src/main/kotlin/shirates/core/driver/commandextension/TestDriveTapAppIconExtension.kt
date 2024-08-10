package shirates.core.driver.commandextension

import shirates.core.customobject.CustomFunctionRepository
import shirates.core.driver.*
import shirates.core.driver.TestMode.isNoLoadRun
import shirates.core.driver.TestMode.isiOS
import shirates.core.logging.Message.message
import shirates.core.storage.appIconName
import shirates.spec.utilily.hasBracket

/**
 * tapAppIcon
 */
fun TestDrive.tapAppIcon(
    appIconName: String = testContext.appIconName,
    tapAppIconMethod: TapAppIconMethod = testContext.tapAppIconMethod
): TestElement {

    val command = "tapAppIcon"
    val subject = if (isNoLoadRun && appIconName == testContext.appIconName) "" else "<${appIconName}>"
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(rootElement)
    context.execOperateCommand(command = command, message = message, subject = appIconName) {

        pressHome()

        if (isiOS) {
            if (rootElement.name.isNotBlank()) {
                pressHome()
            }
        }

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

        if (canSelect(iconLabel)) {
            lastElement.tap()
                .wait()
            return@execOperateCommand
        }

        TestDriver.tapAppIconCore(appIconName = iconLabel, tapAppIconMethod = tapAppIconMethod)
    }

    return lastElement
}
