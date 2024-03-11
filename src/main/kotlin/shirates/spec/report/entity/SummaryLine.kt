package shirates.spec.report.entity

import java.nio.file.Path

class SummaryLine(
    var workbookPath: Path? = null,
    var sheetName: String = "",
    var testClassName: String = "",

    var no: Int = 0,
    var notApplicable: Int = 0,
    var ok: Int = 0,
    var ng: Int = 0,
    var error: Int = 0,
    var suspended: Int = 0,
    var condAuto: Int = 0,
    var manual: Int = 0,
    var skip: Int = 0,
    var notImpl: Int = 0,
    var a: Int = 0,
    var ca: Int = 0,
    var m: Int = 0,
) {
    override fun toString(): String {
        return "$no\t$sheetName\t$total\t$notApplicable\t$ok\t$ng\t$error\t$suspended\t$condAuto\t$manual\t$skip\t$notImpl\t$autoPlusManual\t$a\t$m\ta$automatedRatio"
    }

    val total: Int
        get() {
            return (notApplicable + ok + ng + error + suspended + condAuto + manual + skip + notImpl)
        }

    val autoPlusManual: Int
        get() {
            return a + ca + m
        }

    val automatedRatio: Double
        get() {
            if (autoPlusManual == 0) {
                return 0.0
            }
            return (a + ca).toDouble() / autoPlusManual
        }
}