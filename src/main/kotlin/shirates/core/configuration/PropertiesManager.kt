package shirates.core.configuration

import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.file.PropertiesUtility
import shirates.core.utility.misc.EnvUtility
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
    fun setup(testrunFile: String = shirates.core.Const.TESTRUN_PROPERTIES) {

        clear()
        setupTestrunFile(testrunFile)

        // Get properties from testrun.global.properties
        testrunGlobalProperties = getProperties(propertiesFile = shirates.core.Const.TESTRUN_GLOBAL_PROPERTIES)
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
            this.testrunFile = shirates.core.Const.TESTRUN_PROPERTIES
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

    // Priority --------------------------------------------------

    /**
     * must
     */
    val must: Boolean
        get() {
            val value = getPropertyValue(propertyName = "must")
                ?: return shirates.core.Const.MUST
            return value == "true"
        }

    /**
     * should
     */
    val should: Boolean
        get() {
            val value = getPropertyValue(propertyName = "should")
                ?: return shirates.core.Const.SHOULD
            return value == "true"
        }

    /**
     * want
     */
    val want: Boolean
        get() {
            val value = getPropertyValue(propertyName = "want")
                ?: return shirates.core.Const.WANT
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
                ?: return shirates.core.Const.ENABLE_SYNC_LOG
            return value == "true"
        }

    /**
     * enableTestList
     */
    val enableTestList: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTestList")
                ?: return shirates.core.Const.ENABLE_TEST_LIST
            return value == "true"
        }

    /**
     * enableSpecReport
     */
    val enableSpecReport: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableSpecReport")
                ?: return shirates.core.Const.ENABLE_SPEC_REPORT
            return value == "true"
        }

    /**
     * testListDir
     */
    val testListDir: String
        get() {
            return getPropertyValue(propertyName = "testListDir") ?: ""
        }

    /**
     * enableInnerMacroLog
     */
    val enableInnerMacroLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableInnerMacroLog")
                ?: return shirates.core.Const.ENABLE_INNER_MACRO_LOG
            return value == "true"
        }

    /**
     * enableInnerCommandLog
     */
    val enableInnerCommandLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableInnerCommandLog")
                ?: return shirates.core.Const.ENABLE_SILENT_LOG
            return value == "true"
        }

    /**
     * enableSilentLog
     */
    val enableSilentLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableSilentLog")
                ?: return shirates.core.Const.ENABLE_INNER_COMMAND_LOG
            return value == "true"
        }

    /**
     * enableTapElementImageLog
     */
    val enableTapElementImageLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTapElementImageLog")
                ?: return shirates.core.Const.ENABLE_TAP_ELEMENT_IMAGE_LOG
            return value == "true"
        }

    /**
     * enableXmlSourceDump
     */
    val enableXmlSourceDump: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableXmlSourceDump")
                ?: return shirates.core.Const.ENABLE_XMLSOURCE_DUMP
            return value == "true"
        }

    /**
     * enableRetryLog
     */
    val enableRetryLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableRetryLog")
                ?: return shirates.core.Const.ENABLE_RETRY_LOG
            return value == "true"
        }

    /**
     * enableTrace
     */
    val enableTrace: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTrace")
                ?: return shirates.core.Const.ENABLE_TRACE
            return value == "true"
        }

    /**
     * enableShellExecLog
     */
    val enableShellExecLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableShellExecLog")
                ?: return shirates.core.Const.ENABLE_SHELL_EXEC_LOG
            return value == "true"
        }

    /**
     * enableTimeMeasureLog
     */
    val enableTimeMeasureLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableTimeMeasureLog")
                ?: return shirates.core.Const.ENABLE_TIME_MEASURE_LOG
            return value == "true"
        }

    /**
     * enableImageMatchDebugLog
     */
    val enableImageMatchDebugLog: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableImageMatchDebugLog")
                ?: return shirates.core.Const.ENABLE_IMAGE_MATCH_DEBUG_LOG
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
                    ?: return shirates.core.Const.SCREENSHOT_SCALE
            if (value < 0.1 || value > 1.0) {
                throw TestConfigException(message(id = "screenshotScale", value = "$value"))
            }

            return value
        }

    // Image Matching --------------------------------------------------

    /**
     * enableImageAssertion
     */
    val enableImageAssertion: Boolean
        get() {
            val value = getPropertyValue(propertyName = "enableImageAssertion")
                ?: return shirates.core.Const.ENABLE_IMAGE_ASSERTION
            return value == "true"
        }

    /**
     * imageMatchingScale
     */
    val imageMatchingScale: Double
        get() {
            val value = getPropertyValue(propertyName = "imageMatchingScale")?.toDoubleOrNull()
                ?: return shirates.core.Const.IMAGE_MATCHING_SCALE
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
                ?: return shirates.core.Const.IMAGE_MATCHING_THRESHOLD

            return value
        }

    /**
     * imageMatchingCandidateCount
     */
    val imageMatchingCandidateCount: Int
        get() {
            val value = getPropertyValue(propertyName = "imageMatchingCandidateCount")?.toIntOrNull()
                ?: return shirates.core.Const.IMAGE_MATCHING_CANDIDATE_COUNT

            return value
        }

    // Custom --------------------------------------------------

    /**
     * customObjectScanDir
     */
    val customObjectScanDir: String
        get() {
            return getPropertyValue(propertyName = "CustomObject.scan.dir")
                ?: shirates.core.Const.CUSTOM_OBJECT_SCAN_DIR
        }

    // Macro --------------------------------------------------

    /**
     * macroObjectScanDir
     */
    val macroObjectScanDir: String
        get() {
            return getPropertyValue(propertyName = "MacroObject.scan.dir") ?: shirates.core.Const.MACRO_OBJECT_SCAN_DIR
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
                shirates.core.Const.ANDROID_SWIPE_OFFSET_Y
            } else {
                shirates.core.Const.IOS_SWIPE_OFFSET_Y
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
                        ?: shirates.core.Const.ANDROID_SELECT_IGNORE_TYPES
                    _selectIgnoreTypesForAndroid = types.split(",").toMutableList()
                }
                return _selectIgnoreTypesForAndroid!!.filter { it.isNotEmpty() }.toMutableList()
            } else {
                if (_selectIgnoreTypesForIos == null) {
                    val types = getPropertyValue(propertyName = "ios.selectIgnoreTypes")
                        ?: shirates.core.Const.IOS_SELECT_IGNORE_TYPES
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
                return getPropertyValue("android.titleSelector") ?: shirates.core.Const.ANDROID_TITLE_SELECTOR
            } else {
                return getPropertyValue("ios.titleSelector") ?: shirates.core.Const.IOS_TITLE_SELECTOR
            }
        }

    /**
     * webTitleSelector
     */
    val webTitleSelector: String
        get() {
            if (isAndroid) {
                return getPropertyValue("android.webTitleSelector") ?: shirates.core.Const.ANDROID_WEBTITLE_SELECTOR
            } else {
                return getPropertyValue("ios.webTitleSelector") ?: shirates.core.Const.IOS_WEBTITLE_SELECTOR
            }
        }

    /**
     * jquerySource
     */
    val jquerySource: String
        get() {
            return getPropertyValue("jquerySource") ?: shirates.core.Const.JQUERY_SOURCE
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