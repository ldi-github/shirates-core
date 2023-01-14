package shirates.spec.report.models

import shirates.spec.report.entity.LogLine

class ParameterRepository() {

    val paramMap = mutableMapOf<String, String>()

    /**
     * load
     */
    fun load(logLines: List<LogLine>) {

        for (log in logLines.filter { it.command == "parameter" }) {
            val lastIndexOfSplitter = log.message.lastIndexOf(":")
            if (lastIndexOfSplitter != -1) {
                val name = log.message.substring(0, lastIndexOfSplitter).trim()
                val value = log.message.substring(lastIndexOfSplitter + 1).trim()
                paramMap[name] = value
            } else {
                paramMap[log.message] = log.message.trim()
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