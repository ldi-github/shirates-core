package shirates.core.testcode

import shirates.core.driver.TestDriver
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog

/**
 * CAEPattern
 */
object CAEPattern {

    /**
     * shouldTakeScreenshot
     */
    val shouldTakeScreenshot: Boolean
        get() {
            if (CodeExecutionContext.isInCondition) {
                return testContext.onCondition
            }
            if (CodeExecutionContext.isInAction) {
                return testContext.onAction
            }
            if (CodeExecutionContext.isInExpectation) {
                return testContext.onExpectation
            }

            return true
        }

    /**
     * condition
     */
    fun condition(
        conditionFunc: () -> Unit
    ): CAEPattern {

        val funcName = "condition"

        if (CodeExecutionContext.isInCondition) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "condition"
                )
            )
        }
        if (CodeExecutionContext.isInAction) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "action"
                )
            )
        }
        if (CodeExecutionContext.isInExpectation) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "expectation"
                )
            )
        }

        try {
            CodeExecutionContext.isInCondition = true
            TestLog.condition(message(funcName))

            conditionFunc()

            if (testContext.onCondition) {
                TestDriver.autoScreenshot()
            }
        } finally {
            CodeExecutionContext.isInCondition = false
        }

        return this
    }

    /**
     * action
     */
    fun action(
        actionFunc: () -> Unit
    ): CAEPattern {

        val funcName = "action"

        if (CodeExecutionContext.isInCondition) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "condition"
                )
            )
        }
        if (CodeExecutionContext.isInAction) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "action"
                )
            )
        }
        if (CodeExecutionContext.isInExpectation) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "expectation"
                )
            )
        }

        try {
            CodeExecutionContext.isInAction = true
            TestLog.action(message(funcName))

            actionFunc()

            if (testContext.onAction) {
                TestDriver.autoScreenshot()
            }
        } finally {
            CodeExecutionContext.isInAction = false
        }

        return this
    }

    /**
     * expectation
     */
    fun expectation(
        expectationFunc: () -> Unit
    ): CAEPattern {

        val funcName = "expectation"

        if (CodeExecutionContext.isInCondition) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "condition"
                )
            )
        }
        if (CodeExecutionContext.isInAction) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "action"
                )
            )
        }
        if (CodeExecutionContext.isInExpectation) {
            throw TestDriverException(
                message(
                    id = "callingSomethingInSomethingNotAllowed",
                    arg1 = funcName,
                    arg2 = "expectation"
                )
            )
        }

        try {
            CodeExecutionContext.isInExpectation = true
            TestLog.expectation(message(funcName))

            TestDriver.syncCache()

            if (testContext.onExpectation) {
                TestDriver.autoScreenshot()
            }

            expectationFunc()

            if (testContext.onExpectation) {
                TestDriver.autoScreenshot()
            }
        } finally {
            CodeExecutionContext.isInExpectation = false
        }

        return this
    }

}