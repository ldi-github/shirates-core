package shirates.core.driver

interface TestDrive : Drive {

    /**
     * it
     */
    val it: TestElement
        get() {
            return TestDriver.lastElement
        }

    /**
     * lastElement
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
}