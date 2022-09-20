package shirates.core.utility.ios

import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.descendants

object IosKeyboardUtility {

    private fun getKeyboardArea(): TestElement {

        val keyboardArea = TestDriver.select("#Next keyboard&&.XCUIElementTypeButton")
        return keyboardArea
    }

    /**
     * pressKeys
     */
    fun pressKeys(keys: String) {

        if (isiOS.not()) {
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
}