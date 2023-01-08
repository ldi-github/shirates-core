package shirates.core.report

internal object TestReportUtility {

    fun getShortenMessageWithEllipsis(message: String): String {
        if (message.length <= 120) {
            return message
        }

        val shortenMessage = message.substring(0, 120).trim() + "..."
        return shortenMessage
    }

}