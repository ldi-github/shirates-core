package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.toPath
import java.nio.file.Files

object VisionTextReplacementRepository {

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
            PropertiesManager.visionDirectory.toPath().resolve("texts/text.replacement.tsv").toString()
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
                continue
            }
            val key = tokens[0]
            val value = tokens[1]
            replaceMap[key] = value
        }
    }
}