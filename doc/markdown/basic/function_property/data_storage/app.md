# app

You can configure apps data in **apps.json** file, and you can get properties of apps with **app**
function.

## Example

### apps.json

Create apps.json file, and describe data items. Property names(packageOrBundleId) are arbitrary.

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

Set the path of accounts.json to "**apps**" in **dataset** section of the testConfig.json file.

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

Use app function.

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

- [index](../../../index.md)

