package shirates.core.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.json.JSONObject
import org.junit.jupiter.api.Test
import shirates.core.configuration.repository.DatasetRepository
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.exception.TestConfigException
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.JsonUtility

class DatasetRepositoryManagerTest : UnitTest() {

    @Test
    fun clear() {

        // Arrange
        DatasetRepositoryManager.clear()
        DatasetRepositoryManager.loadFromFile(file = "unitTestData/testConfig/androidSettings/dataset/data.json")
        assertThat(DatasetRepositoryManager.repositories.count() > 0).isTrue()
        // Act
        DatasetRepositoryManager.clear()
        // Assert
        assertThat(DatasetRepositoryManager.repositories.count()).isEqualTo(0)
    }

    @Test
    fun hasRepository() {

        // Act
        DatasetRepositoryManager.clear()
        DatasetRepositoryManager.loadFromFile(file = "unitTestData/testConfig/androidSettings/dataset/data.json")
        // Assert
        assertThat(DatasetRepositoryManager.hasRepository("data")).isTrue()
        assertThat(DatasetRepositoryManager.hasRepository("no-data")).isFalse()
    }

    @Test
    fun getRepository() {

        // Act
        DatasetRepositoryManager.clear()
        DatasetRepositoryManager.loadFromFile(file = "unitTestData/testConfig/androidSettings/dataset/data.json")
        val repo = DatasetRepositoryManager.getRepository("data")
        // Assert
        assertThat(repo.repositoryName).isEqualTo("data")


        // Act, Assert
        assertThatThrownBy {
            DatasetRepositoryManager.getRepository(repositoryName = "no exist")
        }.isInstanceOf(IllegalAccessError::class.java)
            .hasMessage("repository not found. (repositoryName=no exist)")
    }

    @Test
    fun setRepository1() {

        // Arrange
        val jso = JSONObject()
        DatasetRepositoryManager.clear()
        assertThat(DatasetRepositoryManager.repositories.count()).isEqualTo(0)
        // Act
        DatasetRepositoryManager.setRepository(repositoryName = "repo1", jsonObject = jso)
        // Assert
        assertThat(DatasetRepositoryManager.repositories.containsKey("repo1")).isTrue()
    }

    @Test
    fun setRepository2() {

        // Arrange
        val jso = JSONObject()
        DatasetRepositoryManager.clear()
        assertThat(DatasetRepositoryManager.repositories.count()).isEqualTo(0)
        // Act
        val repo = DatasetRepository(repositoryName = "repo2", jsonObject = jso)
        DatasetRepositoryManager.setRepository(repository = repo)
        // Assert
        assertThat(DatasetRepositoryManager.repositories.containsKey("repo2")).isTrue()
    }

    @Test
    fun getValue() {

        // Arrange
        val jso = JsonUtility.getJSONObjectFromFile("unitTestConfig/android/androidSettings/dataset/data.json")
        DatasetRepositoryManager.clear()
        val repositoryName = "data"
        DatasetRepositoryManager.setRepository(repositoryName = repositoryName, jsonObject = jso)
        val invalidLongKey = "invalidLongKey"
        // Act, Assert
        assertThatThrownBy {
            DatasetRepositoryManager.getValue("repository1", invalidLongKey)
        }.isInstanceOf(IllegalAccessError::class.java)
            .hasMessage("Repository not found. (repositoryName=repository1)")

        run {
            // Arrange
            assertThat(DatasetRepositoryManager.repositories.count()).isEqualTo(1)
            // Act, Assert
            assertThatThrownBy {
                DatasetRepositoryManager.getValue(repositoryName, invalidLongKey)
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("invalid format (longKey=$invalidLongKey). sectionName.attributeName is required.")
        }

        run {
            // Act, Assert
            assertThat(DatasetRepositoryManager.getValue(repositoryName, longKey = "[dataset1].key1-1"))
                .isEqualTo("value1-1")
            assertThat(DatasetRepositoryManager.getValue(repositoryName, longKey = "[dataset1].key1-2"))
                .isEqualTo("value1-2")
            assertThat(DatasetRepositoryManager.getValue(repositoryName, longKey = "[dataset2].key2-1"))
                .isEqualTo("value2-1")
            assertThat(DatasetRepositoryManager.getValue(repositoryName, longKey = "[dataset2].key2-2"))
                .isEqualTo("value2-2")
        }

    }
}