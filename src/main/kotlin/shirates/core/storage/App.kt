package shirates.core.storage

import shirates.core.configuration.repository.DatasetRepository
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException

object App {

    /**
     * repository
     */
    var repository: DatasetRepository? = null
        get() {
            if (field == null) {
                field = DatasetRepositoryManager.getRepository("apps")
            }
            return field
        }

    /**
     * getValue
     *
     * longKey format: [datasetName].attributeName
     */
    fun getValue(longKey: String, throwsException: Boolean = true): String {

        if (DatasetRepositoryManager.repositories.containsKey("apps").not()) {
            if (throwsException) {
                throw TestConfigException("Repository 'apps' not found.")
            }
            return ""
        }
        return repository!!.getValue(longKey = longKey, throwsException = throwsException)
    }

}

/**
 * app
 */
fun app(longKey: String, throwsException: Boolean = true): String {

    if (TestMode.isNoLoadRun) {
        return longKey
    }
    return App.getValue(longKey = longKey, throwsException = throwsException)
}

/**
 * app
 */
fun app(datasetName: String, attributeName: String, throwsException: Boolean = true): String {

    val longKey = "${datasetName}.${attributeName}"

    if (TestMode.isNoLoadRun) {
        return longKey
    }
    return App.getValue(longKey = longKey, throwsException = throwsException)
}