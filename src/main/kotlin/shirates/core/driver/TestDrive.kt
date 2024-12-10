package shirates.core.driver

interface TestDrive : Drive {

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
}