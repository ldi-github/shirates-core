package shirates.spec.code.custom

object Keywords {

    val screenKeywords = mutableListOf("画面", "Screen", "screen", "ダイアログ", "Dialog", "dialog")

    val japaneseBrackets = "｢｣「」『』【】《》＜＞（）｛｝".map { it.toString() }

    val brackets = "[]()<>{}".map { it.toString() }

    val assertionContainedTokens = mutableListOf(" is ")

    val assertionEndTokens = mutableListOf("こと。", "こと")

    val displayedContainedTokens = mutableListOf("表示されること", "is displayed")

    val existenceContainedTokens = mutableListOf("存在", "exist")
}