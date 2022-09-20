package shirates.core.unittest.utility.file

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.file.JarUtility
import shirates.core.utility.toPath
import java.io.File
import java.net.URI
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Files

class JarUtilityTest : UnitTest() {

    @Test
    fun classesLocation() {

        // Act
        val jarLocation = JarUtility.classesLocation
        if (TestMode.isRunningOnWindows) {
            // Assert
            val fileExists = Files.exists(jarLocation.removePrefix("file:/").toPath())
            assertThat(fileExists).isEqualTo(true)
        } else {
            // Assert
            val fileExists = Files.exists(jarLocation.removePrefix("file:").toPath())
            assertThat(fileExists).isEqualTo(true)
        }
    }

    @Test
    fun hasJar() {

        assertThat(JarUtility.hasJar).isFalse()     // Always false on JUnit
    }

    @Test
    fun getFileSystem() {

        assertThatThrownBy {
            JarUtility.getFileSystem()  // Always fail on JUnit
        }.isInstanceOf(java.nio.file.ProviderNotFoundException::class.java)
            .hasMessage("Provider \"jar\" not found")
    }

    @Test
    fun getProperties() {

        val jarJs = getJarFsForTest()
        val props = JarUtility.getProperties("spec.properties", jarFileSystem = jarJs)
        assertThat(props["result.suspended"]).isEqualTo("SUSPENDED")
    }

    private fun getJarFsForTest(): FileSystem? {
        val location = "unitTestData/files/shirates-core-test.jar".toPath().toString()
            .replace(File.separator, "/")
        val uriPath =
            if (TestMode.isRunningOnWindows) "jar:file:/${location}"
            else "jar:file:${location}"
        val uri = URI.create(uriPath)
        val env = mapOf<String, String>()
        val jarJs = FileSystems.newFileSystem(uri, env)
        return jarJs
    }

    @Test
    fun copyFromJarToFile() {

        // Arrange
        val fileName = "message.xlsx"
        val targetFile = TestLog.directoryForLog.resolve(fileName)
        assertThat(Files.exists(targetFile)).isFalse()
        val jarFs = getJarFsForTest()
        // Act
        JarUtility.copyFromJarToFile(fileName = fileName, targetFile = targetFile, jarFileSystem = jarFs)
        // Assert
        assertThat(Files.exists(targetFile)).isTrue()

        // Act, Assert
        assertThatThrownBy {
            JarUtility.copyFromJarToFile(
                fileName = "hoge",
                targetFile = targetFile.parent.resolve("hoge"),
                jarFileSystem = jarFs
            )
        }.isInstanceOf(java.nio.file.ClosedFileSystemException::class.java)

    }
}