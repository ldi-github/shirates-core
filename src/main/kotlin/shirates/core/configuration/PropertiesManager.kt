package shirates.core.configuration

import shirates.core.Const
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.testProfile
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.file.PropertiesUtility
import shirates.core.utility.misc.EnvUtility
import shirates.core.utility.replaceUserVars
import shirates.core.utility.toPath
import java.nio.file.Files
import java.util.*

/**
 * PropertiesManager
 */
object PropertiesManager {

    var testrunFile = ""
    var testrun: Testrun? = null

    var testrunGlobalProperties: Properties = Properties()
        private set(value) {
            field = value
        }

    var testrunProperties: Properties = Properties()
        private set(value) {
            field = value
        }

    var envProperties: Properties = Properties()
        private set(value) {
            field = value
        }

    /**
     * properties
     */
    var properties: Properties = Properties()
        private set(value) {
            field = value
        }

    /**
     * clear
     */
    fun clear() {

        testrunGlobalProperties = Properties()
        testrunProperties = Properties()
        envProperties = Properties()
        properties = Properties()
        _selectIgnoreTypesForAndroid = null
        _selectIgnoreTypesForIos = null
    }

    /**
     * setup
     */
    fun setup(testrunFile: String = Const.TESTRUN_PROPERTIES) {

        clear()
        setupTestrunFile(testrunFile)

        // Get properties from testrun.global.properties
        testrunGlobalProperties = getProperties(propertiesFile = Const.TESTRUN_GLOBAL_PROPERTIES)
        for (p in testrunGlobalProperties) {
            properties[p.key.toString()] = p.value.toString()
        }

        // Get properties from testrun.properties
        testrunProperties = getProperties(propertiesFile = this.testrunFile)
        for (p in testrunProperties) {
            properties[p.key.toString()] = p.value.toString()
        }

        // Get properties from environment variables
        val srEnvs = EnvUtility.getSREnvMap()
        if (srEnvs.any()) {
            TestLog.info("Importing environment variables.")
            for (env in srEnvs) {
                val key = env.key.substring("SR_".length)
                envProperties[key] = env.value
                properties[key] = env.value
                TestLog.info("${env.key}=${env.value}")
            }
        }
    }

    private fun setupTestrunFile(testrunFile: String) {

        // args
        this.testrunFile = testrunFile

        // environment variable
        val srTestrunFile = EnvUtility.getEnvValue("SR_testrunFile") ?: ""
        if (srTestrunFile.isNotBlank()) {
            println("Importing environment variable. SR_testrunFile=$srTestrunFile")
            if (Files.exists(srTestrunFile.toPath())) {
                this.testrunFile = srTestrunFile
            } else {
                println("File not found. ($srTestrunFile)")
            }
        }

        // default
        if (this.testrunFile.isBlank()) {
            this.testrunFile = Const.TESTRUN_PROPERTIES
            println("Using default testrun file. (${this.testrunFile})")
        }
    }

    private fun getProperties(propertiesFile: String): Properties {

        val path = propertiesFile.toPath()

        if (Files.exists(path).not()) {
            return Properties()
        }

        return PropertiesUtility.getProperties(path)
    }

    /**
     * getPropertyValue
     */
    fun getPropertyValue(propertyName: String): String? {

        if (properties.containsKey(propertyName)) {
            return properties.getProperty(propertyName)
        }
        return null
    }

    /**
     * setPropertyValue
     */
    fun setPropertyValue(propertyName: String, value: String): String {

        properties[propertyName] = value
        return value
    }

    // OS --------------------------------------------------

    /**
     * os
     */
    val os: String
        get() {
            val value = getPropertyValueOrEnvValue("os")
            if (value.isNotBlank()) {
                return value
            }

            if (TestMode.testTimePlatformName.isNullOrBlank().not()) {
                return TestMode.testTimePlatformName!!
            }

            return TestMode.ANDROID
        }

    // Config --------------------------------------------------

    /**
     * configFile
     */
    val configFile: String
        get() {
            var value = getPropertyValueOrEnvValue("configFile")
            if (value.isNotBlank()) {
                return value
            }

            value = getPropertyValueOrEnvValue("$os.configFile")
            if (value.isNotBlank()) {
                return value
            }

            throw TestConfigException(
                message(
                    id = "requiredInFile",
                    subject = "${os}.configFile",
                    file = testrunFile
                )
            )
        }

    /**
     * profile
     */
    val profile: String
        get() {
            if (testrun?.profile.isNullOrBlank().not()) {
                return testrun!!.profile
            }

            var value = getPropertyValueOrEnvValue("profile")
            if (value.isNotBlank()) {
                return value
            }

            value = getPropertyValueOrEnvValue("$os.profile")
            if (value.isNotBlank()) {
                return value
            }

            throw TestConfigException(
                message(
                    id = "requiredInFile",
                    subject = "${os}.profile",
                    file = testrunFile
                )
            )
        }

    /**
     * statBarHeight
     */
    val statBarHeight: Int
        get() {
            val p = getPropertyValue("${os}.statBarHeight")
            val value = p?.toIntOrNull()
            if (value != null) {
                return value
            }

            if (os == "android") {
                return Const.ANDROID_STATBAR_HEIGHT
            }

            val m = testProfile.deviceName.removePrefix("iPhone ")
            return if (m.startsWith("SE")) 20
            else if (m.startsWith("15")) 47
            else if (m.startsWith("14")) 47
            else if (m.startsWith("13")) 47
            else if (m.startsWith("12")) 47
            else if (m.startsWith("11")) 48
            else if (m.startsWith("X")) 44
            else if (m.startsWith("8")) 20
            else if (m.startsWith("7")) 20
            else if (m.startsWith("6")) 20
            else if (m.startsWith("5")) 20
            else return Const.IOS_STATBAR_HEIGHT
        }

    // Priority --------------------------------------------------

    /**
     * must
     */
    val must: Boolean
        get() {
            val value = getPropertyValue(propertyName = "must")
                ?: return Const.MUST
            return value == "true"
        }

    /**
     * should
     */
    val should: Boolean
        get() {
            val value = getPropertyValue(propertyName = "should")
                ?: return Const.SHOULD
            return value == "true"
        }

    /**
     * want
     */
    val want: Boolean
        get() {
            val value = getPropertyValue(propertyName = "want")
                ?: return Const.WANT
            return value == "true"
        }

    /**
     * none
     */
    val none: Boolean
        get() {
            val value = getPropertyValue(propertyName = "none")
                ?: return true
            return value == "true"
        }

    // Log --------------------------------------------------

    /**
     * testResults
     */
    val testResults: String
        get() {
            return getPropertyValue(propertyName = "testResults") ?: shirates.core.UserVar.TEST_RESULTS
        }

    /**
     * logLanguage
     */
    var logLanguage: String
        get() {
            return getPropertyValue(propertyName = "logLanguage") ?: ""
        }
        set(value) {
            setPropertyValue(propertyName = "logLanguage", value = value)
        }

    /**
     * enableSyncLog
     */
    val enableSyncLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableSyncLog")
                ?: return Const.ENABLE_SYNC_LOG
            return value == "true"
        }

    /**
     * enableTestList
     */
    val enableTestList: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTestList")
                ?: return Const.ENABLE_TEST_LIST
            return value == "true"
        }

    /**
     * enableTestClassList
     */
    val enableTestClassList: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTestClassList")
                ?: return Const.ENABLE_TEST_CLASS_LIST
            return value == "true"
        }

    /**
     * enableSpecReport
     */
    val enableSpecReport: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableSpecReport")
                ?: return Const.ENABLE_SPEC_REPORT
            return value == "true"
        }

    /**
     * enableRelativeCommandTranslation
     */
    val enableRelativeCommandTranslation: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableRelativeCommandTranslation")
                ?: return Const.ENABLE_RELATIVE_COMMAND_TRANSLATION
            return value == "true"
        }

    /**
     * testListDir
     */
    val testListDir: String
        get() {
            return getPropertyValue(propertyName = "testListDir")?.replaceUserVars() ?: ""
        }

    /**
     * reportIndexDir
     */
    val reportIndexDir: String
        get() {
            return getPropertyValue(propertyName = "reportIndexDir")?.replaceUserVars() ?: ""
        }

    /**
     * enableInnerMacroLog
     */
    val enableInnerMacroLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableInnerMacroLog")
                ?: return Const.ENABLE_INNER_MACRO_LOG
            return value == "true"
        }

    /**
     * enableInnerCommandLog
     */
    val enableInnerCommandLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableInnerCommandLog")
                ?: return Const.ENABLE_SILENT_LOG
            return value == "true"
        }

    /**
     * enableSilentLog
     */
    val enableSilentLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableSilentLog")
                ?: return Const.ENABLE_INNER_COMMAND_LOG
            return value == "true"
        }

    /**
     * enableTapElementImageLog
     */
    val enableTapElementImageLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTapElementImageLog")
                ?: return Const.ENABLE_TAP_ELEMENT_IMAGE_LOG
            return value == "true"
        }

    /**
     * enableXmlSourceDump
     */
    val enableXmlSourceDump: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableXmlSourceDump")
                ?: return Const.ENABLE_XMLSOURCE_DUMP
            return value == "true"
        }

    /**
     * enableRetryLog
     */
    val enableRetryLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableRetryLog")
                ?: return Const.ENABLE_RETRY_LOG
            return value == "true"
        }

    /**
     * enableWarnOnRetryError
     */
    val enableWarnOnRetryError: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableWarnOnRetryError")
                ?: return Const.ENABLE_WARN_ON_RETRY_ERROR
            return value == "true"
        }

    /**
     * enableWarnOnSelectTimeout
     */
    val enableWarnOnSelectTimeout: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableWarnOnSelectTimeout")
                ?: return Const.ENABLE_WARN_ON_SELECT_TIMEOUT
            return value == "true"
        }

    /**
     * enableGetSourceLog
     */
    val enableGetSourceLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableGetSourceLog")
                ?: return Const.ENABLE_GET_SOURCE_LOG
            return value == "true"
        }

    /**
     * enableTrace
     */
    val enableTrace: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTrace")
                ?: return Const.ENABLE_TRACE
            return value == "true"
        }

    /**
     * enableShellExecLog
     */
    val enableShellExecLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableShellExecLog")
                ?: return Const.ENABLE_SHELL_EXEC_LOG
            return value == "true"
        }

    /**
     * enableTimeMeasureLog
     */
    val enableTimeMeasureLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTimeMeasureLog")
                ?: return Const.ENABLE_TIME_MEASURE_LOG
            return value == "true"
        }

    /**
     * enableImageMatchDebugLog
     */
    val enableImageMatchDebugLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableImageMatchDebugLog")
                ?: return Const.ENABLE_IMAGE_MATCH_DEBUG_LOG
            return value == "true"
        }

    /**
     * enableIsInViewLog
     */
    val enableIsInViewLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableIsInViewLog")
                ?: return Const.ENABLE_IS_IN_VIEW_LOG
            return value == "true"
        }

    /**
     * enableIsSafeLog
     */
    val enableIsSafeLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableIsSafeLog")
                ?: return Const.ENABLE_IS_SAFE_LOG
            return value == "true"
        }

    /**
     * enableIsScreenLog
     */
    val enableIsScreenLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableIsScreenLog")
                ?: return Const.ENABLE_IS_SCREEN_LOG
            return value == "true"
        }

    // Screenshot --------------------------------------------------

    /**
     * screenshotScale
     */
    val screenshotScale: Double
        get() {
            val value =
                getPropertyValue(propertyName = "screenshotScale")?.toDoubleOrNull()
                    ?: return Const.SCREENSHOT_SCALE
            if (value < 0.1 || value > 1.0) {
                throw TestConfigException(message(id = "screenshotScale", value = "$value"))
            }

            return value
        }

    /**
     * screenshotIntervalSeconds
     */
    var screenshotIntervalSeconds: Double
        get() {
            val value =
                getPropertyValue(propertyName = "screenshotIntervalSeconds")?.toDoubleOrNull()
                    ?: return Const.SCREENSHOT_INTERVAL_SECOND
            return value
        }
        set(value) {
            setPropertyValue(propertyName = "screenshotIntervalSeconds", value = "$value")
        }

    // Image Matching --------------------------------------------------

    /**
     * enableImageAssertion
     */
    val enableImageAssertion: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableImageAssertion")
                ?: return Const.ENABLE_IMAGE_ASSERTION
            return value == "true"
        }

    /**
     * imageMatchingScale
     */
    val imageMatchingScale: Double
        get() {
            val value = getPropertyValue(propertyName = "imageMatchingScale")?.toDoubleOrNull()
                ?: return Const.IMAGE_MATCHING_SCALE
            if (value < 0.1 || value > 1.0) {
                throw TestConfigException(message(id = "imageMatchingScale", value = "$value"))
            }

            return value
        }

    /**
     * imageMatchingThreshold
     */
    val imageMatchingThreshold: Double
        get() {
            val value = getPropertyValue(propertyName = "imageMatchingThreshold")?.toDoubleOrNull()
                ?: return Const.IMAGE_MATCHING_THRESHOLD

            return value
        }

    /**
     * imageMatchingCandidateCount
     */
    val imageMatchingCandidateCount: Int
        get() {
            val value = getPropertyValue(propertyName = "imageMatchingCandidateCount")?.toIntOrNull()
                ?: return Const.IMAGE_MATCHING_CANDIDATE_COUNT

            return value
        }

    // String Comparing --------------------------------------------------

    /**
     * strictCompareMode
     */
    val strictCompareMode: Boolean
        get() {
            val value = getPropertyValue(propertyName = "strictCompareMode")
                ?: return Const.ENABLE_STRICT_COMPARE_MODE
            return value == "true"
        }

    /**
     * keepLF
     */
    val keepLF: Boolean
        get() {
            val value = getPropertyValue(propertyName = "keepLF")
                ?: return Const.KEEP_LF
            return value == "true"
        }

    /**
     * keepTAB
     */
    val keepTAB: Boolean
        get() {
            val value = getPropertyValue(propertyName = "keepTAB")
                ?: return Const.KEEP_TAB
            return value == "true"
        }

    /**
     * keepZenkakuSpace
     */
    val keepZenkakuSpace: Boolean
        get() {
            val value = getPropertyValue(propertyName = "keepZenkakuSpace")
                ?: return Const.KEEP_ZENKAKU_SPACE
            return value == "true"
        }

    /**
     * waveDashToFullWidthTilde
     */
    val waveDashToFullWidthTilde: Boolean
        get() {
            val value = getPropertyValue(propertyName = "waveDashToFullWidthTilde")
                ?: return Const.WAVE_DASH_TO_FULL_WIDTH_TILDE
            return value == "true"
        }

    /**
     * compressWhitespaceCharacters
     */
    val compressWhitespaceCharacters: Boolean
        get() {
            val value = getPropertyValue(propertyName = "compressWhitespaceCharacters")
                ?: return Const.COMPRESS_WHITESPACE_CHARACTORS
            return value == "true"
        }

    /**
     * trimString
     */
    val trimString: Boolean
        get() {
            val value = getPropertyValue(propertyName = "trimString")
                ?: return Const.TRIM_STRING
            return value == "true"
        }

    // Emulator/Simulator

    /**
     * enableWdaInstallOptimization
     */
    val enableWdaInstallOptimization: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableWdaInstallOptimization")
                ?: return Const.ENABLE_WDA_INSTALL_OPTIMIZATION
            return value == "true"
        }

    // TestDriver --------------------------------------------------

    /**
     * enableHealthCheck
     */
    val enableHealthCheck: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableHealthCheck")
                ?: return Const.ENABLE_HEALTH_CHECK
            return value == "true"
        }

    /**
     * tapTestSelector
     */
    val tapTestSelector: String
        get() {
            return getPropertyValue(propertyName = "tapTestSelector")
                ?: Const.TAP_TEST_SELECTOR
        }

    /**
     * enableAutoSyncAndroid
     */
    val enableAutoSyncAndroid: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableAutoSyncAndroid")
                ?: return Const.ENABLE_AUTO_SYNC_ANDROID
            return value == "true"
        }

    /**
     * enableAutoSyncIos
     */
    val enableAutoSyncIos: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableAutoSyncIos")
                ?: return Const.ENABLE_AUTO_SYNC_IOS
            return value == "true"
        }

    /**
     * enableRerunScenario
     */
    val enableRerunScenario: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableRerunScenario")
                ?: return Const.ENABLE_RERUN_SCENARIO
            return value == "true"
        }

    /**
     * enableAlwaysRerunOnErrorAndroid
     */
    val enableAlwaysRerunOnErrorAndroid: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableAlwaysRerunOnErrorAndroid")
                ?: return Const.ENABLE_ALWAYS_RERUN_ON_ERROR_ANDROID
            return value == "true"
        }

    /**
     * enableAlwaysRerunOnErrorIos
     */
    val enableAlwaysRerunOnErrorIos: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableAlwaysRerunOnErrorIos")
                ?: return Const.ENABLE_ALWAYS_RERUN_ON_ERROR_IOS
            return value == "true"
        }

    /**
     * scenarioTimeoutSeconds
     */
    val scenarioTimeoutSeconds: Double
        get() {
            val value =
                getPropertyValue(propertyName = "scenarioTimeoutSeconds")?.toDoubleOrNull()
                    ?: return Const.SCENARIO_TIMEOUT_SECONDS
            if (value < 0) {
                throw TestConfigException(message(id = "scenarioTimeoutSeconds", value = "$value"))
            }

            return value
        }

    /**
     * scenarioMaxCount
     */
    val scenarioMaxCount: Int
        get() {
            val value = getPropertyValue(propertyName = "scenarioMaxCount")?.toIntOrNull()
                ?: return Const.SCENARIO_MAX_COUNT
            if (value < 1) {
                throw TestConfigException(message(id = "scenarioMaxCount", value = "$value"))
            }

            return value
        }

    /**
     * rerunScenarioWords
     */
    val rerunScenarioWords: String
        get() {
            return getPropertyValue(propertyName = "rerunScenarioWords")
                ?: Const.RERUN_SCENARIO_WORDS
        }

    /**
     * enableRerunOnScreenshotBlackout
     */
    val enableRerunOnScreenshotBlackout: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableRerunOnScreenshotBlackout")
                ?: return Const.ENABLE_RERUN_ON_SCREENSHOT_BLACKOUT
            return value == "true"
        }

    /**
     * screenshotBlackoutThreshold
     */
    val screenshotBlackoutThreshold: Double
        get() {
            val value =
                getPropertyValue(propertyName = "screenshotBlackoutThreshold")?.toDoubleOrNull()
                    ?: return Const.SCREENSHOT_BLACKOUT_THRESHOLD
            if (value < 0.9 || value > 1.0) {
                throw TestConfigException(message(id = "screenshotBlackoutThreshold", value = "$value"))
            }

            return value
        }

    // Custom --------------------------------------------------

    /**
     * customObjectScanDir
     */
    val customObjectScanDir: String
        get() {
            return getPropertyValue(propertyName = "CustomObject.scan.dir")
                ?: Const.CUSTOM_OBJECT_SCAN_DIR
        }

    // Macro --------------------------------------------------

    /**
     * macroObjectScanDir
     */
    val macroObjectScanDir: String
        get() {
            return getPropertyValue(propertyName = "MacroObject.scan.dir") ?: Const.MACRO_OBJECT_SCAN_DIR
        }

    // Spec-Report --------------------------------------------------

    /**
     * specReportReplaceMANUAL
     */
    val specReportReplaceMANUAL: String
        get() {
            return getPropertyValue(propertyName = "specReport.replace.MANUAL") ?: ""
        }

    /**
     * specReportReplaceMANUALReason
     */
    val specReportReplaceMANUALReason: String
        get() {
            return getPropertyValue(propertyName = "specReport.replace.MANUAL.reason") ?: ""
        }

    /**
     * specReportReplaceSKIP
     */
    val specReportReplaceSKIP: String
        get() {
            return getPropertyValue(propertyName = "specReport.replace.SKIP") ?: ""
        }

    /**
     * specReportReplaceSKIPReason
     */
    val specReportReplaceSKIPReason: String
        get() {
            return getPropertyValue(propertyName = "specReport.replace.SKIP.reason") ?: ""
        }

    // misc --------------------------------------------------

    /**
     * swipeOffsetY
     */
    val swipeOffsetY: Int
        get() {
            val p = getPropertyValue("${os}.swipeOffsetY")
            val offsetY = p?.toIntOrNull()
            if (offsetY != null) {
                return offsetY
            }

            return if (os == "android") {
                Const.ANDROID_SWIPE_OFFSET_Y
            } else {
                Const.IOS_SWIPE_OFFSET_Y
            }
        }

    /**
     * xmlSourceRemovePattern
     */
    val xmlSourceRemovePattern: String
        get() {
            val value = getPropertyValue(propertyName = "xmlSourceRemovePattern") ?: ""
            return value
        }

    /**
     * selectIgnoreTypes
     */
    internal var _selectIgnoreTypesForAndroid: MutableList<String>? = null
    internal var _selectIgnoreTypesForIos: MutableList<String>? = null
    val selectIgnoreTypes: MutableList<String>
        get() {
            if (isAndroid) {
                if (_selectIgnoreTypesForAndroid == null) {
                    val types = getPropertyValue(propertyName = "android.selectIgnoreTypes")
                        ?: Const.ANDROID_SELECT_IGNORE_TYPES
                    _selectIgnoreTypesForAndroid = types.split(",").toMutableList()
                }
                return _selectIgnoreTypesForAndroid!!.filter { it.isNotEmpty() }.toMutableList()
            } else {
                if (_selectIgnoreTypesForIos == null) {
                    val types = getPropertyValue(propertyName = "ios.selectIgnoreTypes")
                        ?: Const.IOS_SELECT_IGNORE_TYPES
                    _selectIgnoreTypesForIos = types.split(",").toMutableList()
                }
                return _selectIgnoreTypesForIos!!.filter { it.isNotEmpty() }.toMutableList()
            }
        }

    /**
     * titleSelector
     */
    val titleSelector: String
        get() {
            if (isAndroid) {
                return getPropertyValue("android.titleSelector") ?: Const.ANDROID_TITLE_SELECTOR
            } else {
                return getPropertyValue("ios.titleSelector") ?: Const.IOS_TITLE_SELECTOR
            }
        }

    /**
     * webTitleSelector
     */
    val webTitleSelector: String
        get() {
            if (isAndroid) {
                return getPropertyValue("android.webTitleSelector") ?: Const.ANDROID_WEBTITLE_SELECTOR
            } else {
                return getPropertyValue("ios.webTitleSelector") ?: Const.IOS_WEBTITLE_SELECTOR
            }
        }

    /**
     * jquerySource
     */
    val jquerySource: String
        get() {
            return getPropertyValue("jquerySource") ?: Const.JQUERY_SOURCE
        }


    private fun getPropertyValueOrEnvValue(name: String): String {

        var value = getPropertyValue(name) ?: ""
        if (value.isNotBlank()) {
            return value
        }

        value = EnvUtility.getEnvValue("SR_$name") ?: ""
        return value
    }

}