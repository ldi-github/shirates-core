package shirates.core.vision

import org.json.JSONArray
import shirates.core.logging.printWarn
import shirates.core.utility.getStringOrNull
import shirates.core.utility.image.Rectangle
import shirates.core.utility.toPath

class TemplateMatchingResult(
    val jsonString: String,
    val file: String,
    val rectangle: Rectangle,
) {
    private val _candidates = mutableListOf<Candidate>()

    /**
     * candidates
     */
    val candidates: List<Candidate>
        get() {
            if (_candidates.size == 0) {
                try {
                    val jsonArray = JSONArray(jsonString)
                    if (jsonArray.length() > 0) {
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(0)
                            val distance = jsonObject.getFloat("distance")
                            val file = jsonObject.getStringOrNull("file")
                            val fileName = file.toPath().toFile().name
                            val rectangle = if (file == null) Rectangle() else Rectangle(fileName)
                            val c = Candidate(distance = distance, file = file, rectangle = rectangle)
                            _candidates.add(c)
                        }
                    }
                } catch (t: Throwable) {
                    error = t
                    printWarn()
                    throw t
                }
            }
            return _candidates
        }

    /**
     * primaryCandidate
     */
    val primaryCandidate: Candidate
        get() {
            return candidates.firstOrNull() ?: Candidate(
                distance = Float.MAX_VALUE,
                file = null,
                rectangle = Rectangle()
            )
        }

    /**
     * error
     */
    var error: Throwable? = null
        private set

    /**
     * hasError
     */
    val hasError: Boolean
        get() {
            if (_candidates.size == 0) {
                candidates
            }
            return error != null
        }

    override fun toString(): String {
        return "file=$file, jsonString: \n$jsonString"
    }

}