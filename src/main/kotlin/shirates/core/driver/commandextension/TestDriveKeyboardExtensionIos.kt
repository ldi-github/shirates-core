package shirates.core.driver.commandextension

import shirates.core.driver.*

private fun getKeyboardArea(): TestElement {

    val keyboardArea = TestDriver.select("#Next keyboard&&.XCUIElementTypeButton")
    return keyboardArea
}

/**
 * pressKeys
 */
internal fun TestDriveObjectIos.pressKeys(keys: String) {

    if (TestMode.isiOS.not()) {
        throw UnsupportedOperationException("pressKeys function is for iOS.")
    }

    val keyboardArea = getKeyboardArea()
    if (keyboardArea.isEmpty) {
        throw IllegalStateException("Keyboard is not shown.")
    }

    val list = keys.toList().map { "$it" }
    for (key in list) {
        keyboardArea.descendants.filter { it.label == key }
    }
}