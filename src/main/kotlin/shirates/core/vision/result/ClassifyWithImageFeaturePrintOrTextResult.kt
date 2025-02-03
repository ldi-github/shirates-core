package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException

class ClassifyWithImageFeaturePrintOrTextResult(
    val jsonString: String
) {
    var withTextMatching: Boolean
    var baseImageFile: String
    var textMatchingRequiredDiffThreshold: Double
    var firstDistance: Double
    var secondDistance: Double = Double.NaN
    var diffBetweenFirstAndSecond: Double = Double.NaN
    var candidates: List<Candidate>

    /**
     * firstCandidate
     */
    val firstCandidate: Candidate?
        get() {
            return candidates.firstOrNull()
        }

    init {
        val jsonObject = try {
            JSONObject(jsonString)
        } catch (t: Throwable) {
            throw TestDriverException("Could not parse json.\n$jsonString", cause = t)
        }
        if (jsonObject.has("error") && jsonObject.getBoolean("error")) {
            val reason = jsonObject.getString("reason")
            throw TestDriverException(reason)
        }
        withTextMatching = jsonObject.getBoolean("withTextMatching")
        baseImageFile = jsonObject.getString("baseImageFile")
        textMatchingRequiredDiffThreshold = jsonObject.getDouble("textMatchingRequiredDiffThreshold")
        firstDistance = jsonObject.getDouble("firstDistance")
        secondDistance = jsonObject.getDouble("secondDistance")
        diffBetweenFirstAndSecond = jsonObject.getDouble("diffBetweenFirstAndSecond")

        candidates = jsonObject.getJSONArray("candidates").map {
            val jso = it as JSONObject
            Candidate(
                distance = jso.getDouble("distance"),
                imageFile = jso.getString("imageFile"),
                name = jso.getString("name"),
            )
        }
    }

    override fun toString(): String {
        return jsonString
    }

    class Candidate(
        val distance: Double,
        val imageFile: String,
        val name: String,
    ) {
        override fun toString(): String {
            return "$distance, $name, $imageFile"
        }
    }
}