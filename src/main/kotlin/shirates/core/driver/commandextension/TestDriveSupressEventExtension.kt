package shirates.core.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.testContext
import shirates.core.logging.TestLog

/**
 * useHandler
 */
fun TestDrive.useHandler(func: () -> Unit): TestElement {

    val original = testContext.enableIrregularHandler

    try {
        TestLog.info("useHandler(${TestLog.nextLineNo}) {")
        testContext.enableIrregularHandler = true
        func()
    } finally {
        testContext.enableIrregularHandler = original
        TestLog.info("} useHandler(${TestLog.nextLineNo}) {")
    }

    return TestDriver.it
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

    return TestDriver.it
}

/**
 * enableHandler
 */
fun TestDrive.enableHandler(): TestElement {

    TestDriver.testContext.enableIrregularHandler = true

    return TestDriver.it
}

/**
 * disableHandler
 */
fun TestDrive.disableHandler(): TestElement {

    TestDriver.testContext.enableIrregularHandler = false

    return TestDriver.it
}

/**
 * enableScreenHandler
 */
fun TestDrive.enableScreenHandler(): TestElement {

    TestDriver.testContext.enableScreenHandler = true

    return TestDriver.it
}

/**
 * disableScreenHandler
 */
fun TestDrive.disableScreenHandler(): TestElement {

    TestDriver.testContext.enableScreenHandler = false

    return TestDriver.it
}