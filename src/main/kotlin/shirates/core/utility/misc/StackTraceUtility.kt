package shirates.core.utility.misc

/**
 * StackTraceUtility
 */
class StackTraceUtility {

    /**
     * companion object
     */
    companion object {

        /**
         * getLineOfClass
         */
        fun getLineOfClass(error: Throwable, className: String? = null): String {

            val cn = if (className == null)
                error.stackTrace.first().className
            else
                className

            val stackTrace = error.stackTrace.first { it.className == cn }

            return stackTrace.toString()
        }

        /**
         * getLinesOfThrowerClass
         */
        fun getLinesOfThrowerClass(error: Throwable): String {

            val className = error.stackTrace.first().className
            val stackTrace = error.stackTrace
            val sb = StringBuilder()
            stackTrace.forEach {
                val cn = it.className
                if (cn.startsWith(className)) {
                    sb.appendLine(it)
                }
            }

            return sb.toString()
        }

        /**
         * getLines
         */
        fun getLines(error: Throwable): String {

            val stackTrace = error.stackTrace
            val sb = StringBuilder()
            stackTrace.forEach {
                sb.appendLine(it)
            }

            return sb.toString()
        }

        /**
         * getCallerName
         */
        fun getCallerName(
            filterFileName: String = "StackTraceUtility.kt",
            filterMethodName: String = "getCallerName"
        ): String {

            val stacktrace = Thread.currentThread().stackTrace
            val thisStack = stacktrace
                .filter {
                    it.fileName == filterFileName
                            && (it.methodName == filterMethodName || it.methodName == "$filterMethodName\$default")
                }.lastOrNull()
            if (thisStack == null) {
                throw IllegalAccessException()
            }
            val callerStack = stacktrace[stacktrace.indexOf(thisStack) + 1]
            val className = callerStack.className.split(".").last()
            val methodName = callerStack.methodName
            val classAndMethodName = "$className.$methodName"

            return classAndMethodName
        }
    }

}