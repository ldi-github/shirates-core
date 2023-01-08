package shirates.spec.report.entity

class TestListItem {
    var no: Int = 0
    var sheetName = ""
    var testClassName = ""
    var function = ""
    var order = null as Int?
    var scenario = ""
    var exec = ""
    var result = ""
    var lastExecuted = ""
    var message = ""

    val classNameFunctionName: String
        get() {
            return "$testClassName#$function"
        }

    override fun toString(): String {
        return "$no\t$sheetName\t$testClassName\t$function\t$order\t$scenario\t$exec\t$result\t$lastExecuted\t$message"
    }
}