package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDrive

/**
 * CompareResult
 */
open class CompareResult() : TestDrive {

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
    fun setExecuted(condition: String, matched: Boolean) {

        mHistory.add(BranchExecutionState(condition, matched))
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
