package shirates.core.configuration

import org.json.JSONObject
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.getJSONArrayOrEmpty
import shirates.core.utility.toPath
import java.io.File

/**
 * ScreenInfo
 */
class ScreenInfo(val screenFile: String? = null, val screenBaseInfo: ScreenInfo? = null) {

    var key: String = ""
    val includes = mutableListOf<String>()
    val identityElements = mutableListOf<String>()
    var weight: String? = null
    val satelliteExpressions = mutableListOf<String>()
    val selectors = mutableMapOf<String, Selector>()
    var default: String = ""
    val scrollInfo = ScrollInfo()
    val searchWeight: Int
        get() {
            val w = if (weight != null) {
                try {
                    weight!!.toInt()
                } catch (t: Throwable) {
                    throw TestConfigException(message(id = "invalidScreenWeight", subject = weight, file = screenFile))
                }
            } else 0
            return w + identityElements.count()
        }

    override fun toString(): String {
        val list = mutableListOf<String>()
        for (token in identityElements) {
            list.add(token)
        }
        return "$key (${list.joinToString()}), searchWeight=$searchWeight"
    }

    /**
     * setIdentity
     */
    fun setIdentity(exp: String) {

        identityElements.clear()

        val nicknames = NicknameUtility.splitNicknames(exp)
        identityElements.addAll(nicknames)
    }

    /**
     * identitySelectors
     */
    var identitySelectors: MutableList<Selector> = mutableListOf()
        get() {
            if (field.isEmpty()) {
                for (nickname in identityElements) {
                    val sel = this.getSelector(nickname)
                    field.add(sel)
                }
            }
            return field
        }

    /**
     * satelliteSelectors
     */
    val satelliteSelectors: MutableList<Selector> = mutableListOf()
        get() {
            if (field.isEmpty()) {
                for (nickname in satelliteExpressions) {
                    val sel = this.getSelector(nickname)
                    field.add(sel)
                }
            }
            return field
        }

    /**
     * putSelector
     */
    fun putSelector(selector: Selector): Selector {

        val nickname = selector.nickname ?: throw IllegalArgumentException("nickname is null")

        NicknameUtility.validateNickname(nickname)

        selector.screenInfo = this
        selectors[nickname] = selector

        return selector
    }

    /**
     * putSelector
     */
    fun putSelector(nickname: String, expression: String): Selector {

        val selector = Selector(nickname = nickname, expression = expression)
        return putSelector(selector = selector)
    }

    /**
     * init
     */
    init {
        if (screenBaseInfo != null) {
            for (selector in screenBaseInfo.selectors) {
                putSelector(selector = selector.value)
            }
            scrollInfo.scrollable = screenBaseInfo.scrollInfo.scrollable
            scrollInfo.startElements.addAll(screenBaseInfo.scrollInfo.startElements)
            scrollInfo.endElements.addAll(screenBaseInfo.scrollInfo.endElements)
            scrollInfo.overlayElements.addAll(screenBaseInfo.scrollInfo.overlayElements)
        }
        if (screenFile != null) {
            loadFromFile(screenFile)
            validateKeyAndFileName()
        }
    }

    /**
     * getKeyFromFileName
     */
    fun getKeyFromFileName(): String {
        val result = screenFile.toPath().fileName.toString().substringBefore(".json")
        return result
    }

    /**
     * validateKeyAndFileName
     */
    fun validateKeyAndFileName() {

        val expected = getKeyFromFileName()
        if (key != expected) {
            throw TestConfigException(message(id = "keyMustBeSameAsFileName", expected = expected, file = screenFile))
        }
    }

    private fun loadFromFileCore(screenFile: String): JSONObject {

        val jso: JSONObject
        try {
            val path = screenFile.toPath()
            val fileContent = File(path.toUri()).readText()
            jso = JSONObject(fileContent)
        } catch (t: Throwable) {
            throw TestConfigException("Failed to load screen file.(file=$screenFile) $t", t)
        }

        /**
         * key
         */
        if (jso.has("key").not()) {
            throw TestConfigException("Key is required. (file=$screenFile)")
        }
        this.key = jso.getString("key")
        NicknameUtility.validateNickname(this.key)

        /**
         * include
         */
        if (jso.has("include")) {
            val includeArray = jso.getJSONArray("include")
            for (i in 0 until includeArray.length()) {
                val include = includeArray[i].toString()
                if (this.includes.contains(include)) {
                    TestLog.warn("Inclusion duplicated. (include=$include, file=$screenFile)")
                } else {
                    this.includes.add(include)
                }
            }
        }

        return jso
    }

    /**
     * loadFromFile
     */
    fun loadFromFile(screenFile: String): JSONObject {

        val jso = loadFromFileCore(screenFile)

        if (jso.has("weight")) {
            this.weight = jso.getString("weight")
        }
        if (jso.has("default")) {
            this.default = jso.getString("default")
        }

        if (jso.has("selectors")) {
            setSelectors(jso, screenFile)
        }
        if (jso.has("identity")) {
            setIdentities(jso = jso)
        }
        if (jso.has("satellites")) {
            setSatellites(jso = jso)
        }
        if (jso.has("scroll")) {
            setScroll(jso)
        }

        return jso
    }

    private fun setScroll(jso: JSONObject) {

        val scroll = jso.getJSONObject("scroll")
        val scrollable = scroll.getValue("scrollable")
        if (scrollable.isNotBlank()) {
            scrollInfo.scrollable = scrollable
        }
        scrollInfo.startElements.addAll(
            NicknameUtility.splitNicknames(
                scroll.getValue("start-elements")
            )
        )
        scrollInfo.endElements.addAll(
            NicknameUtility.splitNicknames(
                scroll.getValue("end-elements")
            )
        )
        scrollInfo.overlayElements.addAll(
            NicknameUtility.splitNicknames(
                scroll.getValue("overlay-elements")
            )
        )
    }

    private fun JSONObject.getValue(key: String): String {

        if (this.has(key)) {
            return this.getString(key)
        } else {
            return ""
        }
    }

    private fun setIdentities(jso: JSONObject) {

        val identityString = jso.getString("identity")
        setIdentity(identityString)
    }

    private fun setSatellites(jso: JSONObject) {

        val satellites = jso.getJSONArrayOrEmpty("satellites")
        for (o in satellites) {
            try {
                val expression = o as String
                satelliteExpressions.add(expression)
            } catch (t: Throwable) {
                throw TestConfigException(
                    message(id = "valueMustBeString", value = o.toString(), file = screenFile),
                    cause = t
                )
            }
        }
    }

    private fun setSelectors(jso: JSONObject, nicknameFile: String) {

        val nicknameSection = "selectors"
        val nicknamesJson = jso.getJSONObject(nicknameSection)

        val nicknameMap = mutableMapOf<String, String>()
        val incompleteMarkups = listOf("@<", "@[")  // Missing platform annotation (@a, @i)
        for (key in nicknamesJson.keys()) {
            val value = nicknamesJson.getString(key)

            for (token in incompleteMarkups) {
                if (value.contains(token)) {
                    TestLog.error(message(id = "incompleteMarkup", subject = "'$token'", file = nicknameFile))
                }
            }
            val nicknameValue = NicknameUtility.getRuntimeNicknameValue(value)
            nicknameMap[key] = nicknameValue
        }

        fun getOrCreateSelector(nickname: String): Selector {
            val sels = selectors
            if (sels.containsKey(nickname)) {
                if (nicknameMap.containsKey(nickname).not()) {
                    return sels[nickname]!!
                }
            }
            var exp =
                if (nicknameMap.containsKey(nickname)) nicknameMap[nickname]!!
                else ""
            /**
             * Expands nickname
             *   "[nickname1]": ""
             *   to
             *   "[nickname1]": "nickname1"
             */
            if (exp == "") {
                exp = nickname.getNicknameText()
            }

            val commands = Selector.getCommandList(exp)
            val firstCommand = commands[0]

            /**
             * Get or create first selector
             */
            val firstSelector: Selector
            if (firstCommand.isValidNickname()) {
                firstSelector = getOrCreateSelector(firstCommand)
            } else {
                firstSelector = Selector(
                    expression = firstCommand,
                    section = nicknameSection,
                    origin = nicknameFile,
                    screenInfo = this
                )
            }

            /**
             * Chain selectors
             */
            var resultSelector = firstSelector
            for (i in 1 until commands.count()) {
                val command = commands[i]
                if (command.isValidNickname()) {
                    val sel = getOrCreateSelector(command)
                    resultSelector = resultSelector.getChainedSelector(sel)
                } else {
                    resultSelector = resultSelector.getChainedSelector(command)
                }
            }
            resultSelector.nickname = nickname
            selectors.put(nickname, resultSelector)
            nicknameMap.remove(nickname)

            return resultSelector
        }

        /**
         * Instantiate selectors and register as nicknames
         */
        for (nickname in nicknameMap.keys.toList()) {
            try {
                getOrCreateSelector(nickname)
            } catch (t: StackOverflowError) {
                throw TestConfigException(message(id = "nicknameRecursiveCallError", subject = nickname))
            }
        }

        if (nicknameMap.isNotEmpty()) {
            val list = mutableListOf<String>()
            for (key in nicknameMap.keys) {
                if (selectors.containsKey(key).not()) {
                    list.add(key)
                }
            }
            if (list.any()) {
                throw TestConfigException("Nickname not resolved. (nicknames=${list.joinToString(",")}, nicknameFile=$nicknameFile)")
            }
        }
    }

    /**
     * getSelector
     */
    fun getSelector(
        expression: String?
    ): Selector {

        if (expression != null && expression.isValidNickname()) {
            val nickname = expression
            if (selectors.containsKey(nickname)) {
                val sel = selectors[nickname]!!
                return sel
            } else {
                if (TestMode.isNoLoadRun.not()) {
                    throw TestConfigException(
                        message(
                            id = "selectorIsNotRegisteredInScreenFile", subject = nickname,
                            arg1 = TestDriver.currentScreen, file = this.screenFile
                        )
                    )
                }
            }
        }

        val commands = Selector.getCommandList(expression = expression ?: "")
        if (commands.isEmpty()) {
            return Selector()
        }
        if (TestMode.isNoLoadRun) {
            val lastCommand = commands.last()
            if (lastCommand.isValidNickname()) {
                val sel = Selector(nickname = lastCommand)
                return sel
            }
        }

        val firstCommand = commands.first()
        val firstSelector =
            if (firstCommand.isValidNickname() && selectors.containsKey(firstCommand)) selectors[firstCommand]!!
            else Selector(firstCommand)
        var s = firstSelector.copy()
        for (i in 1 until commands.count()) {
            val command = commands[i]
            s = s.getChainedSelector(command)
        }

        s.originalExpression = expression
        return s
    }

    internal fun expandExpression(expression: String): Selector {

        if (selectors.containsKey(key = expression)) {
            return selectors[expression]!!
        } else {
            return getSelector(expression = expression)
        }
    }

}