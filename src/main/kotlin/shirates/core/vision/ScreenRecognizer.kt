package shirates.core.vision

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.getNicknameWithoutSuffix
import shirates.core.driver.testContext
import shirates.core.driver.vision
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.file.resolve
import shirates.core.utility.string.forVisionComparison
import shirates.core.utility.time.StopWatch
import shirates.core.vision.configration.repository.VisionScreenPredicateRepository
import shirates.core.vision.configration.repository.VisionScreenRepository
import shirates.core.vision.configration.repository.VisionTextIndexRepository
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

        val sw = StopWatch("recognizeScreen")
        try {
            return recognizeScreenCore(classifierName, screenImageFile)
        } finally {
            sw.printInfo()
        }
    }

    private fun recognizeScreenCore(classifierName: String, screenImageFile: String): String {

        /**
         * Determine the screen name with textIndex
         */
        val joinedText = vision.rootElement.joinedText.forVisionComparison()
        for (entry in VisionTextIndexRepository.imageIndexFiles) {
            if (entry.indexItems.isNotEmpty()) {
                val screenName = entry.screenName.getNicknameWithoutSuffix()
                var allFound = false
                for (item in entry.indexItems) {
                    val t = item.forVisionComparison()
                    allFound = joinedText.contains(t)
                    if (allFound.not()) {
                        break
                    }
                }
                if (allFound) {
                    TestLog.info("$screenName found by textIndex: ${entry.indexItems}")
                    return screenName
                }
            }
        }

        val classifierDirectory = PropertiesManager.visionBuildDirectory.resolve("vision/classifiers/$classifierName")
        val classifyScreenResult = VisionServerProxy.classifyScreenWithShard(
            inputFile = screenImageFile,
            classifierDirectory = classifierDirectory,
        )
        val screenCandidates = classifyScreenResult.classifyScreenResults.flatMap { it.classifications }
            .sortedByDescending { it.confidence }
        if (screenCandidates.isEmpty()) {
            return "?"
        }
        /**
         * Determine the screen name if confidence == 1.0
         */
        for (candidate in screenCandidates) {
            val screenName = LabelUtility.getShortLabel(candidate.identifier).getNicknameWithoutSuffix()
            if (candidate.confidence == 1.0f) {
                return screenName
            }
        }
        /**
         * Determine the screen name with screenPredicate
         */
        if (testContext.enableScreenPredicate) {
            val screenPredicates = VisionScreenPredicateRepository.getList()
            for (candidate in screenCandidates) {
                val screenName = LabelUtility.getShortLabel(candidate.identifier).getNicknameWithoutSuffix()
                val screenPredicate = screenPredicates.firstOrNull() { it.screenName == screenName }
                if (screenPredicate != null) {
                    val sw = StopWatch("screenPredicate ${screenPredicate.screenName}")
                    val r = screenPredicate.predicate.invoke()
                    sw.stop()
                    if (r) {
                        TestLog.info("screenPredicate: $screenName")
                        return screenName
                    } else {
                        val list = screenPredicates.filter { it.screenName != screenName }
                        for (identifier in list) {
                            val sw2 = StopWatch("screenPredicate ${screenPredicate.screenName}")
                            val r2 = identifier.predicate.invoke()
                            sw2.stop()
                            if (r2) {
                                TestLog.info("$screenName found by screenPredicate")
                                return identifier.screenName
                            }
                        }
                    }
                }
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
        val textMatchingInfoList = getScreenRecognizedTextMatchingInfoList(
            screenLabels = screenCandidates.map { it.identifier },
            recognizeTextObservations = rootElement.visionContext.recognizeTextObservations
        )
        if (textMatchingInfoList.isEmpty()) {
            return "?"
        }

        /**
         * Filter the list by the criteria
         * and take one that has highest matchTextScoreRate.
         */
        val highMatchedList =
            textMatchingInfoList.filter { it.matchedTextMap.count() > 5 && it.matchTextCountRate > 0.3 && it.matchTextScoreRate > 0.3 }
                .sortedByDescending { it.matchTextScoreRate }
        if (highMatchedList.any()) {
            val s = highMatchedList.first()
            TestLog.info("${s.shortScreenLabel} found by matchTextScoreRate")
            return s.shortScreenLabel.getNicknameWithoutSuffix()
        }

        /**
         * Filter screenCandidates
         * and take the highest confidence one.
         */
        val screenCandidate = screenCandidates.first()
        val confidenceThread = PropertiesManager.visionSyncImageMatchRate
        if (screenCandidate.confidence > confidenceThread) {
            val name = LabelUtility.getShortLabel(screenCandidate.identifier)
            TestLog.info("$name found by confidence > $confidenceThread")
            return name.getNicknameWithoutSuffix()
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
            val screenName = LabelUtility.getShortLabel(screenLabel).getNicknameWithoutSuffix()
            val screenEntry = VisionScreenRepository.getScreenEntry(screenName = screenName)
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