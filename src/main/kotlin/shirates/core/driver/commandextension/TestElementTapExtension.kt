package shirates.core.driver.commandextension

import shirates.core.driver.TestElement

/**
 * tapOffset
 */
fun TestElement.tapOffset(
    offsetX: Int = 0,
    offsetY: Int = 0
): TestElement {
    val bounds = this.bounds
    var x =
        if (offsetX == 0) bounds.centerX
        else if (offsetX < 0) bounds.left
        else bounds.right
    x += offsetX

    var y =
        if (offsetY == 0) bounds.centerY
        else if (offsetY < 0) bounds.top
        else bounds.bottom
    y += offsetY

    return tap(x = x, y = y)
}
