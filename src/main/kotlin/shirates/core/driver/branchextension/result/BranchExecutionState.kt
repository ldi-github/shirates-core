package shirates.core.driver.branchextension.result

/**
 * BranchExecutionState
 */
class BranchExecutionState(
    val condition: String,
    val matched: Boolean
) {
    override fun toString(): String {
        return "condition=$condition, matched=$matched"
    }
}