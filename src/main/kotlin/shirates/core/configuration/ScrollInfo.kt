package shirates.core.configuration

import shirates.core.driver.TestElementCache
import shirates.core.driver.viewBounds

/**
 * ScrollInfo
 */
class ScrollInfo {
    var scrollFrame: String = ""
    val headerElements = mutableListOf<String>()
    val footerElements = mutableListOf<String>()
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

        if (scrollFrame.isBlank()) {
            scrollFrame = scrollInfo.scrollFrame
        }
        headerElements.merge(scrollInfo.headerElements)
        footerElements.merge(scrollInfo.footerElements)
        startElements.merge(scrollInfo.startElements)
        endElements.merge(scrollInfo.endElements)
        overlayElements.merge(scrollInfo.overlayElements)
    }

    /**
     * getHeaderBottom
     */
    fun getHeaderBottom(): Int {
        val statBarHeight = PropertiesManager.statBarHeight
        val headerElements = headerElements.map { TestElementCache.select(expression = it, throwsException = false) }
            .filter { it.isFound }
        val sortedElements = headerElements.sortedBy { it.bounds.bottom }
        val headerBottom = sortedElements.lastOrNull()?.bounds?.bottom ?: statBarHeight
        return headerBottom
    }

    /**
     * getFooterTop
     */
    fun getFooterTop(): Int {
        val footerElements = footerElements.map { TestElementCache.select(expression = it, throwsException = false) }
            .filter { it.isFound }
        val sortedElements = footerElements.sortedBy { it.bounds.top }
        val footerTop = sortedElements.firstOrNull()?.bounds?.top ?: (viewBounds.bottom + 1)
        return footerTop
    }

    internal fun getCoveringElements(): List<String> {

        val list = mutableListOf<String>()
        list.addAll(overlayElements)
        list.addAll(headerElements)
        list.addAll(footerElements)
        return list
    }

}