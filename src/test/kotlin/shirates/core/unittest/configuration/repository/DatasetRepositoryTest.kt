package shirates.core.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.TestConfig
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.exception.TestConfigException
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath

class DatasetRepositoryTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        val configPath = "unitTestData/testConfig/androidSettings/testConfigTestData1.json".toPath()
        TestConfig(configPath.toString())
    }

    @Test
    fun contains() {

        // Arrange
        val repo = DatasetRepositoryManager.getRepository(repositoryName = "data")
        // Act, Assert
        assertThat(repo.contains(datasetName = "[dataset1]")).isTrue()
        assertThat(repo.contains(datasetName = "[dataset2]")).isTrue()
        assertThat(repo.contains(datasetName = "[datasetX]")).isFalse()
    }

    @Test
    fun getDataset() {

        // Arrange
        val repo = DatasetRepositoryManager.getRepository(repositoryName = "data")

        run {
            // Act
            val ds = repo.getDataset(datasetName = "[dataset1]")
            // Assert
            assertThat(ds.datasetName).isEqualTo("[dataset1]")
            assertThat(ds.nameValues["attribute1-1"]).isEqualTo("value1-1")
            assertThat(ds.nameValues["attribute1-2"]).isEqualTo("value1-2")
        }

        run {
            // Act
            val ds = repo.getDataset(datasetName = "[dataset2]")
            // Assert
            assertThat(ds.datasetName).isEqualTo("[dataset2]")
            assertThat(ds.nameValues["attribute2-1"]).isEqualTo("value2-1")
            assertThat(ds.nameValues["attribute2-2"]).isEqualTo("value2-2")
        }

        run {
            // Arrange
            repo.jsonObject.getJSONObject("[dataset1]").append("age", 1)
            // Act, Assert
            assertThatThrownBy {
                repo.getDataset("[dataset1]")
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("Set value as string.(key=age, value=[1])")
        }
    }

    @Test
    fun getValue() {

        // Arrange
        val repo = DatasetRepositoryManager.getRepository(repositoryName = "data")
        // Act, Assert
        assertThat(repo.getValue("[dataset1].attribute1-1")).isEqualTo("value1-1")
        assertThat(repo.getValue("[dataset1].attribute1-2")).isEqualTo("value1-2")
        assertThat(repo.getValue("[dataset2].attribute2-1")).isEqualTo("value2-1")
        assertThat(repo.getValue("[dataset2].attribute2-2")).isEqualTo("value2-2")
    }

}