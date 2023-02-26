package shirates.core.report

internal object TestReportUtility {

    fun getShortenMessageWithEllipsis(message: String, maxLength: Int = 120): String {
        if (message.length <= maxLength) {
            return message
        }

        val shortenMessage = message.substring(0, 120).trim() + "..."
        return shortenMessage
    }

}