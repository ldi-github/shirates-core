package shirates.core.driver

internal class TemporalContextScope {

    lateinit var savedData: MutableMap<String, Any?>

    /**
     * execute
     */
    fun execute(func: () -> Unit) {

        savedData = testContext.saveState()
        try {
            func()
        } finally {
            testContext.resumeState(savedData)
        }
    }
}

