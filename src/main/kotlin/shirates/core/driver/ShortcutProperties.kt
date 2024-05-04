package shirates.core.driver

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
        return TestDriver.rootElement
    }
    set(value) {
        TestDriver.rootElement = value
    }

/**
 * view
 */
val view: TestElement
    get() {
        return TestElementCache.viewElement
    }

/**
 * viewBounds
 */
val viewBounds: Bounds
    get() {
        return TestElementCache.viewBounds
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

