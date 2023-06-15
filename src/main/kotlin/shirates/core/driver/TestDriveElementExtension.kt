package shirates.core.driver

/**
 * focusedElement
 */
val TestDrive.focusedElement: TestElement
    get() {
        return try {
            TestDriver.getFocusedElement()
        } catch (t: Throwable) {
            TestElement.emptyElement
        }
    }