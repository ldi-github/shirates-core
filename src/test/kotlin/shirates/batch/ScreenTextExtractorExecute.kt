package shirates.batch

import shirates.core.configuration.PropertiesManager
import shirates.core.vision.batch.ScreenTextExtractor

object ScreenTextExtractorExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        ScreenTextExtractor.execute(
            inputDirectory = "vision/screens",
            language = PropertiesManager.logLanguage,
        )
    }

}