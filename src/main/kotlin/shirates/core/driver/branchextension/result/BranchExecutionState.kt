package shirates.core.driver.branchextension.result

/**
 * BranchExecutionState
 */
class BranchExecutionState(
    val condition: String,
    val matched: Boolean,
    val message: String
) {
    override fun toString(): String {
        return "condition=$condition, matched=$matched, message=$message"
    }
}