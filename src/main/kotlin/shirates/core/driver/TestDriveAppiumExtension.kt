package shirates.core.driver

import shirates.core.driver.TestDriver.appiumDriver
import java.time.Duration

/**
 * implicitWaitMilliseconds
 */
fun TestDrive.implicitWaitMilliseconds(
    timeoutMilliseconds: Int,
    func: () -> Unit
): TestElement {

    val original = appiumDriver.manage().timeouts().implicitWaitTimeout

    try {
        appiumDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(timeoutMilliseconds.toLong()))
        func()
    } finally {
        appiumDriver.manage().timeouts().implicitlyWait(original)
    }

    return lastElement
}