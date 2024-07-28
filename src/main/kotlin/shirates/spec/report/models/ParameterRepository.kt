package shirates.spec.report.models

import shirates.spec.report.entity.CommandItem

class ParameterRepository() {

    val paramMap = mutableMapOf<String, String>()

    /**
     * load
     */
    fun load(commandItems: List<CommandItem>) {

        for (commandItem in commandItems.filter { it.command == "parameter" }) {
            val lastIndexOfSplitter = commandItem.message.lastIndexOf(":")
            if (lastIndexOfSplitter != -1) {
                val name = commandItem.message.substring(0, lastIndexOfSplitter).trim()
                val value = commandItem.message.substring(lastIndexOfSplitter + 1).trim()
                paramMap[name] = value
            } else {
                paramMap[commandItem.message] = commandItem.message.trim()
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