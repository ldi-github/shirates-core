package shirates.core.vision.configration.repository

import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.vision.configration.repository.VisionMLModelRepository.mlmodelClassifiers
import shirates.core.vision.result.RecognizeTextResult

object VisionScreenRepository {

    val screenMap = mutableMapOf<String, ScreenEntry>()

    /**
     * directory
     */
    val directory: String
        get() {
            return VisionMLModelRepository.screenClassifierRepository.imageClassifierDirectory
        }

    /**
     * hasRepository
     */
    val hasRepository: Boolean
        get() {
            return mlmodelClassifiers.containsKey("ScreenClassifier")
        }

    /**
     * labelMap
     */
    val labelMap: Map<String, VisionClassifierRepository.LabelFileInfo>
        get() {
            if (hasRepository.not()) {
                return mapOf()
            }
            return VisionMLModelRepository.screenClassifierRepository.labelMap
        }

    /**
     * setup
     */
    fun setup() {

        if (hasRepository.not()) {
            return
        }
        screenMap.clear()
        for (key in labelMap.keys) {
            val labelFileInfo = labelMap[key]!!
            val screenEntry = ScreenEntry(
                screenName = key,
                labelFileInfo = labelFileInfo
            )
            screenMap[key] = screenEntry
        }
    }

    /**
     * isRegistered
     */
    fun isRegistered(screenName: String): Boolean {

        return screenMap.keys.any { it.contains(screenName) }
    }

    /**
     * getScreenEntry
     */
    fun getScreenEntry(screenName: String): ScreenEntry {

        if (screenMap.containsKey(screenName)) {
            return screenMap[screenName]!!
        }
        val keys = screenMap.keys.filter { it.contains(screenName) }
        if (TestMode.isAndroid) {
            val key = keys.firstOrNull() { it.contains("@a") }
            if (key != null) {
                return screenMap[key]!!
            }
        } else if (TestMode.isiOS) {
            val key = keys.firstOrNull() { it.contains("@i") }
            if (key != null) {
                return screenMap[key]!!
            }
        }

        if (keys.any()) {
            val key = keys.first()
            return screenMap[key]!!
        }
        throw TestConfigException("Screen not registered in ScreenClassifier. (screenName=$screenName)")
    }

    class ScreenEntry(
        val screenName: String,
        val labelFileInfo: VisionClassifierRepository.LabelFileInfo,
        var recognizeTextResult: RecognizeTextResult? = null
    )
}