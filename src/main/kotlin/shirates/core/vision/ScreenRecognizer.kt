package shirates.core.vision

object ScreenRecognizer {

    /**
     * recognizeScreen
     */
    fun recognizeScreen(
        screenImageFile: String,
        withTextMatching: Boolean = false,
        maxDistance: Double = 0.5,
    ): String {

        val result = VisionServerProxy.classifyWithImageFeaturePrintOrText(
            inputFile = screenImageFile,
            withTextMatching = withTextMatching,
            log = true,
        )
        val firstCandidate = result.firstCandidate ?: return "?"
        if (firstCandidate.distance > maxDistance) {
            return "?"
        }

        val name = firstCandidate.name
        return name
    }
}