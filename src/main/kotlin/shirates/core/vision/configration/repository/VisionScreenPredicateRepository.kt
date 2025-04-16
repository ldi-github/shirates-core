package shirates.core.vision.configration.repository

import shirates.core.logging.printWarn

object VisionScreenPredicateRepository {

    internal val screenPredicateMap = mutableMapOf<String, ScreenPredicateEntry>()

    /**
     * register
     */
    fun register(
        screenName: String,
        predicate: () -> Boolean
    ) {
        val entry = ScreenPredicateEntry(screenName = screenName, predicate = predicate)
        screenPredicateMap[screenName] = entry
    }

    /**
     * remove
     */
    fun remove(
        screenName: String
    ) {
        if (screenPredicateMap.containsKey(screenName).not()) {
            printWarn("removing screenPredicate is skipped because the screenPredicate is not registered. (screenName=$screenName)")
            return
        }
        screenPredicateMap.remove(screenName)
    }

    /**
     * clear
     */
    fun clear() {
        screenPredicateMap.clear()
    }

    /**
     * getPredicate
     */
    fun getPredicate(
        screenName: String
    ): (() -> Boolean)? {
        if (screenPredicateMap.containsKey(screenName)) {
            return screenPredicateMap[screenName]!!.predicate
        }
        return null
    }

    /**
     * getList
     */
    fun getList(): List<ScreenPredicateEntry> {
        return screenPredicateMap.values.toList()
    }

    class ScreenPredicateEntry(
        val screenName: String,
        val predicate: () -> Boolean
    )
}