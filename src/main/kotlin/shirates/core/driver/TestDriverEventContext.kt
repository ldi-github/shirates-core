package shirates.core.driver

class TestDriverEventContext {

    var irregularHandler: (() -> Unit)? = null
    var onLaunchHandler: (() -> Unit)? = null
    var onSelectErrorHandler: (() -> Unit)? = null
    var onExistErrorHandler: (() -> Unit)? = null
    var onScreenErrorHandler: (() -> Unit)? = null
    var onRequestingRerunHandler: ((t: Throwable) -> Boolean)? = null
    var onRerunScenarioHandler: ((t: Throwable) -> Unit)? = null

}