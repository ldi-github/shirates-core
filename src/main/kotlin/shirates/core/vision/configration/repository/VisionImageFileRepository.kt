package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.toPath
import java.nio.file.Files
import kotlin.io.path.name

object VisionImageFileRepository {

    val labelMap = mutableMapOf<String, LabelFiles>()

    /**
     * setup
     */
    fun setup() {

        labelMap.clear()

        if (Files.exists(PropertiesManager.visionDirectory.toPath()).not()) {
            return
        }

        PropertiesManager.visionDirectory.toPath().toFile().walk().forEach {
            if (it.extension == "png" || it.extension == "jpg") {
                val label = it.toPath().parent.name
                if (labelMap.containsKey(it.name).not()) {
                    labelMap[label] = LabelFiles(label = label)
                }
                val labelFiles = labelMap[label]!!
                labelFiles.files.add(it.toString())
            }
        }
    }

    /**
     * getFiles
     */
    fun getFiles(label: String): List<String> {

        if (labelMap.containsKey(label).not()) {
            return listOf()
        }
        return labelMap[label]!!.files
    }

    /**
     * getFile
     */
    fun getFile(label: String): String? {

        return getFiles(label = label).firstOrNull()
    }

    class LabelFiles(
        val label: String,
        val files: MutableList<String> = mutableListOf()
    )
}