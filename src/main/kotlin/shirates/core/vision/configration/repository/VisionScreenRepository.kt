package shirates.core.vision.configration.repository

import shirates.core.utility.toPath
import java.nio.file.Files

object VisionScreenRepository {

    lateinit var directory: String
    val screenMap = mutableMapOf<String, String>()

    /**
     * setup
     */
    fun setup(
        directory: String = "vision/screens".toPath().toString(),
    ) {
        this.directory = directory
        val dir = directory.toPath()
        if (Files.exists(dir).not()) {
            return
        }

        val list = dir.toFile().walkTopDown().filter { it.isFile }.toList()
        for (file in list) {
            if (file.extension == "png" && file.extension == "png") {
                screenMap[file.nameWithoutExtension] = file.absolutePath
            }
        }
    }

    /**
     * isRegistered
     */
    fun isRegistered(screenName: String): Boolean {
        return screenMap.containsKey(screenName)
    }
}