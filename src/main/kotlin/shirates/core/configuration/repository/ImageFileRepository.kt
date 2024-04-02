package shirates.core.configuration.repository

import shirates.core.configuration.ImageInfo
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.platformAnnotation
import shirates.core.driver.imageProfile
import shirates.core.driver.testDrive
import shirates.core.logging.Message.message
import shirates.core.utility.image.BufferedImageUtility
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

    private fun getImageFileEntryCore(
        imageExpression: String,
        tag: String = "",
        screenDirectory: String? = null
    ): ImageFileEntry? {

        val imageInfo = ImageInfo(imageExpression)
        val fname = imageInfo.fileName.removeSuffix(".png") + tag
        val allEntries = getAllImageFileEntries()
        val excludedAnnotation1 = if (isAndroid) "@i_" else "@a_"
        val excludedAnnotation2 = if (isAndroid) "@i." else "@a."
        val entries = allEntries.filter {
            it.fileName.contains(excludedAnnotation1).not() && it.fileName.contains(excludedAnnotation2).not()
        }
        val imageFiles = entries.filter { it.fileName.startsWith(fname) }.sortedByDescending { it.fileName }
        if (screenDirectory != null) {
            val dir = screenDirectory.replace("/", File.separator)
            val files = imageFiles.filter { it.filePath.toString().contains(dir) }
            return files.firstOrNull()
        }
        return imageFiles.firstOrNull()
    }

    /**
     * getImageFileEntry
     */
    fun getImageFileEntry(imageExpression: String, screenDirectory: String? = null): ImageFileEntry {

        val imageProfile = testDrive.imageProfile
        val ix = imageProfile.lastIndexOf("x")
        val imageProfileHeightRemoved =
            if (ix >= 0) imageProfile.substring(0, imageProfile.lastIndexOf("x")) else imageProfile

        fun getEntry(imageExpression: String, screenDirectory: String?): ImageFileEntry? {

            val entry =
                getImageFileEntryCore(
                    imageExpression = imageExpression,
                    tag = imageProfile,
                    screenDirectory = screenDirectory
                ) ?: getImageFileEntryCore(
                    imageExpression = imageExpression,
                    tag = imageProfileHeightRemoved,
                    screenDirectory = screenDirectory
                ) ?: getImageFileEntryCore(
                    imageExpression = imageExpression,
                    tag = "${platformAnnotation}.png",
                    screenDirectory = screenDirectory
                ) ?: getImageFileEntryCore(
                    imageExpression = imageExpression,
                    tag = ".png",
                    screenDirectory = screenDirectory
                ) ?: getImageFileEntryCore(
                    imageExpression = imageExpression,
                    tag = "",
                    screenDirectory = screenDirectory
                )
            return entry
        }

        val entry = getEntry(imageExpression = imageExpression, screenDirectory = screenDirectory)
            ?: getEntry(imageExpression = imageExpression, screenDirectory = null)
            ?: throw FileNotFoundException(message(id = "imageFileNotFound", subject = imageExpression))
        return entry
    }

    /**
     * getFilePath
     */
    fun getFilePath(imageExpression: String, screenDirectory: String? = null): Path {

        val entry = getImageFileEntry(imageExpression = imageExpression, screenDirectory = screenDirectory)
        return entry.filePath
    }

    /**
     * getBufferedImage
     */
    fun getBufferedImage(imageExpression: String, screenDirectory: String? = null): BufferedImage {

        return getImageFileEntry(imageExpression = imageExpression, screenDirectory = screenDirectory).bufferedImage!!
    }
}