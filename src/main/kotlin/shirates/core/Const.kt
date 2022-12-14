package shirates.core

/**
 * Const
 */
object Const {

    /**
     * System properties
     */
    val NEW_LINE = System.getProperty("line.separator")

    /**
     * File names
     */
    const val REPORT_STYLE_FILE_NAME = "_ReportStyle.css"
    const val REPORT_SCRIPT_FILE_NAME = "_ReportScript.js"
    const val ELEMENT_CATEGORY_RESOURCE_BASE_NAME = "element_category"


    /**
     * testrun properties
     */
    const val ENABLE_TEST_LIST = true
    const val ENABLE_SPEC_REPORT = true
    const val MUST = true
    const val SHOULD = true
    const val WANT = true
    const val ENABLE_TRACE = false
    const val ENABLE_SYNC_LOG = true
    const val ENABLE_RETRY_LOG = true
    const val ENABLE_INNER_MACRO_LOG = false
    const val ENABLE_INNER_COMMAND_LOG = false
    const val ENABLE_SILENT_LOG = false
    const val ENABLE_TAP_ELEMENT_IMAGE_LOG = false
    const val ENABLE_XMLSOURCE_DUMP = true
    const val ENABLE_SHELL_EXEC_LOG = false
    const val ENABLE_TIME_MEASURE_LOG = false
    const val ENABLE_IMAGE_MATCH_DEBUG_LOG = false
    const val SCREENSHOT_SCALE = 0.5
    const val ENABLE_IMAGE_ASSERTION = true
    const val IMAGE_MATCHING_SCALE = 0.25
    const val IMAGE_MATCHING_THRESHOLD = 1.0
    const val IMAGE_MATCHING_CANDIDATE_COUNT = 20
    const val CUSTOM_OBJECT_SCAN_DIR = "src/test/kotlin"
    const val MACRO_OBJECT_SCAN_DIR = "src/test/kotlin"
    const val ANDROID_SWIPE_OFFSET_Y = -24
    const val IOS_SWIPE_OFFSET_Y = -10

    /**
     * testConfig common properties
     */

    // Emulator/Simulator
    const val EMULATOR_OPTIONS = "-no-boot-anim -no-snapshot"
    const val DEVICE_STARTUP_TIMEOUT_SECONDS: Double = 60.0
    const val DEVICE_WAIT_SECONDS_AFTER_STARTUP: Double = 0.0

    // Appium server
    const val APPIUM_SERVER_ADDRESS = "http://127.0.0.1:4723/"
    const val IMPLICITLY_WAIT_SECONDS = 5.0
    const val REUSE_DRIVER = true
    const val APPIUM_ARGS_SEPARATOR = " "
    const val APPIUM_SERVER_STARTUP_TIMEOUT_SECONDS: Double = 8.0
    const val APPIUM_PROCESS_TERMINATE_TIMEOUT_SECONDS: Double = 5.0
    const val APPIUM_SESSION_STARTUP_TIMEOUT_SECONDS: Double = 30.0
    const val APPIUM_PROXY_READ_TIMEOUT_SECONDS: Double = 10.0

    // Screenshot
    const val AUTO_SCREEN_SHOT = true
    const val ON_CHANGED_ONLY = true
    const val ON_CONDITION = true
    const val ON_ACTION = true
    const val ON_EXPECTATION = true
    const val ON_EXEC_OPERATE_COMMAND = true
    const val ON_CHECK_COMMAND = true
    const val ON_SCROLLING = true
    const val MANUAL_SCREENSHOT = true

    // App operation
    const val RETRY_MAX_COUNT: Long = 1
    const val RETRY_TIMEOUT_SECONDS: Double = 15.0
    const val RETRY_INTERVAL_SECONDS: Double = 2.0
    const val SHORT_WAIT_SECONDS = 1.5
    const val WAIT_SECONDS_FOR_ANIMATION_COMPLETE = 0.5
    const val WAIT_SECONDS_ON_ISSCREEN = 15.0
    const val WAIT_SECONDS_FOR_CONNECTION_ENABLED = 8.0
    const val SWIPE_DURATION_SECONDS = 3.0
    const val FLICK_DURATION_SECONDS = 0.3
    const val SWIPE_MARGIN_RATIO = 0.1
    const val SCROLL_VERTICAL_MARGIN_RATIO = 0.2
    const val SCROLL_HORIZONTAL_MARGIN_RATIO = 0.2
    const val SCROLL_MAX_COUNT = 20
    const val TAP_HOLD_SECONDS = 0.2
    const val SYNC_WAIT_SECONDS = 1.8
    const val SYNC_MAX_LOOP_COUNT = 5
    const val SYNC_INTERVAL_SECONDS = 0.5
    const val BOUNDS_TO_RECT_RATIO_ANDROID = 1
    const val BOUNDS_TO_RECT_RATIO_IOS = 3
    internal const val DO_UNTIL_INTERVAL_SECONDS: Double = 0.2

    /**
     * others
     */
    const val JQUERY_SOURCE = "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"
    const val TESTRUN_PROPERTIES = "testConfig/testrun.properties"
    const val TESTRUN_GLOBAL_PROPERTIES = "testrun.global.properties"

    const val SEPARATOR_LONG =
        "----------------------------------------------------------------------------------------------------"

    const val ANDROID_SELECT_IGNORE_TYPES = ""
    const val IOS_SELECT_IGNORE_TYPES = "XCUIElementTypeCell,XCUIElementTypeApplication"

    const val ANDROID_TITLE_SELECTOR = "<#action_bar||#toolbar||#app_bar>:descendant(\${title}||@\${title})"
    const val IOS_TITLE_SELECTOR =
        "<.XCUIElementTypeNavigationBar>:descendant(.XCUIElementTypeStaticText&&\${title})"

    const val ANDROID_WEBTITLE_SELECTOR = ".android.webkit.WebView&&\${webTitle}"
    const val IOS_WEBTITLE_SELECTOR = "<.XCUIElementTypeWebView>:descendant(\${webTitle}&&visible=*)"
}