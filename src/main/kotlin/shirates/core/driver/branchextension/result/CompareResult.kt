package shirates.core.driver.branchextension.result

/**
 * CompareResult
 */
open class CompareResult() {

    internal val mHistory = mutableListOf<BranchExecutionState>()

    /**
     * history
     */
    val history: List<BranchExecutionState>
        get() {
            return mHistory
        }

    /**
     * setExecuted
     */
    fun setExecuted(condition: String, matched: Boolean, message: String) {

        mHistory.add(BranchExecutionState(condition = condition, matched = matched, message = message))
    }

    /**
     * hasExecuted
     */
    fun hasExecuted(condition: String): Boolean {
        return history.any { it.condition == condition }
    }

    /**
     * anyMatched
     */
    val anyMatched: Boolean
        get() {
            return history.any() { it.matched }
        }
}
