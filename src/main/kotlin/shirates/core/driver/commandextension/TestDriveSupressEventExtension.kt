package shirates.core.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.getTestElement

/**
 * suppressHandler
 */
fun TestDrive?.suppressHandler(func: () -> Unit): TestElement {

    val context = TestDriver.testContext
    val original = context.enableIrregularHandler

    try {
        context.enableIrregularHandler = false
        func()
    } finally {
        context.enableIrregularHandler = original
    }

    return getTestElement()
}

/**
 * disableHandler
 */
fun TestDrive?.disableHandler(): TestElement {

    TestDriver.testContext.enableIrregularHandler = false

    return getTestElement()
}

/**
 * enableHandler
 */
fun TestDrive?.enableHandler(): TestElement {

    TestDriver.testContext.enableIrregularHandler = true

    return getTestElement()
}