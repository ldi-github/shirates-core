# account (Vision)

**account**関数を使用すると**accounts.json**ファイルに設定したアカウント情報のプロパティを取得することができます。

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

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

(`src/test/kotlin/tutorial/basic/Account1.kt`)

Use account function.

```kotlin
    @Test
    @Order(10)
    fun account() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                        .tap("設定を検索")
                        .screenIs("[Android設定検索画面]")
                }.action {
                    it.sendKeys(account("[account1].id"))
                }.expectation {
                    it.textIs(account("[account1].id"))
                }
            }
        }

    }
```

### Link

- [index](../../../../index_ja.md)

