package shirates.core.driver

/**
 * getTestElement
 */
fun TestDrive.getTestElement(): TestElement {

    if (this is TestElement) {
        return this
    }
    return lastElement
}

/**
 * focusedElement
 */
val TestDrive.focusedElement: TestElement
    get() {
        try {
            return TestDriver.getFocusedElement()
        } catch (t: Throwable) {
            return getTestElement()
        }
    }