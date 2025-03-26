# app (Vision)

**app**関数を使用すると**app.json**ファイルに設定したアプリ情報のプロパティを取得することができます。

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### apps.json

`apps.json`ファイルを作成し、データアイテムを記述します。プロパティ名（packageOrBundleIdなど）は任意です。

```
{
  "[設定]": {
    "packageOrBundleId": "com.android.settings",
    "appIconName": "設定"
  },
  "[電卓]": {
    "packageOrBundleId": "com.google.android.calculator",
    "appIconName": "Calculator"
  },
  "[Chrome]": {
    "packageOrBundleId": "com.android.chrome",
    "appIconName": "Chrome"
  },
}
```

### testConfig.json

使用する`apps.json`ファイルのパスを`testConfig.json`ファイルの**dataset**セクションの"**apps**"で指定します。

```
{
  "testConfigName": "testConfig@a",

...

  "dataset": {
    "apps": "testConfig/android/dataset/apps.json",
  },

...
}
```

### App1.kt

(`src/test/kotlin/tutorial/basic/App1.kt`)

Use app function.

```kotlin
    @Test
    @Order(10)
    fun app() {

        scenario {
            case(1) {
                condition {
                    it.launchApp("[設定]")
                        .tap("設定を検索")
                        .screenIs("[Android設定検索画面]")
                        .tap("設定を検索")
                }.action {
                    it.sendKeys(app("[設定].packageOrBundleId"))
                }.expectation {
                    it.textIs(app("[設定].packageOrBundleId"))
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)

