package shirates.core.logging

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.core.configuration.PropertiesManager
import shirates.spec.exception.FileFormatException
import shirates.spec.utilily.*

object Message {

    val messageRepo = mutableMapOf<String, MutableMap<String, MessageRecord>>()
    val relativeMessageRepo = mutableMapOf<String, MutableList<Pair<String, MessageRecord>>>()

    /**
     * getMessageMap
     */
    fun getMessageMap(language: String = ""): MutableMap<String, MessageRecord> {

        if (messageRepo.isEmpty()) {
            setup()
        }

        val lang = if (language == "") "default" else language

        if (messageRepo.containsKey(lang)) {
            return messageRepo[lang]!!
        }
        return mutableMapOf()
    }

    fun setup() {

        val workbook = ExcelUtility.getWorkbook(baseName = "message.xlsx", logLanguage = "")
        loadMessageSheet(workbook = workbook)
    }

    fun loadMessageSheet(workbook: XSSFWorkbook) {

        if (workbook.worksheets.firstOrNull() { it.sheetName.lowercase() == "message" } == null)
            throw FileFormatException("message sheet is required.")
        val worksheet = workbook.worksheets("message")
        val rows1 = worksheet.rows.map { it.cells(1).text }
        val header = rows1.indexOf("id") + 1
        if (header < 1) throw FileFormatException("Column 'id' is required.")

        fun text(row: Int, col: Int): String {
            return worksheet.cells(row, col).text
        }

        val headerRow = worksheet.rows(header)
        for (c in 2..headerRow.count()) {
            val columnName = worksheet.cells(rowNum = header, columnNum = c).text
            if (columnName.isBlank()) {
                continue
            }
            val map = mutableMapOf<String, MessageRecord>()
            for (r in header + 1..worksheet.rows.count()) {
                val id = text(row = r, col = 1)
                if (id.startsWith("#")) {
                    continue
                }
                val content = text(row = r, col = c)
                val record = MessageRecord(id = id, message = content)
                map[id] = record
            }
            messageRepo[columnName] = map

            val relativeList = mutableListOf<Pair<String, MessageRecord>>()
            for (key in map.keys) {
                if (key.startsWith(":")) {
                    val record = map[key]!!
                    relativeList.add(Pair(key, record))
                }
            }
            relativeMessageRepo[columnName] = relativeList.sortedByDescending { it.first.length }.toMutableList()
        }
    }

    /**
     * getRelativeMessageList
     */
    fun getRelativeMessageList(language: String = PropertiesManager.logLanguage): MutableList<Pair<String, MessageRecord>> {

        return relativeMessageRepo[language.ifBlank { "default" }]!!
    }

    /**
     * replaceRelative
     */
    fun replaceRelative(message: String): String {

        var m = message
        val relativeList = getRelativeMessageList()
        for (item in relativeList) {
            val record = item.second
            m = m.replace(record.id, record.message)
        }
        return m
    }

    /**
     * getIndexOfRelative
     */
    fun getIndexOfRelative(message: String): Int {

        val relativeList = getRelativeMessageList()
        for (item in relativeList) {
            val record = item.second
            val index = message.indexOf(record.message)
            if (index >= 0) {
                return index
            }
        }
        return -1
    }

    /**
     * getRelativeRemovedMessage
     */
    fun getRelativeRemovedMessage(message: String): String {

        val index = getIndexOfRelative(message = message)
        if (index < 0) {
            return message
        }
        return message.substring(index)
    }

    /**
     * message
     */
    fun message(
        id: String,
        subject: String? = null,
        expected: String? = null,
        actual: String? = null,
        field1: String? = null,
        name: String? = null,
        repository: String? = null,
        attribute: String? = null,
        dataset: String? = null,
        key: String? = null,
        value: String? = null,
        from: String? = null,
        to: String? = null,
        file: String? = null,
        section: String? = null,
        arg1: String? = null,
        arg2: String? = null,
        submessage: String? = null,
        condition: String? = null,
        lang: String = TestLog.logLanguage,
        replaceRelative: Boolean = false
    ): String {

        val messageMap = getMessageMap(lang)

        var message: String
        if (messageMap.containsKey(id)) {
            val messageRecord = messageMap[id]!!
            message = messageRecord.message
            message = message.replace(placeHolder("subject"), subject ?: "null")
            message = message.replace(placeHolder("expected"), expected ?: "null")
            message = message.replace(placeHolder("actual"), actual ?: "null")
            message = message.replace(placeHolder("field1"), field1 ?: "null")
            message = message.replace(placeHolder("name"), name ?: "null")
            message = message.replace(placeHolder("repository"), repository ?: "null")
            message = message.replace(placeHolder("attribute"), attribute ?: "null")
            message = message.replace(placeHolder("dataset"), dataset ?: "null")
            message = message.replace(placeHolder("key"), key ?: "null")
            message = message.replace(placeHolder("value"), value ?: "null")
            message = message.replace(placeHolder("from"), from ?: "null")
            message = message.replace(placeHolder("to"), to ?: "null")
            message = message.replace(placeHolder("file"), file ?: "null")
            message = message.replace(placeHolder("section"), section ?: "null")
            message = message.replace(placeHolder("arg1"), arg1 ?: "null")
            message = message.replace(placeHolder("arg2"), arg2 ?: "null")
            message = message.replace(placeHolder("submessage"), submessage ?: "null")
            message = message.replace(placeHolder("condition"), condition ?: "null")
            if (replaceRelative) {
                message = replaceRelative(message = message)
            }
        } else {
            message = "message not found.(id=$id" +
                    arg("subject", subject) +
                    arg("expected", expected) +
                    arg("actual", actual) +
                    arg("field1", field1) +
                    arg("name", name) +
                    arg("repository", repository) +
                    arg("attribute", attribute) +
                    arg("dataset", dataset) +
                    arg("key", key) +
                    arg("value", value) +
                    arg("from", from) +
                    arg("to", to) +
                    arg("file", file) +
                    arg("section", section) +
                    arg("arg1", arg1) +
                    arg("arg2", arg2) +
                    arg("submessage", submessage) +
                    arg("condition", submessage) +
                    ")"
        }

        return message
    }

    fun message(args: Map<String, String>): String {

        val lang =
            if (args.containsKey("lang")) args["lang"]!!
            else if (TestLog.logLanguage.isBlank()) "default"
            else TestLog.logLanguage
        val messageMap = getMessageMap(lang)

        if (args.containsKey("id").not()) {
            throw IllegalArgumentException("'id' is required to get message. (map: $args)")
        }
        val id = args["id"]!!
        if (messageMap.containsKey(id).not()) {
            return "Message not found. (map: $args)"
        }

        var message = messageMap[id]!!.message
        for (key in args.keys.filter { it != "id" }) {
            val value = args[key]!!
            message = message.replace(placeHolder(key), value)
        }

        return message
    }

    private fun placeHolder(name: String): String {

        return "$" + "{" + name + "}"
    }

    private fun arg(argName: String, value: String?): String {

        if (value != null) {
            return ", $argName=$value"
        }
        return ""
    }

}
