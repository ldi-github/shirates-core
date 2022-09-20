package shirates.core.storage

import shirates.core.configuration.repository.DatasetRepository
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.driver.TestMode

object Account {

    /**
     * repository
     */
    var repository: DatasetRepository? = null
        get() {
            if (field == null) {
                field = DatasetRepositoryManager.getRepository("accounts")
            }
            return field
        }

    /**
     * getValue
     *
     * longKey format: [datasetName].attributeName
     */
    fun getValue(longKey: String): String {

        val value = repository!!.getValue(longKey = longKey)
        return value
    }
}

/**
 * account
 */
fun account(longKey: String): String {

    if (TestMode.isNoLoadRun) {
        return longKey
    }
    return Account.getValue(longKey = longKey)
}

/**
 * account
 */
fun account(datasetName: String, attributeName: String): String {

    val longKey = "${datasetName}.${attributeName}"

    if (TestMode.isNoLoadRun) {
        return longKey
    }
    return Account.getValue(longKey = longKey)
}