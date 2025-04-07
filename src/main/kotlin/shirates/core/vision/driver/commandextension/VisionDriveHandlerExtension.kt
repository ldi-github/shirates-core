package shirates.core.vision.driver.commandextension

import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * useHandler
 */
fun VisionDrive.useHandler(func: () -> Unit): VisionElement {

    val original = testContext.enableIrregularHandler

    try {
        TestLog.info("useHandler(${TestLog.nextLineNo}) {")
        testContext.enableIrregularHandler = true
        func()
    } finally {
        testContext.enableIrregularHandler = original
        TestLog.info("} useHandler(${TestLog.nextLineNo}) {")
    }

    return lastElement
}

/**
 * suppressHandler
 */
fun VisionDrive.suppressHandler(func: () -> Unit): VisionElement {

    val original = testContext.enableIrregularHandler

    try {
        testContext.enableIrregularHandler = false
        func()
    } finally {
        testContext.enableIrregularHandler = original
    }

    return lastElement
}

/**
 * enableHandler
 */
fun VisionDrive.enableHandler(): VisionElement {

    testContext.enableIrregularHandler = true

    return lastElement
}

/**
 * disableHandler
 */
fun VisionDrive.disableHandler(): VisionElement {

    testContext.enableIrregularHandler = false

    return lastElement
}

/**
 * useScreenHandler
 */
fun VisionDrive.useScreenHandler(func: () -> Unit): VisionElement {

    val original = testContext.enableScreenHandler

    try {
        TestLog.info("useScreenHandler(${TestLog.nextLineNo}) {")
        testContext.enableScreenHandler = true
        func()
    } finally {
        testContext.enableScreenHandler = original
        TestLog.info("} useScreenHandler(${TestLog.nextLineNo}) {")
    }

    return lastElement
}

/**
 * suppressScreenHandler
 */
fun VisionDrive.suppressScreenHandler(func: () -> Unit): VisionElement {

    val original = testContext.enableScreenHandler

    try {
        testContext.enableScreenHandler = false
        func()
    } finally {
        testContext.enableScreenHandler = original
    }

    return lastElement
}

/**
 * enableScreenHandler
 */
fun VisionDrive.enableScreenHandler(): VisionElement {

    testContext.enableScreenHandler = true

    return lastElement
}

/**
 * disableScreenHandler
 */
fun VisionDrive.disableScreenHandler(): VisionElement {

    testContext.enableScreenHandler = false

    return lastElement
}