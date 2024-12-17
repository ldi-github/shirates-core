package shirates.core.driver.commandextension

import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isAndroid

/**
 * radioButton
 */
fun TestElement.radioButton(
): TestElement {

    if (isAndroid) {
        if (this.className == "android.widget.RadioButton") {
            return this
        }
        return TestElement.emptyElement
    }
    return this.leftButton()
}

/**
 * checkBox
 */
fun TestElement.checkBox(
): TestElement {

    if (isAndroid) {
        if (this.className == "android.widget.CheckBox") {
            return this
        }
        return TestElement.emptyElement
    }
    return this.leftButton()
}

