package shirates.core.driver

import shirates.core.Const
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.eventextension.TestDriverOnScreenContext
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.utility.misc.UrlUtility
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

class TestContext(
    val profile: TestProfile = TestProfile()
) {

    // Appium Proxy --------------------------------------------------

    @SaveTarget
    var appiumProxyReadTimeoutSeconds =
        profile.appiumProxyReadTimeoutSeconds?.toDoubleOrNull() ?: Const.APPIUM_PROXY_READ_TIMEOUT_SECONDS

    @SaveTarget
    var appiumProxyGetSourceTimeoutSeconds =
        profile.appiumProxyReadTimeoutSeconds?.toDoubleOrNull() ?: Const.APPIUM_PROXY_GET_SOURCE_TIMEOUT_SECONDS

    // TestDriver --------------------------------------------------

    @SaveTarget
    var reuseDriver = profile.reuseDriver?.toBoolean() ?: Const.REUSE_DRIVER

    @SaveTarget
    var retryMaxCount = profile.retryMaxCount?.toLongOrNull() ?: Const.RETRY_MAX_COUNT

    @SaveTarget
    var retryTimeoutSeconds = profile.retryTimeoutSeconds?.toDoubleOrNull() ?: Const.RETRY_TIMEOUT_SECONDS

    @SaveTarget
    var retryIntervalSeconds =
        profile.retryIntervalSeconds?.toDoubleOrNull() ?: Const.RETRY_INTERVAL_SECONDS

    // Screenshot --------------------------------------------------

    @SaveTarget
    var autoScreenshot = profile.autoScreenshot?.toBoolean() ?: Const.AUTO_SCREEN_SHOT

    @SaveTarget
    var onChangedOnly = profile.onChangedOnly?.toBoolean() ?: Const.ON_CHANGED_ONLY

    @SaveTarget
    var onCondition = profile.onCondition?.toBoolean() ?: Const.ON_CONDITION

    @SaveTarget
    var onAction = profile.onAction?.toBoolean() ?: Const.ON_ACTION

    @SaveTarget
    var onExpectation = profile.onExpectation?.toBoolean() ?: Const.ON_EXPECTATION

    @SaveTarget
    var onExecOperateCommand = profile.onExecOperateCommand?.toBoolean() ?: Const.ON_EXEC_OPERATE_COMMAND

    @SaveTarget
    var onCheckCommand = profile.onCheckCommand?.toBoolean() ?: Const.ON_CHECK_COMMAND

    @SaveTarget
    var onScrolling = profile.onScrolling?.toBoolean() ?: Const.ON_SCROLLING

    @SaveTarget
    var manualScreenshot = profile.manualScreenshot?.toBoolean() ?: Const.MANUAL_SCREENSHOT


    // App operation --------------------------------------------------

    val appIconName: String
        get() {
            return profile.appIconName ?: ""
        }

    val tapAppIconMethod: TapAppIconMethod
        get() {
            if (profile.tapAppIconMethod.isNullOrBlank()) {
                return TapAppIconMethod.auto
            }
            try {
                return TapAppIconMethod.valueOf(profile.tapAppIconMethod!!)
            } catch (t: Throwable) {
                throw TestConfigException(
                    message(
                        id = "tapAppIconMethodIsInvalid",
                        value = profile.tapAppIconMethod,
                        arg1 = "${TapAppIconMethod.values().toList()}"
                    )
                )
            }
        }

    val tapAppIconMacro: String
        get() {
            return profile.tapAppIconMacro ?: ""
        }

    @SaveTarget
    var shortWaitSeconds = profile.shortWaitSeconds?.toDoubleOrNull() ?: Const.SHORT_WAIT_SECONDS

    @SaveTarget
    var waitSecondsForLaunchAppComplete =
        profile.waitSecondsForLaunchAppComplete?.toDoubleOrNull()
            ?: Const.WAIT_SECONDS_FOR_LAUNCH_APP_COMPLETE

    @SaveTarget
    var waitSecondsForAnimationComplete =
        profile.waitSecondsForAnimationComplete?.toDoubleOrNull()
            ?: Const.WAIT_SECONDS_FOR_ANIMATION_COMPLETE

    @SaveTarget
    var waitSecondsOnIsScreen =
        profile.waitSecondsOnIsScreen?.toDoubleOrNull() ?: Const.WAIT_SECONDS_ON_ISSCREEN

    @SaveTarget
    var waitSecondsForConnectionEnabled =
        profile.waitSecondsForConnectionEnabled?.toDoubleOrNull()
            ?: Const.WAIT_SECONDS_FOR_CONNECTION_ENABLED

    @SaveTarget
    var swipeDurationSeconds =
        profile.swipeDurationSeconds?.toDoubleOrNull() ?: Const.SWIPE_DURATION_SECONDS

    @SaveTarget
    var flickDurationSeconds =
        profile.flickDurationSeconds?.toDoubleOrNull() ?: Const.FLICK_DURATION_SECONDS

    @SaveTarget
    var swipeMarginRatio = profile.swipeMarginRatio?.toDoubleOrNull() ?: Const.SWIPE_MARGIN_RATIO

    @SaveTarget
    var scrollVerticalStartMarginRatio =
        profile.scrollVerticalStartMarginRatio?.toDoubleOrNull() ?: Const.SCROLL_VERTICAL_START_MARGIN_RATIO

    @SaveTarget
    var scrollVerticalEndMarginRatio =
        profile.scrollVerticalEndMarginRatio?.toDoubleOrNull() ?: Const.SCROLL_VERTICAL_END_MARGIN_RATIO

    @SaveTarget
    var scrollHorizontalStartMarginRatio =
        profile.scrollHorizontalStartMarginRatio?.toDoubleOrNull() ?: Const.SCROLL_HORIZONTAL_START_MARGIN_RATIO

    @SaveTarget
    var scrollHorizontalEndMarginRatio =
        profile.scrollHorizontalEndMarginRatio?.toDoubleOrNull() ?: Const.SCROLL_HORIZONTAL_END_MARGIN_RATIO

    @SaveTarget
    var scrollToEdgeBoost = profile.scrollToEdgeBoost?.toIntOrNull() ?: Const.SCROLL_TO_EDGE_BOOST

    @SaveTarget
    var scrollIntervalSeconds = profile.scrollIntervalSeconds?.toDoubleOrNull() ?: Const.SCROLL_INTERVAL_SECONDS

    @SaveTarget
    var flickIntrvalSeconds = Const.FLICK_INTERVAL_SECONDS

    @SaveTarget
    var scrollMaxCount = profile.scrollMaxCount?.toIntOrNull() ?: Const.SCROLL_MAX_COUNT

    @SaveTarget
    var tapHoldSeconds = profile.tapHoldSeconds?.toDoubleOrNull() ?: Const.TAP_HOLD_SECONDS

    @SaveTarget
    var enableCache = profile.enableCache?.toBoolean() ?: Const.ENABLE_CACHE

    @SaveTarget
    var forceUseCache = false

    /**
     * useCache
     */
    val useCache: Boolean
        get() {
            if (forceUseCache) {
                return true
            }
            return enableCache
        }

    @SaveTarget
    var findWebElementTimeoutMillisecond =
        ((profile.findWebElementTimeoutSeconds?.toDoubleOrNull() ?: Const.FIND_WEB_ELEMENT_TIMEOUT_SECONDS) * 1000)
            .toInt()

    @SaveTarget
    var syncWaitSeconds = profile.syncWaitSeconds?.toDoubleOrNull() ?: Const.SYNC_WAIT_SECONDS

    @SaveTarget
    var syncMaxLoopCount = profile.syncMaxLoopCount?.toIntOrNull() ?: Const.SYNC_MAX_LOOP_COUNT

    @SaveTarget
    var syncIntervalSeconds =
        profile.syncMaxLoopCount?.toDoubleOrNull() ?: Const.SYNC_INTERVAL_SECONDS


    // misc --------------------------------------------------

    val isEmpty: Boolean
        get() {
            return profile.isEmpty
        }

    val isLocalServer: Boolean
        get() {
            return UrlUtility.isLocal(testProfile.appiumServerUrl)
        }

    val isRemoteServer: Boolean
        get() {
            return UrlUtility.isRemote(testProfile.appiumServerUrl)
        }

    @SaveTarget
    var boundsToRectRatio = profile.boundsToRectRatio?.toIntOrNull()
        ?: (if (isAndroid) Const.BOUNDS_TO_RECT_RATIO_ANDROID else Const.BOUNDS_TO_RECT_RATIO_IOS)

    val platformName: String
        get() {
            return TestMode.testTimePlatformName ?: profile.platformName
        }

    @SaveTarget
    var irregularHandler: (() -> Unit)? = null

    @SaveTarget
    var onLaunchHandler: (() -> Unit)? = null

    @SaveTarget
    var onSelectErrorHandler: (() -> Unit)? = null

    @SaveTarget
    var onExistErrorHandler: (() -> Unit)? = null

    @SaveTarget
    var onScreenErrorHandler: (() -> Unit)? = null

    @SaveTarget
    var isRerunRequested: ((t: Throwable) -> Boolean)? = null

    @SaveTarget
    var onRerunScenarioHandler: ((t: Throwable) -> Unit)? = null

    @SaveTarget
    var onRefreshCurrentScreenHandler: (() -> Unit)? = null

    val screenHandlers = mutableMapOf<String, ((TestDriverOnScreenContext) -> Unit)?>()

    @SaveTarget
    var enableIrregularHandler = true

    @SaveTarget
    var enableScreenHandler = true

    internal val saveTargetProperties = this::class.memberProperties.filterIsInstance<KMutableProperty<*>>()
        .filter {
            it.visibility == KVisibility.PUBLIC
                    && it.annotations.any() { a -> a is SaveTarget }
        }

    internal val saveData = mutableMapOf<String, Any?>()

    /**
     * saveState
     */
    fun saveState(): MutableMap<String, Any?> {

        val data = mutableMapOf<String, Any?>()
        saveTargetProperties.forEach {
            data[it.name] = it.getter.call(this)
        }
        saveData.clear()
        saveData.putAll(data)
        return data
    }

    /**
     * resumeState
     */
    fun resumeState(resumeData: MutableMap<String, Any?>? = null) {

        val data = resumeData ?: saveData

        saveTargetProperties.forEach {
            if (data.containsKey(it.name)) {
                it.setter.call(this, data[it.name])
            }
        }
    }

    /**
     * getScrollStartMarginRatio
     */
    fun getScrollStartMarginRatio(direction: ScrollDirection): Double {

        return if (direction.isVertical) scrollVerticalStartMarginRatio else scrollHorizontalStartMarginRatio
    }

    /**
     * getScrollEndMarginRatio
     */
    fun getScrollEndMarginRatio(direction: ScrollDirection): Double {

        return if (direction.isVertical) scrollVerticalEndMarginRatio else scrollHorizontalEndMarginRatio
    }

    /**
     * getIntervalSeconds
     */
    fun getIntervalSeconds(flick: Boolean = false): Double {

        if (flick) {
            return flickIntrvalSeconds
        } else {
            return scrollIntervalSeconds
        }
    }
}