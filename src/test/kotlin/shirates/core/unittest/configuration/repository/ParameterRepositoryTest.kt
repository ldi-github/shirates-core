package shirates.core.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.repository.ParameterRepository
import shirates.core.testcode.UnitTest

class ParameterRepositoryTest : UnitTest() {

    @Test
    fun clear() {

        // Act
        ParameterRepository.clear()
        // Assert
        assertThat(ParameterRepository.parameters.count()).isEqualTo(0)

        // Arrange
        ParameterRepository.write(name = "name1", value = "value1")
        assertThat(ParameterRepository.parameters.count()).isEqualTo(1)
        // Act
        ParameterRepository.clear()
        // Assert
        assertThat(ParameterRepository.parameters.count()).isEqualTo(0)
    }

    @Test
    fun write() {

        // Arrange
        ParameterRepository.clear()
        assertThat(ParameterRepository.parameters.count()).isEqualTo(0)
        // Act
        ParameterRepository.write("name1", "value1")
        assertThat(ParameterRepository.parameters.count()).isEqualTo(1)
        assertThat(ParameterRepository.parameters["name1"]).isEqualTo("value1")

        // Act
        ParameterRepository.write("name2", "value2")
        assertThat(ParameterRepository.parameters.count()).isEqualTo(2)
        assertThat(ParameterRepository.parameters["name1"]).isEqualTo("value1")
        assertThat(ParameterRepository.parameters["name2"]).isEqualTo("value2")
    }
}