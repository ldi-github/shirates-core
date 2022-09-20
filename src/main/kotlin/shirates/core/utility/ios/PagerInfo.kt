package shirates.core.utility.ios

/**
 * PagerInfo
 */
class PagerInfo(val text: String) {

    var value1 = 0
    var value2 = 0

    val lastPageNumber: Int
        get() {
            return Math.max(value1, value2)
        }

    val isFirstPage: Boolean
        get() {
            return (value1 == 1 || value2 == 1)
        }

    val isLastPage: Boolean
        get() {
            return value1 == value2
        }

    init {
        parse(text)
    }

    private fun parse(value: String) {

        val result = "[^0-9]*(\\d+)[^0-9]*(\\d+)[^0-9]*".toRegex().matchEntire(value)
        if (result?.groupValues?.count() == 3) {
            value1 = result.groupValues.get(1).toInt()
            value2 = result.groupValues.get(2).toInt()
        }
    }

}