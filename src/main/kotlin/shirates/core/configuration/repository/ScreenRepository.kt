package shirates.core.configuration.repository

import shirates.core.configuration.ScreenInfo
import shirates.core.configuration.Selector
import shirates.core.configuration.isValidNickname
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.file.exists
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Files

/**
 * ScreenRepository
 */
object ScreenRepository {

    var screensDirectory: String = ""
    var importDirectories: List<String> = listOf()

    val screenInfoMap = mutableMapOf<String, ScreenInfo>()
    val nicknameIndex = mutableMapOf<String, MutableList<ScreenInfo>>()

    val screenInfoSearchList: List<ScreenInfo>
        get() {
            return screenInfoMap.values.sortedByDescending { it.searchWeight }
        }

    val tempSelectorList = mutableListOf<Pair<String, String>>()

    /**
     * getTempSelectorExpression
     */
    fun getTempSelectorExpression(nickName: String): String {

        val entry = tempSelectorList.find { it.first == nickName }
        if (entry != null) return entry.second
        return ""
    }

    /**
     * clear
     */
    fun clear() {
        screenInfoMap.clear()
        nicknameIndex.clear()
        tempSelectorList.clear()
    }

    /**
     * get
     */
    operator fun get(key: String): ScreenInfo {
        if (screenInfoMap.containsKey(key)) {
            return screenInfoMap[key]!!
        } else {
            throw TestConfigException("key is not registered.(key='$key')")
        }
    }

    /**
     * set
     */
    operator fun set(repositoryKey: String, value: ScreenInfo) {
        screenInfoMap[repositoryKey] = value
    }

    /**
     * has
     */
    fun has(repositoryKey: String): Boolean {
        return screenInfoMap.containsKey(repositoryKey)
    }

    /**
     * getScreenInfo
     */
    fun getScreenInfo(screenName: String): ScreenInfo {

        if (has(screenName).not()) {
            throw TestConfigException(message(id = "screenNotRegistered", subject = screenName))
        }

        return get(screenName)
    }

    /**
     * getSelector
     */
    fun getSelector(screenName: String, nickName: String): Selector {

        val screenInfo = getScreenInfo(screenName = screenName)

        return screenInfo.getSelector(expression = nickName)
    }

    /**
     * setup
     */
    fun setup(screensDirectory: String, importDirectories: List<String> = mutableListOf()) {

        if (screensDirectory.exists().not()) {
            if (TestMode.isClassicTest) {
                throw TestConfigException(message(id = "screensDirectoryNotFound", file = screensDirectory))
            } else {
                return  // VisionTest
            }
        }
        if (Files.isDirectory(screensDirectory.toPath()).not()) {
            val message = "screensDirectory is not directory. ($screensDirectory)"
            if (TestMode.isClassicTest) {
                throw TestConfigException(message)
            } else {
                TestLog.warn(message)
                return  // VisionTest
            }
        }
        this.screensDirectory = screensDirectory
        this.importDirectories = importDirectories
        this.clear()

        loadFromFiles()
        updateNicknameIndex()
    }

    internal fun loadFromFiles() {

        for (directory in importDirectories) {
            loadFromDirectory(directory)
        }
        loadFromDirectory(screensDirectory)
    }

    private fun loadFromDirectory(directory: String): List<File> {

        if (Files.isDirectory(directory.toPath()).not()) {
            return mutableListOf()
        }
        TestLog.info("Loading screen files.(directory=$directory)")

        val files = File(directory.toPath().toUri()).walkTopDown()
            .filter {
                it.name.lowercase().endsWith(".json") &&
                        it.nameWithoutExtension.startsWith("[")
                        && it.nameWithoutExtension.endsWith("]")
            }.toMutableList()

        // Create screenBaseInfo from [screen-base].json
        val screenBaseFiles = files.filter { it.name == "[screen-base].json" }
        val screenBaseFile = screenBaseFiles.firstOrNull()
        for (file in screenBaseFiles) {
            files.remove(file)
        }
        val screenBaseInfo =
            if (screenBaseFile != null) ScreenInfo(screenFile = screenBaseFile.absolutePath)
            else null
        if (screenBaseInfo != null) {
            set("", screenBaseInfo)
        } else {
            set("", ScreenInfo())
        }

        for (file in files) {
            val screenInfo = ScreenInfo(screenFile = file.absolutePath, screenBaseInfo = screenBaseInfo)
            val key = screenInfo.key
            // check key duplication
            if (this.screenInfoMap.containsKey(key)) {
                val r = this.screenInfoMap[key]?.screenFile
                TestLog.warn("Screen nickname $key is duplicated. Registered file: $r , Loading file: ${file.absolutePath}")
            }

            // register
            this.set(screenInfo.key, screenInfo)
        }
        if (screenBaseInfo != null) {
            this.set(screenBaseInfo.key, screenBaseInfo)    // for debugging
        }

        /**
         * Include files in "include" section
         */
        for (screenInfo in screenInfoMap.values) {
            for (includedScreenName in screenInfo.includes) {
                if (screenInfoMap.containsKey(includedScreenName).not()) {
                    val msg = message(
                        id = "failedToIncludeNoneRegisteredScreen",
                        key = includedScreenName,
                        file = screenInfo.screenFile
                    )
                    throw TestConfigException(msg)
                }
                val includedScreenInfo = screenInfoMap[includedScreenName]!!
                includeScreen(screenInfo = screenInfo, includedScreenInfo = includedScreenInfo)
            }
        }

        val s = if (files.count() == 1) "" else "s"
        TestLog.info("Screen files loaded.(${files.count()} file$s)")

        return files
    }

    private fun includeScreen(screenInfo: ScreenInfo, includedScreenInfo: ScreenInfo) {

        for (includedSelector in includedScreenInfo.selectorMap) {
            if (screenInfo.selectorMap.containsKey(includedSelector.key)) {
                val thisSelector = screenInfo.selectorMap[includedSelector.key]!!
                if (thisSelector.isBase) {
                    // overrides screen-base selector by included selector
                    screenInfo.selectorMap.set(includedSelector.key, includedSelector.value.copy())
                }
            } else {
                screenInfo.selectorMap.set(includedSelector.key, includedSelector.value.copy())
            }
        }
        screenInfo.scrollInfo.importFrom(includedScreenInfo.scrollInfo)
    }

    /**
     * updateNicknameIndex
     */
    fun updateNicknameIndex() {

        nicknameIndex.clear()
        for (screenInfo in screenInfoSearchList.toList()) {
            val nicknames = screenInfo.selectorMap.values.map { it.nickname ?: "" }.filter { it.isValidNickname() }
            for (nickname in nicknames) {
                val screensForNickname =
                    if (nicknameIndex.containsKey(nickname)) nicknameIndex[nickname]!!
                    else mutableListOf()
                screensForNickname.add(screenInfo)
                nicknameIndex[nickname] = screensForNickname
            }
        }
    }
}