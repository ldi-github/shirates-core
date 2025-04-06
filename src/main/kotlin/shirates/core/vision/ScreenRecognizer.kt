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
            val candidate = screenCandidates.first()
            if (candidate.confidence == 1.0f) {
                return LabelUtility.getShortLabel(screenCandidates.first().identifier)
            }
        }
        /**
         * Get texts from current screen image
         */
        val rootElement = vision.rootElement
        rootElement.visionContext.recognizeText()
        /**
         * Get texts of candidate screens
         * and get text matching score between current screen image and candidate screen images.
         * The result list is sorted by descending with matchScore.
         */
        val list = getScreenRecognizedTextMatchingInfoList(
            screenLabels = screenCandidates.map { it.identifier },
            recognizeTextObservations = rootElement.visionContext.recognizeTextObservations
        )
        if (list.isEmpty()) {
            return "?"
        }

        /**
         * Get the highest matchScore entry.
         * If matchKeywords is true, return it.
         */
        val screenName = list.first().shortScreenLabel
        val screenEntry = VisionScreenRepository.getScreenEntry(screenName)
        if (screenEntry.matchKeywords()) {
            return LabelUtility.getShortLabel(screenEntry.screenName)
        }

        /**
         * Filter the list by the criteria
         * and take one that has highest matchTextRate.
         */
        val highMatchedList =
            list.filter { it.matchedTextMap.count() > 5 && it.matchTextCountRate > 0.3 && it.matchTextScoreRate > 0.3 }
                .sortedByDescending { it.matchTextScoreRate }
        if (highMatchedList.any()) {
            val s = highMatchedList.first()
            return s.shortScreenLabel
        }

        /**
         * Filter screenCandidates
         * and take the highest confidence one.
         */
        val screenCandidate = screenCandidates.first()
        if (screenCandidate.confidence > 0.9) {
            return LabelUtility.getShortLabel(screenCandidate.identifier)
        }

        /**
         * Unidentified
         */
        return "?"
    }

    private fun getScreenRecognizedTextMatchingInfoList(
        screenLabels: List<String>,
        recognizeTextObservations: List<RecognizeTextObservation>
    ): List<ScreenRecognizedTextMatchingInfo> {

        val list = mutableListOf<ScreenRecognizedTextMatchingInfo>()
        for (screenLabel in screenLabels) {
            val screenEntry = VisionScreenRepository.getScreenEntry(screenName = screenLabel)
            var r = screenEntry.recognizeTextResult
            if (r == null) {
                screenEntry.recognizeTextResult =
                    VisionServerProxy.recognizeText(inputFile = screenEntry.labelFileInfo.primaryFile)
                r = screenEntry.recognizeTextResult
            }
            val entry = ScreenRecognizedTextMatchingInfo(screenLabel = screenLabel, textCandidates = r!!.candidates)
            list.add(entry)
            entry.matchWith(recognizeTextObservations = recognizeTextObservations)
        }
        return list.sortedByDescending { it.matchScore }
    }

    class ScreenRecognizedTextMatchingInfo(
        val screenLabel: String,
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

        val matchTextCountRate: Double
            get() {
                if (totalTextCount == 0) {
                    return 0.0
                }
                return matchedTextMap.count().toDouble() / totalTextCount
            }

        val matchTextScoreRate: Double
            get() {
                val count = matchedTextMap.count()
                if (count == 0) {
                    return 0.0
                }
                return matchScore.toDouble() / count
            }

        val shortScreenLabel: String
            get() {
                return LabelUtility.getShortLabel(screenLabel)
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