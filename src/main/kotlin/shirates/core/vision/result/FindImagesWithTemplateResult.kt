package shirates.core.vision.result

import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.logging.printWarn
import shirates.core.utility.getStringOrNull
import shirates.core.utility.image.Rectangle
import shirates.core.utility.toPath
import shirates.core.vision.Candidate

class FindImagesWithTemplateResult(
    val jsonString: String,
    val localRegionX: Int = 0,
    val localRegionY: Int = 0,
    val horizontalMargin: Int = 0,
    val verticalMargin: Int = 0,
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
        internal set

    /**
     * hasError
     */
    val hasError: Boolean
        get() {
            return error != null
        }

    init {
        if (jsonString.isNotBlank()) {
            try {
                val jso = JSONObject(jsonString)
                val jsonArray = jso.getJSONArray("candidates")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val distance = jsonObject.getFloat("distance")
                        val file = jsonObject.getStringOrNull("file")
                        val fileName = file.toPath().toFile().name
                        val rectangle = if (file == null) Rectangle() else Rectangle(fileName)
                        val c = Candidate(
                            distance = distance,
                            file = file,
                            rectangle = rectangle,
                            localRegionX = localRegionX,
                            localRegionY = localRegionY,
                            rectOnLocalRegion = Rectangle(
                                left = rectangle.left,
                                top = rectangle.top,
                                width = rectangle.width,
                                height = rectangle.height
                            ),
                            horizontalMargin = horizontalMargin,
                            verticalMargin = verticalMargin,
                        )
                        candidates.add(c)
                        c.image
                    }
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
        } else {
            file = ""
            rectangle = Rectangle()
        }
    }

    private fun parse() {
    }

    override fun toString(): String {
        return "file=$file, jsonString: \n$jsonString"
    }

}