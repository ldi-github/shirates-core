# app

**app**関数を使用すると**app.json**ファイルに設定したアプリ情報のプロパティを取得することができます。

## 例

### apps.json

`apps.json`ファイルを作成し、データアイテムを記述します。プロパティ名（packageOrBundleIdなど）は任意です。

```
{
  "[Settings]": {
    "packageOrBundleId": "com.android.settings"
  },
  "[Calculator]": {
    "packageOrBundleId": "com.google.android.calculator"
  },
  "[Chrome]": {
    "packageOrBundleId": "com.android.chrome"
  }
}
```

### androidSettingsConfig.json (testConfig.json)

使用する`apps.json`ファイルのパスを`testConfig.json`ファイルの**dataset**セクションの"**apps**"で指定します。

```
{
  "testConfigName": "androidSettingsConfig",

  "dataset": {
    "apps": "testConfig/android/androidSettings/dataset/apps.json"
  },

...
```

### App1.kt

(`kotlin/tutorial/basic/App1.kt`)

app関数を使用します。

```kotlin
@Test
@Order(10)
fun app() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
                    .tap("Search settings")
                    .screenIs("[Android Settings Search Screen]")
                    .tap("[Search Box]")
            }.action {
                it.sendKeys(app("[Settings].packageOrBundleId"))
            }.expectation {
                it.textIs(app("[Settings].packageOrBundleId"))
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

