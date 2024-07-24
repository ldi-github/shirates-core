package shirates.core.logging

fun List<LogLine>.getRedundancyRemoved(): List<LogLine> {

    var before = this
    while (true) {
        val after = getRedundancyRemovedCore(lines = before)
        if (after.count() == before.count()) {
            return after
        }
        before = after
    }
}

private fun getRedundancyRemovedCore(
    lines: List<LogLine>
): MutableList<LogLine> {

    val resultLines = mutableListOf<LogLine>()

    val lastIndex = lines.size - 1
    for (i in 0..lastIndex) {
        val line = lines[i]
        var canAdd = true
        if (line.message.trim().endsWith("{")) {
            if (i != lastIndex) {
                val nextLine = lines[i + 1]
                if (nextLine.message.trim().startsWith("}")) {
                    canAdd = false
                }
            }
        } else if (line.message.trim().startsWith("}")) {
            if (i != 0) {
                val lastLine = lines[i - 1]
                if (lastLine.message.trim().endsWith("{")) {
                    canAdd = false
                }
            }
        }
        if (canAdd) {
            resultLines.add(line)
        }
    }
    return resultLines
}