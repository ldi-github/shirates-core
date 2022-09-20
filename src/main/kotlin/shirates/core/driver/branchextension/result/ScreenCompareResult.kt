package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDriver

/**
 * ScreenCompareResult
 */
class ScreenCompareResult() : CompareResult() {

    /**
     * ifScreenIs
     */
    fun ifScreenIs(
        vararg screenNames: String,
        onTrue: () -> Unit
    ): ScreenCompareResult {

        if (screenNames.isEmpty()) {
            return this
        }

        val screenName = TestDriver.currentScreen
        val matched = screenNames.contains(screenName)
        if (matched) {
            onTrue.invoke()
            setExecuted(condition = screenName, matched = true)
        }

        return this
    }

    /**
     * ifScreenIsNot
     */
    fun ifScreenIsNot(
        vararg screenNames: String,
        onTrue: () -> Unit
    ): ScreenCompareResult {

        if (screenNames.isEmpty()) {
            return this
        }

        val screenName = TestDriver.currentScreen
        val matched = screenNames.contains(screenName)
        if (matched.not()) {
            onTrue.invoke()
            setExecuted(condition = "$screenName not", matched = true)
        }

        return this
    }

    /**
     * ifElse
     */
    fun ifElse(
        onOthers: () -> Unit
    ): ScreenCompareResult {

        if (anyMatched) {
            return this
        }

        onOthers.invoke()
        setExecuted(condition = "ifElse", matched = true)

        return this
    }

    /**
     * not
     */
    fun not(
        onOthers: () -> Unit
    ): ScreenCompareResult {

        return ifElse(onOthers)
    }
}

