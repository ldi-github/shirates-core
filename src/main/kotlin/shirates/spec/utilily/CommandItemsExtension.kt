package shirates.spec.utilily

import shirates.spec.report.entity.CommandItem

fun List<CommandItem>.getRedundancyRemoved(): List<CommandItem> {

    var before = this
    while (true) {
        val after = getRedundancyRemovedCore(commandItems = before)
        if (after.count() == before.count()) {
            return after
        }
        before = after
    }
}

private fun getRedundancyRemovedCore(
    commandItems: List<CommandItem>
): MutableList<CommandItem> {

    val resultItems = mutableListOf<CommandItem>()

    val lastIndex = commandItems.size - 1
    for (i in 0..lastIndex) {
        val commandItem = commandItems[i]
        var canAdd = true
        if (commandItem.message.contains("screenshot")) {
            canAdd = false
        } else if (commandItem.message.trim().endsWith("{")) {
            if (i != lastIndex) {
                val nextLine = commandItems[i + 1]
                if (nextLine.message.trim().startsWith("}")) {
                    canAdd = false
                }
            }
        } else if (commandItem.message.trim().startsWith("}")) {
            if (i != 0) {
                val lastLine = commandItems[i - 1]
                if (lastLine.message.trim().endsWith("{")) {
                    canAdd = false
                }
            }
        }
        if (canAdd) {
            resultItems.add(commandItem)
        }
    }
    return resultItems
}