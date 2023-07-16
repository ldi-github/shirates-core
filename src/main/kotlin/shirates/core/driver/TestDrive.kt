package shirates.core.driver

interface TestDrive {

    /**
     * Shortcut for TestDriver object
     */
    val driver: TestDriver
        get() {
            return TestDriver
        }

    /**
     * Returns last accessed element
     */
    var lastElement: TestElement
        get() {
            return TestDriver.lastElement
        }
        set(value) {
            TestDriver.lastElement = value
        }

    /**
     * toTestElement
     */
    val toTestElement: TestElement
        get() {
            if (this is TestElement) {
                return this
            }
            return TestElement.emptyElement
        }

    /**
     * Returns lastElement after syncing cache.
     */
    val it: TestElement
        get() {
            return TestDriver.it
        }
}