package shirates.spec.utilily

/**
 * getGroupValue
 */
fun String.getGroupValue(regex: Regex): String {

    val matchResult = regex.matchEntire(this)
    if (matchResult != null) {
        if (matchResult.groupValues.count() >= 1) {
            val value1 = matchResult.groupValues.get(1)
            return value1
        }
    }
    return ""
}