package shirates.core.configuration.repository

import shirates.core.configuration.ImageInfo
import shirates.core.driver.TestMode.platformAnnotation
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.BufferedImageUtility
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path

/**
 * ImageFileRepository
 */
object ImageFileRepository {

    internal val imageFileMap = mutableMapOf<String, ImageFileEntry>()   // <filename, ImageFileEntry>

    class ImageFileEntry(
        val filePath: Path,
    ) {
        val fileName: String
            get() {
                return filePath.fileName.toString()
            }
        var bufferedImage: BufferedImage? = null
            get() {
                if (field == null) {
                    field = BufferedImageUtility.getBufferedImage(filePath.toString())
                }
                return field
            }
    }

    /**
     * clear
     */
    fun clear() {

        imageFileMap.clear()
    }

    /**
     * setup
     */
    fun setup(screenDirectory: Path, importDirectories: List<Path> = listOf()) {

        clear()
        val directories = importDirectories.toMutableList()
        directories.add(screenDirectory)

        for (directory in directories) {
            val files = File(directory.toUri()).walkTopDown()
                .filter { it.name.lowercase().endsWith(".png") }.toList()
            for (file in files) {
                setFile(file.toPath())
            }
        }
    }

    /**
     * setFile
     */
    fun setFile(filePath: Path): ImageFileEntry {

        val fileName = filePath.fileName.toString()
        val fileAbsolutePath = filePath.toAbsolutePath()

        if (imageFileMap.containsKey(fileName)) {
            TestLog.warn(message(id = "imageFileNameDuplicated", file = fileAbsolutePath.toString()))
        }
        val imageFileEntry = ImageFileEntry(filePath = fileAbsolutePath)
        imageFileMap[fileName] = imageFileEntry
        return imageFileEntry
    }

    /**
     * getFileName
     */
    fun getFileName(imageExpression: String): String {

        val imageInfo = ImageInfo(imageExpression)
        val suffix: String = platformAnnotation
        if (suffix.isNotBlank()) {
            val fileNameWithSuffix = imageInfo.fileName.fileNameWithSuffix(suffix)
            if (imageFileMap.containsKey(fileNameWithSuffix)) {
                return fileNameWithSuffix
            }
        }
        return imageInfo.fileName
    }

    private fun String.fileNameWithSuffix(suffix: String): String {

        val fileNameWithSuffix = "${this.removeSuffix(".png")}$suffix.png"
        return fileNameWithSuffix
    }

    /**
     * getImageFileEntry
     */
    fun getImageFileEntry(imageExpression: String): ImageFileEntry {

        val resolvedFileName = getFileName(imageExpression)
        val value =
            imageFileMap.values.firstOrNull { it.fileName == resolvedFileName || it.filePath.toString() == imageExpression }
        if (value != null) {
            return value
        }

        throw TestConfigException(message = message(id = "imageFileNotFound", subject = imageExpression))
    }

    /**
     * getFilePath
     */
    fun getFilePath(imageExpression: String): Path {

        val entry = getImageFileEntry(imageExpression = imageExpression)
        return entry.filePath
    }

    /**
     * getBufferedImage
     */
    fun getBufferedImage(imageExpression: String): BufferedImage {

        return getImageFileEntry(imageExpression = imageExpression).bufferedImage!!
    }
}