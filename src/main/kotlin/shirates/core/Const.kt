package shirates.core

/**
 * Const
 */
object Const {

    /**
     * System properties
     */
    val NEW_LINE = System.lineSeparator()

    /**
     * File names
     */
    const val REPORT_STYLE_FILE_NAME = "_ReportStyle.css"
    const val REPORT_SCRIPT_FILE_NAME = "_ReportScript.js"
    const val ELEMENT_CATEGORY_RESOURCE_BASE_NAME = "element_category"

    /**
     * CpuLoadService
     */
    const val CPU_LOAD_FOR_SAFETY: Int = 70
    const val ENABLE_WAIT_CPU_LOAD = true
    const val ENABLE_WAIT_CPU_LOAD_PRINT_DEBUG = false
    const val CPU_LOAD_INTERVAL_MILLISECONDS: Long = 1000
    const val CPU_LOAD_MAX_HISTORIES: Int = 60 * 10

    /**
     * testrun properties
     */
    const val ENABLE_TEST_LIST = true
    const val ENABLE_TEST_CLASS_LIST = true
    const val ENABLE_SPEC_REPORT = true
    const val ENABLE_RELATIVE_COMMAND_TRANSLATION = true
    const val MUST = true
    const val SHOULD = true
    const val WANT = true
    const val ENABLE_TRACE = false
    const val ENABLE_SYNC_LOG = true
    const val ENABLE_RETRY_LOG = true
    const val ENABLE_WARN_ON_RETRY_ERROR = false
    const val ENABLE_WARN_ON_SELECT_TIMEOUT = false
    const val ENABLE_GET_SOURCE_LOG = false
    const val ENABLE_INNER_MACRO_LOG = false
    const val ENABLE_INNER_COMMAND_LOG = false
    const val ENABLE_SILENT_LOG = false
    const val ENABLE_TAP_ELEMENT_IMAGE_LOG = false
    const val ENABLE_XMLSOURCE_DUMP = true
    const val ENABLE_SHELL_EXEC_LOG = false
    const val ENABLE_TIME_MEASURE_LOG = false
    const val ENABLE_STOP_WATCH_LOG = false
    const val ENABLE_IMAGE_MATCH_DEBUG_LOG = false
    const val ENABLE_IS_IN_VIEW_LOG = false
    const val ENABLE_IS_SAFE_LOG = true
    const val ENABLE_IS_SCREEN_LOG = false
    const val SCREENSHOT_SCALE = 1.0
    const val ENABLE_IMAGE_ASSERTION = true
    const val IMAGE_MATCHING_SCALE = 0.25
    const val IMAGE_MATCHING_THRESHOLD = 1.0
    const val IMAGE_MATCHING_CANDIDATE_COUNT = 5
    const val ENABLE_WDA_INSTALL_OPTIMIZATION = true
    const val EMULATOR_PORT = 5554
    const val ENABLE_HEALTH_CHECK = true
    const val TAP_TEST_SELECTOR = ".label"
    const val ENABLE_AUTO_SYNC_ANDROID = false
    const val ENABLE_AUTO_SYNC_IOS = false
    const val ENABLE_LAUNCH_APP_ON_SCENARIO = true
    const val LAUNCH_APP_METHOD = "auto"
    const val ENABLE_RERUN_SCENARIO = true
    const val ENABLE_ALWAYS_RERUN_ON_ERROR_ANDROID = false
    const val ENABLE_ALWAYS_RERUN_ON_ERROR_IOS = false
    const val SCENARIO_TIMEOUT_SECONDS = 60 * 15.0
    const val SCENARIO_MAX_COUNT = 3
    const val RERUN_SCENARIO_WORDS =
        "Read timed out||AppiumProxy.getSource() timed out||Could not start a new session. Response code 500.|| is still running after||Could not proxy command to the remote server.||current thread is not owner"
    const val ENABLE_RERUN_ON_SCREENSHOT_BLACKOUT = true
    const val SCREENSHOT_BLACKOUT_THRESHOLD = 0.995
    const val ENABLE_RESTART_DEVICE_ON_RESETTING_APPIUM_SESSION = false
    const val CUSTOM_OBJECT_SCAN_DIR = "src/test/kotlin"
    const val MACRO_OBJECT_SCAN_DIR = "src/test/kotlin"
    const val ANDROID_SWIPE_OFFSET_Y = -24
    const val IOS_SWIPE_OFFSET_Y = -10
    const val SPECREPORT_EXCLUDE_DETAIL = false

    /**
     * testConfig common properties
     */

    // String Comparing
    const val ENABLE_STRICT_COMPARE_MODE = false
    const val KEEP_LF = false
    const val KEEP_TAB = false
    const val KEEP_ZENKAKU_SPACE = true
    const val WAVE_DASH_TO_FULL_WIDTH_TILDE = true
    const val TRIM_STRING = true
    const val COMPRESS_WHITESPACE_CHARACTORS = true

    // Emulator/Simulator
    const val EMULATOR_OPTIONS = "-no-boot-anim -no-snapshot"
    const val DEVICE_STARTUP_TIMEOUT_SECONDS: Double = 60.0
    const val DEVICE_WAIT_SECONDS_AFTER_STARTUP: Double = 0.0
    const val ANDROID_LANGUAGE_AND_REGION = ""

    // Appium server
    const val APPIUM_SERVER_ADDRESS = "http://127.0.0.1:4723/"
    const val IMPLICITLY_WAIT_SECONDS = 5.0
    const val REUSE_DRIVER = true
    const val APPIUM_ARGS_SEPARATOR = " "
    const val APPIUM_SERVER_STARTUP_TIMEOUT_SECONDS: Double = 30.0
    const val APPIUM_PROCESS_TERMINATE_TIMEOUT_SECONDS: Double = 5.0
    const val APPIUM_SESSION_STARTUP_TIMEOUT_SECONDS: Double = 30.0
    const val APPIUM_PROXY_READ_TIMEOUT_SECONDS: Double = 20.0
    const val APPIUM_PROXY_GET_SOURCE_TIMEOUT_SECONDS: Double = 60.0

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
    const val SCREENSHOT_INTERVAL_SECOND = 0.5

    // App operation
    const val RETRY_MAX_COUNT: Long = 2
    const val RETRY_TIMEOUT_SECONDS: Double = 15.0
    const val RETRY_INTERVAL_SECONDS: Double = 2.0
    const val SHORT_WAIT_SECONDS = 1.5
    const val WAIT_SECONDS_FOR_EXIST = 0.5
    const val WAIT_SECONDS_FOR_LAUNCH_APP_COMPLETE = 15.0
    const val WAIT_SECONDS_FOR_ANIMATION_COMPLETE = 0.5
    const val WAIT_SECONDS_ON_ISSCREEN = 15.0
    const val WAIT_SECONDS_FOR_CONNECTION_ENABLED = 8.0
    const val SWIPE_DURATION_SECONDS = 3.0
    const val FLICK_DURATION_SECONDS = 0.3
    const val SWIPE_INTERVAL_SECONDS = 0.1
    const val FLICK_INTERVAL_SECONDS = 0.3
    const val SWIPE_MARGIN_RATIO = 0.0
    const val SCROLL_VERTICAL_START_MARGIN_RATIO = 0.15
    const val SCROLL_VERTICAL_END_MARGIN_RATIO = 0.1
    const val SCROLL_HORIZONTAL_START_MARGIN_RATIO = 0.2
    const val SCROLL_HORIZONTAL_END_MARGIN_RATIO = 0.1
    const val SCROLL_TO_EDGE_BOOST = 1
    const val SCROLL_INTERVAL_SECONDS = 0.5
    const val SCROLL_MAX_COUNT = 20
    const val TAP_HOLD_SECONDS = 0.0
    const val ENABLE_CACHE = true
    const val FIND_WEB_ELEMENT_TIMEOUT_SECONDS = 0.2
    const val SYNC_WAIT_SECONDS = 1.8
    const val SYNC_MAX_LOOP_COUNT = 5
    const val SYNC_INTERVAL_SECONDS = 0.5
    const val BOUNDS_TO_RECT_RATIO_ANDROID = 1
    const val BOUNDS_TO_RECT_RATIO_IOS = 3

    // Vision
    const val VISION_OCR_LANGUAGE = ""
    const val VISION_DIRECTORY = "vision"
    const val VISION_BUILD_DIRECTORY = "build"
    const val VISION_ENABLE_LEARNING_ON_STARTUP = true
    const val VISION_SERVER_URL = "http://127.0.0.1:8081"
    const val VISION_SEGMENT_MARGIN_HORIZONTAL = 20
    const val VISION_SEGMENT_MARGIN_VERTICAL = 5
    const val VISION_SEGMENT_CROPPING_MARGIN = 5
    const val VISION_FIND_IMAGE_THRESHOLD = 0.1
    const val VISION_FIND_IMAGE_BINARY_THRESHOLD = 1
    const val VISION_FIND_IMAGE_ASPECT_RATIO_TOLERANCE = 0.2
    const val VISION_SYNC_IMAGE_MATCH_RATE = 0.995
    const val VISION_TEXT_INDEX_TRIM_CHARS_FOR_JA = " '・■▪️◾️◎、,-」"
    const val VISION_CLASSIFIER_SHARD_NODE_COUNT = "ScreenClassifier=3"

    /**
     * internal
     */
    internal const val SYNC_UTILITY_DO_UNTIL_INTERVAL_SECONDS: Double = 0.2
    internal const val WAIT_UTILITY_WAIT_SECONDS: Double = 60.0
    internal const val WAIT_UTILITY_DO_UNTIL_INTERVAL_SECONDS: Double = 0.2
    internal const val SHELL_RESULT_WAIT_FOR_SECONDS: Double = 30.0
    internal const val EMULATOR_REBOOT_WAIT_SECONDS: Double = 60.0
    internal const val EMULATOR_BOOTANIMATION_WAIT_SECONDS: Double = 15.0
    internal const val EMULATOR_SHUTDOWN_WAIT_SECONDS: Double = 60.0

    /**
     * others
     */
    const val ANDROID_STATBAR_HEIGHT = 137
    const val IOS_STATBAR_HEIGHT = 53

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

    const val WAVE_DASH = "\u301C"
    const val FULLWIDTH_TILDE = "\uFF5E"
    const val LF = "\u000A"
    const val CR = "\u000D"
    const val TAB = "\u0009"
    const val NBSP = "\u00A0"
    const val ZERO_WIDTH_SPACE = "\u200B"
    const val ZERO_WIDTH_NBSP = "\uFEFF"
    const val ZENKAKU_SPACE = "　"   // IDEOGRAPHIC SPACE
    val ZsCategorySpaces = listOf(
        // Unicode White_Space(Zs) Category
        "\u0020",   // Space(SP)
        "\u00A0",   // No-Break Space(NBSP)
        "\u1680",   // Ogham Space Mark
        "\u2000",   // En Quad
        "\u2001",   // Em Quad
        "\u2002",   // En Space
        "\u2003",   // Em Space
        "\u2004",   // Three-Per-Em Space
        "\u2005",   // Four-Per-Em Space
        "\u2006",   // Six-Per-Em Space
        "\u2007",   // Figure Space
        "\u2008",   // Punctuation Space
        "\u2009",   // Thin Space
        "\u200A",   // Hair Space
        "\u202F",   // Narrow No-Break Space(NNBSP)
        "\u205F",   // Medium Mathematical Space(MMSP)
        "\u3000",   // Ideographic Space
    )
}