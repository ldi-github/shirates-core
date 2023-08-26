package shirates.core.configuration.repository

import shirates.core.configuration.ImageInfo
import shirates.core.driver.TestDriver
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.BufferedImageUtility
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
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

    private fun String.fileNameWithSuffix(suffix: String): String {

        val fileNameWithSuffix = "${this.removeSuffix(".png")}$suffix.png"
        return fileNameWithSuffix
    }

    private fun getImageFileEntryCore(
        imageExpression: String,
        suffix: String
    ): ImageFileEntry? {

        val imageInfo = ImageInfo(imageExpression)
        val fileNameWithSuffix = imageInfo.fileName.fileNameWithSuffix(suffix)
        if (imageFileMap.containsKey(fileNameWithSuffix)) {
            val entry = imageFileMap[fileNameWithSuffix]!!
            TestLog.trace("imageExpression=$imageExpression, resolvedFileName=${entry.fileName}")
            return entry
        }
        return null
    }

    /**
     * getImageFileEntry
     */
    fun getImageFileEntry(imageExpression: String): ImageFileEntry {

        val entry = getImageFileEntryCore(imageExpression = imageExpression, suffix = TestDriver.suffixForImage)
            ?: getImageFileEntryCore(imageExpression = imageExpression, suffix = TestDriver.suffixForImageDefault)
            ?: getImageFileEntryCore(imageExpression = imageExpression, suffix = "")
            ?: throw FileNotFoundException(message(id = "imageFileNotFound", subject = imageExpression))
        return entry
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