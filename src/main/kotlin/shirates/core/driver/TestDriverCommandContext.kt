package shirates.core.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.logging.LogLine
import shirates.core.logging.LogType
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.misc.StackTraceUtility
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionElement

private const val COMMAND_CONTEXT_FILE_NAME: String = "TestDriverCommandContext.kt"

class TestDriverCommandContext(val testElementContext: TestElement?) {


    var callerName: String = ""
    var begin = false
    var beginLogLine: LogLine? = null
    internal var savedSelector: Selector? = null

    override fun toString(): String {
        return "$beginLogLine. caller=$callerName"
    }

    init {
        savedSelector = testElementContext?.selector
    }

    private fun resumeSelector() {
        if (testElementContext != null) {
            testElementContext.selector = savedSelector
        }
    }

    private fun pushToCommandStack() {
        if (begin.not()) {
            TestLog.commandStack.push(this)
            begin = true
        }
    }

    companion object {
        /**
         * shouldTakeScreenshot
         */
        val shouldTakeScreenshot: Boolean
            get() {
                if (CodeExecutionContext.isInOperationCommand) {
                    return testContext.onExecOperateCommand
                }
                if (CodeExecutionContext.isInCheckCommand) {
                    return testContext.onCheckCommand
                }

                return true
            }
    }

    /**
     * execSelectCommand
     */
    fun execSelectCommand(
        selector: Selector,
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        fireEvent: Boolean = true,
        func: () -> Unit
    ): LogLine? {
        if (TestMode.isNoLoadRun) {
            TestDriver.lastElement = TestElement(selector = selector)
            return null
        }

        if (fireEvent) {
            TestDriver.fireIrregularHandler()
            resumeSelector()
        }

        val commandText = getCommandText(command = "select", subject = subject, arg1 = arg1, arg2 = arg2)
        val ms = Measure(commandText)

        val original = CodeExecutionContext.isInSelectCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execSelectCommand"
            )

            pushToCommandStack()

            CodeExecutionContext.isInSelectCommand = true
            func()
        } finally {
            try {
                CodeExecutionContext.isInSelectCommand = original
                endCommand()
            } finally {
                ms.end()
            }
        }

        return beginLogLine
    }

    /**
     * execRelativeCommand
     */
    fun execRelativeCommand(
        message: String = "",
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        fireEvent: Boolean = true,
        log: Boolean = TestLog.enableTrace,
        func: () -> Unit
    ): LogLine? {
        if (TestMode.isNoLoadRun) {
            return null
        }

        if (fireEvent) {
            TestDriver.fireIrregularHandler()
            resumeSelector()
        }

        val commandText = getCommandText(command = "relative", subject = subject, arg1 = arg1, arg2 = arg2)
        val ms = Measure(commandText)

        val original = CodeExecutionContext.isInRelativeCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execRelativeCommand"
            )

            pushToCommandStack()

            beginLogLine = TestLog.select(
                message = message,
                subject = subject ?: callerName,
                arg1 = arg1 ?: "",
                arg2 = arg2 ?: "",
                log = log
            )

            CodeExecutionContext.isInRelativeCommand = true
            func()
        } finally {
            try {
                CodeExecutionContext.isInRelativeCommand = original
                endCommand()
            } finally {
                ms.end()
            }
        }

        return beginLogLine
    }

    /**
     * execBooleanCommand
     */
    fun execBooleanCommand(
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        fireEvent: Boolean = true,
        log: Boolean = TestLog.enableTrace,
        func: () -> Unit
    ): LogLine? {
        if (TestMode.isNoLoadRun) {
            return null
        }

        if (fireEvent) {
            TestDriver.fireIrregularHandler()
            resumeSelector()
        }

        val commandText = getCommandText(command = "boolean", subject = subject, arg1 = arg1, arg2 = arg2)
        val ms = Measure(commandText)

        val original = CodeExecutionContext.isInBooleanCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execBooleanCommand"
            )

            pushToCommandStack()

            val funcName = callerName.split(".").last()
            val msg = "$funcName $subject"
            beginLogLine = TestLog.boolean(
                message = msg,
                subject = subject ?: callerName,
                arg1 = arg1 ?: "",
                arg2 = arg2 ?: "",
                log = log
            )

            CodeExecutionContext.isInBooleanCommand = true
            func()
        } finally {
            try {
                CodeExecutionContext.isInBooleanCommand = original
                endCommand()
            } finally {
                ms.end()
            }
        }

        return beginLogLine
    }

    /**
     * execOperateCommand
     */
    fun execOperateCommand(
        command: String,
        message: String = "",
        scriptCommand: String? = null,
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        fileName: String? = null,
        fireEvent: Boolean = true,
        suppressBeforeScreenshot: Boolean = false,
        func: () -> Unit
    ): LogLine? {

        val commandText = getCommandText(command = command, subject = subject, arg1 = arg1, arg2 = arg2)
        val ms = Measure(commandText)
        try {
            return execOperateCommandCore(
                command = command,
                message = message,
                subject = subject,
                arg1 = arg1,
                arg2 = arg2,
                fileName = fileName,
                fireEvent = fireEvent,
                scriptCommand = scriptCommand,
                suppressBeforeScreenshot = suppressBeforeScreenshot,
                func = func
            )
        } finally {
            ms.end()
        }
    }

    private fun execOperateCommandCore(
        command: String,
        message: String,
        subject: String?,
        arg1: String?,
        arg2: String?,
        fileName: String?,
        fireEvent: Boolean,
        scriptCommand: String?,
        suppressBeforeScreenshot: Boolean = false,
        func: () -> Unit
    ): LogLine? {
        callerName = command

        val sw = StopWatch()

        val outputLog = CodeExecutionContext.shouldOutputLog

        if (TestMode.isNoLoadRun) {
            if (outputLog) {
                loggingOnNoLoadRun(
                    logType = LogType.OPERATE,
                    message = message,
                    command = command,
                    subject = subject,
                    arg1 = arg1,
                    arg2 = arg2
                )
            }
            return null
        }

        if (fireEvent) {
            TestDriver.fireIrregularHandler()
            resumeSelector()
        }

        if (suppressBeforeScreenshot.not()) {
            TestDriver.autoScreenshot()
        }

        val original = CodeExecutionContext.isInOperationCommand
        try {
            pushToCommandStack()

            beginLogLine = TestLog.operate(
                message = message,
                scriptCommand = scriptCommand,
                subject = subject ?: command,
                arg1 = arg1 ?: "",
                arg2 = arg2 ?: "",
                fileName = fileName ?: "",
                log = outputLog
            )

            TestDriver.autoScreenshot()
            CodeExecutionContext.isInOperationCommand = true
            func()
        } finally {
            CodeExecutionContext.isInOperationCommand = original
            try {
                TestDriver.autoScreenshot(sync = false)
            } finally {
                endCommand()
            }
        }

        if (PropertiesManager.enableTimeMeasureLog) {
            TestLog.info("[execOperateCommand ($command) in ${sw.elapsedSeconds}sec]")
        }

        return beginLogLine
    }

    /**
     * execLogCommand
     */
    fun execLogCommand(
        message: String = "",
        scriptCommand: String? = null,
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        func: () -> Unit
    ): LogLine? {
        // fireEvent is not implemented here

        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execLogCommand"
            )

            pushToCommandStack()

            beginLogLine = TestLog.getLogLine(
                message = message,
                scriptCommand = scriptCommand,
                subject = subject ?: "",
                arg1 = arg1 ?: "",
                arg2 = arg2 ?: "",
            )

            if (CodeExecutionContext.shouldOutputLog) {
                func()
            }
        } finally {
            endCommand()
        }

        return beginLogLine
    }

    /**
     * execCheckCommand
     */
    fun execCheckCommand(
        command: String,
        message: String = "",
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        fireEvent: Boolean = testContext.enableIrregularHandler,
        log: Boolean = TestLog.enableTrace,
        func: () -> Unit
    ): LogLine? {
        callerName = command

        if (TestMode.isNoLoadRun) {
            loggingOnNoLoadRun(
                logType = LogType.CHECK,
                message = message,
                command = command,
                subject = subject,
                arg1 = arg1,
                arg2 = arg2
            )
            if (subject != null) {
                TestDriver.select(subject)
            }
            return null
        }

        val commandText = getCommandText(command = command, subject = subject, arg1 = arg1, arg2 = arg2)
        val ms = Measure(commandText)
        try {
            val sw = StopWatch()

            if (fireEvent) {
                TestDriver.fireIrregularHandler()
                resumeSelector()
            }

            val original = CodeExecutionContext.isInCheckCommand
            try {
                pushToCommandStack()

                beginLogLine = TestLog.check(
                    message = message,
                    subject = subject ?: callerName,
                    arg1 = arg1 ?: "",
                    arg2 = arg2 ?: "",
                    log = log
                )

                CodeExecutionContext.isInCheckCommand = true
                func()
            } finally {
                CodeExecutionContext.isInCheckCommand = original
                endCommand()
            }

            if (PropertiesManager.enableTimeMeasureLog) {
                TestLog.info("[execCheckCommand ($command) in ${sw.elapsedSeconds}sec]")
            }
            return beginLogLine
        } finally {
            ms.end()
        }
    }

    private fun getCommandText(
        subject: String?,
        arg1: String?,
        arg2: String?,
        command: String
    ): String {
        val tokens = mutableListOf<String>()
        if (subject != null) {
            tokens.add(subject)
        }
        if (arg1 != null) {
            tokens.add("arg1=$arg1")
        }
        if (arg2 != null) {
            tokens.add("arg2=$arg2")
        }
        val commandText = "$command(${tokens.joinToString(",")})"
        return commandText
    }

    private fun loggingOnNoLoadRun(
        logType: LogType,
        command: String,
        message: String,
        subject: String?,
        arg1: String?,
        arg2: String?,
        result: LogType = LogType.NONE,
        resultMessage: String? = null
    ): LogLine? {
        if (CodeExecutionContext.isInSilentCommand) {
            return null
        }

        try {
            pushToCommandStack()

            val logType2 =
                if (TestMode.isManualing) LogType.MANUAL
                else logType

            beginLogLine = TestLog.write(
                message = message,
                logType = logType2,
                scriptCommand = command,
                subject = subject ?: "",
                arg1 = arg1 ?: "",
                arg2 = arg2 ?: "",
                result = result,
                resultMessage = resultMessage
            )
        } finally {
            endCommand()
        }

        return beginLogLine
    }

    /**
     * execProcedureCommand
     */
    fun execProcedureCommand(
        message: String = "",
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        fireEvent: Boolean = true,
        log: Boolean = true,
        func: () -> Unit
    ): LogLine? {
        val command = "procedure"

        if (TestMode.isNoLoadRun) {
            loggingOnNoLoadRun(
                logType = LogType.PROCEDURE,
                message = message,
                command = command,
                subject = subject,
                arg1 = arg1,
                arg2 = arg2
            )
            if (subject != null) {
                TestDriver.select(subject)
            }
            return null
        }

        if (fireEvent) {
            TestDriver.fireIrregularHandler()
            resumeSelector()
        }

        val commandText = getCommandText(command = "procedure", subject = subject, arg1 = arg1, arg2 = arg2)
        val ms = Measure(commandText)

        val original = CodeExecutionContext.isInProcedureCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execProcedureCommand"
            )

            pushToCommandStack()
            val outputLog = log && CodeExecutionContext.shouldOutputLog

            beginLogLine = TestLog.procedure(
                message = message,
                subject = subject ?: callerName,
                arg1 = arg1 ?: "",
                arg2 = arg2 ?: "",
                log = outputLog
            )

            CodeExecutionContext.isInProcedureCommand = true
            func()
        } finally {
            try {
                CodeExecutionContext.isInProcedureCommand = original
                endCommand()
            } finally {
                ms.end()
            }
        }

        return beginLogLine
    }

    /**
     * execSilentCommand
     */
    fun execSilentCommand(
        message: String = "",
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        fireEvent: Boolean = true,
        log: Boolean = TestLog.enableTrace,
        func: () -> Unit
    ): LogLine? {

        val ms = Measure()

        val original = CodeExecutionContext.isInSilentCommand
        try {
            if (fireEvent) {
                TestDriver.fireIrregularHandler()
                resumeSelector()
            }

            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execSilentCommand"
            )

            pushToCommandStack()

            beginLogLine = TestLog.silent(
                message = message,
                subject = subject ?: callerName,
                arg1 = arg1 ?: "",
                arg2 = arg2 ?: "",
                log = log
            )

            CodeExecutionContext.isInSilentCommand = true
            func()
        } finally {
            try {
                CodeExecutionContext.isInSilentCommand = original
                endCommand()
            } finally {
                ms.end()
            }
        }

        return beginLogLine
    }

    /**
     * endCommand
     */
    fun endCommand(): LogLine {

        if (begin.not()) {
            TestLog.warn("endCommand() called without calling beginCommand().")
            return LogLine()
        }

        val command = TestLog.commandStack.peek()
        if (this != command) {
            val stack = TestLog.commandStack.map { it.callerName }.joinToString(",")
            TestLog.trace("commandStack: $stack")
            throw IllegalCallerException("endCommand() must be called in ${callerName}, but called in ${command.callerName}.")
        }

        val logLine = TestLog.trace(message = callerName)

        TestLog.commandStack.pop()
        return logLine
    }

    /**
     * execOS
     */
    fun execOS(
        os: String,
        func: () -> Unit
    ): LogLine? {

        val osName = os.lowercase()
        if (osName != "android" && osName != "ios" && osName != "intel" && osName != "arm64") {
            throw IllegalArgumentException("os not supported($os)")
        }

        val ms = Measure()

        val original = CodeExecutionContext.isInOSCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execOS"
            )

            TestLog.osStack.push(callerName)
            TestLog.os = osName

            beginLogLine = TestLog.branch(
                message = "$osName {",
                scriptCommand = "os",
                subject = osName
            )

            CodeExecutionContext.isInOSCommand = true
            TestLog.incrementIndentLevel()
            func()
        } finally {
            try {
                TestLog.decrementIndentLevel()
                CodeExecutionContext.isInOSCommand = original
                endOS()
            } finally {
                ms.end()
            }
        }

        return beginLogLine
    }

    /**
     * endOS
     */
    fun endOS(): LogLine {

        val branch = TestLog.osStack.peek()
        if (callerName != branch) {
            throw IllegalCallerException("endOS() must be called in ${branch}, but called in ${callerName}")
        }
        val osName = TestLog.os
        TestLog.os = ""
        TestLog.osStack.pop()

        val ms = Measure()
        try {
            return TestLog.branch(
                message = "} $osName",
                scriptCommand = "os",
                subject = osName
            )
        } finally {
            ms.end()
        }
    }

    /**
     * execBranch
     */
    fun <R> execBranch(
        command: String,
        condition: String,
        func: () -> R
    ): R {

        val ms = Measure()

        val original = CodeExecutionContext.isInSpecialCommand
        return try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execBranch"
            )

            TestLog.branchCallerStack.push(callerName)

            TestLog.branchStack.push(condition)

            val log = CodeExecutionContext.isInSilentCommand.not()
            beginLogLine = TestLog.branch(
                message = "$condition {",
                scriptCommand = command,
                subject = condition,
                log = log
            )

            CodeExecutionContext.isInSpecialCommand = true
            TestLog.incrementIndentLevel()
            func()
        } finally {
            try {
                TestLog.decrementIndentLevel()
                CodeExecutionContext.isInSpecialCommand = original
                endBranch()
            } finally {
                ms.end()
            }
        }
    }

    /**
     * endBranch
     */
    fun endBranch(): LogLine {

        val lastCallerName = TestLog.branchCallerStack.peek()
        if (callerName != lastCallerName) {
            throw IllegalCallerException("endBranch() must be called in ${lastCallerName}, but called in ${callerName}")
        }

        var condition = ""
        if (TestLog.branchStack.any()) {
            condition = TestLog.branchStack.peek()
        }
        TestLog.branchCallerStack.pop()
        TestLog.branchStack.pop()

        val ms = Measure()
        try {
            val log = CodeExecutionContext.isInSilentCommand.not()
            return TestLog.branch(
                message = "} $condition",
                scriptCommand = beginLogLine?.scriptCommand,
                log = log
            )
        } finally {
            ms.end()
        }
    }

    /**
     * execSpecial
     */
    fun execSpecial(
        subject: String,
        condition: String,
        func: () -> Unit
    ): LogLine? {

        val ms = Measure()

        val original = CodeExecutionContext.isInSpecialCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execSpecial"
            )

            TestLog.specialCallerStack.push(callerName)

            TestLog.specialStack.push(condition)

            val log = CodeExecutionContext.isInSilentCommand.not()
            beginLogLine = TestLog.branch(
                message = "$condition {",
                scriptCommand = "special",
                subject = subject,
                arg1 = condition,
                log = log
            )

            CodeExecutionContext.isInSpecialCommand = true
            TestLog.incrementIndentLevel()
            func()
        } finally {
            try {
                TestLog.decrementIndentLevel()
                CodeExecutionContext.isInSpecialCommand = original
                endSpecial()
            } finally {
                ms.end()
            }
        }

        return beginLogLine
    }

    /**
     * endSpecial
     */
    fun endSpecial(): LogLine {

        val lastCallerName = TestLog.specialCallerStack.peek()
        if (callerName != lastCallerName) {
            throw IllegalCallerException("endSpecial() must be called in ${lastCallerName}, but called in ${callerName}")
        }

        var condition = ""
        if (TestLog.specialStack.any()) {
            condition = TestLog.specialStack.peek()
        }
        TestLog.specialCallerStack.pop()
        TestLog.specialStack.pop()

        val ms = Measure()
        try {
            val log = CodeExecutionContext.isInSilentCommand.not()
            return TestLog.branch(
                message = "} $condition",
                scriptCommand = "special",
                subject = condition,
                log = log
            )
        } finally {
            ms.end()
        }
    }

    /**
     * execWithScroll
     */
    fun execWithScroll(
        command: String,
        withScroll: Boolean = true,
        scrollDirection: ScrollDirection?,
        scrollVisionElement: VisionElement = CodeExecutionContext.workingRegionElement,
        scrollFrame: String = "",
        scrollableElement: TestElement? = null,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
        scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
        scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
        swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
        message: String = "",
        log: Boolean = false,
        func: () -> Unit
    ): LogLine? {

        val originalWithScroll = CodeExecutionContext.withScroll
        val originalScrollDirection = CodeExecutionContext.scrollDirection
        val originalScrollFrame = CodeExecutionContext.scrollFrame
        val originalScrollableElement = CodeExecutionContext.scrollableElement
        val originalScrollVisionElement = CodeExecutionContext.scrollVisionElement
        val originalScrollDurationSeconds = CodeExecutionContext.scrollDurationSeconds
        val originalScrollIntervalSeconds = CodeExecutionContext.scrollIntervalSeconds
        val originalScrollStartMarginRatio = CodeExecutionContext.scrollStartMarginRatio
        val originalScrollEndMarginRatio = CodeExecutionContext.scrollEndMarginRatio
        val originalScrollMaxCount = CodeExecutionContext.scrollMaxCount
        val originalScrollToEdgeBoost = CodeExecutionContext.scrollToEdgeBoost
        val originalSwipeToSafePosition = CodeExecutionContext.swipeToSafePosition

        val ms = Measure()
        try {
            CodeExecutionContext.withScroll = withScroll
            CodeExecutionContext.scrollDirection = scrollDirection
            CodeExecutionContext.scrollFrame = scrollFrame
            CodeExecutionContext.scrollableElement = scrollableElement
            CodeExecutionContext.scrollVisionElement = scrollVisionElement
            CodeExecutionContext.scrollDurationSeconds = scrollDurationSeconds
            CodeExecutionContext.scrollIntervalSeconds = scrollIntervalSeconds
            CodeExecutionContext.scrollStartMarginRatio = scrollStartMarginRatio
            CodeExecutionContext.scrollEndMarginRatio = scrollEndMarginRatio
            CodeExecutionContext.scrollMaxCount = scrollMaxCount
            CodeExecutionContext.scrollToEdgeBoost = scrollToEdgeBoost
            CodeExecutionContext.swipeToSafePosition = swipeToSafePosition

            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execWithScroll"
            )

            TestLog.withScrollStack.push(callerName)

            if (log) {
                beginLogLine = TestLog.withScroll(
                    message = "$message {",
                    scriptCommand = command
                )
            }

            func()
        } finally {
            try {
                CodeExecutionContext.withScroll = originalWithScroll
                CodeExecutionContext.scrollDirection = originalScrollDirection
                CodeExecutionContext.scrollFrame = originalScrollFrame
                CodeExecutionContext.scrollableElement = originalScrollableElement
                CodeExecutionContext.scrollVisionElement = originalScrollVisionElement
                CodeExecutionContext.scrollDurationSeconds = originalScrollDurationSeconds
                CodeExecutionContext.scrollIntervalSeconds = originalScrollIntervalSeconds
                CodeExecutionContext.scrollStartMarginRatio = originalScrollStartMarginRatio
                CodeExecutionContext.scrollEndMarginRatio = originalScrollEndMarginRatio
                CodeExecutionContext.scrollMaxCount = originalScrollMaxCount
                CodeExecutionContext.scrollToEdgeBoost = originalScrollToEdgeBoost
                CodeExecutionContext.swipeToSafePosition = originalSwipeToSafePosition
                endExecWithScroll(command = command, log = log)
            } finally {
                ms.end()
            }
        }

        return beginLogLine
    }

    /**
     * endExecWithScroll
     */
    fun endExecWithScroll(command: String, log: Boolean): LogLine? {

        val current = TestLog.currentWithScroll
        if (callerName != current) {
            throw IllegalCallerException("endExecWithScroll() must be called in ${current}, but called in ${callerName}")
        }
        TestLog.withScrollStack.pop()
        val message = message(id = command)

        if (log) {
            return TestLog.withScroll(
                message = "} $message",
                scriptCommand = command
            )
        } else {
            return null
        }
    }

}