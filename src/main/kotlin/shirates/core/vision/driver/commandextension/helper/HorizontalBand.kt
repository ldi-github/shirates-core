package shirates.core.vision.driver.commandextension.helper

class HorizontalBand(
    internal var top: Int,
    internal var bottom: Int,
) {

    internal val members = mutableListOf<IRect>()

    constructor(
        baseElement: IRect,
    ) : this(
        top = baseElement.getRectInfo().top,
        bottom = baseElement.getRectInfo().bottom,
    ) {
        merge(element = baseElement)
    }

    /**
     * canMerge
     */
    fun canMerge(
        element: IRect,
        margin: Int = 0
    ): Boolean {

        if (members.isEmpty()) {
            return true
        }
        val elementRect = element.getRectInfo()
        if (elementRect.bottom <= this.top - margin) {
            return false
        }
        if (this.bottom + margin <= elementRect.top) {
            return false
        }
        val s1 = element.toString()
        val contains = members.any() { it.toString() == s1 }
        if (contains) {
            return false
        }
        return true
    }

    private fun refreshTopAndBottom() {
        top = members.map { it.getRectInfo().top }.minOrNull() ?: 0
        bottom = members.map { it.getRectInfo().bottom }.maxOrNull() ?: 0
    }

    /**
     * merge
     */
    fun merge(
        element: IRect,
        margin: Int = 0
    ): Boolean {

        if (canMerge(element = element, margin = margin).not()) {
            return false
        }

        members.add(element)
        members.sortWith(compareBy<IRect> { it.getRectInfo().left }
            .thenBy { it.getRectInfo().top }
            .thenBy { -it.getRectInfo().area })
        refreshTopAndBottom()
        return true
    }

    /**
     * getElements
     */
    fun getElements(): MutableList<IRect> {
        return members.toMutableList()
    }

}