package shirates.spec.code.entity

class TestClass(
    var testClassName: String,
    var sheetName: String,
    var scenarios: List<Scenario>
) {

    /**
     * getCodeLines
     */
    fun getCodeLines(): MutableList<String> {

        val lines = mutableListOf<String>()

        lines.add("package generated")
        lines.add("")
        lines.add("import org.junit.jupiter.api.DisplayName")
        lines.add("import org.junit.jupiter.api.Test")
        lines.add("import shirates.core.testcode.Manual")
        lines.add("import shirates.core.testcode.SheetName")
        lines.add("import shirates.core.vision.driver.branchextension.*")
        lines.add("import shirates.core.vision.driver.commandextension.*")
        lines.add("import shirates.core.vision.testcode.*")
        lines.add("")
        lines.add("@SheetName(\"$sheetName\")")
        lines.add("class $testClassName : VisionTest() {")
        for (scenario in scenarios) {
            lines.add("")
            lines.addAll(scenario.getCodeLines())
        }
        lines.add("")
        lines.add("}")  // end of TestClass

        return lines
    }

    /**
     * getCodeString
     */
    fun getCodeString(): String {

        val lines = getCodeLines()
        var tab = 0
//        var dotIndent = 0
        val tabs = mutableListOf(
            "",
            "\t",
            "\t\t",
            "\t\t\t",
            "\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t"
        )

        val dotIndentMap = mutableMapOf<Int, String>()

        fun dotIndent(): String {

            val result = dotIndentMap.values.joinToString("")
            return result
        }

        fun refreshDotIndentMap(line: String) {
            if (line.startsWith("it.")) {
                dotIndentMap[tab] = ""
            } else if (line.startsWith(".")) {
                dotIndentMap[tab] = "\t"
            } else if (dotIndentMap[tab] == null) {
                dotIndentMap[tab] = ""
            }
        }

        val tempLines = mutableListOf<String>()

        fun incrementTab() {
            tab++
            dotIndentMap[tab] = ""
        }

        fun decrementTab() {
            dotIndentMap.remove(tab)
            tab--
        }

        fun addLine(line: String) {
            refreshDotIndentMap(line)
            val tempLine = tabs[tab] + dotIndent() + line
            tempLines.add(tempLine)
        }

        for (i in 0 until lines.count()) {
            val line = lines[i].trim()
            try {
                if (line.startsWith("}") && line.endsWith("{")) {
                    decrementTab()
                    addLine(line)
                    incrementTab()
                } else if (line.endsWith("{")) {
                    addLine(line)
                    incrementTab()
                } else if (line.startsWith("}")) {
                    decrementTab()
                    addLine(line)
                } else {
                    addLine(line)
                }
            } catch (t: Throwable) {
                println(t)
                println(tempLines.joinToString("\n"))
                break
            }
        }

        val sb = StringBuilder()
        for (line in tempLines) {
            sb.appendLine(line)
        }

        return sb.toString()
    }
}