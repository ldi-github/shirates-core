package shirates.core.driver.branchextension

import shirates.core.driver.TestElement

/**
 * ifCheckON
 */
fun TestElement.ifCheckON(
    onCheckON: ((TestElement) -> Unit)
): TestElement {

    if (isChecked) {
        onCheckON.invoke(this)
    }

    return this
}

/**
 * ifCheckOFF
 */
fun TestElement.ifCheckOFF(
    onCheckOFF: ((TestElement) -> Unit)
): TestElement {

    if (isChecked.not()) {
        onCheckOFF.invoke(this)
    }

    return this
}

