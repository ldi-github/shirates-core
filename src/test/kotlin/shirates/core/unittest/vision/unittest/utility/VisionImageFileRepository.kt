package shirates.core.unittest.vision.unittest.utility

import org.junit.jupiter.api.Test
import shirates.core.vision.configration.repository.VisionImageFileRepository

class VisionImageFileRepository {

    @Test
    fun setup() {

        VisionImageFileRepository.setup()

        VisionImageFileRepository.labelMap
    }
}