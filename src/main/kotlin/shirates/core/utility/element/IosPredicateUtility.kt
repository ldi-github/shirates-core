package shirates.core.utility.element

object IosPredicateUtility {

    fun getQuotedText(text: String, withoutQuote: Boolean): String {

        if (withoutQuote) {
            return text
        }

        if (text.contains("'").not()) {
            return "'$text'"
        }
        if (text.contains("\"").not()) {
            return "\"$text\""
        }
        val temp = text.replace("'", "__'__").replace("\"", "__\"__")
        val tokens = temp.split("__")
        val list = mutableListOf<String>()
        for (token in tokens) {
            if (token.contains("'")) {
                list.add("\"$token\"")
            } else if (token.contains("\"")) {
                list.add("'$token'")
            } else {
                list.add("'$token'")
            }
        }
        return "IN (${list.joinToString(",")})"
    }
}