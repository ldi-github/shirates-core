package shirates.core.unittest.storage

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.TestConfig
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.storage.App
import shirates.core.storage.app
import shirates.core.storage.appIconName
import shirates.core.storage.appId
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath

class AppTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        val configPath = "unitTestData/testConfig/androidSettings/testConfigTestData1.json".toPath()
        TestConfig(configPath.toString())
    }

    @Test
    fun value() {

        // Act, Assert
        assertThat(App.getValue(longKey = "[App1].packageOrBundleId")).isEqualTo("com.example.app1")
    }

    @Test
    fun app_longKey() {

        // Act, Assert
        assertThat(app(longKey = "[App1].packageOrBundleId")).isEqualTo("com.example.app1")
    }

    @Test
    fun app_datasetName_attributeName() {

        // Act, Assert
        assertThat(app(datasetName = "[App1]", attributeName = "packageOrBundleId")).isEqualTo("com.example.app1")
        assertThat(app("[App1]", "packageOrBundleId")).isEqualTo("com.example.app1")
    }

    @Test
    fun appIdTest() {

        // Act, Assert
        assertThat(appId("[App1]")).isEqualTo("com.example.app1")
        assertThat(appId("[not.registered.id]")).isEqualTo("not.registered.id")
    }

    @Test
    fun appIconNameTest() {

        // Act, Assert
        assertThat(appIconName("[App1]")).isEqualTo("App1")
        assertThat(appIconName("[not.registered.name]")).isEqualTo("not.registered.name")
    }

    @Test
    fun noExist() {

        // Act, Assert
        assertThat(App.getValue(longKey = "[no exist].key1", throwsException = false)).isEmpty()

        // Act, Assert
        assertThatThrownBy {
            App.getValue(longKey = "[no exist].key1", throwsException = true)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "datasetNotFoundInRepository", repository = "apps", dataset = "[no exist]"))

        // Act, Assert
        assertThatThrownBy {
            App.getValue(longKey = "[App1].noExistKey", throwsException = true)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(
                message(
                    id = "attributeNotFoundInDataset",
                    repository = "apps",
                    dataset = "[App1]",
                    attribute = "noExistKey"
                )
            )
    }

}
