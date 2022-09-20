package shirates.core.utility.misc

object PlaceholderUtility {

    /**
     * replacePlaceholders
     */
    fun replacePlaceholders(
        tokens: MutableList<String>,
        placeholderValues: Map<String, Any?>
    ) {

        for (i in 0 until tokens.count()) {
            val token = tokens[i]
            tokens[i] = replacePlaceholder(placeholderValues = placeholderValues, text = token)
        }
    }

    /**
     * replacePlaceholder
     */
    fun replacePlaceholder(
        placeholderValues: Map<String, Any?>,
        text: String,
    ): String {
        var newText = text
        for (parameterName in placeholderValues.keys) {
            val placeholder = "\${$parameterName}"
            if (newText.contains(placeholder)) {
                val parameterValue = placeholderValues[parameterName]
                if (parameterValue != null) {
                    newText = newText.replace(placeholder, "$parameterValue")
                }
            }
        }
        return newText
    }
}