package shirates.core.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogLine
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.StackTraceUtility
import shirates.core.utility.time.StopWatch

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
        log: Boolean = TestLog.enableTrace,
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

        val original = CodeExecutionContext.isInSelectCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execSelectCommand"
            )

            pushToCommandStack()

            TestLog.trace(message = callerName)

            val funcName = callerName.split(".").last()
            val msg = "$funcName $selector"
            beginLogLine = TestLog.select(
                message = msg,
                subject = subject ?: callerName,
                arg1 = arg1 ?: "",
                arg2 = arg2 ?: "",
                log = log
            )

            CodeExecutionContext.isInSelectCommand = true
            func()
        } finally {
            CodeExecutionContext.isInSelectCommand = original
            endCommand()
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

        val original = CodeExecutionContext.isInRelativeCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execRelativeCommand"
            )

            TestLog.trace(message = callerName)

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
            CodeExecutionContext.isInRelativeCommand = original
            endCommand()
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

        val original = CodeExecutionContext.isInBooleanCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execBooleanCommand"
            )

            TestLog.trace(message = callerName)

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
            CodeExecutionContext.isInBooleanCommand = original
            endCommand()
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
        forceLog: Boolean = false,
        suppressBeforeScreenshot: Boolean = false,
        func: () -> Unit
    ): LogLine? {

        return execOperateCommandCore(
            command = command,
            message = message,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            fileName = fileName,
            fireEvent = fireEvent,
            forceLog = forceLog,
            scriptCommand = scriptCommand,
            suppressBeforeScreenshot = suppressBeforeScreenshot,
            func = func
        )
    }

    private fun execOperateCommandCore(
        command: String,
        message: String,
        subject: String?,
        arg1: String?,
        arg2: String?,
        fileName: String?,
        fireEvent: Boolean,
        forceLog: Boolean,
        scriptCommand: String?,
        suppressBeforeScreenshot: Boolean = false,
        func: () -> Unit
    ): LogLine? {
        callerName = command

        val sw = StopWatch()

        if (TestMode.isNoLoadRun) {
            loggingOnNoLoadRun(
                logType = LogType.OPERATE,
                message = message,
                command = command,
                subject = subject,
                arg1 = arg1,
                arg2 = arg2
            )
            return null
        }
//        if (TestDriver.skip) {
//            return TestLog.skip(
//                message = message,
//                subject = subject,
//                arg1 = arg1,
//                arg2 = arg2
//            )
//        }

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
            val outputLog = forceLog || CodeExecutionContext.shouldOutputLog

            TestLog.trace(message = callerName)

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
                TestDriver.autoScreenshot()
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

            TestLog.trace(message = callerName)

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

        val sw = StopWatch()

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
//        if (TestDriver.skip) {
//            return TestLog.skip(
//                message = message,
//                subject = subject,
//                arg1 = arg1,
//                arg2 = arg2
//            )
//        }

        if (fireEvent) {
            TestDriver.fireIrregularHandler()
            resumeSelector()
        }

        val original = CodeExecutionContext.isInCheckCommand
        try {
            pushToCommandStack()

            TestLog.trace(message = callerName)

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
        try {
            pushToCommandStack()

            beginLogLine = TestLog.write(
                message = message,
                logType = logType,
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

        if (fireEvent) {
            TestDriver.fireIrregularHandler()
            resumeSelector()
        }

        val original = CodeExecutionContext.isInProcedureCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execProcedureCommand"
            )

            pushToCommandStack()
            val outputLog = log && CodeExecutionContext.shouldOutputLog

            TestLog.trace(message = callerName)

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
            CodeExecutionContext.isInProcedureCommand = original
            endCommand()
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

        if (TestMode.isNoLoadRun) {
            return null
        }

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

            TestLog.trace(message = callerName)

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
            CodeExecutionContext.isInSilentCommand = original
            endCommand()
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
            func()
        } finally {
            CodeExecutionContext.isInOSCommand = original
            endOS()
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
        return TestLog.branch(
            message = "} $osName",
            scriptCommand = "os",
            subject = osName
        )
    }

    /**
     * execSpecial
     */
    fun execSpecial(
        subject: String,
        expected: String,
        func: () -> Unit
    ): LogLine? {

        val original = CodeExecutionContext.isInSpecialCommand
        try {
            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execSpecial"
            )

            TestLog.specialCallerStack.push(callerName)

            val special = expected
            TestLog.specialStack.push(special)

            beginLogLine = TestLog.branch(
                message = "$special {",
                scriptCommand = "special",
                subject = subject,
                arg1 = expected
            )

            CodeExecutionContext.isInSpecialCommand = true
            func()
        } finally {
            CodeExecutionContext.isInSpecialCommand = original
            endSpecial()
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

        var special = ""
        if (TestLog.specialStack.any()) {
            special = TestLog.specialStack.peek()
        }
        TestLog.specialCallerStack.pop()
        TestLog.specialStack.pop()

        return TestLog.branch(
            message = "} $special",
            scriptCommand = "special",
            subject = special
        )
    }

    /**
     * execWithScroll
     */
    fun execWithScroll(
        command: String,
        scrollDirection: ScrollDirection?,
        scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
        scrollToEdgeBoost: Int = testContext.scrollToEdgeBoost,
        scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
        scrollMaxCount: Int = testContext.scrollMaxCount,
        message: String = "",
        func: () -> Unit
    ): LogLine? {

        val originalWithScrollDirection = CodeExecutionContext.withScrollDirection
        val originalScrollDurationSeconds = testContext.swipeDurationSeconds
        val originalScrollIntervalSeconds = testContext.scrollIntervalSeconds
        val originalScrollToEdgeBoost = testContext.scrollToEdgeBoost
        val originalScrollMaxCount = testContext.scrollMaxCount

        try {
            CodeExecutionContext.withScrollDirection = scrollDirection
            testContext.swipeDurationSeconds = scrollDurationSeconds
            testContext.scrollToEdgeBoost = scrollToEdgeBoost
            testContext.scrollIntervalSeconds = scrollIntervalSeconds
            testContext.scrollMaxCount = scrollMaxCount

            callerName = StackTraceUtility.getCallerName(
                filterFileName = COMMAND_CONTEXT_FILE_NAME,
                filterMethodName = "execWithScroll"
            )

            TestLog.withScrollStack.push(callerName)

            beginLogLine = TestLog.withScroll(
                message = "$message {",
                scriptCommand = command
            )

            func()
        } finally {
            CodeExecutionContext.withScrollDirection = originalWithScrollDirection
            testContext.swipeDurationSeconds = originalScrollDurationSeconds
            testContext.scrollToEdgeBoost = originalScrollToEdgeBoost
            testContext.scrollIntervalSeconds = originalScrollIntervalSeconds
            testContext.scrollMaxCount = originalScrollMaxCount
            endExecWithScroll(command = command)
        }

        return beginLogLine
    }

    /**
     * endExecWithScroll
     */
    fun endExecWithScroll(command: String): LogLine {

        val current = TestLog.currentWithScroll
        if (callerName != current) {
            throw IllegalCallerException("endExecWithScroll() must be called in ${current}, but called in ${callerName}")
        }
        TestLog.withScrollStack.pop()
        val message = message(id = command)
        return TestLog.withScroll(
            message = "} $message",
            scriptCommand = command
        )
    }

}