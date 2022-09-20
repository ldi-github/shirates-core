package shirates.core.utility.file

import org.apache.commons.codec.net.URLCodec
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*

object ResourceUtility {

    val propertiesMap = mutableMapOf<String, Properties>()

    private fun getPropertiesFromMap(baseName: String, logLanguage: String): Properties {

        val key = "baseName=$baseName,logLanguage=$logLanguage"
        if (propertiesMap.containsKey(key)) {
            return propertiesMap[key]!!
        }
        return Properties()
    }

    private fun setPropertiesToMap(baseName: String, logLanguage: String, properties: Properties) {

        val key = "baseName=$baseName,logLanguage=$logLanguage"
        propertiesMap[key] = properties
    }

    /**
     * getResourcePath
     */
    fun getResourcePath(fileName: String, logLanguage: String = TestLog.logLanguage): String {

        if (logLanguage.isBlank()) {
            return fileName
        } else {
            return "$logLanguage/$fileName"
        }
    }

    /**
     * getResourceAsStream
     */
    fun getResourceAsStream(fileName: String, logLanguage: String = TestLog.logLanguage): InputStream? {

        val resourcePath = getResourcePath(fileName = fileName, logLanguage = logLanguage)
        try {
            val stream = javaClass.classLoader.getResourceAsStream(resourcePath)
            if (stream != null) {
                return stream
            }
//            println("File not found in resource (fileName=$fileName, logLanguage=$logLanguage). Default resource is used.")
            return javaClass.classLoader.getResourceAsStream(fileName)
        } catch (t: Throwable) {
//            println("File not found in resource (fileName=$fileName, logLanguage=$logLanguage). Default resource is used.")
            return javaClass.classLoader.getResourceAsStream(fileName)
        }
    }

    /**
     * getResourceAsReader
     */
    fun getResourceAsReader(fileName: String, logLanguage: String = TestLog.logLanguage): InputStreamReader? {

        val inputStream = getResourceAsStream(fileName = fileName, logLanguage = logLanguage) ?: return null
        return InputStreamReader(inputStream, StandardCharsets.UTF_8)
    }

    /**
     * getResourceBundle
     */
    fun getResourceBundle(baseName: String, logLanguage: String = TestLog.logLanguage): ResourceBundle? {

        val resourcePath = getResourcePath(fileName = baseName, logLanguage = logLanguage)
        try {
            return ResourceBundle.getBundle(resourcePath, Locale(""))
        } catch (t: Throwable) {
//            println("Resource not found (baseName=$baseName, logLanguage=$logLanguage). Default resource is used.")
            return ResourceBundle.getBundle(baseName, Locale(""))
        }
    }

    /**
     * getProperties
     */
    fun getProperties(baseName: String, logLanguage: String = TestLog.logLanguage): Properties {

        val properties = getPropertiesFromMap(baseName = baseName, logLanguage = logLanguage)
        if (properties.any()) {
            return properties
        }

        val fileName = "${baseName}.properties"

        // default resource(from jar)
        if (JarUtility.hasJar) {
            val jarProperties = JarUtility.getProperties(fileName = fileName, logLanguage = "")
            jarProperties.forEach {
                properties[it.key] = it.value
            }
        }
        // default resource(from resource directory)
        val defaultBundle = getResourceBundle(baseName = baseName, logLanguage = "")
        defaultBundle?.keySet()?.forEach { key ->
            properties[key] = defaultBundle.getString(key)
        }

        if (logLanguage != "") {
            // language resource(from jar)
            if (JarUtility.hasJar) {
                val jarProperties = JarUtility.getProperties(fileName = fileName, logLanguage = logLanguage)
                jarProperties.forEach {
                    properties[it.key] = it.value
                }
            }
            // language resource(from language directory)
            val languageBundle = getResourceBundle(baseName = baseName, logLanguage = logLanguage)
            languageBundle?.keySet()?.forEach { key ->
                properties[key] = languageBundle.getString(key)
            }
        }

        return properties
    }

    /**
     * copyFile
     */
    fun copyFile(fileName: String, targetFile: Path, logLanguage: String = TestLog.logLanguage) {

        val resourcePath = getResourcePath(fileName = fileName, logLanguage = logLanguage)
        val url = javaClass.classLoader.getResource(resourcePath)
            ?: throw FileNotFoundException("resourcePath=$resourcePath")

        val decodedPath =
            URLCodec("UTF-8").decode(url.path)    // Decode is required for username including percent encoded space (eg. "user%20name")

        val path = (
                if (TestMode.isRunningOnWindows && decodedPath.startsWith("/")) decodedPath.substring(1)
                else if (TestMode.isRunningOnWindows && decodedPath.startsWith("file:/")) decodedPath.substring("file:/".length)
                else decodedPath
                ).toPath()
        if (Files.exists(path)) {
            TestLog.createDirectoryForLog()
            println("Copying $resourcePath to ${TestLog.directoryForLog}")
            Files.copy(path, targetFile, StandardCopyOption.REPLACE_EXISTING)
        } else {
            JarUtility.copyFromJarToFile(fileName = resourcePath, targetFile = targetFile, logLanguage = "")
        }
    }
}