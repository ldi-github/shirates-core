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
import java.nio.file.Files
import java.util.*

class PropertiesManagerTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        PropertiesManager.clear()
        EnvUtility.reset()
        EnvUtility.setEnvForTesting("SR_os", "")
    }

    @Test
    fun setup_clear() {

        run {
            // Act
            PropertiesManager.clear()
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.properties.count()).isEqualTo(0)
        }
        run {
            // Arrange
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.properties.count()).isEqualTo(0)

            val globalPropsPath = Const.TESTRUN_GLOBAL_PROPERTIES.toPath()
            val testrunPath = Const.TESTRUN_PROPERTIES.toPath()
            var props = Properties()
            var globalProps = Properties()
            var testrunProps = Properties()
            var srEnvs = mapOf<String, String>()

            val refreshAction = {
                props = Properties()
                globalProps = PropertiesUtility.getProperties(globalPropsPath)
                println()
                println("[testrun.global.properties] $globalPropsPath")
                for (p in globalProps) {
                    props[p.key] = p.value
                    println("${p.key}=${p.value}")
                }
                testrunProps =
                    if (Files.exists(testrunPath)) PropertiesUtility.getProperties(testrunPath)
                    else Properties()
                println()
                println("[testrun.properties] $testrunPath")
                for (p in testrunProps) {
                    props[p.key] = p.value
                    println("${p.key}=${p.value}")
                }
                srEnvs = EnvUtility.getEnv().filter { it.key.uppercase().startsWith("SR_") }
                println()
                println("[SR_ environment variables]")
                for (env in srEnvs) {
                    val key = env.key.substring("SR_".length)
                    props[key] = env.value
                    println("${key}=${env.value}")
                }
            }
            refreshAction()
            // Act
            PropertiesManager.setup()
            println()
            println("[properties] count=${PropertiesManager.properties.count()}")
            for (p in PropertiesManager.properties) {
                println(p)
            }
            refreshAction()
            // Assert
            println("// testrunGlobalProperties=${PropertiesManager.testrunGlobalProperties.count()}")
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(globalProps.count())
            println("// testrunProperties=${PropertiesManager.testrunProperties.count()}")
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(testrunProps.count())
            println("// envProperties=${PropertiesManager.envProperties.count()}")
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(srEnvs.count())
            println("// properties=${PropertiesManager.properties.count()}")
            assertThat(PropertiesManager.properties.count()).isEqualTo(props.count())
            PropertiesManager.properties.forEach { println(it) }
        }

        // Act
        PropertiesManager.clear()
        // Assert
        assertThat(PropertiesManager.properties.count()).isEqualTo(0)
    }

    @Test
    fun setup_testrunFile_env() {

        // Arrange
        val globalProps = PropertiesUtility.getProperties("testrun.global.properties".toPath())

        // testrunFile(not exist)
        run {
            // Arrange
            val testrunFile = "not.exist.testrun.properties"
            assertThat(Files.exists(testrunFile.toPath())).isFalse()
            // Act
            PropertiesManager.setup(testrunFile = testrunFile)
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(globalProps.count())
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(EnvUtility.getSREnvMap().count())
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
            // Act
            PropertiesManager.setup(testrunFile)
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(globalProps.count())
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(EnvUtility.getSREnvMap().count())
        }
        //
        run {
            // Arrange
            val testrunFile = ""
            EnvUtility.reset()
            // Act
            PropertiesManager.setup(testrunFile = testrunFile)
            // Assert
            assertThat(PropertiesManager.testrunGlobalProperties.count()).isEqualTo(globalProps.count())
            assertThat(PropertiesManager.testrunProperties.count()).isEqualTo(0)
            assertThat(PropertiesManager.envProperties.count()).isEqualTo(EnvUtility.getSREnvMap().count())
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
    fun configFile() {

        run {
            // Arrange
            TestMode.setAndroid()
            PropertiesManager.clear()
            val configFile = "unitTestConfig/android/androidSettings/testrun.properties"
            PropertiesManager.setPropertyValue("android.configFile", configFile)
            // Act, Assert
            assertThat(PropertiesManager.configFile).isEqualTo(configFile)
        }
        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThatThrownBy {
                PropertiesManager.configFile
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessageStartingWith("android.configFile is required.")
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
                // Act, Assert
                assertThatThrownBy {
                    PropertiesManager.profile
                }.isInstanceOf(TestConfigException::class.java)
                    .hasMessageStartingWith("android.profile is required.")
            }
        }
        run {

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
    fun enableTimeDiff() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.enableTimeDiff).isEqualTo(Const.ENABLE_TIME_DIFF)
        }
        run {
            // Arrange
            val value = Const.ENABLE_TIME_DIFF.not()
            PropertiesManager.setPropertyValue("enableTimeDiff", value.toString())
            // Act, Assert
            assertThat(PropertiesManager.enableTimeDiff).isEqualTo(value)
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
    fun specReportReplaceMANUAL() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.specReportReplaceMANUAL).isEqualTo("")
        }
        run {
            // Arrange
            val value = "MANUAL"
            PropertiesManager.setPropertyValue("specReport.replace.MANUAL", value)
            // Act, Assert
            assertThat(PropertiesManager.specReportReplaceMANUAL).isEqualTo(value)
        }
    }

    @Test
    fun specReportReplaceMANUALReason() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.specReportReplaceMANUALReason).isEqualTo("")
        }
        run {
            // Arrange
            val value = "MANUAL"
            PropertiesManager.setPropertyValue("specReport.replace.MANUAL.reason", value)
            // Act, Assert
            assertThat(PropertiesManager.specReportReplaceMANUALReason).isEqualTo(value)
        }
    }

    @Test
    fun specReportReplaceSKIP() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.specReportReplaceSKIP).isEqualTo("")
        }
        run {
            // Arrange
            val value = "SKIP"
            PropertiesManager.setPropertyValue("specReport.replace.SKIP", value)
            // Act, Assert
            assertThat(PropertiesManager.specReportReplaceSKIP).isEqualTo(value)
        }
    }

    @Test
    fun specReportReplaceSKIPReason() {

        run {
            // Arrange
            PropertiesManager.clear()
            // Act, Assert
            assertThat(PropertiesManager.specReportReplaceSKIPReason).isEqualTo("")
        }
        run {
            // Arrange
            val value = "SKIP"
            PropertiesManager.setPropertyValue("specReport.replace.SKIP.reason", value)
            // Act, Assert
            assertThat(PropertiesManager.specReportReplaceSKIPReason).isEqualTo(value)
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