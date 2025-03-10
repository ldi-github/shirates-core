package shirates.core.storage

import shirates.core.configuration.repository.DatasetRepository
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.spec.utilily.removeBrackets

object App {

    /**
     * repository
     */
    var repository: DatasetRepository? = null
        get() {
            if (field == null) {
                if (DatasetRepositoryManager.hasRepository("apps")) {
                    field = DatasetRepositoryManager.getRepository("apps")
                }
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

/**
 * appId
 */
fun appId(datasetName: String): String {

    val r = app(datasetName = datasetName, attributeName = "packageOrBundleId", throwsException = false)
    if (r.isNotBlank()) {
        return r
    }
    return datasetName.removeBrackets()
}

/**
 * appIconName
 */
fun appIconName(datasetName: String): String {

    val r = app(datasetName = datasetName, attributeName = "appIconName", throwsException = false)
    if (r.isNotBlank()) {
        return r
    }
    return datasetName.removeBrackets()
}
