package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.core.utility.file.exists
import shirates.core.utility.file.toFile
import shirates.core.utility.toPath
import shirates.core.vision.VisionServerProxy
import java.io.File
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

object VisionTextIndexRepository {

    var visionDirectory: String = ""

    var imageIndexFiles = mutableListOf<VisionTextIndexEntry>()

    /**
     * setup
     */
    fun setup(
        visionDirectory: String = PropertiesManager.visionDirectory,
        force: Boolean = false
    ) {
        lockFile(filePath = visionDirectory.toPath()) {
            setupCore(
                visionDirectory = visionDirectory,
                force = force,
            )
        }
    }

    private fun File.isImageIndexFile(): Boolean {

        if (this.name.startsWith("#").not()) {
            return false
        }

        val ext = this.extension.lowercase()
        return ext == "png" || ext == "jpg" || ext == "jpeg"
    }

    private fun setupCore(
        visionDirectory: String,
        force: Boolean
    ) {
        this.visionDirectory = visionDirectory

        imageIndexFiles = visionDirectory.toFile().walkTopDown().filter { it.isImageIndexFile() }
            .map { VisionTextIndexEntry(imageIndexFile = it.path) }.toMutableList()

        for (entry in imageIndexFiles) {
            fun createTextIndexFile() {
                val r = VisionServerProxy.recognizeText(
                    inputFile = entry.imageIndexFile,
                )
                val list = r.candidates.map { it.text }
                entry.index = list.toMutableList()

                val s = list.joinToString("\n")
                entry.textIndexFile.toFile().writeText(s)
                println("TextIndexFile created. ${entry.textIndexFile}")
            }

            if (entry.textIndexFile.exists()) {
                val textIndexFile = entry.textIndexFile.toFile()
                val imageIndexFile = entry.imageIndexFile.toFile()
                if (textIndexFile.lastModified() < imageIndexFile.lastModified()) {
                    createTextIndexFile()
                } else {
                    println("TextIndexFile already exists. ${entry.textIndexFile}")
                    entry.index = textIndexFile.readLines().toMutableList()
                }
            } else {
                createTextIndexFile()
            }
        }

        imageIndexFiles.sortByDescending { it.index.count() }
    }

    /**
     * VisionTextIndexEntry
     */
    class VisionTextIndexEntry(
        val imageIndexFile: String
    ) {
        private var _fileName: String? = null

        var index = mutableListOf<String>()

        val fileName: String
            get() {
                if (_fileName == null) {
                    _fileName = imageIndexFile.toPath().name
                }
                return _fileName!!
            }

        val isIndexFile: Boolean
            get() {
                return fileName.startsWith("#")
            }

        val textIndexFile: String
            get() {
                if (isIndexFile.not()) {
                    return ""
                }
                val p = imageIndexFile.toPath()
                val f = p.parent.resolve("${p.nameWithoutExtension}.txt").toString()
                return f
            }

        val screenName: String
            get() {
                return textIndexFile.toPath().parent.name
            }

        override fun toString(): String {
            return "$screenName $index"
        }
    }
}