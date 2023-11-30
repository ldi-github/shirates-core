package shirates.spec.report.entity

class SpecLine(
    var type: String = "",

    var ID: Int = 0,
    var step: String = "",
    var condition: String = "",
    var action: String = "",
    var target: String = "",
    var expectation: String = "",
    var os: String = "",
    var special: String = "",
    var auto: String = "",
    var result: String = "",
    var date: String = "",
    var tester: String = "",
    var environment: String = "",
    var build: String = "",
    var supplement: String = "",
    var suspend: String = "",
    var ticketNo: String = "",
    var remarks: String = "",
    var importantMessage: String = ""
) {

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return toString().replace("0", "").trim() == ""
        }

    override fun toString(): String {

        val str =
            "$ID\t$step\t$condition\t$action\t$target\t$expectation\t$os\t$special\t$auto\t$result\t$date\t$tester\t$environment\t$build\t$supplement\t$suspend\t$ticketNo\t$remarks"
        return str.replace("\n", "\\n")
    }

}