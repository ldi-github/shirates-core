package shirates.core.utility.misc

import org.apache.commons.exec.environment.EnvironmentUtils

object EnvironmentVariableUtility {

    /**
     * getEnvironmentVariables
     */
    fun getEnvironmentVariables(): Map<String, String> {

        val command = arrayOf("/bin/bash", "-c", "printenv")
        val process = ProcessBuilder(*command).start()

        val output = process.inputStream.bufferedReader().use { it.readText() }

        val map = EnvironmentUtils.getProcEnvironment()
        for (line in output.split("\n")) {
            val index = line.indexOf("=")
            if (index >= 0) {
                val key = line.substring(0, index).trim()
                val value = line.substring(index).trim()
                map[key] = value
            }
        }
        return map
    }
}