package shirates.core.driver

class TestDriverEventContext {

    var irregularHandler: (() -> Unit)? = null
    var onLaunchHandler: (() -> Unit)? = null
}