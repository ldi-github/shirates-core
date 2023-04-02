package shirates.core.driver

import shirates.core.configuration.Filter
import shirates.core.configuration.Selector
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.relative
import shirates.core.utility.image.ImageMatchSizeFilterStrategy
import shirates.core.utility.image.ImageMatchSortStrategy
import shirates.core.utility.image.ImageMatchSortStrategy.*
import java.awt.image.BufferedImage

/**
 * filterBySelector
 */
fun List<TestElement>.filterBySelector(
    selector: Selector,
    throwsException: Boolean = false
): MutableList<TestElement> {

    if (selector.pos == 0) {
        throw IndexOutOfBoundsException("pos can not be zero.")
    }

    val selectors = mutableListOf<Selector>()
    selectors.add(selector)
    selectors.addAll(selector.orSelectors)

    val filtered = mutableListOf<TestElement>()
    for (sel in selectors) {
        // select
        val list = filterBySelectorCore(list = this, selector = sel)
        if (list.isEmpty() && selector.isNegation) {
            filtered.add(TestElement.dummyElement)
        }
        for (e in list) {
            // get relative
            if (selector.relativeSelectors.any()) {
                val exps = selector.expandRelativeExpressions()
                var relative = e
                for (exp in exps) {
                    relative = relative.relative(command = exp, scopeElements = this)
                }
                relative.selector = selector
                if (relative.isEmpty.not() && filtered.contains(relative).not()) {
                    filtered.add(relative)
                }
            } else {
                if (filtered.contains(e).not()) {
                    filtered.add(e)
                }
            }
        }
    }

    if (selector.pos == null) {
        return filtered
    }

    val positionFiltered = mutableListOf<TestElement>()
    val pos = selector.pos!!
    if (pos < 0) {
        val count = filtered.count()
        val newPos = count + 1 + pos
        if (newPos < 1 || count < newPos) {
            if (throwsException) {
                throw IndexOutOfBoundsException("selector.pos out of range.(pos=$pos, count=$count)")
            } else {
                return mutableListOf()
            }
        }
        val ix = newPos - 1
        positionFiltered.add(filtered[ix])
        return positionFiltered
    } else {
        val count = filtered.count()
        if (pos > count) {
            if (throwsException) {
                throw IndexOutOfBoundsException("selector.pos out of range.(pos=$pos, count=$count)")
            } else {
                return mutableListOf()
            }
        }
        val ix = pos - 1
        positionFiltered.add(filtered[ix])
        return positionFiltered
    }
}

private fun filterBySelectorCore(
    list: List<TestElement>,
    selector: Selector,
): MutableList<TestElement> {

    var result = list

    for (filter in selector.filterMap.values) {
        when (filter.noun) {
            "pos" -> {
                // pos is processed later.
            }

            "visible" -> {
                // visible is processed later.
            }

            else -> {
                result = result.filter { e ->
                    val m = filter.evaluate(element = e)
                    m
                }
            }
        }
    }

    /**
     * Note:
     *
     * ignoreTypes applied if className is not specified.
     * In iOS XCUIElementTypeCell is ignored by default because it is noisy for selecting label.
     *
     * ex.
    <XCUIElementTypeCell type="XCUIElementTypeCell" label="General">
    __<XCUIElementTypeStaticText type="XCUIElementTypeStaticText" label="General" value="General">
    __</XCUIElementTypeStaticText>
    </XCUIElementTypeCell>
     *
     * select("General") ... XCUIElementTypeStaticText matches. className is not specified.
     * select(.XCUIElementTypeCell&&"General") ... XCUIElementTypeCell matches. className is specified.
     *
     */
    if ((selector.filterMap.containsKey("className")).not()) {
        result = result.filter {
            val m = Filter.isNotIgnoreTypes(
                classOrType = it.classOrType,
                ignoreTypes = selector.ignoreTypes
            )
            m
        }
    }

    if (isiOS) {
        result = result.filter { e ->
            val m = Filter.matchVisible(element = e, selector = selector)
            m
        }
    }

    return result as MutableList<TestElement>
}

/**
 * filterBySizeStrategy
 */
@Deprecated("This function is not used in framework currently.")
fun List<TestElement>.filterBySizeStrategy(
    sizeFilterStrategy: ImageMatchSizeFilterStrategy,
    templateImage: BufferedImage
): List<TestElement> {

    if (sizeFilterStrategy == ImageMatchSizeFilterStrategy.none) {
        return this
    }

    val templareArea = templateImage.width * templateImage.height
    val ratio = sizeFilterStrategy.getRatio()
    val threshold = templareArea * ratio

    val result = this.filter { item ->
        val area = item.bounds.toScaledRect().area()
        area <= threshold
    }
    return result
}

/**
 * sortByStrategy
 */
@Deprecated("This function is not used in framework currently.")
fun List<TestElement>.sortByStrategy(
    sortStrategy: ImageMatchSortStrategy,
    templateImage: BufferedImage? = null
): List<TestElement> {

    if (this.count() == 0) {
        return this
    }

    when (sortStrategy) {
        SizeSmall -> {
            return this.sortedBy { it.bounds.area }
        }

        SizeLarge -> {
            return this.sortedByDescending { it.bounds.area }
        }

        SizeNear -> {
            if (templateImage == null) {
                throw IllegalArgumentException("templateImage required on sizeNear storategy.")
            }
            val area = templateImage.width * templateImage.height
            return this.sortedBy { Math.abs(area - it.bounds.area) }
        }

        TopLeft -> {
            return this.sortedBy { it.bounds.x1 + it.bounds.y1 * 10000 }
        }

        BottomRight -> {
            return this.sortedByDescending { it.bounds.x1 + it.bounds.y1 * 10000 }
        }
    }
}

/**
 * filterByPointIncluded
 */
fun List<TestElement>.filterByPointIncluded(
    x: Int,
    y: Int
): List<TestElement> {

    return this.filter { it.bounds.includesPoint(x = x, y = y) }
}
