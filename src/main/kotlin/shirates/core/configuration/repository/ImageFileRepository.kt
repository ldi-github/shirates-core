package shirates.core.configuration.repository

import shirates.core.configuration.ImageInfo
import shirates.core.driver.TestMode.isAndroid
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.toPath
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path

/**
 * ImageFileRepository
 */
object ImageFileRepository {

    internal val imageFileMap = mutableMapOf<String, MutableList<ImageFileEntry>>()   // <filename, ImageFileEntry>

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
    fun setup(screenDirectory: String, importDirectories: List<String> = listOf()) {

        clear()
        val directories = importDirectories.toMutableList()
        directories.add(screenDirectory)

        for (directory in directories) {
            val files = File(directory.toPath().toUri()).walkTopDown()
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

        val imageFileEntry = ImageFileEntry(filePath = fileAbsolutePath)
        if (imageFileMap.containsKey(fileName).not()) {
            imageFileMap[fileName] = mutableListOf()
        }
        val list = imageFileMap[fileName]!!
        if (list.any() { it.filePath == imageFileEntry.filePath }.not()) {
            list.add(imageFileEntry)
        }
        return imageFileEntry
    }

    private fun String.fileNameWithSuffix(suffix: String): String {

        val fileNameWithSuffix = "${this.removeSuffix(".png")}$suffix.png"
        return fileNameWithSuffix
    }

    private fun getAllImageFileEntries(): List<ImageFileEntry> {

        val list = mutableListOf<ImageFileEntry>()
        imageFileMap.values.forEach {
            it.forEach {
                list.add(it)
            }
        }
        return list
    }

    /**
     * getImageFileEntries
     */
    fun getImageFileEntries(
        imageExpression: String,
        tag: String = "",
    ): List<ImageFileEntry> {

        val imageInfo = ImageInfo(imageExpression)
        val fname = imageInfo.fileName.removeSuffix(".png") + tag
        val allEntries = getAllImageFileEntries()
        val excludedAnnotation1 = if (isAndroid) "@i_" else "@a_"
        val excludedAnnotation2 = if (isAndroid) "@i." else "@a."
        val entries = allEntries.filter {
            it.fileName.contains(excludedAnnotation1).not() && it.fileName.contains(excludedAnnotation2).not()
        }
        val imageFileEntries =
            entries.filter { it.fileName.startsWith("${fname}.") || it.fileName.startsWith("${fname}@") }
                .sortedByDescending { it.fileName }
        return imageFileEntries
    }

    /**
     * getImageFileEntry
     */
    fun getImageFileEntry(
        imageExpression: String,
        screenDirectory: String? = null
    ): ImageFileEntry? {

        val entries = getImageFileEntries(imageExpression = imageExpression)
        if (screenDirectory != null) {
            val sd = screenDirectory.replace("/", File.separator)
            return entries.filter { it.filePath.toString().contains(sd) }.firstOrNull()
        }
        return entries.firstOrNull()
            ?: throw FileNotFoundException("Image file not found. (expression=$imageExpression)")
    }

    /**
     * getFilePath
     */
    fun getFilePath(imageExpression: String, screenDirectory: String? = null): Path {

        val entry = getImageFileEntry(imageExpression = imageExpression, screenDirectory = screenDirectory)
            ?: throw FileNotFoundException("imageExpression=$imageExpression, screenDirectory=$screenDirectory")
        return entry.filePath
    }

    /**
     * getBufferedImage
     */
    fun getBufferedImage(imageExpression: String, screenDirectory: String? = null): BufferedImage {

        val entry = getImageFileEntry(imageExpression = imageExpression, screenDirectory = screenDirectory)!!
        return entry.bufferedImage!!
    }
}