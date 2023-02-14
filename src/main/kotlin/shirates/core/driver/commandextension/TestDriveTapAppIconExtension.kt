package shirates.core.driver.commandextension

import shirates.core.customobject.CustomFunctionRepository
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.isNoLoadRun
import shirates.core.driver.TestMode.isiOS
import shirates.core.logging.Message.message

/**
 * tapAppIcon
 */
fun TestDrive?.tapAppIcon(
    appIconName: String = testContext.appIconName,
    tapAppIconMethod: TapAppIconMethod = testContext.tapAppIconMethod
): TestElement {

    val command = "tapAppIcon"
    val subject = if (isNoLoadRun && appIconName == testContext.appIconName) "" else "<${appIconName}>"
    val message = message(id = command, subject = subject)
    val context = TestDriverCommandContext(rootElement)
    context.execOperateCommand(command = command, message = message, subject = appIconName) {

        if (isiOS) {
            if (canSelect("#SpotlightSearchField||#dewey-search-field")) {
                pressHome()
            }
        }

        // launch app by macro
        if (testContext.tapAppIconMacro.isNotBlank()) {
            macro(macroName = testContext.tapAppIconMacro, appIconName)
            return@execOperateCommand
        }

        // launch by custom function
        if (CustomFunctionRepository.hasFunction("tapAppIcon")) {
            val tapped = CustomFunctionRepository.call(functionName = "tapAppIcon", appIconName)
            if (tapped == true) {
                return@execOperateCommand
            }
        }

        if (canSelect(appIconName)) {
            lastElement.tap()
                .wait()
            return@execOperateCommand
        }

        TestDriver.tapAppIconCore(appIconName = appIconName, tapAppIconMethod = tapAppIconMethod)
    }

    return lastElement
}
