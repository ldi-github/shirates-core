package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.vision.configration.repository.VisionClassifierRepository

class ClassifyImageWithShardResult(
    val jsonString: String
) {
    /**
     * classifyImageResults
     */
    val classifyImageResults = mutableListOf<ClassifyImageResult>()

    /**
     * primaryClassification
     */
    val primaryClassification: ClassifyImageResult.Classification
        get() {
            if (_primaryClassification == null) {
                _primaryClassification = classifications.first()
            }
            return _primaryClassification!!
        }
    private var _primaryClassification: ClassifyImageResult.Classification? = null

    /**
     * getClassifications
     */
    val classifications: List<ClassifyImageResult.Classification>
        get() {
            if (_classifications == null) {
                _classifications =
                    classifyImageResults.flatMap { it.classifications }.sortedByDescending { it.confidence }
            }
            return _classifications!!
        }
    private var _classifications: List<ClassifyImageResult.Classification>? = null

    init {
        val jsonObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            val msg = "Error while parsing JSON results. $jsonString"
            TestLog.warn(msg)
            throw TestDriverException(msg, t)
        }
        if (jsonObject.has("error")) {
            val reason = jsonObject.getString("reason")
            if (reason.contains("File not found") && reason.contains(".mlmodel"))
                throw TestDriverException("mlmodel file not found. $reason")
        }

        val jsonArray = jsonObject.getJSONArray("items")
        for (i in 0 until jsonArray.length()) {
            val jso = jsonArray.getJSONObject(i)
            val item = ClassifyImageResult(jso.toString())
            classifyImageResults.add(item)
        }
    }

    override fun toString(): String {
        return jsonString
    }

    /**
     * getCandidates
     */
    fun getCandidates(
        count: Int = VisionClassifierRepository.defaultClassifier.shardCount
    ): List<ClassifyImageResult.Classification> {

        val list = classifyImageResults.flatMap { it.classifications }
            .sortedByDescending { it.confidence }.take(count).filter { it.confidence > 0.1 }
        return list
    }

}