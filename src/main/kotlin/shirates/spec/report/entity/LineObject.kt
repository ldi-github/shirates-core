package shirates.spec.report.entity

class LineObject(var type: String) {

    var id: String = ""
    var step: String = ""
    val conditions = mutableListOf<String>()
    val actions = mutableListOf<String>()
    var target = ""
    val expectations = mutableListOf<String>()
    var os = ""
    var special = ""
    var auto = ""
    var result = ""
    var date = ""
    var tester = ""
    var environment = ""
    var build = ""
    var supplement = ""
    var indentLevel = 0

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            val r = step.isBlank() &&
                    conditions.isEmpty() &&
                    actions.isEmpty() &&
                    target.isBlank() &&
                    expectations.isEmpty() &&
                    os.isBlank() &&
                    special.isBlank() &&
                    auto.isBlank() &&
                    result.isBlank() &&
                    date.isBlank() &&
                    tester.isBlank() &&
                    environment.isBlank() &&
                    build.isBlank() &&
                    supplement.isBlank()
            return r
        }

    /**
     * incrementIndent
     */
    fun incrementIndent(): Int {

        indentLevel++
        return indentLevel
    }

    /**
     * decrementIndent
     */
    fun decrementIndent(): Int {

        indentLevel--
        if (indentLevel < 0) {
            indentLevel = 0
        }
        return indentLevel
    }

    /**
     * resetIndent
     */
    fun resetIndent(): Int {
        indentLevel = 0
        return indentLevel
    }

    /**
     * clear
     */
    fun clear() {

        conditions.clear()
        actions.clear()
        target = ""
        os = ""
        special = ""
        auto = ""
        result = ""
        date = ""
        tester = ""
        environment = ""
        build = ""
        supplement = ""
    }

    /**
     * toSpecLine
     */
    fun toSpecLine(): SpecLine {

        val s = SpecLine()
        s.type = type
        s.step = step
        s.condition = conditions.joinToString("\n")
        s.action = actions.joinToString("\n")
        s.target = target
        s.expectation = expectations.joinToString("\n")
        s.os = os
        s.special = special
        s.auto = auto
        s.result = result
        s.date = date
        s.environment = environment
        s.build = build
        s.tester = tester
        s.supplement = supplement

        return s
    }

    /**
     * toString
     */
    override fun toString(): String {

        val condition = conditions.joinToString("\\n")
        val action = actions.joinToString("\\n")
        val expectation = expectations.joinToString("\\n")

        return "$id\t$step\t$condition\t$action\t$target\t$expectation\t$os\t$special\t$auto\t$result\t$date\t$tester\t$environment\t$build\t$supplement"
    }
}