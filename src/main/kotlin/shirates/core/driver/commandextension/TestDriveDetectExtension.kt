package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.VisionElementCache

/**
 * detect
 */
fun TestDrive.detect(
    expression: String,
    allowScroll: Boolean = true,
    swipeToCenter: Boolean = false,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
//    selectContext: TestElement = rootElement,
    frame: Bounds? = viewBounds,
//    useCache: Boolean = testContext.useCache,
//    updateLastElement: Boolean = true,
//    safeElementOnly: Boolean = CodeExecutionContext.isScrolling,
    log: Boolean = false,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val swDetect = StopWatch("detect")

    val sel = getSelector(expression = expression)

    var v = VisionElementCache.detect(selector = sel)
    if (v.isEmpty.not()) {
        return v
    }

    val sw = StopWatch("screenshot")
    testDrive.screenshot()
    sw.printInfo()

    SrvisionProxy.callTextRecognizer(expression)
    v = VisionElementCache.detect(selector = sel)

    if (func != null) {
        func(v)
    }
    if (TestMode.isNoLoadRun) {
        v.selector = sel
    }

    swDetect.printInfo()

    return v
}

