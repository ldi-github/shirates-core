package shirates.core.vision

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.printWarn
import shirates.core.utility.getStringOrNull
import shirates.core.utility.image.Rectangle
import shirates.core.utility.toPath

class TemplateMatchingResult(
    val jsonString: String,
) {
    val file: String
    val rectangle: Rectangle

    /**
     * candidates
     */
    val candidates: MutableList<Candidate> = mutableListOf()

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
            return error != null
        }

    init {
        try {
            val jso = JSONObject(jsonString)
            val jsonArray = jso.getJSONArray("candidates")
            if (jsonArray.length() > 0) {
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(0)
                    val distance = jsonObject.getFloat("distance")
                    val file = jsonObject.getStringOrNull("file")
                    val fileName = file.toPath().toFile().name
                    val rectangle = if (file == null) Rectangle() else Rectangle(fileName)
                    val c = Candidate(distance = distance, file = file, rectangle = rectangle)
                    candidates.add(c)
                }
                candidates.sortBy { it.distance }
                file = candidates.first().file!!
                rectangle = Rectangle(file.toPath().toFile().nameWithoutExtension)
            } else {
                file = ""
                rectangle = Rectangle()
            }
        } catch (t: Throwable) {
            error = TestDriverException("Could not parse json string.\n$jsonString", t)
            printWarn()
            throw error!!
        }
    }

    override fun toString(): String {
        return "file=$file, jsonString: \n$jsonString"
    }

}