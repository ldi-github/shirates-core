package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.TestConfig
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath

class TestConfigTest : UnitTest() {

    @Test
    fun lastCreated() {

        // Arrange
        TestConfig.lastCreated = null
        run {
            // Act
            val config = TestConfig("unitTestData/testConfig/androidSettings/androidSettingsConfig.json")
            // Assert
            assertThat(TestConfig.lastCreated).isEqualTo(config)
        }
        run {
            // Act
            val config = TestConfig("unitTestData/testConfig/androidSettings/testConfigTestData1.json")
            // Assert
            assertThat(TestConfig.lastCreated).isEqualTo(config)
        }
    }

    @Test
    fun toStringTest() {

        // Arrange
        val file = "unitTestData/testConfig/androidSettings/androidSettingsConfig.json"
        val config = TestConfig(file)
        assertThat(config.testConfigFile).isEqualTo(file)
        // Act, Assert
        assertThat(config.toString()).isEqualTo(file)

    }

    @Test
    fun testConfig_fileNotFound() {

        val file = "not/exist/config.json"
        assertThatThrownBy {
            TestConfig(file)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageContaining(message(id = "fileNotFound", file = file.toPath().toString()))
    }

    @Test
    fun init_brokenJsonFile() {

        val file = "unitTestData/testConfig/errorScreens/[brokenJson].json"
        assertThatThrownBy {
            TestConfig(file)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageContaining(
                message(id = "failedToConvertToJSONObject", file = file.toPath().toString())
            )
    }

    @Test
    fun getTestProfile_profileName() {

        // Arrange
        val config = TestConfig("unitTestData/testConfig/androidSettings/testConfigTestData1.json")

        // Act, Assert
        val p = config["profile1"]
        assertThat(p.implicitlyWaitSeconds).isEqualTo(null)

        // Act, Assert
        val profileName = "no name"
        assertThatThrownBy {
            config[profileName]
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "notFound", subject = "profileName", value = profileName))
    }

    @Test
    fun init_noTestConfigName() {

        val file = "unitTestData/testConfig/errorConfig/noTestConfigName.json"
        val path = file.toPath()
        assertThatThrownBy {
            TestConfig(file)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "requiredInFile", subject = "testConfigName", file = "$path"))
    }

    @Test
    fun init_missmatchTestConfigName() {

        val file = "unitTestData/testConfig/errorConfig/missmatchTestConfigName.json"
        assertThatThrownBy {
            TestConfig(file)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(
                message(
                    id = "setTestConfigNameToTheFileName",
                    subject = "testConfigName",
                    file = file.toPath().toString()
                )
            )
    }

    @Test
    fun init_noProfileName() {

        assertThatThrownBy {
            TestConfig("unitTestData/testConfig/errorConfig/noProfileNameConfig.json")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageStartingWith("profileName is required.(index=0)")
    }

    @Test
    fun init_section() {

        run {
            // Arrange
            val file = "unitTestData/testConfig/androidSettings/testConfigSection1.json"
            PropertiesManager.setup()
            DatasetRepositoryManager.clear()
            // Act
            val config = TestConfig(file)
            // Assert
            assertThat(DatasetRepositoryManager.repositories.count()).isEqualTo(3)
            assertThat(config.importScreenDirectories.count()).isEqualTo(1)
            for (profile in config.profileMap.values) {
                assertThat(profile.settings.contains("always_finish_activities")).isTrue()
            }
        }
        run {
            // Arrange
            val file = "unitTestData/testConfig/androidSettings/testConfigSection2.json"
            DatasetRepositoryManager.clear()
            // Act
            val config = TestConfig(file)
            // Assert
            assertThat(DatasetRepositoryManager.repositories.count()).isEqualTo(0)
            assertThat(config.importScreenDirectories.count()).isEqualTo(0)
            for (profile in config.profileMap.values) {
                assertThat(profile.settings.contains("always_finish_activities")).isFalse()
            }
        }
        run {
            // Arrange
            val file = "unitTestData/testConfig/androidSettings/testConfigSection3.json"
            DatasetRepositoryManager.clear()
            // Act
            val config = TestConfig(file)
            // Assert
            assertThat(DatasetRepositoryManager.repositories.count()).isEqualTo(0)
            assertThat(config.importScreenDirectories.count()).isEqualTo(0)
            for (profile in config.profileMap.values) {
                assertThat(profile.settings.contains("always_finish_activities")).isFalse()
            }
        }
    }

    @Test
    fun init_commonProfile() {

        // Arrange
        PropertiesManager.setup()
        // Act
        val config = TestConfig("unitTestData/testConfig/androidSettings/testConfigCommonProfile1.json")
        // Assert
        assertThat(config.commonProfile.appiumPath).isEqualTo("appiumPath")
        assertThat(config.commonProfile.appiumArgs).isEqualTo("appiumArgs")
        assertThat(config.commonProfile.appiumArgsSeparator).isEqualTo("appiumArgsSeparator")
        assertThat(config.commonProfile.appiumServerStartupTimeoutSeconds).isEqualTo("appiumServerStartupTimeoutSeconds")
        assertThat(config.commonProfile.appiumSessionStartupTimeoutSeconds).isEqualTo("appiumSessionStartupTimeoutSeconds")
    }

    @Test
    fun init_notImplementedProperty() {

        // Arrange
        TestLog.info("")
        assertThat(TestLog.lines.last().message).isEqualTo("")

        // Act
        TestConfig("unitTestData/testConfig/errorConfig/notImplementedPropertyConfig.json")

        // Assert
        val last = TestLog.lines.last()
        assertThat(last.logType).isEqualTo(LogType.WARN)
        assertThat(last.message).isEqualTo(message(id = "failedToSetProperty", subject = "property999"))
    }

    @Test
    fun init_config_properties_file() {

        // Arrange
        PropertiesManager.setup("unitTestData/testConfig/androidSettings/configProperties.testrun.properties")
        // Act
        val config = TestConfig(PropertiesManager.getPropertyValue("android.configFile")!!)
        // Assert
        val profile = config.profileMap.values.first()
        assertThat(profile.settings.count()).isEqualTo(3)
        assertThat(profile.settings["settings_test_data_1"]).isEqualTo("1")
        assertThat(profile.settings["settings_test_data_2"]).isEqualTo("2")
        assertThat(profile.capabilities["capability1"]).isEqualTo("1")
        assertThat(profile.capabilities["capability2"]).isEqualTo("2")
    }

    @Test
    fun init_datasetFileNotFound() {

        // Act, Assert
        assertThatThrownBy {
            TestConfig("unitTestData/testConfig/errorConfig/datasetFileNotFound.json")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Dataset file not found. (dataset=data, file=not.exist.json)")
    }

    @Test
    fun init_datasetNameMustBeSameAsFileName() {

        // Act, Assert
        assertThatThrownBy {
            TestConfig("unitTestData/testConfig/errorConfig/datasetNameMustBeSameAsFileName.json")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Dataset name must be the same as file name. (dataset=dataset1, file=unitTestData/testConfig/androidSettings/dataset/data.json)")

    }

    @Test
    fun init_importMustBeJSONArray() {

        // Arrange
        val file = "unitTestData/testConfig/errorConfig/importMustBeJSONArray.json"
        // Act, Assert
        assertThatThrownBy {
            TestConfig(file)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("import must be JSONArray. (file=${file.toPath().toAbsolutePath()})")
    }

    @Test
    fun init_screensDirectoryNotFound() {

        // Arrange
        val file = "unitTestData/testConfig/errorConfig/screensDirectoryNotFound.json"
        // Act, Assert
        assertThatThrownBy {
            TestConfig(file)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("screens directory not found. (not/exist/directory/screens)")
    }

    @Test
    fun testConfigTestData1() {

        /**
         * Arrange, Act
         */
        PropertiesManager.setup()
        val config = TestConfig("unitTestData/testConfig/androidSettings/testConfigTestData1.json")
        val prof = config.commonProfile

        /**
         * Assert
         */
        assertThat(config.testConfigName).isEqualTo("testConfigTestData1")
        assertThat(prof.autoScreenshot).isEqualTo("false")
        assertThat(prof.onChangedOnly).isEqualTo("false")
        assertThat(prof.onCondition).isEqualTo("false")
        assertThat(prof.onAction).isEqualTo("false")
        assertThat(prof.onExpectation).isEqualTo("false")
        assertThat(prof.onExecOperateCommand).isEqualTo("false")
        assertThat(prof.onCheckCommand).isEqualTo("false")
        assertThat(prof.onScrolling).isEqualTo("false")
        assertThat(prof.manualScreenshot).isEqualTo("false")
        assertThat(prof.appiumPath).isNullOrEmpty()
        assertThat(prof.stubServerUrl).isEqualTo("http://stub1")
//        assertThat(prof.appiumServerUrl).isEqualTo("http://127.0.0.1:4720/")
        assertThat(prof.implicitlyWaitSeconds).isEqualTo(null)
        assertThat(prof.shortWaitSeconds).isEqualTo("2")
        assertThat(prof.waitSecondsOnIsScreen).isEqualTo("21.1")
        assertThat(prof.waitSecondsForLaunchAppComplete).isEqualTo("32.1")
        assertThat(prof.waitSecondsForAnimationComplete).isEqualTo("1.23")
        assertThat(prof.swipeDurationSeconds).isEqualTo("3.2")
        assertThat(prof.flickDurationSeconds).isEqualTo("0.33")
        assertThat(prof.swipeMarginRatio).isEqualTo("0.25")
        assertThat(prof.scrollVerticalStartMarginRatio).isEqualTo("0.15")
        assertThat(prof.scrollVerticalEndMarginRatio).isEqualTo("0.1")
        assertThat(prof.scrollHorizontalStartMarginRatio).isEqualTo("0.2")
        assertThat(prof.scrollHorizontalEndMarginRatio).isEqualTo("0.05")
        assertThat(prof.scrollMaxCount).isEqualTo("7")
        assertThat(prof.tapHoldSeconds).isEqualTo("0.4")
        assertThat(prof.enableCache).isEqualTo("false")
        assertThat(prof.findWebElementTimeoutSeconds).isEqualTo("0.123")
        assertThat(prof.syncWaitSeconds).isEqualTo("0.22")
        assertThat(prof.syncMaxLoopCount).isEqualTo("5")
        assertThat(prof.syncIntervalSeconds).isEqualTo("1.5")
        assertThat(prof.retryMaxCount).isEqualTo("3")
        assertThat(prof.retryIntervalSeconds).isEqualTo("3.0")
        assertThat(prof.appPackageDir).isNull()
        assertThat(prof.appPackageFile).isEqualTo("app1.apk")

        // settings
        assertThat(prof.settings["always_finish_activities"]).isEqualTo("1")

        // profiles
        assertThat(config.profileMap.count()).isEqualTo(4)

        run {
            // profile1
            val p1 = config.profileMap["profile1"]!!
            assertThat(p1.profileName).isEqualTo("profile1")
            assertThat(p1.autoScreenshot).isEqualTo("true")
            assertThat(p1.onChangedOnly).isEqualTo("true")
            assertThat(p1.onCondition).isEqualTo("true")
            assertThat(p1.onAction).isEqualTo("true")
            assertThat(p1.onExpectation).isEqualTo("true")
            assertThat(p1.onExecOperateCommand).isEqualTo("true")
            assertThat(p1.onCheckCommand).isEqualTo("true")
            assertThat(p1.onScrolling).isEqualTo("true")
            assertThat(p1.manualScreenshot).isEqualTo("true")
            assertThat(p1.implicitlyWaitSeconds).isEqualTo(null)
            assertThat(p1.retryMaxCount).isEqualTo("1")
            assertThat(p1.retryIntervalSeconds).isEqualTo("3.5")
            val cap1 = mapOf(
                "appPackage" to "com.example.app1",
                "appActivity" to "com.example.app1.MainActivity",
                "platformVersion" to "9",
                "deviceName" to "Pixel_2_API_28",
            )
            assertThat(p1.capabilities).containsAllEntriesOf(cap1)
        }

        run {
            // profile2
            val p2 = config.profileMap["profile2"]!!
            assertThat(p2.profileName).isEqualTo("profile2")
            assertThat(p2.autoScreenshot).isEqualTo("false")
            assertThat(p2.onChangedOnly).isEqualTo("false")
            assertThat(p2.onCondition).isEqualTo("false")
            assertThat(p2.onAction).isEqualTo("false")
            assertThat(p2.onExpectation).isEqualTo("false")
            assertThat(p2.onExecOperateCommand).isEqualTo("false")
            assertThat(p2.onCheckCommand).isEqualTo("false")
            assertThat(p2.onScrolling).isEqualTo("false")
            assertThat(p2.manualScreenshot).isEqualTo("false")
            assertThat(p2.implicitlyWaitSeconds).isEqualTo(null)
            assertThat(p2.retryMaxCount).isEqualTo("3")
            assertThat(p2.retryIntervalSeconds).isEqualTo("3.0")
            val cap2 = mapOf(
                "appPackage" to "com.example.app1",
                "appActivity" to "com.example.app1.MainActivity",
                "automationName" to "UiAutomator3",
                "platformVersion" to "8.1",
                "deviceName" to "Pixel"
            )
            assertThat(p2.capabilities).containsAllEntriesOf(cap2)
        }

        run {
            // profile3
            val p3 = config.profileMap["profile3"]!!
            assertThat(p3.profileName).isEqualTo("profile3")
            assertThat(p3.autoScreenshot).isEqualTo("false")
            assertThat(p3.onChangedOnly).isEqualTo("false")
            assertThat(p3.onCondition).isEqualTo("false")
            assertThat(p3.onAction).isEqualTo("false")
            assertThat(p3.onExpectation).isEqualTo("false")
            assertThat(p3.onExecOperateCommand).isEqualTo("false")
            assertThat(p3.onCheckCommand).isEqualTo("false")
            assertThat(p3.onScrolling).isEqualTo("false")
            assertThat(p3.manualScreenshot).isEqualTo("false")
            assertThat(p3.implicitlyWaitSeconds).isEqualTo(null)
            assertThat(p3.retryMaxCount).isEqualTo("3")
            assertThat(p3.retryIntervalSeconds).isEqualTo("3.0")
            assertThat(p3.capabilities["appActivity"]).isEqualTo("com.example.app1.MainActivity")
        }
    }
}