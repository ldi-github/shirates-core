package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.TestConfigContainer
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath
import java.io.FileNotFoundException

class TestConfigContainerTest : UnitTest() {

    @Test
    fun init_with_starts_pattern() {

        // Act
        val container = TestConfigContainer("unitTestConfig/others/testConfig/test1@*")
        // Assert
        assertThat(container.testConfigDirectory).isEqualTo("unitTestConfig/others/testConfig".toPath())
        assertThat(container.testConfigs.count()).isEqualTo(2)
        assertThat(container.testConfigs.any { it.testConfigName == "test1@a" }).isTrue()
        assertThat(container.testConfigs.any { it.testConfigName == "test1@i" }).isTrue()
    }

    @Test
    fun init_with_contains_pattern() {

        // Act
        val container = TestConfigContainer("unitTestConfig/others/testConfig/test1@*.json")
        // Assert
        assertThat(container.testConfigs.count()).isEqualTo(2)
        assertThat(container.testConfigs.any { it.testConfigName == "test1@a" }).isTrue()
        assertThat(container.testConfigs.any { it.testConfigName == "test1@i" }).isTrue()
    }

    @Test
    fun init_with_ends_pattern() {

        // Arrange
        val pattern = "unitTestConfig/others/testConfig/*@i.json"
        // Act, Assert
        assertThatThrownBy {
            TestConfigContainer(pattern)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(
                message(
                    id = "multipleConfigurationFileNotAllowed",
                    arg1 = pattern,
                    arg2 = "test1@i.json, test2@i.json"
                )
            )
    }

    @Test
    fun init_with_all_pattern() {

        // Arrange
        val pattern = "unitTestConfig/others/testConfig/*"
        // Act, Assert
        assertThatThrownBy {
            TestConfigContainer(pattern)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(
                message(
                    id = "multipleConfigurationFileNotAllowed",
                    arg1 = pattern,
                    arg2 = "test1@a.json, test2@a.json"
                )
            )
    }

    @Test
    fun init_with_multiple_wildcard() {

        // Act, Assert
        assertThatThrownBy {
            TestConfigContainer("unitTestConfig/others/testConfig/*@i*")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage(message(id = "multipleWildcardNotAllowed", arg1 = "unitTestConfig/others/testConfig/*@i*"))
    }

    @Test
    fun init_with_invalid_directory() {

        // Act, Assert
        assertThatThrownBy {
            TestConfigContainer("hoge/hoge/*.json")
        }.isInstanceOf(FileNotFoundException::class.java)
            .hasMessage(message(id = "directoryNotFoundWithPattern", arg1 = "hoge/hoge/*.json"))
    }

    @Test
    fun init_with_directory_without_files() {

        val container = TestConfigContainer("unitTestConfig/others/noFileDirectory/*")
        assertThat(container.testConfigs.count()).isEqualTo(0)
    }

    @Test
    fun init_with_profile_name_duplicated() {

        // Arrange
        val pattern = "unitTestConfig/others/profileNameDuplicated/*"
        // Act, Assert
        assertThatThrownBy {
            TestConfigContainer(pattern)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "profileNameDuplicated", arg1 = "profile1", arg2 = ""))
    }

    @Test
    fun getProfile() {

        // Arrange
        val container = TestConfigContainer("unitTestConfig/others/testConfig/test1@*.json")
        // Act
        val profile = container.getProfile(profileName = "Android 12")
        // Assert
        assertThat(profile?.profileName).isEqualTo("Android 12")
        assertThat(profile?.testConfig?.testConfigName).isEqualTo("test1@a")

        // Act, Assert
        assertThat(container.getProfile(profileName = "foo")).isNull()
    }

    @Test
    fun lastCreated() {

        // Arrange
        TestConfigContainer("unitTestConfig/others/testConfig/test1@*.json")
        val last1 = TestConfigContainer.lastCreated
        // Act
        TestConfigContainer("unitTestConfig/others/testConfig/test1@*.json")
        val last2 = TestConfigContainer.lastCreated
        // Assert
        assertThat(last1).isNotEqualTo(last2)
    }
}