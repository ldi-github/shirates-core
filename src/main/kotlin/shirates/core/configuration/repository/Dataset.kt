package shirates.core.configuration.repository

/**
 * Dataset
 */
data class Dataset(
    var datasetName: String,
    var nameValues: MutableMap<String, String> = mutableMapOf()
) {
    /**
     * hasAny
     */
    val hasAny: Boolean
        get() {
            return nameValues.any()
        }

}
