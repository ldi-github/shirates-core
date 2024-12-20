package shirates.core.vision.configration.repository

import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestConfigException
import shirates.core.logging.printInfo
import shirates.core.utility.toPath
import java.nio.file.Files
import kotlin.io.path.name

class VisionClassifierRepository {

    var imageClassifierDirectory = ""
    val labelMap = mutableMapOf<String, LabelFileInfo>()

    /**
     * classifierName
     */
    val classifierName: String
        get() {
            return imageClassifierDirectory.toPath().name
        }

    /**
     * setup
     */
    fun setup(imageClassifierDirectory: String) {

        this.imageClassifierDirectory = imageClassifierDirectory.toPath().toString()
        labelMap.clear()

        if (Files.exists(this.imageClassifierDirectory.toPath()).not()) {
            return
        }

        this.imageClassifierDirectory.toPath().toFile().walkTopDown().forEach {
            if (it.extension == "png" || it.extension == "jpg") {
                val label = it.toPath().parent.name
                if (labelMap.containsKey(label).not()) {
                    labelMap[label] = LabelFileInfo(
                        label = label,
                        imageClassifierDirectory = this.imageClassifierDirectory
                    )
                }
                val file = it.toString()
                if (file.toPath().parent.parent.name != "test") {
                    val labelFiles = labelMap[label]!!
                    labelFiles.files.add(it.toString())
//                    println("label: $label, file: $it")
                }
            }
        }

        /**
         * Check label duplication
         */
        for (label in labelMap.keys) {
            val files = labelMap[label]!!.files
            val dirs = files.map { it.toPath().parent }.distinct()
            if (dirs.count() > 1) {
                val duplicated = dirs.joinToString(", ")
                throw TestConfigException("Label directory is duplicated. A label can be belong to only one directory. (label=$label, dirs=$duplicated)")
            }
        }

        val classifierName = imageClassifierDirectory.toPath().name
        printInfo("Classifier files loaded.($classifierName, ${labelMap.keys.count()} labels, directory=$imageClassifierDirectory)")
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

        val files = getFiles(label = label)
        val platformSymbol = if (isAndroid) "@a." else "@i."
        val file = files.firstOrNull() { it.toPath().name.contains(platformSymbol) }
        if (file != null) {
            return file
        }
        return files.firstOrNull()
    }

    class LabelFileInfo(
        val label: String,
        val imageClassifierDirectory: String,
        val files: MutableList<String> = mutableListOf()
    )
}