package shirates.core.unittest.storage

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.TestConfig
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.storage.Account
import shirates.core.storage.account
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath

class AccountTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        val configPath = "unitTestData/testConfig/androidSettings/testConfigTestData1.json".toPath()
        TestConfig(configPath.toString())
    }

    @Test
    fun value() {

        // Act, Assert
        assertThat(Account.getValue(longKey = "[account1].id")).isEqualTo("account1@example.com")
        assertThat(Account.getValue(longKey = "[account1].password")).isEqualTo("p@ssword")
    }

    @Test
    fun account_longKey() {

        // Act, Assert
        assertThat(account(longKey = "[account1].id")).isEqualTo("account1@example.com")
        assertThat(account("[account1].password")).isEqualTo("p@ssword")
    }

    @Test
    fun account_datasetName_attributeName() {

        // Act, Assert
        assertThat(account(datasetName = "[account1]", attributeName = "id")).isEqualTo("account1@example.com")
        assertThat(account("[account1]", "id")).isEqualTo("account1@example.com")
    }

    @Test
    fun exceptions() {

        // Act, Assert
        assertThatThrownBy {
            Account.getValue(longKey = "[no exist].key1")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "datasetNotFoundInRepository", repository = "accounts", dataset = "[no exist]"))

        // Act, Assert
        assertThatThrownBy {
            Account.getValue(longKey = "[account1].noExistKey")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(
                message(
                    id = "attributeNotFoundInDataset",
                    repository = "accounts",
                    dataset = "[account1]",
                    attribute = "noExistKey"
                )
            )
    }

}
