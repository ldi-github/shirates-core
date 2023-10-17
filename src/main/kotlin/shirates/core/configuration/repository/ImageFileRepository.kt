package shirates.core.configuration.repository

import shirates.core.configuration.ImageInfo
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.platformAnnotation
import shirates.core.driver.testDrive
import shirates.core.driver.viewport
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

        override fun toString(): String {
            return "$fileName, $filePath"
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
        tag: String = ""
    ): ImageFileEntry? {

        val imageInfo = ImageInfo(imageExpression)
        val fname = imageInfo.fileName.removeSuffix(".png") + tag
        val imageFiles = imageFileMap.values.filter { it.fileName.startsWith(fname) }.sortedByDescending { it.fileName }
        return imageFiles.firstOrNull()
    }

    /**
     * getImageFileEntry
     */
    fun getImageFileEntry(imageExpression: String): ImageFileEntry {

        val vp = testDrive.viewport
        val offset = if (isAndroid) -1 else 0
        val width = (vp.width + offset).toString()
        val height = (vp.height + offset).toString()

        val entry =
            getImageFileEntryCore(imageExpression = imageExpression, tag = "${platformAnnotation}_${width}x${height}")
                ?: getImageFileEntryCore(imageExpression = imageExpression, tag = "${platformAnnotation}_${width}")
                ?: getImageFileEntryCore(imageExpression = imageExpression, tag = "${platformAnnotation}.png")
                ?: getImageFileEntryCore(imageExpression = imageExpression, tag = ".png")
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