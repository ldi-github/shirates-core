package shirates.core.utility.string

interface StringNormalizer {

    /**
     * normalizeForComparison
     */
    fun String?.normalizeForComparison(
        ignoreCase: Boolean = true,
        ignoreFullWidthHalfWidth: Boolean = true,
    ): String {

        return this.forVisionComparisonDefault(
            ignoreCase = ignoreCase,
            ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth
        )
    }
}