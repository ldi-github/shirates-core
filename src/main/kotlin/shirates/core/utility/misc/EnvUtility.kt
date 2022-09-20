package shirates.core.utility.misc

object EnvUtility {

    private val envMap = mutableMapOf<String, String>()

    /**
     * reset
     */
    fun reset() {

        envMap.clear()
        for (env in System.getenv()) {
            envMap[env.key] = env.value
        }
    }

    /**
     * getEnv
     */
    fun getEnv(): Map<String, String> {

        if (envMap.isEmpty()) {
            reset()
        }

        return envMap
    }

    /**
     * getEnvValue
     */
    fun getEnvValue(name: String): String? {

        return getEnv()[name]
    }

    /**
     * setEnvForTesting
     */
    fun setEnvForTesting(name: String, value: String): MutableMap.MutableEntry<String, String> {

        envMap[name] = value
        return envMap.entries.first() { it.key == name }
    }

    /**
     * getSREnvMap
     */
    fun getSREnvMap(): Map<String, String> {

        return envMap.filter { it.key.startsWith("SR_") }.toMap()
    }

}