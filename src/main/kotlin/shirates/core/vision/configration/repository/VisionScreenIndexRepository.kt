package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.TestLog
import shirates.core.logging.printWarn
import shirates.core.utility.file.exists
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile

object VisionScreenIndexRepository {

    val screenIndexMap = mutableMapOf<String, ScreenIndexEntry>()

    /**
     * setup
     */
    fun setup(
        screenIndexFile: String = PropertiesManager.visionDirectory.resolve("classifiers/ScreenClassifier/screen-index.tsv")
    ) {
        if (screenIndexFile.exists().not()) {
            TestLog.info("screenIndex file not found. ($screenIndexFile)")
            return
        }

        val lines = screenIndexFile.toFile().readLines()
        for (line in lines) {
            if (line.startsWith("[")) {
                val entry = ScreenIndexEntry().parse(line = line)
                screenIndexMap[entry.screenName] = entry
            }
        }
    }

    /**
     * register
     */
    fun register(
        screenName: String,
        index: List<String>
    ) {
        val entry = ScreenIndexEntry(screenName = screenName, index = index)
        screenIndexMap[screenName] = entry
    }

    /**
     * remove
     */
    fun remove(
        screenName: String
    ) {
        if (screenIndexMap.containsKey(screenName).not()) {
            printWarn("removing screenIndex is skipped because the screenIndex is not registered. (screenName=$screenName)")
            return
        }
        screenIndexMap.remove(screenName)
    }

    /**
     * clear
     */
    fun clear() {
        screenIndexMap.clear()
    }

    /**
     * getIndex
     */
    fun getIndex(
        screenName: String
    ): List<String> {
        if (screenIndexMap.containsKey(screenName)) {
            return screenIndexMap[screenName]!!.index
        }
        return listOf()
    }

    /**
     * getList
     */
    fun getList(): List<ScreenIndexEntry> {
        return screenIndexMap.values.toList()
    }

    /**
     * ScreenIndexEntry
     */
    class ScreenIndexEntry(
        var screenName: String = "",
        var index: List<String> = listOf()
    ) {
        /**
         * parse
         */
        fun parse(line: String): ScreenIndexEntry {
            val tokens = line.split("\t").toMutableList()
            if (tokens.isEmpty()) {
                throw TestConfigException("Failed to parse the text as screenIndex. ($line)")
            }

            screenName = tokens[0]
            tokens.removeAt(0)
            index = tokens.toList()

            return this
        }

    }
}