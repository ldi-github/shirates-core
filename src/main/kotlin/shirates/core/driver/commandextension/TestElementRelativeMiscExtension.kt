package shirates.core.driver.commandextension

import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isAndroid

/**
 * leftButton
 */
fun TestElement.radio(
): TestElement {

    if (isAndroid) {
        if (this.className == "android.widget.RadioButton") {
            return this
        }
        return TestElement.emptyElement
    }
    return this.leftButton()
}

