package shirates.core.unittest.storage

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.TestConfig
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.storage.Data
import shirates.core.storage.data
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath

class DataTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        val configPath = "unitTestData/testConfig/androidSettings/testConfigTestData1.json".toPath()
        TestConfig(configPath.toString())
    }

    @Test
    fun value() {

        // Act, Assert
        assertThat(Data.getValue(longKey = "[dataset1].attribute1-1")).isEqualTo("value1-1")
        assertThat(Data.getValue(longKey = "[dataset1].attribute1-2")).isEqualTo("value1-2")
    }

    @Test
    fun data_longKey() {

        // Act, Assert
        assertThat(data(longKey = "[dataset1].attribute1-1")).isEqualTo("value1-1")
        assertThat(data("[dataset1].attribute1-2")).isEqualTo("value1-2")
    }

    @Test
    fun data_datasetName_attributeName() {

        // Act, Assert
        assertThat(data(datasetName = "[dataset1]", attributeName = "attribute1-1")).isEqualTo("value1-1")
        assertThat(data("[dataset1]", "attribute1-2")).isEqualTo("value1-2")
    }

    @Test
    fun exceptions() {

        // Act, Assert
        assertThatThrownBy {
            data("[no exist].key1")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "datasetNotFoundInRepository", repository = "data", dataset = "[no exist]"))

        // Act, Assert
        assertThatThrownBy {
            data("[dataset1].noExistKey")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(
                message(
                    id = "attributeNotFoundInDataset",
                    repository = "data",
                    dataset = "[dataset1]",
                    attribute = "noExistKey"
                )
            )
    }

}
