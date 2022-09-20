package shirates.core.configuration

/**
 * ScrollInfo
 */
class ScrollInfo {
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

        startElements.merge(scrollInfo.startElements)
        endElements.merge(scrollInfo.endElements)
        overlayElements.merge(scrollInfo.overlayElements)
    }
}