package shirates.core.logging

fun List<LogLine>.getParameter(parameterName: String): String {

    return this.firstOrNull() { it.scriptCommand == "parameter" && it.message.startsWith("$parameterName:") }
        ?.message?.replace("$parameterName:", "")?.trim() ?: ""
}