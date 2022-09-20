package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.testContext

/**
 * SpecialTagCompareResult
 */
class SpecialTagCompareResult() : CompareResult() {

    /**
     * specialTag
     */
    fun specialTag(
        specialTag: String,
        onTrue: () -> Unit
    ): SpecialTagCompareResult {

        val matched = TestMode.isNoLoadRun || testContext.profile.hasSpecialTag(specialTag)
        if (matched) {
            val context = TestDriverCommandContext(null)
            context.execSpecial(subject = "special", expected = specialTag) {
                onTrue.invoke()
            }
        }
        setExecuted(condition = specialTag, matched = matched)

        return this
    }

    /**
     * notMatched
     */
    fun notMatched(
        onOthers: () -> Unit
    ): SpecialTagCompareResult {

        if (anyMatched) {
            return this
        }

        val expected = "not(${history.map { it.condition }.joinToString(",")})"

        val context = TestDriverCommandContext(null)
        context.execSpecial(subject = "special", expected = expected) {
            onOthers.invoke()
        }
        setExecuted(condition = "not", matched = true)

        return this
    }
}

