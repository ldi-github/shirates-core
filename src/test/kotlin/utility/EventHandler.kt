package utility

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver.it
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.tap

fun TestDrive?.handleIrregulars(): Any? {

    if (TestMode.isNoLoadRun) {
        return this
    }

    ifCanSelect("Allow widgets from*") {
        it.tap("Allow")
    }
    ifCanSelect("Allow While Using App") {
        it.tap()
    }

    return this
}