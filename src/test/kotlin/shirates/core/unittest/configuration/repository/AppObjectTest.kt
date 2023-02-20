package shirates.core.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.AppNameUtility

class AppObjectTest : UnitTest() {

    @Test
    fun getAppNameFromPackageName() {

        // Arrange
        DatasetRepositoryManager.clear()
        DatasetRepositoryManager.loadFromFile("unitTestData/testConfig/androidSettings/dataset/apps.json")
        // Act, Assert
        assertThat(AppNameUtility.getAppNickNameFromPackageName("com.android.settings")).isEqualTo("[Settings]")
        assertThat(AppNameUtility.getAppNickNameFromPackageName("com.google.android.calculator")).isEqualTo("[Calculator]")
        assertThat(AppNameUtility.getAppNickNameFromPackageName("no.exist.package")).isEqualTo("")
    }

}