package shirates.core.vision.utility.label

object LabelUtility {

    /**
     * getShortLabel
     */
    fun getShortLabel(fullLabel: String): String {

        val lastIndex = fullLabel.lastIndexOf('[')
        if (lastIndex == -1) {
            return fullLabel
        }
        val label = fullLabel.substring(lastIndex)
        return label
    }
}