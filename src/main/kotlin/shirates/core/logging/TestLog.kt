package shirates.core.logging

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.report.TestReport
import shirates.core.report.TestReportIndex
import shirates.core.testcode.CodeExecutionContext
import shirates.core.testcode.UITestCallbackExtension
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.core.utility.format
import shirates.core.utility.toPath
import shirates.spec.report.TestClassListReport
import shirates.spec.report.TestListReport
import shirates.spec.report.models.SpecReportExecutor
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.full.memberProperties

/**
 * TestLog
 */
object TestLog {

    /**
     * logLanguage
     */
    var logLanguage: String
        get() {
            return PropertiesManager.logLanguage
        }
        set(value) {
            PropertiesManager.setPropertyValue("logLanguage", value)
        }

    /**
     * currentTestClass
     */
    var currentTestClass: Class<*>? = null

    /**
     * currentTestClassName
     */
    val currentTestClassName: String
        get() {
            return if (currentTestClass == null)
                ""
            else
                currentTestClass!!.simpleName
        }

    /**
     * currentTestMethodName
     */
    val currentTestMethodName: String
        get() {
            return UITestCallbackExtension.uiTestBase?.currentTestMethodName ?: ""
        }

    /**
     * testConfigName
     */
    var testConfigName: String = ""

    /**
     * sessionStartTime
     */
    val sessionStartTime = Date()

    /**
     * sessionStartTimeLabel
     */
    val sessionStartTimeLabel: String
        get() {
            val f = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.JAPANESE)
            return f.format(sessionStartTime)
        }

    /**
     * lines
     */
    val lines = mutableListOf<LogLine>()

    /**
     * currentLineNo
     */
    val currentLineNo: Int
        get() {
            return lines.size
        }

    /**
     * nextLineNo
     */
    val nextLineNo: Int
        get() {
            return currentLineNo + 1
        }

    /**
     * getNextScreenshotFileName
     */
    fun getNextScreenshotFileName(suffix: String = ""): String {
        return "${nextLineNo}${suffix}.png"
    }

    /**
     * allLines
     */
    val allLines = mutableListOf<LogLine>()

    /**
     * histories
     */
    val histories = mutableListOf<LogLine>()

    /**
     * dateFormatter
     */
    var dateFormatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS")

    /**
     * testResults
     */
    var testResults = PropertiesManager.testResults.toPath()

    /**
     * directoryForTestConfig
     */
    val directoryForTestConfig: Path
        get() {
            return if (testConfigName.isBlank()) {
                testResults
            } else {
                testResults.resolve(testConfigName)
            }
        }

    /**
     * directoryForLog
     */
    val directoryForLog: Path
        get() {
            return directoryForTestConfig.resolve("$sessionStartTimeLabel/$currentTestClassName")
        }

    /**
     * directoryForTestList
     */
    val directoryForTestList: Path
        get() {
            return if (PropertiesManager.testListDir.isBlank()) testResults
            else PropertiesManager.testListDir.toPath()
        }

    /**
     * directoryForReportIndex
     */
    val directoryForReportIndex: Path
        get() {
            return if (PropertiesManager.reportIndexDir.isBlank()) testResults
            else PropertiesManager.reportIndexDir.toPath()
        }

    /**
     * directoryMap
     */
    val directoryMap: Map<String, String>
        get() {
            val map = TestLog::class.memberProperties
                .filter { it.returnType.toString() == "java.nio.file.Path" }
                .map { it.name to it.get(this).toString() }.toMap()
            return map
        }

    /**
     * testScenarioId
     */
    var testScenarioId: String = ""

    /**
     * stepNo
     */
    internal var stepNo: Int? = null

    /**
     * enableTestList
     */
    var enableTestList: Boolean = false

    /**
     * enableTestClassList
     */
    var enableTestClassList: Boolean = false

    /**
     * enableSpecReport
     */
    var enableSpecReport: Boolean = false

    /**
     * enableTrace
     */
    var enableTrace: Boolean = false

    /**
     * enableXmlSourceDump
     */
    var enableXmlSourceDump: Boolean = true

    /**
     * lastScenarioLog
     */
    var lastScenarioLog: LogLine? = null

    /**
     * lastCaseLog
     */
    var lastCaseLog: LogLine? = null

    /**
     * lastTestLog
     */
    val lastTestLog: LogLine?
        get() {
            return lines.lastOrNull()
        }

    /**
     * testCaseId
     */
    val testCaseId: String
        get() {
            val separator = if (stepNo == null) {
                ""
            } else {
                "-"
            }
            return "$testScenarioId${separator}$stepNo"
        }


    /**
     * command stack
     */
    var commandStack = Stack<TestDriverCommandContext>()

    /**
     * userCallCommand
     */
    val userCallCommand: TestDriverCommandContext?
        get() {
            if (commandStack.isEmpty()) {
                return null
            } else {
                return commandStack[0]
            }
        }

    /**
     * os
     */
    var os = ""

    /**
     * osStack
     */
    var osStack = Stack<String>()

    /**
     * currentOs
     */
    val currentOs: String
        get() {
            if (osStack.any()) {
                return osStack.peek()
            } else {
                return ""
            }
        }

    /**
     * branchCallerStack
     */
    var branchCallerStack = Stack<String>()

    /**
     * branchStack
     */
    var branchStack = Stack<String>()

    /**
     * specialCallerStack
     */
    var specialCallerStack = Stack<String>()

    /**
     * specialStack
     */
    var specialStack = Stack<String>()

    /**
     * currentSpecial
     */
    val currentSpecial: String
        get() {
            if (specialStack.any()) {
                return specialStack.peek()
            } else {
                return ""
            }
        }

    /**
     * withScrollStack
     */
    var withScrollStack = Stack<String>()

    /**
     * currentWithScroll
     */
    val currentWithScroll: String
        get() {
            if (withScrollStack.any()) {
                return withScrollStack.peek()
            } else {
                return ""
            }
        }

    /**
     * currentCAEPattern
     */
    val currentCAEPattern: LogType
        get() {
            val current =
                lines.lastOrNull() {
                    it.logType == LogType.CONDITION ||
                            it.logType == LogType.ACTION ||
                            it.logType == LogType.EXPECTATION
                }
            if (current != null) {
                return current.logType
            } else {
                return LogType.NONE
            }
        }

    /**
     * configPrinted
     */
    val configPrinted: Boolean
        get() {
            return lines.any() { it.scriptCommand == "parameter" && it.message.startsWith("testConfigName") }
        }

    /**
     * capabilityPrinted
     */
    val capabilityPrinted: Boolean
        get() {
            return lines.any() { it.scriptCommand == "parameter" && it.message.startsWith("appium:automationName") }
        }

    /**
     * getOKType
     */
    fun getOKType(): LogType {
        if (CodeExecutionContext.isInExpectation) {
            return LogType.OK
        } else {
            return LogType.ok
        }
    }

    /**
     * setupTestResults
     */
    fun setupTestResults(testResults: String, testConfigName: String) {
        this.testConfigName = testConfigName

        val path = testResults.toPath()
        if (path.isAbsolute) {
            this.testResults = path
        } else {
            val newPath = shirates.core.UserVar.downloads.resolve(testResults)
            this.testResults = newPath.toAbsolutePath()
        }
    }

    /**
     * createDirectoryForLog
     */
    fun createDirectoryForLog() {

        if (Files.exists(directoryForLog).not()) {
            File(directoryForLog.toUri()).mkdirs()
        }
    }

    /**
     * printLogDirectory
     */
    fun printLogDirectory() {

        val logDir = getLogDirectoryUrl()
        info(message(id = "logOutput", file = logDir))
    }

    /**
     * write
     */
    fun write(
        message: String,
        logType: LogType = LogType.NONE,
        auto: String = if (TestMode.isManual || TestMode.isManualing) "M" else "A",
        environment: String = TestMode.environment,
        supplement: String = "",
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        fileName: String = "",
        result: LogType = LogType.NONE,
        resultMessage: String? = null,
        exception: Throwable? = null,
        log: Boolean = true,
        logLineCallback: ((LogLine) -> Unit)? = null
    ): LogLine {

        val logLine = getLogLine(
            message = message,
            resultMessage = resultMessage,
            exception = exception,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            fileName = fileName,
            logType = logType,
            auto = auto,
            environment = environment,
            supplement = supplement,
            result = result
        )
        logLineCallback?.invoke(logLine)

        allLines.add(logLine)

        if (log.not()) return logLine

        lines.add(logLine)
        histories.add(logLine)

        if (lines.count() == 1) {
            printline(LogLine.getHeaderForConsole())
        }

        printline(logLine.toStringForConsole())

        return logLine
    }

    /**
     * firstLogLine
     */
    val firstLogLine: LogLine
        get() {
            if (lines.isEmpty()) {
                throw IllegalAccessError("TestLog.lines is empty")
            }
            return lines[0]
        }

    private var mMonitorLogPath: Path? = null

    /**
     * monitorLogPath
     */
    val monitorLogPath: Path
        get() {
            if (mMonitorLogPath == null) {
                val dateLabel = firstLogLine.logDateTime.format("yyyyMMddHHmmss")
                mMonitorLogPath = Path.of("$directoryForLog/TestLog_$dateLabel.log")
            }
            return mMonitorLogPath!!
        }

    private fun printline(obj: Any?) {

        val text = obj?.toString() ?: "(null)"
        println(text)

        if (lines.isEmpty()) {
            return
        }

        if (Files.exists(directoryForLog).not()) {
            return
        }

        try {
            if (Files.exists(monitorLogPath).not()) {
                val bufferedText = LogLine.getHeaderForConsole() + Const.NEW_LINE + lines.joinToString(Const.NEW_LINE)
                monitorLogPath.toFile().writeText(bufferedText + Const.NEW_LINE)
            } else {
                monitorLogPath.toFile().appendText(text + Const.NEW_LINE)
            }
        } catch (t: Throwable) {
            println(t)
        }
    }

    const val MAXIMUM_CELL_CONTENT_LENGTH = 32767

    internal fun getLogLine(
        message: String,
        logType: LogType = LogType.NONE,
        auto: String = "A",
        environment: String = "",
        supplement: String = "",
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        fileName: String = "",
        result: LogType = LogType.NONE,
        resultMessage: String? = null,
        exception: Throwable? = null
    ): LogLine {
        var msg = message
            .replace("\r", "\\r")
            .replace("\n", "\\n")
        if (msg.length > MAXIMUM_CELL_CONTENT_LENGTH) {
            msg = msg.substring(0, MAXIMUM_CELL_CONTENT_LENGTH - 1)
        }
        msg = getIndentString() + msg
        val resultMsg = resultMessage ?: exception?.message ?: ""
        val special = specialStack.toList().joinToString("/")

        var group = commandStack.toList().firstOrNull()?.callerName ?: ""
        if (group.contains(".")) {
            group = group.split(".")[1]
        }

        var command = scriptCommand ?: commandStack.toList().lastOrNull()?.callerName ?: ""
        if (command.contains(".")) {
            command = command.split(".")[1]
        }

        val lineNumber = lines.count() + 1
        val commandLevel = commandStack.count()
        val commandGroupNo = userCallCommand?.beginLogLine?.commandGroupNo ?: lineNumber
        val mode =
            if (TestMode.isManual) "MANUAL"
            else if (TestMode.isSkipping) "SKIP"
            else if (TestMode.isManualing) "MANUAL"
            else if (TestMode.isNoLoadRun) "NLR"
            else if (testContext.useCache) "C"  // cache mode
            else "!"    // direct access mode
        val result2 =
            if (TestMode.isManual || TestMode.isManualing) LogType.NONE
            else if (TestMode.isSkipping) LogType.SKIP
            else result

        val logLine = LogLine(
            lineNumber = lineNumber,
            logDateTime = Date(),
            message = msg,
            os = this.os,
            special = special.replace("\n", "\\n"),
            commandGroup = group,
            commandGroupNo = commandGroupNo,
            commandLevel = commandLevel,
            scriptCommand = command,
            subject = subject.replace("\n", "\\n"),
            arg1 = arg1.replace("\n", "\\n"),
            arg2 = arg2.replace("\n", "\\n"),
            fileName = fileName,
            logType = logType,
            auto = auto,
            environment = environment,
            supplement = supplement,
            mode = mode,
            testScenarioId = testScenarioId,
            stepNo = stepNo,
            result = result2,
            resultMessage = resultMsg,
            exception = exception,
            lastScreenshot =
                if (CodeExecutionContext.lastScreenshotName.isNullOrBlank()) ""
                else CodeExecutionContext.lastScreenshotName.toPath().fileName.toString(),
            testClassName = currentTestClassName,
            testMethodName = currentTestMethodName
        )
        val lastLine = lines.lastOrNull()
        if (lastLine != null) {
            logLine.timeDiffMilliseconds =
                logLine.logDateTime.toInstant().toEpochMilli() - lastLine.logDateTime.toInstant().toEpochMilli()
        }
        logLine.timeElapsed = logLine.logDateTime.time - sessionStartTime.time

        logLine.macroStackDepth = CodeExecutionContext.macroStack.count()
        logLine.macroName =
            CodeExecutionContext.macroStack.lastOrNull() ?: "-"
        logLine.isInCheckCommand = CodeExecutionContext.isInCheckCommand
        logLine.isInSilentCommand = CodeExecutionContext.isInSilentCommand
        logLine.isInOSCommand = CodeExecutionContext.isInOSCommand
        logLine.isInBooleanCommand = CodeExecutionContext.isInBooleanCommand
        logLine.isInSelectCommand = CodeExecutionContext.isInSelectCommand
        logLine.isInSpecialCommand = CodeExecutionContext.isInSpecialCommand
        logLine.isInRelativeCommand = CodeExecutionContext.isInRelativeCommand
        logLine.isInProcedureCommand = CodeExecutionContext.isInProcedureCommand
        logLine.isInOperationCommand = CodeExecutionContext.isInOperationCommand
        logLine.isScrolling = CodeExecutionContext.isScrolling
        logLine.withScrollDirection = CodeExecutionContext.scrollDirection
        logLine.isNoLoadRun = TestMode.isNoLoadRun
        // CAE
        logLine.isInScenario = CodeExecutionContext.isInScenario
        logLine.isInCase = CodeExecutionContext.isInCase
        logLine.isInCondition = CodeExecutionContext.isInCondition
        logLine.isInAction = CodeExecutionContext.isInAction
        logLine.isInExpectation = CodeExecutionContext.isInExpectation

        return logLine
    }

    internal var indentLevel = 0

    internal fun incrementIndentLevel(): Int {

        indentLevel++
        return indentLevel
    }

    internal fun decrementIndentLevel(): Int {

        indentLevel--
        if (indentLevel < 0) {
            indentLevel = 0
        }
        return indentLevel
    }

    internal fun getIndentString(level: Int = indentLevel): String {

        return " ".repeat(level * 4)
    }

    /**
     * write
     */
    fun write(
        exception: Throwable,
        logType: LogType = LogType.NONE,
        result: LogType = LogType.NONE,
        resultMessage: String? = null,
        log: Boolean = true
    ): LogLine {

        val msg = "$exception"

        return write(
            message = msg,
            logType = logType,
            result = result,
            resultMessage = resultMessage,
            exception = exception,
            log = log
        )
    }

    /**
     * info
     */
    fun info(message: String, log: Boolean = true): LogLine {
        if (log.not()) return LogLine()

        return write(
            message = message,
            logType = LogType.INFO,
            log = log
        )
    }

    /**
     * warn
     */
    fun warn(message: String, log: Boolean = true): LogLine {
        if (log.not()) return LogLine()

        return write(
            message = message,
            logType = LogType.WARN,
            log = log
        )
    }

    /**
     * trace
     */
    fun trace(message: String = "", log: Boolean = enableTrace): LogLine {

        if (log.not()) return LogLine()

        val stackFrame = Thread.currentThread().stackTrace[1]

        return traceCore(message = message, eventName = "trace", userCalledStackFrame = stackFrame, log = log)
    }

    internal fun traceCore(
        message: String = "",
        eventName: String,
        userCalledStackFrame: StackTraceElement = Thread.currentThread().stackTrace[1],
        logLineCallback: ((LogLine) -> Unit)? = null,
        log: Boolean = enableTrace
    ): LogLine {

        if (log.not()) return LogLine()

        val calledStack = userCalledStackFrame

        val stacktrace = Thread.currentThread().stackTrace
        val frames = mutableListOf<StackTraceElement>()
        for (i in 0 until stacktrace.count()) {
            val first = stacktrace[i]
            if (first.fileName == calledStack.fileName
                && (first.methodName == calledStack.methodName || first.methodName == "${calledStack.methodName}\$default")
            ) {
                frames.add(first)
                val second = stacktrace[i + 1]
                frames.add(second)
                if (second.methodName == calledStack.methodName || second.methodName == "${calledStack.methodName}\$default") {
                    val third = stacktrace[i + 2]
                    frames.add(third)
                }
                break
            }
        }
        if (frames.isEmpty()) {
            return LogLine()
        }

        val callerStack = frames.last()
        val className = callerStack.className.split(".").last()
        val methodName = callerStack.methodName
        val classAndMethodName = "$className.$methodName"

        val msg = if (message.isBlank()) "[${classAndMethodName}] -$eventName"
        else "[${classAndMethodName}] $message"

        return write(
            message = msg,
            logType = LogType.TRACE,
            log = log,
            logLineCallback = logLineCallback
        )
    }

    /**
     * error
     */
    fun error(message: String, exception: Throwable? = null, log: Boolean = true): LogLine {
        if (log.not()) return LogLine()

        result(LogType.ERROR, exception)
        return write(
            message = message,
            exception = exception,
            logType = LogType.ERROR,
            result = LogType.ERROR,
            log = log
        )
    }

    /**
     * error
     */
    fun error(exception: Throwable, log: Boolean = true): LogLine {
        if (log.not()) return LogLine()

        result(LogType.ERROR, exception)
        return write(
            exception = exception,
            logType = LogType.ERROR,
            result = LogType.ERROR,
            log = log
        )
    }

    /**
     * result
     */
    fun result(result: LogType, resultMessage: String, exception: Throwable? = null) {

        val lastCheckLog = userCallCommand?.beginLogLine

        if (lastCheckLog != null) {
            resultCore(lastCheckLog, result, resultMessage, exception)
        }

        if (lastCaseLog != null) {
            resultCore(lastCaseLog!!, result, resultMessage, exception)
        }

        if (lastScenarioLog != null) {
            resultCore(lastScenarioLog!!, result, resultMessage, exception)
        }
    }

    private fun resultCore(
        lastLog: LogLine,
        result: LogType,
        resultMessage: String,
        exception: Throwable?
    ) {
        if (lastLog.result.isFailType) {
            // do not overwrite when lastLog is FailType
            return
        }
        if (result.isFailType) {
            // overwrite by result when lastLog is not FailType
            setScenarioLog(lastLog, result, resultMessage, exception)
            return
        }
        if (lastLog.result.isSkipType) {
            // do not overwrite when lastLog is SkipType
            return
        }
        if (lastLog.result.isInconclusiveType) {
            // do not overwrite when lastLog is Inconclusive
            return
        }

        // overwrite by result
        setScenarioLog(lastLog, result, resultMessage, exception)
    }

    private fun setScenarioLog(
        scenarioLog: LogLine,
        result: LogType,
        resultMessage: String,
        exception: Throwable?
    ) {
        scenarioLog.result = result
        scenarioLog.resultMessage = resultMessage
        scenarioLog.exception = exception
    }

    /**
     * result
     */
    fun result(result: LogType, exception: Throwable?) {

        val msg = exception?.message ?: "$exception"
        result(result = result, resultMessage = msg, exception = exception)
    }

    private fun scriptCommand(
        scriptCommand: String,
        subject: String,
        arg1: String,
        arg2: String,
        message: String?,
        log: Boolean,
        logType: LogType = LogType.INFO
    ): LogLine {
        if (log.not()) return LogLine()

        val a1 = arg1.ifBlank { "" }
        val a2 = arg2.ifBlank { "" }
        val values = mutableListOf(a1, a2).filter { it.isNotBlank() }.joinToString(",")
        val msg = message ?: "$subject: $values"

        return write(
            message = msg,
            logType = logType,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * parameter
     */
    fun parameter(
        subject: String,
        arg1: String = "",
        arg2: String = "",
        message: String? = null,
        log: Boolean = true,
        logType: LogType = LogType.NONE
    ): LogLine {

        return scriptCommand(
            scriptCommand = "parameter",
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            message = message,
            log = log,
            logType = logType
        )
    }

    /**
     * execute
     */
    fun execute(
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        message: String? = null,
        log: Boolean = true
    ): LogLine {

        return scriptCommand(
            scriptCommand = "execute",
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            message = message,
            log = log,
        )
    }

    private fun getCommandContextLogLine(): LogLine? {

        if (commandStack.isEmpty()) {
            return null
        }

        var context: TestDriverCommandContext

        context = commandStack[0]
        if (context.beginLogLine?.scriptCommand == "procedure" && commandStack.count() > 1) {
            // in case of inside "procedure"
            context = commandStack[1]
        }

        return context.beginLogLine
    }

    /**
     * ok
     */
    fun ok(
        message: String,
        auto: String = "A",
        scriptCommand: String? = null,
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        log: Boolean = CodeExecutionContext.shouldOutputLog
    ): LogLine {

        val okType = getOKType()
        result(okType, message)

        val line = getCommandContextLogLine()
        return write(
            message = message,
            logType = okType,
            auto = auto,
            result = okType,
            scriptCommand = scriptCommand ?: line?.scriptCommand ?: "",
            subject = subject ?: line?.subject ?: "",
            arg1 = arg1 ?: line?.arg1 ?: "",
            arg2 = arg2 ?: line?.arg2 ?: "",
            log = log,
            exception = null
        )
    }

    /**
     * ng
     */
    fun ng(
        exception: Throwable,
        auto: String = "A",
        scriptCommand: String? = null,
        subject: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        log: Boolean = true
    ): LogLine {

        result(LogType.NG, exception)
        val line = getCommandContextLogLine()
        return write(
            message = exception.message ?: exception.stackTraceToString(),
            logType = LogType.NG,
            auto = auto,
            result = LogType.NG,
            scriptCommand = scriptCommand ?: line?.scriptCommand ?: "",
            subject = subject ?: line?.subject ?: "",
            arg1 = arg1 ?: line?.arg1 ?: "",
            arg2 = arg2 ?: line?.arg2 ?: "",
            log = log,
            exception = exception
        )
    }

    /**
     * manual
     */
    fun manual(
        message: String,
        scriptCommand: String? = "manual",
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = enableTrace || CodeExecutionContext.isInSilentCommand.not()
    ): LogLine {

        result(LogType.MANUAL, message)
        return write(
            message = message,
            logType = LogType.MANUAL,
            auto = "M",
            result = LogType.MANUAL,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * conditionalAuto
     */
    fun conditionalAuto(
        message: String,
        scriptCommand: String? = "conditionalAuto",
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = enableTrace || CodeExecutionContext.isInSilentCommand.not()
    ): LogLine {

        result(LogType.COND_AUTO, message)
        return write(
            message = message,
            logType = LogType.COND_AUTO,
            auto = "CA",
            result = LogType.COND_AUTO,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * skipScenario
     */
    fun skipScenario(
        message: String,
        log: Boolean = true
    ): LogLine {
        result(LogType.SKIP_SCENARIO, message)
        return write(
            message = message,
            logType = LogType.SKIP_SCENARIO,
            result = LogType.SKIP_SCENARIO,
            scriptCommand = "skipScenario",
            log = log,
        )
    }

    /**
     * skipCase
     */
    fun skipCase(
        message: String,
        log: Boolean = true
    ): LogLine {
        return write(
            message = message,
            logType = LogType.SKIP_CASE,
            scriptCommand = "skipCase",
            log = log,
        )
    }

    /**
     * manualScenario
     */
    fun manualScenario(
        message: String,
        log: Boolean = true
    ): LogLine {
        result(LogType.MANUAL_SCENARIO, message)
        return write(
            message = message,
            logType = LogType.MANUAL_SCENARIO,
            result = LogType.MANUAL_SCENARIO,
            scriptCommand = "manualScenario",
            log = log,
        )
    }

    /**
     * manualCase
     */
    fun manualCase(
        message: String,
        log: Boolean = true
    ): LogLine {
        return write(
            message = message,
            logType = LogType.MANUAL_CASE,
            scriptCommand = "manualCase",
            log = log,
        )
    }

    /**
     * skip
     */
    fun skip(
        message: String,
        subject: String? = "",
        arg1: String? = "",
        arg2: String? = "",
        log: Boolean = true
    ): LogLine {

        result(LogType.SKIP, message)
        return write(
            message = message,
            subject = subject ?: "",
            arg1 = arg1 ?: "",
            arg2 = arg2 ?: "",
            logType = LogType.SKIP,
            result = LogType.SKIP,
            scriptCommand = "skip",
            log = log
        )
    }

    /**
     * skip
     */
    fun skip(
        exception: Exception,
        log: Boolean = true
    ): LogLine {

        result(LogType.SKIP, exception)
        return write(
            message = exception.message ?: "",
            logType = LogType.SKIP,
            result = LogType.SKIP,
            scriptCommand = "skip",
            log = log,
            exception = exception
        )
    }

    /**
     * notImpl
     */
    fun notImpl(
        exception: Throwable,
        log: Boolean = true
    ): LogLine {

        result(LogType.NOTIMPL, exception)
        return write(
            message = exception.message ?: exception.stackTraceToString(),
            logType = LogType.NOTIMPL,
            result = LogType.NOTIMPL,
            scriptCommand = "notImpl",
            log = log,
            exception = exception
        )
    }

    /**
     * knownIssue
     */
    fun knownIssue(
        message: String,
        ticketUrl: String,
        log: Boolean = true
    ): LogLine {

        result(LogType.KNOWNISSUE, message)
        return write(
            message = message(id = "knownIssue", arg1 = message, arg2 = ticketUrl),
            scriptCommand = "knownIssue",
            logType = LogType.KNOWNISSUE,
            result = LogType.KNOWNISSUE,
            log = log
        )
    }

    /**
     * scenario
     */
    fun scenario(testScenarioId: String, order: Int? = null, log: Boolean = true, desc: String? = null): LogLine {

        this.testScenarioId = testScenarioId
        val message = desc ?: testScenarioId
        lastScenarioLog = write(
            message = message,
            logType = LogType.SCENARIO,
            supplement = TestMode.manualSupplement,
            scriptCommand = "scenario",
            subject = testScenarioId,
            arg1 = message,
            arg2 = order?.toString() ?: "",
            log = log
        )

        return lastScenarioLog!!
    }

    /**
     * case
     */
    fun case(stepNo: Int, log: Boolean = true, desc: String? = null): LogLine {

        this.stepNo = stepNo
        val message = if (desc == null) "($stepNo)" else "($stepNo)$desc"
        lastCaseLog = write(
            message = message,
            logType = LogType.CASE,
            scriptCommand = "case",
            subject = testCaseId,
            arg1 = "$stepNo",
            log = log
        )

        return lastCaseLog!!
    }

    /**
     * condition
     */
    fun condition(message: String, log: Boolean = true): LogLine {

        return write(
            message = message,
            logType = LogType.CONDITION,
            scriptCommand = "condition",
            log = log
        )
    }

    /**
     * action
     */
    fun action(message: String, log: Boolean = true): LogLine {

        return write(
            message = message,
            logType = LogType.ACTION,
            scriptCommand = "action",
            log = log
        )
    }

    /**
     * target
     */
    fun target(targetName: String, log: Boolean = true): LogLine {

        return write(
            message = targetName,
            logType = LogType.TARGET,
            scriptCommand = "target",
            subject = targetName,
            log = log
        )
    }

    /**
     * expectation
     */
    fun expectation(message: String, log: Boolean = true): LogLine {

        return write(
            message = message,
            logType = LogType.EXPECTATION,
            scriptCommand = "expectation",
            log = log
        )
    }

    /**
     * select
     */
    fun select(
        message: String,
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = true
    ): LogLine {

        return write(
            message = message,
            logType = LogType.SELECT,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * boolean
     */
    fun boolean(
        message: String,
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = true
    ): LogLine {

        return write(
            message = message,
            logType = LogType.BOOLEAN,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * branch
     */
    fun branch(
        message: String,
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = true
    ): LogLine {

        return write(
            message = message,
            logType = LogType.BRANCH,

            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * operate
     */
    fun operate(
        message: String,
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        fileName: String = "",
        log: Boolean = true
    ): LogLine {

        return write(
            message = message,
            logType = LogType.OPERATE,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            fileName = fileName,
            log = log
        )
    }

    /**
     * check
     */
    fun check(
        message: String,
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = true
    ): LogLine {

        return write(
            message = message,
            logType = LogType.CHECK,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * procedure
     */
    fun procedure(
        message: String,
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = true
    ): LogLine {

        return write(
            message = message,
            logType = LogType.PROCEDURE,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * silent
     */
    fun silent(
        message: String,
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = true
    ): LogLine {

        return write(
            message = message,
            logType = LogType.SILENT,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * withScroll
     */
    fun withScroll(
        message: String,
        scriptCommand: String? = null,
        subject: String = "",
        arg1: String = "",
        arg2: String = "",
        log: Boolean = true
    ): LogLine {

        return write(
            message = message,
            logType = LogType.WITHSCROLL,
            scriptCommand = scriptCommand,
            subject = subject,
            arg1 = arg1,
            arg2 = arg2,
            log = log
        )
    }

    /**
     * caption
     */
    fun caption(message: String, log: Boolean = true): LogLine {

        var format = message(id = "caption", subject = "\${caption}")
        if (format.isBlank()) {
            format = "(\${caption})"
        }
        val msg = format.replace("\${caption}", message)

        return write(
            message = msg,
            logType = LogType.CAPTION,
            scriptCommand = "caption",
            subject = message,
            log = log
        )
    }

    /**
     * describe
     */
    fun describe(message: String, log: Boolean = true): LogLine {

        return write(
            message = message,
            logType = LogType.DESCRIBE,
            scriptCommand = "describe",
            subject = message,
            log = log
        )
    }

    /**
     * output
     */
    fun output(message: String, log: Boolean = true): LogLine {

        return write(
            message = message,
            logType = LogType.OUTPUT,
            scriptCommand = "output",
            subject = message,
            log = log
        )
    }

    /**
     * comment
     */
    fun comment(message: String, log: Boolean = true): LogLine {

        var format = message(id = "comment", subject = "\${comment}")
        if (format.isBlank()) {
            format = "// \${comment}"
        }
        val msg = format.replace("\${comment}", message)

        return write(
            message = msg,
            logType = LogType.COMMENT,
            scriptCommand = "comment",
            subject = message,
            log = log
        )
    }

    /**
     * clear
     */
    fun clear() {
        lines.clear()
        resetTestScenarioInfo()
        CodeExecutionContext.clear()
    }

    /**
     * toString
     */
    override fun toString(): String {

        val sb = StringBuilder()
        lines.forEach() {
            sb.append(it.toString())
        }

        return sb.toString()
    }

    /**
     * toTsvString
     */
    fun toTsvString(lines: List<LogLine>, commandList: Boolean = false): String {

        val sb = StringBuilder()
        if (commandList) {
            sb.appendLine(LogLine.getHeaderForCommandList())
            lines.forEach() {
                sb.appendLine(it.toStringForCommandList())
            }
        } else {
            sb.appendLine(LogLine.getHeaderForToString())
            lines.forEach() {
                sb.appendLine(it.toString())
            }
        }

        return sb.toString()
    }

    /**
     * get operator
     */
    operator fun get(index: Int): LogLine {

        if (index > lines.count() - 1) {
            throw IndexOutOfBoundsException()
        }
        return lines[index]
    }

    /**
     * outputLogTrace
     */
    fun outputLogTrace(format: LogFileFormat) {

        outputLogFile(
            filterName = "trace",
            logLines = lines,
            format = format
        )
    }

    /**
     * outputLogDetail
     */
    fun outputLogDetail(format: LogFileFormat) {

        val detailLines = lines.filter { it.isForDetail }.getRedundancyRemoved()
        outputLogFile(
            filterName = "detail",
            logLines = detailLines,
            format = format
        )
    }

    /**
     * outputLogSimple
     */
    fun outputLogSimple(format: LogFileFormat) {

        val simpleLines = lines.filter { it.isForSimple }.getRedundancyRemoved()
        outputLogFile(
            filterName = "simple",
            logLines = simpleLines,
            format = format
        )
    }

    /**
     * outputCommandList
     */
    fun outputCommandList() {

        val commandLines = lines.filter { it.isForCommandList && it.deleted == false }.getRedundancyRemoved()
        outputLogFile(
            filterName = "commandList",
            logLines = commandLines,
            format = LogFileFormat.Text,
            commandList = true
        )
    }

    /**
     * outputTextReplaceList
     */
    fun outputTextReplaceList() {

        val replaceList = lines.filter { it.message.contains(" is replaced to ") }
        outputLogFile(
            filterName = "textReplaceList",
            logLines = replaceList,
            format = LogFileFormat.Text,
            commandList = true
        )
    }

    /**
     * outputLogFile
     */
    fun outputLogFile(
        filterName: String,
        logLines: List<LogLine>,
        format: LogFileFormat,
        commandList: Boolean = false
    ) {
        var dt = Date()
        if (logLines.isNotEmpty()) {
            dt = logLines[0].logDateTime
        }
        val dateLabel = SimpleDateFormat("yyyyMMddHHmmss").format(dt)

        val dir = directoryForLog.toFile()
        if (dir.exists().not()) {
            dir.mkdirs()
        }

        try {
            when (format) {

                LogFileFormat.Text -> {
                    if (commandList) {
                        File("$directoryForLog/TestLog(${filterName})_$dateLabel.log")
                            .writeText(
                                toTsvString(
                                    lines = logLines,
                                    commandList = true
                                )
                            )
                    } else {
                        File("$directoryForLog/TestLog(${filterName})_$dateLabel.log")
                            .writeText(
                                toTsvString(
                                    logLines
                                )
                            )
                    }
                }

                LogFileFormat.Html -> {
                    val reportFileName = "$directoryForLog/_Report(${filterName})_$dateLabel.html"
                    TestReport(
                        filterName = filterName,
                        fileName = reportFileName,
                        lines = logLines
                    ).writeHtml()
                    createOrUpdateTestReportIndex(
                        filterName = filterName,
                        inputReportFileName = reportFileName,
                        logLines = logLines
                    )
                }
            }

        } catch (e: Exception) {
            throw TestDriverException("failed to output log file. (${e})")
        }
    }

    internal fun createOrUpdateTestReportIndex(
        filterName: String,
        inputReportFileName: String? = null,
        logLines: List<LogLine> = listOf()
    ) {
        val reportIndexFilePath = directoryForReportIndex.resolve("_ReportIndex($filterName).html")

        lockFile(filePath = reportIndexFilePath) {
            val tri = TestReportIndex(
                indexFilePath = reportIndexFilePath,
                isNoLoadRun = TestMode.isNoLoadRun
            )
            if (inputReportFileName != null) {
                tri.add(fileName = inputReportFileName, logLines = logLines)
            }
            tri.writeFile()
        }
    }

    /**
     * outputSpecReport
     */
    fun outputSpecReport() {

        if (enableSpecReport.not()) {
            return
        }

        SpecReportExecutor(inputDirPath = directoryForLog).execute()
    }

    /**
     * outputTestList
     */
    fun outputTestList() {

        if (enableTestList.not()) {
            return
        }

        val testListPath = directoryForLog.resolve("TestList_${testConfigName}.xlsx")
        lockFile(testListPath) {
            TestListReport()
                .loadFileOnExist(testListPath = testListPath)
                .mergeLogLines(this.lines)
                .outputXlsx(outputTestListPath = testListPath)
        }

        /**
         * Merge into TestList.xlsx
         */
        if (Files.exists(directoryForTestList).not()) {
            directoryForTestList.toFile().mkdirs()
        }
        val mergedTestListPath = directoryForTestList.resolve("TestList.xlsx")
        lockFile(mergedTestListPath) {
            TestListReport()
                .mergeOutput(sourceTestListPath = testListPath, outputTestListPath = mergedTestListPath)
        }
    }

    /**
     * outputTestClassList
     */
    fun outputTestClassList() {

        if (enableTestClassList.not()) {
            return
        }

        val testClassListPath = testResults.resolve("TestClassList.txt")
        TestClassListReport()
            .loadFileOnExist(testClassListPath = testClassListPath)
            .merge(currentTestClass!!.name)
            .output(outputTestClassListPath = testClassListPath)

        /**
         * Copy the TestClassList to testListDir and merge if testListDir is defined.
         */
        val testListDir = PropertiesManager.testListDir
        if (testListDir.isNotBlank()) {
            if (Files.exists(testListDir.toPath()).not()) {
                throw FileNotFoundException(message(id = "testListDirNotFound", file = testListDir))
            }
            val targetPath = testListDir.toPath().resolve(testClassListPath.fileName)
            TestClassListReport()
                .loadFileOnExist(testClassListPath = targetPath)
                .merge(line = currentTestClass!!.name)
                .output(outputTestClassListPath = targetPath)
        }

    }

    /**
     * resetTestScenarioInfo
     */
    fun resetTestScenarioInfo() {

        testScenarioId = ""
        stepNo = null
        commandStack.clear()

        lastScenarioLog = null
        lastCaseLog = null
    }

    /**
     * getLinesOfTestScenario
     */
    fun getLinesOfTestScenario(testScenarioId: String): List<LogLine> {

        return lines.filter { it.testScenarioId == testScenarioId }
    }

    /**
     * getLinesOfCurrentTestScenario
     */
    fun getLinesOfCurrentTestScenario(): List<LogLine> {

        return lines.filter { it.testScenarioId == testScenarioId }
    }

    /**
     * getLinesOfCurrentTestCaseId
     */
    fun getLinesOfCurrentTestCaseId(): List<LogLine> {

        return lines.filter { it.testScenarioId == testScenarioId && it.stepNo == stepNo }
    }

    /**
     * getTestCaseIds
     */
    fun getTestCaseIds(lines: List<LogLine> = this.lines): List<String> {

        return lines.groupBy { it.testCaseId }.map { it.key }.sortedBy { it }
    }

    /**
     * getLogDirectoryUrl
     */
    fun getLogDirectoryUrl(): String {

        val logDir = "$directoryForLog/".replace(File.separator, "/")
        return "file:///$logDir"
    }

    /**
     * outputLogDump
     */
    fun outputLogDump() {

        directoryForLog.resolve("${lines.count()}_LogDump.txt")
            .toFile().writeText(lines.joinToString("\n"))

    }
}