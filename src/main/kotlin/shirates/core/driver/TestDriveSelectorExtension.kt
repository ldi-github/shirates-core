package shirates.core.driver

import shirates.core.configuration.Selector
import shirates.core.driver.commandextension.getChainedSelector

fun TestDrive.getSelector(expression: String): Selector {

    val sel = TestDriver.screenInfo.expandExpression(expression = expression)
    if (sel.isRelative.not()) {
        return sel
    }

    if (this is TestElement) {
        return this.getChainedSelector(sel)
    }

    return sel.copy()
}