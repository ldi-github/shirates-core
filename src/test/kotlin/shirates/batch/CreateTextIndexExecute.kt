package shirates.batch

import shirates.core.vision.configration.repository.VisionTextIndexRepository

object CreateTextIndexExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        VisionTextIndexRepository.setup()
    }
}