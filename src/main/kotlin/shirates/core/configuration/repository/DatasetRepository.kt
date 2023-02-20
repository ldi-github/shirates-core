package shirates.core.configuration.repository

import org.json.JSONObject
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message

/**
 * DatasetRepository
 */
class DatasetRepository(
    val repositoryName: String,
    val jsonObject: JSONObject
) {
    /**
     * contains
     */
    fun contains(datasetName: String): Boolean {

        return jsonObject.has(datasetName)
    }

    /**
     * getDataset
     */
    fun getDataset(datasetName: String): Dataset {

        val dataset = Dataset(datasetName = datasetName)

        if (contains(datasetName)) {
            val jsonObject = jsonObject.getJSONObject(datasetName)
            for (key in jsonObject.keys()) {
                val obj = jsonObject.get(key)
                if ((obj is String).not()) {
                    throw TestConfigException(message(id = "setValueAsString", key = key, value = "$obj"))
                }
                dataset.nameValues[key] = "$obj"
            }
        }

        return dataset
    }

    /**
     * getValue
     *
     * longKey format: datasetName.attributeName
     */
    fun getValue(longKey: String, throwsException: Boolean = true): String {

        val (datasetName, attributeName) = getDatasetNameAndAttributeName(longKey)

        if (contains(datasetName = datasetName).not()) {
            if (throwsException)
                throw TestConfigException(
                    message(
                        id = "datasetNotFoundInRepository",
                        repository = repositoryName,
                        dataset = datasetName
                    )
                )
            else {
                return ""
            }
        }

        val dataset = getDataset(datasetName = datasetName)

        if (dataset.nameValues.containsKey(attributeName).not()) {
            if (throwsException) {
                throw TestConfigException(
                    message(
                        id = "attributeNotFoundInDataset",
                        repository = repositoryName,
                        dataset = datasetName,
                        attribute = attributeName
                    )
                )
            } else {
                return ""
            }
        }

        return dataset.nameValues[attributeName]!!
    }

    private fun getDatasetNameAndAttributeName(longKey: String): Pair<String, String> {

        val lastDot = longKey.lastIndexOf(".")
        if (lastDot < 0) {
            throw TestConfigException("invalid format (longKey=$longKey). sectionName.attributeName is required.")
        }

        val sectionName = longKey.substring(0, lastDot)
        val attributeName = longKey.substring(lastDot + 1)
        return Pair(sectionName, attributeName)
    }

}