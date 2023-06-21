package shirates.core.driver

import org.openqa.selenium.By
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestDriveObject.driver

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
        val e = driver.appiumDriver.findElement(By.xpath("//*")).toTestElement()
        return e
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

