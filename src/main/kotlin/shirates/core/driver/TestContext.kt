package shirates.core.driver

import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

class TestContext(
    val profile: TestProfile = TestProfile()
) {

    @SaveTarget
    var boundsToRectRatio = profile.boundsToRectRatio?.toIntOrNull()
        ?: (if (isAndroid) shirates.core.Const.BOUNDS_TO_RECT_RATIO_ANDROID else shirates.core.Const.BOUNDS_TO_RECT_RATIO_IOS)

    @SaveTarget
    var reuseDriver = profile.reuseDriver?.toBoolean() ?: shirates.core.Const.REUSE_DRIVER

    @SaveTarget
    var autoScreenshot = profile.autoScreenshot?.toBoolean() ?: shirates.core.Const.AUTO_SCREEN_SHOT

    @SaveTarget
    var onChangedOnly = profile.onChangedOnly?.toBoolean() ?: shirates.core.Const.ON_CHANGED_ONLY

    @SaveTarget
    var onCondition = profile.onCondition?.toBoolean() ?: shirates.core.Const.ON_CONDITION

    @SaveTarget
    var onAction = profile.onAction?.toBoolean() ?: shirates.core.Const.ON_ACTION

    @SaveTarget
    var onExpectation = profile.onExpectation?.toBoolean() ?: shirates.core.Const.ON_EXPECTATION

    @SaveTarget
    var onExecOperateCommand = profile.onExecOperateCommand?.toBoolean() ?: shirates.core.Const.ON_EXEC_OPERATE_COMMAND

    @SaveTarget
    var onCheckCommand = profile.onCheckCommand?.toBoolean() ?: shirates.core.Const.ON_CHECK_COMMAND

    @SaveTarget
    var onScrolling = profile.onScrolling?.toBoolean() ?: shirates.core.Const.ON_SCROLLING

    @SaveTarget
    var manualScreenshot = profile.manualScreenshot?.toBoolean() ?: shirates.core.Const.MANUAL_SCREENSHOT

    @SaveTarget
    var retryMaxCount = profile.retryMaxCount?.toLongOrNull() ?: shirates.core.Const.RETRY_MAX_COUNT

    @SaveTarget
    var retryTimeoutSeconds = profile.retryTimeoutSeconds?.toDoubleOrNull() ?: shirates.core.Const.RETRY_TIMEOUT_SECONDS

    @SaveTarget
    var retryIntervalSeconds =
        profile.retryIntervalSeconds?.toDoubleOrNull() ?: shirates.core.Const.RETRY_INTERVAL_SECONDS

    @SaveTarget
    var shortWaitSeconds = profile.shortWaitSeconds?.toDoubleOrNull() ?: shirates.core.Const.SHORT_WAIT_SECONDS

    @SaveTarget
    var waitSecondsOnIsScreen =
        profile.waitSecondsOnIsScreen?.toDoubleOrNull() ?: shirates.core.Const.WAIT_SECONDS_ON_ISSCREEN

    @SaveTarget
    var waitSecondsForAnimationComplete =
        profile.waitSecondsForAnimationComplete?.toDoubleOrNull()
            ?: shirates.core.Const.WAIT_SECONDS_FOR_ANIMATION_COMPLETE

    @SaveTarget
    var waitSecondsForConnectionEnabled =
        profile.waitSecondsForConnectionEnabled?.toDoubleOrNull()
            ?: shirates.core.Const.WAIT_SECONDS_FOR_CONNECTION_ENABLED

    @SaveTarget
    var swipeDurationSeconds =
        profile.swipeDurationSeconds?.toDoubleOrNull() ?: shirates.core.Const.SWIPE_DURATION_SECONDS

    @SaveTarget
    var flickDurationSeconds =
        profile.flickDurationSeconds?.toDoubleOrNull() ?: shirates.core.Const.FLICK_DURATION_SECONDS

    @SaveTarget
    var swipeMarginRatio = profile.swipeMarginRatio?.toDoubleOrNull() ?: shirates.core.Const.SWIPE_MARGIN_RATIO

    @SaveTarget
    var scrollVerticalMarginRatio =
        profile.scrollVerticalMarginRatio?.toDoubleOrNull() ?: shirates.core.Const.SCROLL_VERTICAL_MARGIN_RATIO

    @SaveTarget
    var scrollHorizontalMarginRatio =
        profile.scrollHorizontalMarginRatio?.toDoubleOrNull() ?: shirates.core.Const.SCROLL_HORIZONTAL_MARGIN_RATIO

    @SaveTarget
    var scrollMaxCount = profile.scrollMaxCount?.toIntOrNull() ?: shirates.core.Const.SCROLL_MAX_COUNT

    @SaveTarget
    var tapHoldSeconds = profile.tapHoldSeconds?.toDoubleOrNull() ?: shirates.core.Const.TAP_HOLD_SECONDS

    @SaveTarget
    var syncWaitSeconds = profile.syncWaitSeconds?.toDoubleOrNull() ?: shirates.core.Const.SYNC_WAIT_SECONDS

    @SaveTarget
    var syncMaxLoopCount = profile.syncMaxLoopCount?.toIntOrNull() ?: shirates.core.Const.SYNC_MAX_LOOP_COUNT

    @SaveTarget
    var syncIntervalSeconds =
        profile.syncMaxLoopCount?.toDoubleOrNull() ?: shirates.core.Const.SYNC_INTERVAL_SECONDS

    val platformName: String
        get() {
            return TestMode.testTimePlatformName ?: profile.platformName
        }

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

    var irregularHandler: (() -> Unit)? = null

    var enableIrregularHandler = true

    val isEmpty: Boolean
        get() {
            return profile.isEmpty
        }

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
}