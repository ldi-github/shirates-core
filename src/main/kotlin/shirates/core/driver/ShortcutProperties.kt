package shirates.core.driver

import org.openqa.selenium.By
import shirates.core.configuration.TestProfile

/**
 * testDrive
 */
val testDrive: TestDrive
    get() {
        return TestDriveObject
    }

/**
 * rootElement
 */
var rootElement: TestElement
    get() {
        if (testContext.useCache) {
            return TestElementCache.rootElement
        }
        return testDrive.findElement(timeoutMilliseconds = 0, locator = By.xpath("//*"))
    }
    set(value) {
        TestElementCache.rootElement = value
    }

/**
 * sourceXml
 */
val sourceXml: String
    get() {
        return TestElementCache.sourceXml
    }

/**
 * testContext
 */
val testContext: TestContext
    get() {
        return TestDriver.testContext
    }

/**
 * testProfile
 */
val testProfile: TestProfile
    get() {
        return testContext.profile
    }

