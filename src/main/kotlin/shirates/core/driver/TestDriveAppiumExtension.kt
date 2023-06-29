package shirates.core.driver

import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
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

/**
 * findElement
 */
internal fun TestDrive.findElement(timeoutMilliseconds: Int, locator: By): TestElement {

    var e: TestElement? = null
    try {
        testDrive.implicitWaitMilliseconds(timeoutMilliseconds = timeoutMilliseconds) {
            e = appiumDriver.findElement(locator).toTestElement()
        }
    } catch (t: NoSuchElementException) {
        e = TestElement.emptyElement
    }
    return e!!
}