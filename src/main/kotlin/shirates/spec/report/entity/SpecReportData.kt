package shirates.spec.report.entity

import shirates.spec.report.models.ParameterRepository
import shirates.spec.report.models.SpecSheetPosition
import shirates.spec.utilily.SpecResourceUtility
import java.nio.file.Path

class SpecReportData {

    var specReportFile: Path? = null
    val logLines = mutableListOf<LogLine>()
    val specLines = mutableListOf<SpecLine>()

    var okCount = 0
    var ngCount = 0
    var errorCount = 0
    var condAuto = 0
    var manualCount = 0
    var skipCount = 0
    var notImplCount = 0
    var naCount = 0
    var suspendedCount = 0
    var emptyCount = 0

    lateinit var sheetPosition: SpecSheetPosition

    val p = ParameterRepository()

    var testClassName: String
        get() {
            return p.getValue("testClass")?.split('.')?.lastOrNull() ?: "UnknownClass"
        }
        set(value) {
            p.setValue("testClass", value)
        }
    val sheetName: String
        get() {
            return p.getValue("sheetName") ?: testClassName
        }
    val logLanguage: String
        get() {
            return p.getValue("logLanguage") ?: ""
        }
    val profileName: String
        get() {
            return p.getValue("profileName") ?: ""
        }
    val appIconName: String
        get() {
            return p.getValue("appIconName") ?: ""
        }
    val platformName: String
        get() {
            return p.getValue("platformName") ?: ""
        }
    val platformVersion: String
        get() {
            return p.getValue("appium:platformVersion") ?: ""
        }
    val deviceModel: String
        get() {
            if (platformName == "ANDROID") {
                return p.getValue("appium:deviceModel") ?: ""
            } else {
                return p.getValue("appium:deviceName") ?: ""
            }
        }
    var testDate = ""
    var testDateTime = ""
    var tester = "auto"
    val environment: String
        get() {
            return p.getValue("appEnvironment") ?: ""
        }
    val appBuild: String
        get() {
            return p.getValue("appBuild") ?: ""
        }
    val noLoadRun: Boolean
        get() {
            return p.getValue("noLoadRun") == "true"
        }

    val osSymbol: String
        get() {
            val s = ("${platformName.lowercase()} ").substring(0, 1).trim()
            if (s.isBlank()) {
                return ""
            } else {
                return "@$s"
            }
        }

    /**
     * refresh
     */
    fun refresh() {

        refreshParameters()
        refreshCounts()
    }

    internal fun refreshParameters() {

        p.paramMap.clear()
        p.load(logLines)

        val firstLine = logLines.firstOrNull()
        if (firstLine != null) {
            testDateTime = shirates.spec.SpecConst.DATE_FORMAT.format(firstLine.logDateTime)
            testDate = testDateTime.substring(0, 10)
        }
    }

    internal fun refreshCounts() {

        val group = specLines.groupBy { it.result }
        okCount = group["OK"]?.count() ?: 0
        ngCount = group["NG"]?.count() ?: 0
        errorCount = group["ERROR"]?.count() ?: 0
        condAuto = group["COND_AUTO"]?.count() ?: 0
        manualCount = group["MANUAL"]?.count() ?: 0
        skipCount = group["SKIP"]?.count() ?: 0
        suspendedCount = group[SpecResourceUtility.suspended]?.count() ?: 0
        notImplCount = group["NOTIMPL"]?.count() ?: 0
        naCount = group[SpecResourceUtility.notApplicable]?.count() ?: 0
        emptyCount = group[""]?.count() ?: 0
    }

}