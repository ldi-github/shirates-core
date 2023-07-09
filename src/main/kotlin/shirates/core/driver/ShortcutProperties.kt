package shirates.core.driver

import org.openqa.selenium.By
import shirates.core.configuration.TestProfile
import shirates.core.driver.commandextension.findWebElementBy

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
        if (TestMode.isNoLoadRun) {
            return TestElement.emptyElement
        }
        if (testContext.useCache) {
            return TestElementCache.rootElement
        }
        return testDrive.findWebElementBy(locator = By.xpath("//*"), timeoutMilliseconds = 0)
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

