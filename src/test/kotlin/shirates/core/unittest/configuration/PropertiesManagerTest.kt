package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.Const
import shirates.core.UserVar
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.file.PropertiesUtility
import shirates.core.utility.misc.EnvUtility
import shirates.core.utility.toPath
import java.io.FileNotFoundException
import java.nio.file.Files

class PropertiesManagerTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        PropertiesManager.clear()
        EnvUtility.reset()
        EnvUtility.setEnvForTesting("SR_os", "")
    }

    private fun getSREnvMap(): MutableMap<String, String> {

        val map = System.getenv().filter { it.key.startsWith("SR_") }.toMutableMap()
        if (map.containsKey("SR_os").not()) {
            map["SR_os"] = ""   // EnvUtility.setEnvForTesting("SR_os", "")
        }
        return map
    }

    private fun getProperties(): MutableMap<Any, Any> {

        val properties = mutableMapOf<Any, Any>()
        properties["os"] = ""
        for (p in PropertiesManager.testrunGlobalProperties) {
            properties[p.key] = p.value
        }
        for (p in PropertiesManager.testrunProperties) {
            properties[p.key] = p.value
        }
        for (p in PropertiesManager.envProperties) {
            properties[p.key] = p.value
        }
        return properties
    }

    @Test
    fun clear_setup() {

        fun clear() {
            // Act
            PropertiesManager.clear()
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.properties.count()).isEqualTo(0)
        }

        run {
            // Act
            clear()
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.properties.count()).isEqualTo(0)

            // Act
            PropertiesManager.setup()
            // Assert
            assertThat(PropertiesManager.testrunFile).isEqualTo(Const.TESTRUN_PROPERTIES)
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(6)
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(0)
            val srEnvMap = getSREnvMap()
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(srEnvMap.count())
            val properties = getProperties()
            for (p in PropertiesManager.properties) {
                println("PropertiesManager.properties[\"${p.key}\"]=${p.value}")
            }
            for (p in properties) {
                println("properties[\"${p.key}\"]=${p.value}")
            }
            assertThat(PropertiesManager.properties.count()).isEqualTo(properties.count())
            assertThat(PropertiesManager.configFile).isEqualTo("testConfig/android/testConfig@a.json")

            val expectedProfile = if (srEnvMap.containsKey("SR_profile")) srEnvMap["SR_profile"] else ""
            assertThat(PropertiesManager.profile).isEqualTo(expectedProfile)
        }
        run {
            // Clear
            clear()
            // Act
            PropertiesManager.setup(testrunFile = "unitTestConfig/android/androidSettings/testrun.properties")
            // Assert
            assertThat(PropertiesManager.testrunFile).isEqualTo("unitTestConfig/android/androidSettings/testrun.properties")
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(6)
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(2)
            val srEnvMap = getSREnvMap()
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(srEnvMap.count())
            val properties = getProperties()
            assertThat(PropertiesManager.properties.count()).isEqualTo(properties.count())
            assertThat(PropertiesManager.configFile).isEqualTo("unitTestConfig/android/androidSettings/androidSettingsConfig.json")

            val expectedProfile = if (srEnvMap.containsKey("SR_profile")) srEnvMap["SR_profile"] else ""
            assertThat(PropertiesManager.profile).isEqualTo(expectedProfile)
        }
    }

    @Test
    fun setup_testrunFile_env() {

        // Arrange
        val globalProps = PropertiesUtility.getProperties("testrun.global.properties".toPath())

        // testrunFile(blank)
        run {
            // Arrange
            val testrunFile = ""
            // Act, Assert
            assertThatThrownBy {
                PropertiesManager.setup(testrunFile = testrunFile)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("testrunFile is required.")
        }
        // testrunFile(not exist)
        run {
            // Arrange
            val testrunFile = "not.exist.testrun.properties"
            assertThat(Files.exists(testrunFile.toPath())).isFalse()
            // Act, Assert
            assertThatThrownBy {
                PropertiesManager.setup(testrunFile = testrunFile)
            }.isInstanceOf(FileNotFoundException::class.java)
                .hasMessage("testrunFile is not found. (testrunFile=not.exist.testrun.properties)")
        }
        // testrunFile(exist)
        run {
            // Arrange
            val testrunFile = "unitTestConfig/android/androidSettings/testrun.properties"
            assertThat(Files.exists(testrunFile.toPath())).isTrue()
            val testrunPros = PropertiesUtility.getProperties(testrunFile.toPath())
            // Act
            PropertiesManager.setup(testrunFile = testrunFile)
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(globalProps.count())
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(testrunPros.count())
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(EnvUtility.getSREnvMap().count())
        }
        // not SR_env
        run {
            // Arrange
            val testrunFile = "unitTestConfig/android/androidSettings/testrun.properties"
            val testrunPros = PropertiesUtility.getProperties(testrunFile.toPath())
            EnvUtility.reset()
            EnvUtility.setEnvForTesting("name1", "value1")
            // Act
            PropertiesManager.setup(testrunFile)
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(globalProps.count())
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(testrunPros.count())
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(EnvUtility.getSREnvMap().count())
        }
        // SR_env
        run {
            // Arrange
            val testrunFile = "unitTestConfig/android/androidSettings/testrun.properties"
            val testrunPros = PropertiesUtility.getProperties(testrunFile.toPath())
            EnvUtility.reset()
            EnvUtility.setEnvForTesting("SR_name1", "SR_value1")
            // Act
            PropertiesManager.setup(testrunFile)
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(globalProps.count())
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(testrunPros.count())
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(EnvUtility.getSREnvMap().count())
        }
        // SR_testrunFile(exist)
        run {
            // Arrange
            val testrunFile = "unitTestConfig/android/androidSettings/testrun.properties"
            val testrunPros = PropertiesUtility.getProperties(testrunFile.toPath())
            EnvUtility.reset()
            EnvUtility.setEnvForTesting("SR_testrunFile", testrunFile)
            // Act
            PropertiesManager.setup(testrunFile)
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(globalProps.count())
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(testrunPros.count())
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(EnvUtility.getSREnvMap().count())
        }
        // SR_testrunFile(not exist)
        run {
            // Arrange
            val testrunFile = "not.exist.testrun.properties"
            EnvUtility.reset()
            EnvUtility.setEnvForTesting("SR_testrunFile", testrunFile)
            // Act, Assert
            assertThatThrownBy {
                PropertiesManager.setup(testrunFile = testrunFile)
            }.isInstanceOf(FileNotFoundException::class.java)
                .hasMessage("testrunFile is not found. (testrunFile=not.exist.testrun.properties)")
        }
    }

    @Test
    fun getPropertyValue_setPropertyValue() {

        run {
            /**
             * import SR_envs into properties
             */
            // Arrange
            EnvUtility.setEnvForTesting("SR_name1", "value1")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(PropertiesManager.getPropertyValue("name1")).isEqualTo("value1")
        }
        run {
            // Act
            PropertiesManager.setPropertyValue("name2", "value2")
            // Assert
            assertThat(PropertiesManager.getPropertyValue("name1")).isEqualTo("value1")
            assertThat(PropertiesManager.getPropertyValue("name2")).isEqualTo("value2")
        }
        run {
            /**
             * overwrite name1
             */
            // Act
            PropertiesManager.setPropertyValue("name1", "value1-2")
            // Assert
            assertThat(PropertiesManager.getPropertyValue("name1")).isEqualTo("value1-2")
        }
    }

    @Test
    fun getLogLangugage_setLogLangugage() {

        PropertiesManager.logLanguage = "ja"
        assertThat(PropertiesManager.logLanguage).isEqualTo("ja")

        PropertiesManager.logLanguage = ""
        assertThat(PropertiesManager.logLanguage).isEqualTo("")
    }

    @Test
    fun os() {

        run {
            /**
             * from "SR_os" env
             */

            // Arrange
            EnvUtility.setEnvForTesting("SR_os", "")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(PropertiesManager.os).isEqualTo("android")

            // Arrange
            EnvUtility.setEnvForTesting("SR_os", "ios")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(PropertiesManager.os).isEqualTo("ios")
        }
        run {
            /**
             * overwrite "os"
             */

            // Arrange
            PropertiesManager.setPropertyValue("os", "os1")
            // Act, Assert
            assertThat(PropertiesManager.os).isEqualTo("os1")
        }
    }

    @Test
    fun noLoadRun() {

        run {
            /**
             * from "SR_noLoadRun" env
             */

            // Arrange
            EnvUtility.setEnvForTesting("SR_noLoadRun", "")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(PropertiesManager.noLoadRun).isFalse()

            // Arrange
            EnvUtility.setEnvForTesting("SR_noLoadRun", "true")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(PropertiesManager.noLoadRun).isTrue()

            // Arrange
            EnvUtility.setEnvForTesting("SR_noLoadRun", "false")
            PropertiesManager.setup()
            // Act, Assert
            assertThat(PropertiesManager.noLoadRun).isFalse()
        }
        run {
            /**
             * overwrite "noLoadRun"
             */

            // Arrange
            PropertiesManager.setPropertyValue("noLoadRun", "true")
            // Act, Assert
            assertThat(PropertiesManager.noLoadRun).isTrue()

            // Arrange
            PropertiesManager.setPropertyValue("noLoadRun", "false")
            // Act, Assert
            assertThat(PropertiesManager.noLoadRun).isFalse()
        }
    }

    @Test
    fun configFile() {

        kotlin.run {
            // Arrange
            TestMode.setAndroid()
            PropertiesManager.clear()
            // Act, Assert
            assertThatThrownBy {
                PropertiesManager.configFile
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("PropertiesManager is not initialized.")
        }
        run {
            // Arrange
            TestMode.setAndroid()
            PropertiesManager.setup()
            val configFile = "unitTestConfig/android/androidSettings/testrun.properties"
            PropertiesManager.setPropertyValue("android.configFile", configFile)
            // Act, Assert
            assertThat(PropertiesManager.configFile).isEqualTo(configFile)
        }
        run {
            // Arrange
            TestMode.setAndroid()
            PropertiesManager.setup()
            // Act, Assert
            assertThat(PropertiesManager.configFile).isEqualTo("testConfig/android/testConfig@a.json")
        }
        run {
            // Arrange
            TestMode.setIos()
            PropertiesManager.setup()
            // Act, Assert
            assertThat(PropertiesManager.configFile).isEqualTo("testConfig/ios/testConfig@i.json")
        }
        run {
            // Arrange
            PropertiesManager.setPropertyValue("configFile", "configFile1")
            // Act
            assertThat(PropertiesManager.configFile).isEqualTo("configFile1")
        }
    }

    @Test
    fun profile() {

        run {
            // Arrange
            TestMode.setAndroid()
            PropertiesManager.clear()
            val profile = "profile1"
            PropertiesManager.setPropertyValue("profile", profile)
            // Act, Assert
            assertThat(PropertiesManager.profile).isEqualTo(profile)
        }
        run {
            if (EnvUtility.getSREnvMap().containsKey("SR_profile").not()) {
                // Arrange
                PropertiesManager.clear()
                // Act
                val result = PropertiesManager.profile
                // Assert
                assertThat(result).isEqualTo("")
            }
        }
    }

    @Test
    fun priority() {

        run {
            // Arrange
            PropertiesManager.clear()
            PropertiesManager.setup(testrunFile = "unitTestData/testConfig/priorityConfig/testrun.skipMustTest.properties")
            // Act, Assert
            assertThat(PropertiesManager.must).isEqualTo(false)
            assertThat(PropertiesManager.should).isEqualTo(true)
            assertThat(PropertiesManager.want).isEqualTo(true)
            assertThat(PropertiesManager.none).isEqualTo(true)
        }
        run {
            // Arrange
            PropertiesManager.clear()
            PropertiesManager.setup(testrunFile = "unitTestData/testConfig/priorityConfig/testrun.skipShouldTest.properties")
            // Act, Assert
            assertThat(PropertiesManager.must).isEqualTo(true)
            assertThat(PropertiesManager.should).isEqualTo(false)
            assertThat(PropertiesManager.want).isEqualTo(true)
            assertThat(PropertiesManager.none).isEqualTo(true)
        }
        run {
            // Arrange
            PropertiesManager.clear()
            PropertiesManager.setup(testrunFile = "unitTestData/testConfig/priorityConfig/testrun.skipShouldWantTest.properties")
            // Act, Assert
            assertThat(PropertiesManager.must).isEqualTo(true)
            assertThat(PropertiesManager.should).isEqualTo(false)
            assertThat(PropertiesManager.want).isEqualTo(false)
            assertThat(PropertiesManager.none).isEqualTo(true)
        }
        run {
            // Arrange
            PropertiesManager.clear()
            PropertiesManager.setup(testrunFile = "unitTestData/testConfig/priorityConfig/testrun.skipWantTest.properties")
            // Act, Assert
            assertThat(PropertiesManager.must).isEqualTo(true)
            assertThat(PropertiesManager.should).isEqualTo(true)
            assertThat(PropertiesManager.want).isEqualTo(false)
            assertThat(PropertiesManager.none).isEqualTo(true)

            // Arrange
            PropertiesManager.setPropertyValue("none", "false")
            // Act, Assert
            assertThat(PropertiesManager.none).isEqualTo(false)
        }
    }

    @Test
    fun testResults() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.testResults).isEqualTo(shirates.core.UserVar.TEST_RESULTS)
        }
        run {
            // Arrange
            val path = "Downloads"
            PropertiesManager.setPropertyValue("testResults", path)
            // Act, Assert
            assertThat(PropertiesManager.testResults).isEqualTo(path)
        }
    }

    @Test
    fun logLanguage() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.logLanguage).isEqualTo("")
        }
        run {
            // Arrange
            PropertiesManager.setPropertyValue("logLanguage", "ja")
            // Act, Assert
            assertThat(PropertiesManager.logLanguage).isEqualTo("ja")
        }
    }

    @Test
    fun enableSyncLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableSyncLog).isEqualTo(Const.ENABLE_SYNC_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_SYNC_LOG.not()
            PropertiesManager.setPropertyValue("enableSyncLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableSyncLog).isEqualTo(value)
        }
    }

    @Test
    fun enableTestList() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableTestList).isEqualTo(Const.ENABLE_TEST_LIST)
        }
        run {
            // Arrange
            val value = Const.ENABLE_TEST_LIST.not()
            PropertiesManager.setPropertyValue("enableTestList", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableTestList).isEqualTo(value)
        }
    }

    @Test
    fun enableTestClassList() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableTestClassList).isEqualTo(Const.ENABLE_TEST_CLASS_LIST)
        }
        run {
            // Arrange
            val value = Const.ENABLE_TEST_LIST.not()
            PropertiesManager.setPropertyValue("enableTestClassList", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableTestClassList).isEqualTo(value)
        }
    }

    @Test
    fun enableSpecReport() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableSpecReport).isEqualTo(Const.ENABLE_SPEC_REPORT)
        }
        run {
            // Arrange
            val value = Const.ENABLE_SPEC_REPORT.not()
            PropertiesManager.setPropertyValue("enableSpecReport", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableSpecReport).isEqualTo(value)
        }
    }

    @Test
    fun enableRelativeCommandTranslation() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableRelativeCommandTranslation).isEqualTo(Const.ENABLE_RELATIVE_COMMAND_TRANSLATION)
        }
        run {
            // Arrange
            val value = Const.ENABLE_RELATIVE_COMMAND_TRANSLATION.not()
            PropertiesManager.setPropertyValue("enableRelativeCommandTranslation", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableRelativeCommandTranslation).isEqualTo(value)
        }
    }

    @Test
    fun testListDir() {

        TestLog.directoryMap

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.testListDir).isEqualTo("")
        }
        run {
            // Arrange
            val value = "dir1"
            PropertiesManager.setPropertyValue("testListDir", value)
            // Act, Assert
            assertThat(PropertiesManager.testListDir).isEqualTo(value)
        }
        run {
            // Arrange
            val value = "{TEST_RESULTS}"
            PropertiesManager.setPropertyValue("testListDir", value)
            // Act, Assert
            assertThat(PropertiesManager.testListDir).isEqualTo(UserVar.TEST_RESULTS)
        }
    }

    @Test
    fun reportIndexDir() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.reportIndexDir).isEqualTo("")
        }
        run {
            // Arrange
            val value = "dir1"
            PropertiesManager.setPropertyValue("reportIndexDir", value)
            // Act, Assert
            assertThat(PropertiesManager.reportIndexDir).isEqualTo(value)
        }
        run {
            // Arrange
            val value = "{TEST_RESULTS}"
            PropertiesManager.setPropertyValue("reportIndexDir", value)
            // Act, Assert
            assertThat(PropertiesManager.reportIndexDir).isEqualTo(UserVar.TEST_RESULTS)
        }
    }

    @Test
    fun enableInnerMacroLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableInnerMacroLog).isEqualTo(Const.ENABLE_INNER_MACRO_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_INNER_MACRO_LOG.not()
            PropertiesManager.setPropertyValue("enableInnerMacroLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableInnerMacroLog).isEqualTo(value)
        }
    }

    @Test
    fun enableInnerCommandLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableInnerCommandLog).isEqualTo(Const.ENABLE_INNER_COMMAND_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_INNER_COMMAND_LOG.not()
            PropertiesManager.setPropertyValue("enableInnerCommandLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableInnerCommandLog).isEqualTo(value)
        }
    }

    @Test
    fun enableSilentLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableSilentLog).isEqualTo(Const.ENABLE_SILENT_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_SILENT_LOG.not()
            PropertiesManager.setPropertyValue("enableSilentLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableSilentLog).isEqualTo(value)
        }
    }

    @Test
    fun enableTapElementImageLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableTapElementImageLog).isEqualTo(Const.ENABLE_INNER_MACRO_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_INNER_MACRO_LOG.not()
            PropertiesManager.setPropertyValue("enableTapElementImageLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableTapElementImageLog).isEqualTo(value)
        }
    }

    @Test
    fun enableXmlSourceDump() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableXmlSourceDump).isEqualTo(Const.ENABLE_XMLSOURCE_DUMP)
        }
        run {
            // Arrange
            val value = Const.ENABLE_XMLSOURCE_DUMP.not()
            PropertiesManager.setPropertyValue("enableXmlSourceDump", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableXmlSourceDump).isEqualTo(value)
        }
    }

    @Test
    fun enableRetryLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableRetryLog).isEqualTo(Const.ENABLE_RETRY_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_RETRY_LOG.not()
            PropertiesManager.setPropertyValue("enableRetryLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableRetryLog).isEqualTo(value)
        }
    }

    @Test
    fun enableWarnOnRetryError() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableWarnOnRetryError).isEqualTo(Const.ENABLE_WARN_ON_RETRY_ERROR)
        }
        run {
            // Arrange
            val value = Const.ENABLE_WARN_ON_RETRY_ERROR.not()
            PropertiesManager.setPropertyValue("enableWarnOnRetryError", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableWarnOnRetryError).isEqualTo(value)
        }
    }

    @Test
    fun enableWarnOnSelectTimeout() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableWarnOnSelectTimeout).isEqualTo(Const.ENABLE_WARN_ON_SELECT_TIMEOUT)
        }
        run {
            // Arrange
            val value = Const.ENABLE_WARN_ON_SELECT_TIMEOUT.not()
            PropertiesManager.setPropertyValue("enableWarnOnSelectTimeout", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableWarnOnSelectTimeout).isEqualTo(value)
        }
    }

    @Test
    fun enableGetSourceLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableGetSourceLog).isEqualTo(Const.ENABLE_GET_SOURCE_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_GET_SOURCE_LOG.not()
            PropertiesManager.setPropertyValue("enableGetSourceLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableGetSourceLog).isEqualTo(value)
        }
    }

    @Test
    fun enableTrace() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableTrace).isEqualTo(Const.ENABLE_TRACE)
        }
        run {
            // Arrange
            val value = Const.ENABLE_TRACE.not()
            PropertiesManager.setPropertyValue("enableTrace", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableTrace).isEqualTo(value)
        }
    }

    @Test
    fun enableShellExecLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableShellExecLog).isEqualTo(Const.ENABLE_SHELL_EXEC_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_SHELL_EXEC_LOG.not()
            PropertiesManager.setPropertyValue("enableShellExecLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableShellExecLog).isEqualTo(value)
        }
    }

    @Test
    fun enableTimeMeasureLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableTimeMeasureLog).isEqualTo(Const.ENABLE_TIME_MEASURE_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_TIME_MEASURE_LOG.not()
            PropertiesManager.setPropertyValue("enableTimeMeasureLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableTimeMeasureLog).isEqualTo(value)
        }
    }

    @Test
    fun enableStopWatchLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableStopWatchLog).isEqualTo(Const.ENABLE_STOP_WATCH_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_STOP_WATCH_LOG.not()
            PropertiesManager.setPropertyValue("enableStopWatchLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableStopWatchLog).isEqualTo(value)
        }
    }

    @Test
    fun imageMatchDetailLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableImageMatchDebugLog).isEqualTo(Const.ENABLE_IMAGE_MATCH_DEBUG_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_IMAGE_MATCH_DEBUG_LOG.not()
            PropertiesManager.setPropertyValue("enableImageMatchDebugLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableImageMatchDebugLog).isEqualTo(value)
        }
    }

    @Test
    fun enableIsInViewLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableIsInViewLog).isEqualTo(Const.ENABLE_IS_IN_VIEW_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_IS_IN_VIEW_LOG.not()
            PropertiesManager.setPropertyValue("enableIsInViewLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableIsInViewLog).isEqualTo(value)
        }
    }

    @Test
    fun enableIsSafeLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableIsSafeLog).isEqualTo(Const.ENABLE_IS_SAFE_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_IS_SAFE_LOG.not()
            PropertiesManager.setPropertyValue("enableIsSafeLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableIsSafeLog).isEqualTo(value)
        }
    }

    @Test
    fun enableIsScreenLog() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableIsScreenLog).isEqualTo(Const.ENABLE_IS_SCREEN_LOG)
        }
        run {
            // Arrange
            val value = Const.ENABLE_IS_SCREEN_LOG.not()
            PropertiesManager.setPropertyValue("enableIsScreenLog", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableIsScreenLog).isEqualTo(value)
        }
    }

    @Test
    fun screenshotScale() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.screenshotScale).isEqualTo(Const.SCREENSHOT_SCALE)
        }
        run {
            // Arrange
            val value = Const.SCREENSHOT_SCALE / 2
            PropertiesManager.setPropertyValue("screenshotScale", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.screenshotScale).isEqualTo(value)
        }
        run {
            // Arrange
            val value = 0.09
            PropertiesManager.setPropertyValue("screenshotScale", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.screenshotScale
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("screenshotScale is allowed from 0.1 to 1.0. (0.09)")
        }
        run {
            // Arrange
            val value = 1.1
            PropertiesManager.setPropertyValue("screenshotScale", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.screenshotScale
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("screenshotScale is allowed from 0.1 to 1.0. (1.1)")
        }
    }

    @Test
    fun screenshotIntervalSeconds() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.screenshotIntervalSeconds).isEqualTo(Const.SCREENSHOT_INTERVAL_SECOND)
        }
        run {
            // Arrange
            val value = 1.5
            PropertiesManager.setPropertyValue("screenshotIntervalSeconds", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.screenshotIntervalSeconds).isEqualTo(value)
        }
    }

    // strictCompareMode
    @Test
    fun strictCompareMode() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.strictCompareMode).isEqualTo(Const.ENABLE_STRICT_COMPARE_MODE)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("strictCompareMode", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.strictCompareMode).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("strictCompareMode", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.strictCompareMode).isEqualTo(value)
        }
    }

    // keepLF
    @Test
    fun keepLF() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.keepLF).isEqualTo(Const.KEEP_LF)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("keepLF", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.keepLF).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("keepLF", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.keepLF).isEqualTo(value)
        }
    }

    // keepTAB
    @Test
    fun keepTAB() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.keepTAB).isEqualTo(Const.KEEP_TAB)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("keepTAB", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.keepTAB).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("keepTAB", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.keepTAB).isEqualTo(value)
        }
    }

    @Test
    fun keepZenkakuSpace() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.keepZenkakuSpace).isEqualTo(Const.KEEP_ZENKAKU_SPACE)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("keepZenkakuSpace", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.keepZenkakuSpace).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("keepZenkakuSpace", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.keepZenkakuSpace).isEqualTo(value)
        }
    }

    @Test
    fun waveDashToFullWidthTilde() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.waveDashToFullWidthTilde).isEqualTo(Const.WAVE_DASH_TO_FULL_WIDTH_TILDE)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("waveDashToFullWidthTilde", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.waveDashToFullWidthTilde).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("waveDashToFullWidthTilde", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.waveDashToFullWidthTilde).isEqualTo(value)
        }
    }

    @Test
    fun compressWhitespaceCharacters() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.compressWhitespaceCharacters).isEqualTo(Const.COMPRESS_WHITESPACE_CHARACTORS)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("compressWhitespaceCharacters", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.compressWhitespaceCharacters).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("compressWhitespaceCharacters", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.compressWhitespaceCharacters).isEqualTo(value)
        }
    }

    @Test
    fun trimString() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.trimString).isEqualTo(Const.TRIM_STRING)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("trimString", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.trimString).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("trimString", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.trimString).isEqualTo(value)
        }
    }

    @Test
    fun enableImageAssertion() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableImageAssertion).isEqualTo(Const.ENABLE_IMAGE_ASSERTION)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableImageAssertion", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableImageAssertion).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableImageAssertion", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableImageAssertion).isEqualTo(value)
        }
    }

    @Test
    fun imageMatchingScale() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.imageMatchingScale).isEqualTo(Const.IMAGE_MATCHING_SCALE)
        }
        run {
            // Arrange
            val value = 0.9
            PropertiesManager.setPropertyValue("imageMatchingScale", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.imageMatchingScale).isEqualTo(value)
        }
        run {
            // Arrange
            val value = 0.09
            PropertiesManager.setPropertyValue("imageMatchingScale", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.imageMatchingScale
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("imageMatchingScale is allowed from 0.1 to 1.0. (0.09)")
        }
        run {
            // Arrange
            val value = 1.1
            PropertiesManager.setPropertyValue("imageMatchingScale", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.imageMatchingScale
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("imageMatchingScale is allowed from 0.1 to 1.0. (1.1)")
        }
    }

    @Test
    fun imageMatchingThreshold() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.imageMatchingThreshold).isEqualTo(Const.IMAGE_MATCHING_THRESHOLD)
        }
        run {
            // Arrange
            val value = 99.9
            PropertiesManager.setPropertyValue("imageMatchingThreshold", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.imageMatchingThreshold).isEqualTo(value)
        }
    }

    @Test
    fun imageMatchingCandidateCount() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.imageMatchingCandidateCount).isEqualTo(Const.IMAGE_MATCHING_CANDIDATE_COUNT)
        }
        run {
            // Arrange
            val value = 99
            PropertiesManager.setPropertyValue("imageMatchingCandidateCount", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.imageMatchingCandidateCount).isEqualTo(value)
        }
    }

    @Test
    fun enableWdaInstallOptimization() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableWdaInstallOptimization).isEqualTo(Const.ENABLE_WDA_INSTALL_OPTIMIZATION)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableWdaInstallOptimization", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableWdaInstallOptimization).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableWdaInstallOptimization", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableWdaInstallOptimization).isEqualTo(value)
        }
    }

//    @Test
//    fun emulatorPort() {
//
//        run {
//            // Arrange
//            PropertiesManager.clear()
//            // Act, Assert
//            assertThat(PropertiesManager.emulatorPort).isEqualTo(Const.EMULATOR_PORT)
//        }
//        run {
//            // Arrange
//            val value = 5558
//            PropertiesManager.setPropertyValue("emulatorPort", value.toString())
//            // Act, Assert
//            assertThat(PropertiesManager.emulatorPort).isEqualTo(value)
//        }
//    }

    @Test
    fun enableHealthCheck() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableHealthCheck).isEqualTo(Const.ENABLE_HEALTH_CHECK)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableHealthCheck", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableHealthCheck).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableHealthCheck", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableHealthCheck).isEqualTo(value)
        }
    }

    @Test
    fun enableAutoSyncAndroid() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableAutoSyncAndroid).isEqualTo(Const.ENABLE_AUTO_SYNC_ANDROID)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableAutoSyncAndroid", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableAutoSyncAndroid).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableAutoSyncAndroid", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableAutoSyncAndroid).isEqualTo(value)
        }
    }

    @Test
    fun enableAutoSyncIos() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableAutoSyncIos).isEqualTo(Const.ENABLE_AUTO_SYNC_IOS)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableAutoSyncIos", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableAutoSyncIos).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableAutoSyncIos", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableAutoSyncIos).isEqualTo(value)
        }
    }

    @Test
    fun enableLaunchAppOnScenario() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableLaunchOnScenario).isEqualTo(Const.ENABLE_LAUNCH_APP_ON_SCENARIO)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableLaunchOnScenario", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableLaunchOnScenario).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableLaunchOnScenario", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableLaunchOnScenario).isEqualTo(value)
        }
    }

    @Test
    fun launchAppMethod() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.launchAppMethod).isEqualTo(Const.LAUNCH_APP_METHOD)
        }
        run {
            // Arrange
            val value = "shell"
            PropertiesManager.setPropertyValue("launchAppMethod", value)
            // Act, Assert
            assertThat(PropertiesManager.launchAppMethod).isEqualTo(value)
        }
        run {
            // Arrange
            val value = "auto"
            PropertiesManager.setPropertyValue("launchAppMethod", value)
            // Act, Assert
            assertThat(PropertiesManager.launchAppMethod).isEqualTo(value)
        }
    }

    @Test
    fun enableRerunScenario() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableRerunScenario).isEqualTo(Const.ENABLE_RERUN_SCENARIO)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableRerunScenario", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableRerunScenario).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableRerunScenario", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableRerunScenario).isEqualTo(value)
        }
    }

    @Test
    fun enableAlwaysRerunOnErrorAndroid() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableAlwaysRerunOnErrorAndroid).isEqualTo(Const.ENABLE_ALWAYS_RERUN_ON_ERROR_ANDROID)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableAlwaysRerunOnErrorAndroid).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableAlwaysRerunOnErrorAndroid).isEqualTo(value)
        }
    }

    @Test
    fun enableAlwaysRerunOnErrorIos() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableAlwaysRerunOnErrorIos).isEqualTo(Const.ENABLE_ALWAYS_RERUN_ON_ERROR_IOS)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorIos", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableAlwaysRerunOnErrorIos).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorIos", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableAlwaysRerunOnErrorIos).isEqualTo(value)
        }
    }

    @Test
    fun scenarioTimeoutSeconds() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.scenarioTimeoutSeconds).isEqualTo(Const.SCENARIO_TIMEOUT_SECONDS)
        }
        run {
            // Arrange
            val value = 0.0
            PropertiesManager.setPropertyValue("scenarioTimeoutSeconds", "$value")
            // Act, Assert
            assertThat(PropertiesManager.scenarioTimeoutSeconds).isEqualTo(value)
        }
        run {
            // Arrange
            val value = -1.0
            PropertiesManager.setPropertyValue("scenarioTimeoutSeconds", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.scenarioTimeoutSeconds
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("scenarioTimeoutSeconds should be set to 0 or higher. (-1.0)")
        }
    }

    @Test
    fun scenarioMaxCount() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.scenarioMaxCount).isEqualTo(Const.SCENARIO_MAX_COUNT)
        }
        run {
            // Arrange
            val value = 1
            PropertiesManager.setPropertyValue("scenarioMaxCount", "$value")
            // Act, Assert
            assertThat(PropertiesManager.scenarioMaxCount).isEqualTo(value)
        }
        run {
            // Arrange
            val value = 0
            PropertiesManager.setPropertyValue("scenarioMaxCount", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.scenarioMaxCount
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("scenarioMaxCount should be set to 1 or higher. (0)")
        }
    }

    @Test
    fun enableRerunOnScreenshotBlackout() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableRerunOnScreenshotBlackout).isEqualTo(Const.ENABLE_RERUN_ON_SCREENSHOT_BLACKOUT)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableRerunOnScreenshotBlackout", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableRerunOnScreenshotBlackout).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableRerunOnScreenshotBlackout", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableRerunOnScreenshotBlackout).isEqualTo(value)
        }
    }

    @Test
    fun screenshotBlackoutThreshold() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.screenshotBlackoutThreshold).isEqualTo(Const.SCREENSHOT_BLACKOUT_THRESHOLD)
        }
        run {
            // Arrange
            val value = 0.95
            PropertiesManager.setPropertyValue("screenshotBlackoutThreshold", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.screenshotBlackoutThreshold).isEqualTo(value)
        }
        run {
            // Arrange
            val value = 0.89
            PropertiesManager.setPropertyValue("screenshotBlackoutThreshold", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.screenshotBlackoutThreshold
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("screenshotBlackoutThreshold is allowed from 0.9 to 1.0. (0.89)")
        }
        run {
            // Arrange
            val value = 1.1
            PropertiesManager.setPropertyValue("screenshotBlackoutThreshold", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.screenshotBlackoutThreshold
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("screenshotBlackoutThreshold is allowed from 0.9 to 1.0. (1.1)")
        }
    }

    @Test
    fun enableRestartDeviceOnResettingAppiumSession() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableRestartDeviceOnResettingAppiumSession).isEqualTo(Const.ENABLE_RESTART_DEVICE_ON_RESETTING_APPIUM_SESSION)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableRestartDeviceOnResettingAppiumSession", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableRestartDeviceOnResettingAppiumSession).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableRestartDeviceOnResettingAppiumSession", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableRestartDeviceOnResettingAppiumSession).isEqualTo(value)
        }
    }

    @Test
    fun safeCpuLoad() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.safeCpuLoad).isEqualTo(Const.CPU_LOAD_FOR_SAFETY)
        }
        run {
            // Arrange
            val value = 80
            PropertiesManager.setPropertyValue("safeCpuLoad", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.safeCpuLoad).isEqualTo(value)
        }
        run {
            // Arrange
            val value = -1
            PropertiesManager.setPropertyValue("safeCpuLoad", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.safeCpuLoad
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("safeCpuLoad is allowed from 0 to 100. (-1)")
        }
        run {
            // Arrange
            val value = 101
            PropertiesManager.setPropertyValue("safeCpuLoad", value.toString())
            // Act, Assert
            assertThatThrownBy() {
                PropertiesManager.safeCpuLoad
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("safeCpuLoad is allowed from 0 to 100. (101)")
        }
    }

    @Test
    fun enableWaitCpuLoad() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableWaitCpuLoad).isEqualTo(Const.ENABLE_WAIT_CPU_LOAD)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableWaitCpuLoad", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableWaitCpuLoad).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableWaitCpuLoad", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableWaitCpuLoad).isEqualTo(value)
        }
    }

    @Test
    fun enableWaitCpuLoadPrintDebug() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableWaitCpuLoadPrintDebug).isEqualTo(Const.ENABLE_WAIT_CPU_LOAD_PRINT_DEBUG)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("enableWaitCpuLoadPrintDebug", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableWaitCpuLoadPrintDebug).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("enableWaitCpuLoadPrintDebug", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableWaitCpuLoadPrintDebug).isEqualTo(value)
        }
    }

    @Test
    fun tapTestSelector() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.tapTestSelector).isEqualTo(Const.TAP_TEST_SELECTOR)
        }
        run {
            // Arrange
            val value = ".button"
            PropertiesManager.setPropertyValue("tapTestSelector", value)
            // Act, Assert
            assertThat(PropertiesManager.tapTestSelector).isEqualTo(value)
        }
    }

    @Test
    fun macroObjectsScanDir() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.macroObjectScanDir).isEqualTo(Const.MACRO_OBJECT_SCAN_DIR)
        }
        run {
            // Arrange
            val value = Const.MACRO_OBJECT_SCAN_DIR + "2"
            PropertiesManager.setPropertyValue("MacroObject.scan.dir", value)
            // Act, Assert
            assertThat(PropertiesManager.macroObjectScanDir).isEqualTo(value)
        }
    }

    @Test
    fun visionOCRLanguage() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionOCRLanguage).isEqualTo(Const.VISION_OCR_LANGUAGE)
        }
        run {
            // Arrange
            val value = "ja"
            PropertiesManager.setPropertyValue("visionOCRLanguage", value)
            // Act, Assert
            assertThat(PropertiesManager.visionOCRLanguage).isEqualTo(value)
        }
    }

    @Test
    fun visionDirectory() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionDirectory).isEqualTo(Const.VISION_DIRECTORY)
        }
        run {
            // Arrange
            val value = "temp"
            PropertiesManager.setPropertyValue("visionDirectory", value)
            // Act, Assert
            assertThat(PropertiesManager.visionDirectory).isEqualTo(value)
        }
    }

    @Test
    fun visionBuildDirectory() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionBuildDirectory).isEqualTo(Const.VISION_BUILD_DIRECTORY)
        }
        run {
            // Arrange
            val value = "temp"
            PropertiesManager.setPropertyValue("visionBuildDirectory", value)
            // Act, Assert
            assertThat(PropertiesManager.visionBuildDirectory).isEqualTo(value)
        }
    }

    @Test
    fun visionEnableLearningOnStartup() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionEnableLearningOnStartup).isEqualTo(Const.VISION_ENABLE_LEARNING_ON_STARTUP)
        }
        run {
            // Arrange
            val value = Const.VISION_ENABLE_LEARNING_ON_STARTUP.not()
            PropertiesManager.setPropertyValue("visionEnableLearningOnStartup", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.visionEnableLearningOnStartup).isEqualTo(value)
        }
    }

    @Test
    fun visionServerUrl() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionServerUrl).isEqualTo(Const.VISION_SERVER_URL)
        }
        run {
            // Arrange
            val value = "http://127.0.0.1:8082"
            PropertiesManager.setPropertyValue("visionServerUrl", value)
            // Act, Assert
            assertThat(PropertiesManager.visionServerUrl).isEqualTo(value)
        }
    }

    @Test
    fun segmentMarginHorizontal() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.segmentMarginHorizontal).isEqualTo(Const.VISION_SEGMENT_MARGIN_HORIZONTAL)
        }
        run {
            // Arrange
            val value = 30
            PropertiesManager.setPropertyValue("segmentMarginHorizontal", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.segmentMarginHorizontal).isEqualTo(value)
        }
    }

    @Test
    fun segmentMarginVertical() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.segmentMarginVertical).isEqualTo(Const.VISION_SEGMENT_MARGIN_VERTICAL)
        }
        run {
            // Arrange
            val value = 15
            PropertiesManager.setPropertyValue("segmentMarginVertical", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.segmentMarginVertical).isEqualTo(value)
        }
    }

    @Test
    fun segmentCroppingMargin() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.segmentCroppingMargin).isEqualTo(Const.VISION_SEGMENT_CROPPING_MARGIN)
        }
        run {
            // Arrange
            val value = 15
            PropertiesManager.setPropertyValue("segmentCroppingMargin", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.segmentCroppingMargin).isEqualTo(value)
        }
    }

    @Test
    fun visionFindImageThreshold() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionFindImageThreshold).isEqualTo(Const.VISION_FIND_IMAGE_THRESHOLD)
        }
        run {
            // Arrange
            val value = 0.5
            PropertiesManager.setPropertyValue("visionFindImageThreshold", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.visionFindImageThreshold).isEqualTo(value)
        }
    }

    @Test
    fun visionFindImageBinaryThreshold() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionFindImageBinaryThreshold).isEqualTo(Const.VISION_FIND_IMAGE_BINARY_THRESHOLD)
        }
        run {
            // Arrange
            val value = 100
            PropertiesManager.setPropertyValue("visionFindImageBinaryThreshold", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.visionFindImageBinaryThreshold).isEqualTo(value)
        }
    }

    @Test
    fun visionFindImageAspectRatioTolerance() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionFindImageAspectRatioTolerance).isEqualTo(Const.VISION_FIND_IMAGE_ASPECT_RATIO_TOLERANCE)
        }
        run {
            // Arrange
            val value = 100.0
            PropertiesManager.setPropertyValue("visionFindImageAspectRatioTolerance", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.visionFindImageAspectRatioTolerance).isEqualTo(value)
        }
    }

    @Test
    fun visionTextIndexTrimChars() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionTextIndexTrimChars).isEqualTo(Const.VISION_TEXT_INDEX_TRIM_CHARS_FOR_JA)
        }
        run {
            // Arrange
            val value = "-,."
            PropertiesManager.setPropertyValue("visionTextIndexTrimChars", value)
            // Act, Assert
            assertThat(PropertiesManager.visionTextIndexTrimChars).isEqualTo(value)
        }
    }

    @Test
    fun visionSyncImageMatchRate() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.visionSyncImageMatchRate).isEqualTo(Const.VISION_SYNC_IMAGE_MATCH_RATE)
        }
        run {
            // Arrange
            val value = 1.0
            PropertiesManager.setPropertyValue("visionSyncImageMatchRate", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.visionSyncImageMatchRate).isEqualTo(value)
        }
    }

    @Test
    fun specReportExcludeIDetail() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.specReportExcludeDetail).isEqualTo(Const.SPECREPORT_EXCLUDE_DETAIL)
        }
        run {
            // Arrange
            val value = true
            PropertiesManager.setPropertyValue("specReport.exclude.detail", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.specReportExcludeDetail).isEqualTo(value)
        }
        run {
            // Arrange
            val value = false
            PropertiesManager.setPropertyValue("specReport.exclude.detail", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.specReportExcludeDetail).isEqualTo(value)
        }
    }

    @Test
    fun specReportSKIPReason() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.specReportSKIPReason).isEqualTo("")
        }
        run {
            // Arrange
            val value = "SKIP"
            PropertiesManager.setPropertyValue("specReport.SKIP.reason", value)
            // Act, Assert
            assertThat(PropertiesManager.specReportSKIPReason).isEqualTo(value)
        }
    }

    @Test
    fun specReportEXCLUDEDReason() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.specReportEXCLUDEDReason).isEqualTo("")
        }
        run {
            // Arrange
            val value = "EXCLUDED"
            PropertiesManager.setPropertyValue("specReport.EXCLUDED.reason", value)
            // Act, Assert
            assertThat(PropertiesManager.specReportEXCLUDEDReason).isEqualTo(value)
        }
    }

//    @Test
//    fun swipeOffsetY() {
//
//        run {
//            // Arrange
//            TestMode.setAndroid()
//            PropertiesManager.setPropertyValue("os", "android")
//            // Act, Assert
//            assertThat(PropertiesManager.swipeOffsetY).isEqualTo(Const.ANDROID_SWIPE_OFFSET_Y)
//        }
//        run {
//            // Arrange
//            TestMode.setAndroid()
//            PropertiesManager.clear()
//            val value = 1
//            PropertiesManager.setPropertyValue("android.swipeOffsetY", value.toString())
//            // Act, Assert
//            assertThat(PropertiesManager.swipeOffsetY).isEqualTo(value)
//        }
//        run {
//            // Arrange
//            TestMode.setIos()
//            PropertiesManager.setPropertyValue("os", "ios")
//            // Act, Assert
//            assertThat(PropertiesManager.swipeOffsetY).isEqualTo(Const.IOS_SWIPE_OFFSET_Y)
//        }
//        run {
//            // Arrange
//            PropertiesManager.setPropertyValue("os", "ios")
//            val value = 2
//            PropertiesManager.setPropertyValue("ios.swipeOffsetY", value.toString())
//            // Act, Assert
//            assertThat(PropertiesManager.swipeOffsetY).isEqualTo(value)
//        }
//    }

    @Test
    fun xmlSourceRemovePattern() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.xmlSourceRemovePattern).isEqualTo("")
        }
        run {
            // Arrange
            val value = "xmlSourceRemovePattern"
            PropertiesManager.setPropertyValue("xmlSourceRemovePattern", value)
            // Act, Assert
            assertThat(PropertiesManager.xmlSourceRemovePattern).isEqualTo(value)
        }
    }

    @Test
    fun selectIgnoreTypes() {

        run {
            // Arrange
            TestMode.setAndroid()
            PropertiesManager.clear()
            PropertiesManager.setPropertyValue("os", "android")
            // Act, Assert
            assertThat(PropertiesManager.selectIgnoreTypes).isEqualTo(
                Const.ANDROID_SELECT_IGNORE_TYPES.split(",").filter { it.isNotBlank() }.toMutableList()
            )

            // Arrange
            PropertiesManager._selectIgnoreTypesForAndroid = null
            PropertiesManager.setPropertyValue("android.selectIgnoreTypes", "value1")
            // Act, Assert
            assertThat(PropertiesManager.selectIgnoreTypes).isEqualTo(mutableListOf("value1"))
        }
        run {
            // Arrange
            TestMode.setIos()
            PropertiesManager.clear()
            PropertiesManager.setPropertyValue("os", "ios")
            // Act, Assert
            assertThat(PropertiesManager.selectIgnoreTypes).isEqualTo(
                Const.IOS_SELECT_IGNORE_TYPES.split(",").toMutableList()
            )

            // Arrange
            PropertiesManager._selectIgnoreTypesForIos = null
            PropertiesManager.setPropertyValue("ios.selectIgnoreTypes", "value2")
            // Act, Assert
            assertThat(PropertiesManager.selectIgnoreTypes).isEqualTo(mutableListOf("value2"))
        }
    }

    @Test
    fun titleSelector() {

        run {
            // Arrange
            TestMode.setAndroid()
            PropertiesManager.clear()
            PropertiesManager.setPropertyValue("os", "android")
            // Act, Assert
            assertThat(PropertiesManager.titleSelector).isEqualTo(Const.ANDROID_TITLE_SELECTOR)
        }
        run {
            // Arrange
            TestMode.setIos()
            PropertiesManager.clear()
            PropertiesManager.setPropertyValue("os", "ios")
            // Act, Assert
            assertThat(PropertiesManager.titleSelector).isEqualTo(Const.IOS_TITLE_SELECTOR)
        }
    }

    @Test
    fun webTitleSelector() {

        run {
            // Arrange
            TestMode.setAndroid()
            PropertiesManager.clear()
            PropertiesManager.setPropertyValue("os", "android")
            // Act, Assert
            assertThat(PropertiesManager.webTitleSelector).isEqualTo(Const.ANDROID_WEBTITLE_SELECTOR)
        }
        run {
            // Arrange
            TestMode.setIos()
            PropertiesManager.clear()
            PropertiesManager.setPropertyValue("os", "ios")
            // Act, Assert
            assertThat(PropertiesManager.webTitleSelector).isEqualTo(Const.IOS_WEBTITLE_SELECTOR)
        }
    }

    @Test
    fun jquerySource() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.jquerySource).isEqualTo(Const.JQUERY_SOURCE)
        }
        run {
            // Arrange
            PropertiesManager.clear()
            PropertiesManager.setPropertyValue("jquerySource", "jquerySource")
            // Act, Assert
            assertThat(PropertiesManager.jquerySource).isEqualTo("jquerySource")
        }

    }
}