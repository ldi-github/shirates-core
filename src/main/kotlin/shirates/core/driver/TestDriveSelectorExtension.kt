package shirates.core.driver

import shirates.core.configuration.Selector
import shirates.core.configuration.isRelativeNickname
import shirates.core.driver.commandextension.getChainedSelector

fun TestDrive.getSelector(expression: String): Selector {

    val screenInfo = TestDriver.screenInfo
    val sel = screenInfo.expandExpression(expression = expression)
    val newSel = sel.copy()
    if (newSel.isRelative.not()) {
        return newSel
    }

    if (this is TestElement) {
        if (TestMode.isNoLoadRun && expression.isRelativeNickname()) {
            return Selector("${this.selector}$expression")
        }
        return this.getChainedSelector(newSel)
    }

    return newSel
}