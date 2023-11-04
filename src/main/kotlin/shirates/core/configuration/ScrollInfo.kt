package shirates.core.configuration

import shirates.core.driver.commandextension.select
import shirates.core.driver.rootBounds
import shirates.core.driver.testDrive

/**
 * ScrollInfo
 */
class ScrollInfo {
    var scrollable: String = ""
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

        if (scrollable.isBlank()) {
            scrollable = scrollInfo.scrollable
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
        val headerElements = headerElements.map { testDrive.select(expression = it) }
        val sortedElements = headerElements.sortedBy { it.bounds.bottom }
        val headerBottom = sortedElements.lastOrNull()?.bounds?.bottom ?: statBarHeight
        return headerBottom
    }

    /**
     * getFooterTop
     */
    fun getFooterTop(): Int {
        val footerElements = footerElements.map { testDrive.select(expression = it) }
        val sortedElements = footerElements.sortedBy { it.bounds.top }
        val footerTop = sortedElements.firstOrNull()?.bounds?.top ?: (rootBounds.bottom + 1)
        return footerTop
    }
}