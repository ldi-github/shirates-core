package shirates.core.vision.configration.repository

import org.json.JSONObject
import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import java.io.FileNotFoundException
import java.nio.file.Files

object ScreenTextRepository {

    internal val screenTextMap = mutableMapOf<String, ScreenTextInfo>()

    /**
     * screenInfoList
     */
    val screenInfoList: List<ScreenTextInfo>
        get() {
            if (_screenInfoList.isEmpty()) {
                _screenInfoList = screenTextMap.values
                    .sortedBy { it.joinedTextLength }
                    .toMutableList()
            }
            return _screenInfoList
        }
    private var _screenInfoList: MutableList<ScreenTextInfo> = mutableListOf()

    /**
     * setup
     */
    fun setup(
        screenTextFile: String = PropertiesManager.visionDirectory.toPath().resolve("screens/screenText.json")
            .toString()
    ) {
        if (Files.exists(screenTextFile.toPath()).not()) {
            throw FileNotFoundException("screenTextFile not found. (screenTextFile=$screenTextFile)")
        }
        TestLog.info("Loading screen text repository.(screenTextFile=$screenTextFile)")

        try {
            val jso = JSONObject(Files.readString(screenTextFile.toPath()))
            for (key in jso.keys()) {
                val textArray = jso.getJSONArray(key)
                val screenTextInfo = ScreenTextInfo(
                    screenName = key,
                    texts = textArray.map { it.toString() }
                )
                screenTextMap[key] = screenTextInfo
            }
        } catch (t: Throwable) {
            throw TestConfigException(
                message = "Could not load screen text file. (screenTextFile=$screenTextFile)",
                cause = t
            )
        }
    }

    /**
     * clear
     */
    fun clear() {
        screenTextMap.clear()
        _screenInfoList.clear()
    }

    /**
     * getScreenTextInfo
     */
    fun getScreenTextInfo(screenName: String): ScreenTextInfo? {

        if (screenTextMap.containsKey(screenName).not()) {
            return null
        }
        return screenTextMap[screenName]!!
    }

    /**
     * getScreenTextList
     */
    fun getScreenTextList(screenName: String): List<String> {

        if (screenTextMap.containsKey(screenName).not()) {
            return listOf()
        }
        return screenTextMap[screenName]!!.texts
    }

    /**
     * getScreenTextInfoListSortedByLengthDistance
     */
    fun getScreenTextInfoListSortedByLengthDistance(length: Int): List<ScreenTextInfo> {

        val map = mutableMapOf<ScreenTextInfo, Int>()
        for (info in screenInfoList) {
            val lengthDistance = info.joinedTextLength
            map[info] = lengthDistance
        }
        val entries = map.entries.sortedBy { it.value }
        return entries.map { it.key }
    }

    class ScreenTextInfo(
        val screenName: String,
        val texts: List<String>,
    ) {
        /**
         * joinedText
         */
        val joinedText: String
            get() {
                if (_joinedText == null) {
                    _joinedText = texts.joinToString("\n")
                }
                return _joinedText!!
            }
        private var _joinedText: String? = null

        /**
         * joinedTextLength
         */
        val joinedTextLength: Int
            get() {
                if (_length == null) {
                    _length = joinedText.length
                }
                return _length!!
            }
        private var _length: Int? = null

        override fun toString(): String {
            return "$screenName\n${joinedText}"
        }

    }
}