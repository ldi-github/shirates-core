package shirates.core.vision

import org.apache.commons.text.similarity.LevenshteinDistance
import org.json.JSONArray
import org.json.JSONObject
import shirates.core.exception.TestDriverException
import shirates.core.vision.configration.repository.ScreenTextRepository

object ScreenRecognizer {

    /**
     * recognizeScreen
     */
    fun recognizeScreen(
        screenImageFile: String,
        allowableDifferenceRate: Double = 0.1,
    ): String {

        return getScreenTextDistanceInfo(
            screenImageFile = screenImageFile,
            allowableDifferenceRate = allowableDifferenceRate
        )?.screenTextInfo?.screenName ?: "?"
    }

    /**
     * getScreenTextDistanceInfo
     */
    fun getScreenTextDistanceInfo(
        screenImageFile: String,
        allowableDifferenceRate: Double = 0.1,
    ): ScreenTextDistanceInfo? {

        val jsonString = SrvisionProxy.callTextRecognizer(inputFile = screenImageFile)

        val jsonArray = try {
            JSONArray(jsonString)

        } catch (t: Throwable) {
            throw TestDriverException(
                "Could not recognize screen image file. (screenImageFile=$screenImageFile)\n$jsonString",
                cause = t
            )
        }

        val texts = jsonArray.map { (it as JSONObject).getString("text") }
        val joinedText = texts.joinToString("\n")
        val joinedTextLength = joinedText.length
        if (joinedText.isEmpty()) {
            return null
        }

        /**
         * Calculate LevenshteinDistance between current screen and each screen(registered in repository).
         */
        val screenInfoList = ScreenTextRepository.getScreenTextInfoListSortedByLengthDistance(length = joinedTextLength)
        val distanceInfoList = screenInfoList.map { it.calculateDistance(joinedText) }.sortedBy { it.distance }
        val filteredList = distanceInfoList.filter { it.diffRate < allowableDifferenceRate }
        val screenTextDistanceInfo = filteredList.firstOrNull()

        return screenTextDistanceInfo
    }

    class ScreenTextDistanceInfo(
        val screenTextInfo: ScreenTextRepository.ScreenTextInfo,
        var targetText: String
    ) {
        var distance: Int = Int.MAX_VALUE
        var diffRate: Double = Double.NaN

        override fun toString(): String {
            return "${screenTextInfo.screenName} distance: $distance, diffRate: $diffRate"
        }

        init {
            this.distance = LevenshteinDistance().apply(screenTextInfo.joinedText, targetText)
            this.diffRate =
                Math.abs((screenTextInfo.joinedTextLength - targetText.length) / targetText.length.toDouble())
        }
    }
}