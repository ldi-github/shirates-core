package shirates.spec.report.models

import shirates.spec.report.entity.LogLine

class ParameterRepository() {

    val paramMap = mutableMapOf<String, String>()

    /**
     * load
     */
    fun load(logLines: List<LogLine>) {

        for (log in logLines.filter { it.command == "parameter" }) {
            val tokens = log.message.split(":").map { it.trim() }
            if (tokens.count() == 2) {
                val name = tokens[0]
                val value = tokens[1]
                paramMap[name] = value
            } else {
                paramMap[log.message] = log.message
            }
        }
    }

    /**
     * getValue
     */
    fun getValue(name: String): String? {

        if (paramMap.containsKey(name)) {
            return paramMap[name]
        }
        return null
    }

    /**
     * setValue
     */
    fun setValue(name: String, value: String) {

        paramMap[name] = value
    }

}