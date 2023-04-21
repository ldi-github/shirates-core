package shirates.core.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.testContext

fun <T> TestDrive.withContext(
    useCache: Boolean? = null,
    useHandler: Boolean? = null,
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
        if (useHandler != null) {
            if (useHandler) {
                testContext.enableIrregularHandler = true
            } else {
                testContext.enableIrregularHandler = false
            }
        }

        return func()
    } finally {
        if (useHandler != null) {
            val blockName = if (useHandler) "useHandler" else "suppressHandler"
            context.enableIrregularHandler = original.enableIrregularHandler
        }
        if (useCache != null) {
            val blockName = if (useCache) "useCache" else "suppressCache"
            context.forceUseCache = original.forceUseCache
            context.enableCache = original.enableCache
        }
    }
}