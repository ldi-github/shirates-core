package shirates.spec.report.entity

import shirates.core.configuration.PropertiesManager
import shirates.spec.report.models.ParameterRepository
import shirates.spec.report.models.SpecSheetPosition
import shirates.spec.utilily.SpecResourceUtility
import java.nio.file.Path

class SpecReportData {

    var specReportFile: Path? = null
    val commandItems = mutableListOf<CommandItem>()
    val specLines = mutableListOf<SpecLine>()

    var okCount = 0
    var ngCount = 0
    var errorCount = 0
    var suspendedCount = 0
    var noneCount = 0

    var condAutoCount = 0
    var manualCount = 0
    var skipCount = 0
    var notImplCount = 0
    var excludedCount = 0

    var totalCount = 0

    val replaceOK = SpecResourceUtility.OK
    val replaceNG = SpecResourceUtility.NG
    val replaceERROR = SpecResourceUtility.ERROR
    val replaceSUSPENDED = SpecResourceUtility.SUSPENDED
    val replaceNONE = SpecResourceUtility.NONE
    val replaceCOND_AUTO = SpecResourceUtility.COND_AUTO
    val replaceSKIP = SpecResourceUtility.SKIP
    val replaceMANUAL = SpecResourceUtility.MANUAL
    val replaceNOTIMPL = SpecResourceUtility.NOTIMPL
    val replaceEXCLUDED = SpecResourceUtility.EXCLUDED
    val replaceDELETED = SpecResourceUtility.DELETED

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
            if (noLoadRun) return ""
            return p.getValue("profileName") ?: ""
        }
    val appIconName: String
        get() {
            return p.getValue("appIconName") ?: ""
        }
    val platformName: String
        get() {
            if (noLoadRun) return ""
            return p.getValue("platformName") ?: ""
        }
    val platformVersion: String
        get() {
            if (noLoadRun) return ""
            return p.getValue("appium:platformVersion") ?: ""
        }
    val deviceModel: String
        get() {
            if (noLoadRun) return ""
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
    val specReportExcludeDetail: Boolean
        get() {
            return p.getValue("specReport.exclude.detail") == "true"
        }

    val osSymbol: String
        get() {
            if (noLoadRun) {
                return ""
            }
            val s = ("${platformName.lowercase()} ").substring(0, 1).trim()
            if (s.isBlank()) {
                return ""
            } else {
                return "@$s"
            }
        }

    fun refreshParameters() {

        p.paramMap.clear()
        p.load(commandItems)

        val firstLine = commandItems.firstOrNull()
        if (firstLine != null) {
            testDateTime = shirates.spec.SpecConst.DATE_FORMAT.format(firstLine.logDateTime)
            testDate = testDateTime.substring(0, 10)
        }
    }

    fun refreshCount() {

        okCount = 0
        ngCount = 0
        errorCount = 0
        suspendedCount = 0
        noneCount = 0
        condAutoCount = 0
        manualCount = 0
        skipCount = 0
        notImplCount = 0
        excludedCount = 0


        for (line in specLines) {
            val r = line.result
            if (r.isBlank()) {
                continue
            }

            if (r == "OK" || r == replaceOK) {
                okCount++
            } else if (r == "NG" || r == replaceNG) {
                ngCount++
            } else if (r == "ERROR" || r == replaceERROR) {
                errorCount++
            } else if (r == "SUSPENDED" || r == replaceSUSPENDED) {
                suspendedCount++
            } else if (r == "NONE" || r == replaceNONE) {
                noneCount++
            } else if (r == "COND_AUTO" || r == replaceCOND_AUTO) {
                condAutoCount++
            } else if (r == "MANUAL" || r == replaceMANUAL) {
                manualCount++
            } else if (r == "SKIP" || r == replaceSKIP) {
                skipCount++
            } else if (r == "NOTIMPL" || r == replaceNOTIMPL) {
                notImplCount++
            } else if (r == "EXCLUDED" || r == replaceEXCLUDED) {
                excludedCount++
            }
        }

        totalCount = okCount + ngCount + errorCount + suspendedCount + noneCount + condAutoCount +
                condAutoCount + manualCount + skipCount + notImplCount + excludedCount
    }

    /**
     * refresh
     */
    fun refresh() {

        refreshCount()
        replaceResults()
    }

    fun replaceResults() {

        fun SpecLine.replaceResult(result: String, replace: String, reason: String? = null) {
            if (this.result != result && this.result != replace) {
                return
            }

            if (this.result == result) {
                if (replace.isNotBlank()) {
                    this.result = replace
                }
            }
            if (reason.isNullOrBlank()) {
                return
            }
            if (this.supplement.contains(reason)) {
                return
            }

            this.supplement = listOf(this.supplement, reason).filter { it.isBlank().not() }.joinToString("\n")
        }

        val p = PropertiesManager

        for (line in specLines) {
            line.replaceResult("OK", replaceOK)
            line.replaceResult("NG", replaceNG)
            line.replaceResult("ERROR", replaceERROR)
            line.replaceResult("SUSPENDED", replaceSUSPENDED)
            line.replaceResult("NONE", replaceNONE)
            line.replaceResult("COND_AUTO", replaceCOND_AUTO)
            line.replaceResult("SKIP", replaceSKIP, p.specReportSKIPReason)
            line.replaceResult("NOTIMPL", replaceNOTIMPL)
            line.replaceResult("EXCLUDED", replaceEXCLUDED, p.specReportEXCLUDEDReason)
            line.replaceResult("DELETED", replaceDELETED)
        }
    }
}