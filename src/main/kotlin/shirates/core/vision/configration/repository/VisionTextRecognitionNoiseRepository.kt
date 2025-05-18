package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.string.forVisionComparison
import shirates.core.utility.toPath
import java.nio.file.Files

object VisionTextRecognitionNoiseRepository {

    internal val noiseList = mutableListOf<String>()

    /**
     * clear
     */
    fun clear() {

        noiseList.clear()
    }

    /**
     * setup
     */
    fun setup(
        textReplacementFile: String =
            PropertiesManager.visionDirectory.toPath().resolve("texts/noise.txt").toString()
    ) {
        if (Files.exists(textReplacementFile.toPath()).not()) {
            return
        }

        val lines = textReplacementFile.toPath().toFile().readText().split('\n', '\r')
        for (i in 0..(lines.size - 1)) {
            val line = lines[i].forVisionComparison().trim()
            if (line.isNotBlank()) {
                noiseList.add(line)
            }
        }
    }

}