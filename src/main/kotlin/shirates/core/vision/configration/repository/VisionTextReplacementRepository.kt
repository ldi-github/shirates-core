package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import java.nio.file.Files

object VisionTextReplacementRepository {

    internal val convertMap = mapOf(
        /**
         * Full-width kana small to normal
         */
        "ぁ" to "あ",
        "ぃ" to "い",
        "ぅ" to "う",
        "ぇ" to "え",
        "ぉ" to "お",
        "ゃ" to "や",
        "ゅ" to "ゆ",
        "ょ" to "よ",
        "っ" to "つ",
        "ゎ" to "わ",
        "ゕ" to "か",
        "ゖ" to "け",
        /**
         * Full-width katakana small to Half-Width katakana normal
         */
        "ァ" to "ｱ",
        "ィ" to "ｲ",
        "ゥ" to "ｳ",
        "ェ" to "ｴ",
        "ォ" to "ｵ",
        "ャ" to "ﾔ",
        "ュ" to "ﾕ",
        "ョ" to "ﾖ",
        "ッ" to "ﾂ",
        /**
         * Full-width katakana to Half-Width katakana
         */
        "ワ" to "ﾜ",
        "カ" to "ｶ",
        "ケ" to "ｹ",
        /**
         * Full-width katakana small to Half-Width katakana normal
         */
        "ヮ" to "ﾜ",
        "ヵ" to "ｶ",
        "ヶ" to "ｹ",
        /**
         * Half-width katakana small to normal
         */
        "ｧ" to "ｱ",
        "ｨ" to "ｲ",
        "ｩ" to "ｳ",
        "ｪ" to "ｴ",
        "ｫ" to "ｵ",
        "ｬ" to "ﾔ",
        "ｭ" to "ﾕ",
        "ｮ" to "ﾖ",
        "ｯ" to "ﾂ",
        /**
         * misc
         */
        "く" to "<",     // U+304 to U+003C
        "〜" to "~",     // U+301C(WAVE DASH) to U+007E
        "～" to "~",     // U+FF5E(FULLWIDTH TILDE) to U+007E
        "｜" to "I",     // U+FF5C to U+0049
        "二" to "ニ",    // U+4E8C to U+30CB
        "−" to "-",     // U+2212 to U+002D
        "【" to "[",    // U+3010 to U+005B
        "】" to "]",    // U+3011 to U+005D
        "「" to "",     // U+300C to ""
        "」" to "",     // U+300D to ""
    )

    val replaceMap = mutableMapOf<String, String>()

    /**
     * clear
     */
    fun clear() {

        replaceMap.clear()
    }

    /**
     * setup
     */
    fun setup(
        textReplacementFile: String =
            PropertiesManager.visionDirectory.toPath().resolve("texts/errata.tsv").toString()
    ) {
        if (Files.exists(textReplacementFile.toPath()).not()) {
            return
        }

        val lines = textReplacementFile.toPath().toFile().readText().split('\n', '\r')
        for (i in 0..(lines.size - 1)) {
            val line = lines[i]
            if (i == 0 && line.startsWith('#')) {
                continue
            }
            val tokens = line.split("\t")
            if (tokens.size != 2) {
                if (line.isNotBlank()) {
                    TestLog.warn("Invalid format. Missing tab character. (\"$line\", line=${i + 1}, file=$textReplacementFile)")
                }
                continue
            }
            val key = tokens[0]
            val value = tokens[1]
            replaceMap[key] = value
        }
    }
}