package shirates.core.configuration.repository

import shirates.core.logging.TestLog

/**
 * ParameterRepository
 *
 * Storing metadata (testClass, sheetName, logLanguage, testrunFile, appIconName, capabilities, etc)
 * for reporting
 */
object ParameterRepository {
    val parameters = mutableMapOf<String, String>()

    /**
     * clear
     */
    fun clear() {
        parameters.clear()
    }

    /**
     * write
     */
    fun write(name: String, value: String) {

        parameters[name] = value
        TestLog.parameter(name, value)
    }
}