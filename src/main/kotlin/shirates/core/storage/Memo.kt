package shirates.core.storage

import shirates.core.logging.Message.message
import shirates.core.logging.TestLog

/**
 * Memo
 */
object Memo {

    /**
     * map
     */
    val map = mutableMapOf<String, MutableList<String>>()

    /**
     * clear
     */
    fun clear() {
        map.clear()
    }

    /**
     * containsKey
     */
    fun containsKey(key: String): Boolean {

        return map.containsKey(key)
    }

    /**
     * write
     */
    fun write(key: String, text: String): String {

        val valueList: MutableList<String>
        if (containsKey(key).not()) {
            valueList = mutableListOf()
        } else {
            valueList = map[key]!!
        }
        valueList.add(text)
        map.put(key = key, value = valueList)

        return text
    }

    /**
     * read
     */
    fun read(key: String): String {

        val lastValue = getLastValue(key = key)
        TestLog.info(message(id = "valueOfKeyIs", key = key, value = lastValue))

        return lastValue
    }

    /**
     * getLastValue
     */
    fun getLastValue(key: String): String {

        if (containsKey(key = key).not()) {
            return ""
        }

        val lastValue = map[key]!!.lastOrNull() ?: ""

        return lastValue
    }

    /**
     * getFirstValue
     */
    fun getFirstValue(key: String): String {

        if (containsKey(key = key).not()) {
            return ""
        }

        val firstValue = map[key]!!.firstOrNull() ?: ""
        return firstValue
    }

    /**
     * getValueAt
     */
    fun getValueAt(key: String, number: Int): String {

        val index = number - 1
        if (containsKey(key).not()) {
            return "[value not found at $number]"
        }

        val list = map[key]!!
        if (number > list.count()) {
            return "[value not found at $number]"
        }

        return list[index]
    }

    /**
     * getValueHistory
     */
    fun getValueHistory(key: String): MutableList<String> {

        if (containsKey(key = key).not()) {
            return mutableListOf()
        }

        val valueHistory = map[key]!!.toMutableList()
        return valueHistory
    }

    /**
     * print
     */
    fun print() {

        map.keys.forEach {
            val key = it
            val value = getLastValue(key)
            println("$key=$value")
        }
    }

    /**
     * printHistory
     */
    fun printHistory() {
// TODO implement
    }

}