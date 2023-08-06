package shirates.core.configuration

/**
 * ScrollInfo
 */
class ScrollInfo {
    var scrollable: String = ""
    val startElements = mutableListOf<String>()
    val endElements = mutableListOf<String>()
    val overlayElements = mutableListOf<String>()

    private fun MutableList<String>.merge(list: List<String>) {

        for (e in list) {
            if (this.contains(e).not()) {
                this.add(e)
            }
        }
    }

    /**
     * importFrom
     */
    fun importFrom(scrollInfo: ScrollInfo) {

        if (scrollable.isBlank()) {
            scrollable = scrollInfo.scrollable
        }
        startElements.merge(scrollInfo.startElements)
        endElements.merge(scrollInfo.endElements)
        overlayElements.merge(scrollInfo.overlayElements)
    }
}