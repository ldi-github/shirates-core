# account (Vision)

You can configure account data in **accounts.json** file, and you can get properties of accounts with **account**
function.

## Sample code

[Getting samples](../../getting_samples.md)

### accounts.json

Create `accounts.json` file, and describe data items. Property names(id, password, etc) are arbitrary.

```
{
  "[account1]": {
    "id": "account1@example.com",
    "password": "p@ssword"
  },

  "[account2]": {
    "id": "account2@example.com",
    "password": "p@ssword"
  }
}
```

### androidSettingsConfig.json (testConfig.json)

Set the path of accounts.json to "**accounts**" in **dataset** section of the `testConfig.json` file.

```
{
  "testConfigName": "androidSettingsConfig",

  "dataset": {
    "accounts": "testConfig/android/androidSettings/dataset/accounts.json"
  },

...
```

### Account1.kt

(`kotlin/tutorial/basic/Account1.kt`)

Use account function.

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.storage.account
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Account1 : UITest() {

    @Test
    @Order(10)
    fun account() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .tap("Search settings")
                        .screenIs("[Android Settings Search Screen]")
                        .tap("[Search Box]")
                }.action {
                    it.sendKeys(account("[account1].id"))
                }.expectation {
                    it.textIs(account("[account1].id"))
                }
            }
        }

    }
}
```

### Link

- [index](../../../../index.md)

