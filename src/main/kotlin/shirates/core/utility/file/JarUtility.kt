package shirates.core.utility.file

import shirates.core.logging.TestLog
import java.io.FileNotFoundException
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


object JarUtility {

    /**
     * classesLocation
     */
    val classesLocation: String
        get() {
            return javaClass.protectionDomain.codeSource.location.toString()
        }

    /**
     * hasJar
     */
    val hasJar: Boolean
        get() {
            return classesLocation.endsWith(".jar")
        }

    /**
     * getFileSystem
     */
    fun getFileSystem(): FileSystem? {

        val jarLoc = classesLocation
        val uri = URI.create("jar:$jarLoc")
        val env = mapOf<String, String>()
        return FileSystems.newFileSystem(uri, env)
    }

    /**
     * getProperties
     */
    fun getProperties(
        fileName: String,
        logLanguage: String = TestLog.logLanguage,
        jarFileSystem: FileSystem? = null
    ): Properties {

        val properties = Properties()
        val jarFs = jarFileSystem ?: runCatching { getFileSystem() }.getOrNull() ?: return properties
        TestLog.trace("Loading properties from $fileName in $jarFs")

        jarFs.use {
            val resourcePath = ResourceUtility.getResourcePath(fileName = fileName, logLanguage = logLanguage)
            val path = kotlin.runCatching { jarFs.getPath(resourcePath) }.getOrNull() ?: return properties
            if (Files.exists(path).not()) {
                return properties
            }
            val reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)
            properties.load(reader)
        }

        return properties
    }

    /**
     * copyFromJarToFile
     */
    fun copyFromJarToFile(
        fileName: String,
        targetFile: Path,
        logLanguage: String = "",
        jarFileSystem: FileSystem? = null
    ) {

        val jarFs = jarFileSystem ?: getFileSystem()
        jarFs.use {
            val resourcePath = ResourceUtility.getResourcePath(fileName = fileName, logLanguage = logLanguage)
            val path = jarFs!!.getPath(resourcePath)
            if (Files.exists(path).not()) {
                throw FileNotFoundException("path=$path")
            }
            TestLog.createDirectoryForLog()
            println("Copying jar content $resourcePath to ${TestLog.directoryForLog}")
            Files.copy(path, targetFile)
        }
    }
}