package shirates.core.vision

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.vision
import shirates.core.utility.file.resolve
import shirates.core.vision.configration.repository.VisionScreenRepository
import shirates.core.vision.driver.commandextension.rootElement
import shirates.core.vision.result.RecognizeTextResult
import shirates.core.vision.utility.label.LabelUtility

object ScreenRecognizer {

    /**
     * recognizeScreen
     */
    fun recognizeScreen(
        screenImageFile: String,
        classifierName: String = "ScreenClassifier",
    ): String {

        val mlmodelFile: String =
            PropertiesManager.visionBuildDirectory.resolve("vision/classifiers/$classifierName/$classifierName.mlmodel")

        /**
         * Get screen candidates from current screen image
         */
        val classifyScreenResult = VisionServerProxy.classifyScreen(
            inputFile = screenImageFile,
            mlmodelFile = mlmodelFile,
        )
        val screenCandidates = classifyScreenResult.classifications.filter { it.confidence > 0.1 }
        if (screenCandidates.isEmpty()) {
            return "?"
        }
        if (screenCandidates.size == 1) {
            return LabelUtility.getShortLabel(screenCandidates.first().identifier)
        }
        /**
         * Get texts from current screen image
         */
        val rootElement = vision.rootElement
        rootElement.visionContext.recognizeText()
        /**
         * Get texts of candidate screens
         * and get text matching rate between current screen image and candidate screen images
         */
        val list = getScreenRecognizedTextMatchingInfoList(
            screenNames = screenCandidates.map { it.identifier },
            recognizeTextObservations = rootElement.visionContext.recognizeTextObservations
        )

        val screenName = list.firstOrNull()?.screenName ?: "?"
        return LabelUtility.getShortLabel(screenName)
    }

    private fun getScreenRecognizedTextMatchingInfoList(
        screenNames: List<String>,
        recognizeTextObservations: List<RecognizeTextObservation>
    ): List<ScreenRecognizedTextMatchingInfo> {

        val list = mutableListOf<ScreenRecognizedTextMatchingInfo>()
        for (screenName in screenNames) {
            val screenEntry = VisionScreenRepository.getScreenEntry(screenName = screenName)
            var r = screenEntry.recognizeTextResult
            if (r == null) {
                screenEntry.recognizeTextResult =
                    VisionServerProxy.recognizeText(inputFile = screenEntry.labelFileInfo.primaryFile)
                r = screenEntry.recognizeTextResult
            }
            val entry = ScreenRecognizedTextMatchingInfo(screenName = screenName, textCandidates = r!!.candidates)
            list.add(entry)
            entry.matchWith(recognizeTextObservations = recognizeTextObservations)
        }
        return list.sortedByDescending { it.matchScore }
    }

    class ScreenRecognizedTextMatchingInfo(
        val screenName: String,
        val textCandidates: List<RecognizeTextResult.Candidate>,
    ) {
        val matchedTextMap = mutableMapOf<String, Float>()
        val unmatchedTextMap = mutableMapOf<String, Float>()

        val totalTextCount: Int
            get() {
                return textCandidates.size
            }

        val matchScore: Float
            get() {
                if (totalTextCount == 0) {
                    return 0.0f
                }
                return matchedTextMap.values.sum()
            }

        /**
         * matchWith
         */
        fun matchWith(recognizeTextObservations: List<RecognizeTextObservation>) {
            for (t in textCandidates) {
                if (recognizeTextObservations.any { it.text == t.text }) {
                    matchedTextMap[t.text] = t.confidence
                } else {
                    unmatchedTextMap[t.text] = t.confidence
                }
            }
        }
    }
}