package shirates.core.storage

import org.json.JSONObject
import shirates.core.configuration.repository.DatasetRepository
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.driver.TestMode

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
    fun getValue(longKey: String): String {

        val value = repository!!.getValue(longKey = longKey)
        return value
    }

    /**
     * getAppNameOfPackageName
     */
    fun getAppNameOfPackageName(packageName: String): String {

        val jsonObject = repository!!.jsonObject
        for (key in jsonObject.keySet().filter { it != "key" }) {
            val e = jsonObject[key] as JSONObject? ?: continue
            if (e.keySet().contains("packageOrBundleId").not()) continue
            if (e["packageOrBundleId"] == packageName) {
                return key
            }
        }
        return ""
    }
}

/**
 * app
 */
fun app(longKey: String): String {

    if (TestMode.isNoLoadRun) {
        return longKey
    }
    return App.getValue(longKey = longKey)
}

/**
 * app
 */
fun app(datasetName: String, attributeName: String): String {

    val longKey = "${datasetName}.${attributeName}"

    if (TestMode.isNoLoadRun) {
        return longKey
    }
    return App.getValue(longKey = longKey)
}