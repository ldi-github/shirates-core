package shirates.core.vision.configration.repository

import shirates.core.driver.TestMode
import shirates.core.driver.vision
import shirates.core.exception.TestConfigException
import shirates.core.utility.file.toFile
import shirates.core.vision.configration.repository.VisionMLModelRepository.mlmodelClassifiers
import shirates.core.vision.driver.commandextension.canDetect
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
        var recognizeTextResult: RecognizeTextResult? = null,
    ) {
        private var _imageFileKeywords: ImageFileKeywords? = null
        val imageFileKeywords: ImageFileKeywords
            get() {
                if (_imageFileKeywords == null) {
                    _imageFileKeywords = ImageFileKeywords(screenName = screenName)
                    for (file in labelFileInfo.files) {
                        _imageFileKeywords!!.registerImageFile(file = file)
                    }
                }
                return _imageFileKeywords!!
            }

        fun matchKeywords(): Boolean {

            val map = imageFileKeywords.imageFileKeywordsMap
            if (map.isEmpty()) {
                return false
            }
            for (key in map.keys) {
                val keywords = map[key]!!
                fun allKeywordsMatched(): Boolean {
                    for (keyword in keywords) {
                        val found = vision.canDetect(expression = keyword, allowScroll = false)
                        if (found.not()) {
                            return false
                        }
                    }
                    return true
                }
                if (allKeywordsMatched()) {
                    return true
                }
            }
            return false
        }
    }

    class ImageFileKeywords(
        val screenName: String,
        val imageFileKeywordsMap: MutableMap<String, List<String>> = mutableMapOf()
    ) {
        /**
         * registerImageFile
         */
        fun registerImageFile(file: String): List<String> {

            fun String.getKeywordsPart(): String {

                val startIndex = this.lastIndexOf("(")
                if (startIndex < 0) {
                    return ""
                }
                val endIndex = this.lastIndexOf(")")
                if (endIndex < 0) {
                    return ""
                }
                return this.substring(startIndex, endIndex)
            }

            val name = file.toFile().nameWithoutExtension
            if (name.contains("_binary")) {
                return mutableListOf()
            }

            val keywordsPart = name.getKeywordsPart()
            val keywords = keywordsPart.trim('_', '(', ')').split("_").filter { it.trim().isNotBlank() }
            imageFileKeywordsMap[file] = keywords

            return keywords
        }
    }
}