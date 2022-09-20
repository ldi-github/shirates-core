package shirates.core.storage

import shirates.core.configuration.repository.DatasetRepository
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.driver.TestMode

object Data {

    /**
     * repository
     */
    var repository: DatasetRepository? = null
        get() {
            if (field == null) {
                field = DatasetRepositoryManager.getRepository("data")
            }
            return field
        }

    /**
     * getValue
     *
     * longKey format: [dataSetName].attributeName
     */
    fun getValue(longKey: String): String {

        val value = repository!!.getValue(longKey = longKey)
        return value
    }
}

/**
 * data
 */
fun data(longKey: String): String {

    if (TestMode.isNoLoadRun) {
        return longKey
    }
    return Data.getValue(longKey = longKey)
}

/**
 * data
 */
fun data(datasetName: String, attributeName: String): String {

    val longKey = "${datasetName}.${attributeName}"

    if (TestMode.isNoLoadRun) {
        return longKey
    }
    return Data.getValue(longKey = longKey)
}