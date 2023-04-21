package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.logging.TestLog

/**
 * useHandler
 */
fun TestDrive.useHandler(func: () -> Unit): TestElement {

    val original = testContext.enableIrregularHandler

    try {
        TestLog.info("useHandler(${TestLog.lines.count() + 1}) {")
        testContext.enableIrregularHandler = true
        func()
    } finally {
        testContext.enableIrregularHandler = original
        TestLog.info("} useHandler(${TestLog.lines.count() + 1}) {")
    }

    return getTestElement()
}

/**
 * suppressHandler
 */
fun TestDrive.suppressHandler(func: () -> Unit): TestElement {

    val original = testContext.enableIrregularHandler

    try {
        testContext.enableIrregularHandler = false
        func()
    } finally {
        testContext.enableIrregularHandler = original
    }

    return getTestElement()
}

/**
 * enableHandler
 */
fun TestDrive.enableHandler(): TestElement {

    TestDriver.testContext.enableIrregularHandler = true

    return getTestElement()
}

/**
 * disableHandler
 */
fun TestDrive.disableHandler(): TestElement {

    TestDriver.testContext.enableIrregularHandler = false

    return getTestElement()
}
