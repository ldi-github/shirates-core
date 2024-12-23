package shirates.batch

import shirates.core.configuration.PropertiesManager
import shirates.core.vision.batch.ScreenTextExtractor

@Deprecated("screenText.json is no longer needed. `SrvisionProxy.setupImageFeaturePrintConfig` is called automatically in beforeAllAfterSetup in VisionTest base class.")
object ScreenTextExtractorExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        ScreenTextExtractor.execute(
            inputDirectory = "vision/screens",
            language = PropertiesManager.logLanguage,
        )
    }

}