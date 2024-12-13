package shirates.core.driver.commandextension

import shirates.core.driver.Drive
import shirates.core.driver.testContext

fun <T> Drive.withContext(
    useCache: Boolean? = null,
    func: () -> T
): T {
    val context = testContext
    val original = object {
        val forceUseCache = context.forceUseCache
        val enableCache = context.enableCache
        val enableIrregularHandler = context.enableIrregularHandler
    }

    try {
        if (useCache != null) {
            if (useCache) {
                context.forceUseCache = true
            } else {
                context.forceUseCache = false
                context.enableCache = false
            }
        }

        return func()
    } finally {
        if (useCache != null) {
            val blockName = if (useCache) "useCache" else "suppressCache"
            context.forceUseCache = original.forceUseCache
            context.enableCache = original.enableCache
            context.enableIrregularHandler = original.enableIrregularHandler
        }
    }
}