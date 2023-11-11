# account

**account**関数を使用すると**accounts.json**ファイルに設定したアカウント情報のプロパティを取得することができます。

## 例

### accounts.json

`accounts.json`ファイルを作成し、データアイテムを記述します。プロパティ名（id, passwordなど）は任意です。

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

使用する`accounts.json`ファイルのパスを`testConfig.json`ファイルの**dataset**セクションの"**accounts**"で指定します。

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

account関数を使用します。

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

- [index](../../../index_ja.md)

