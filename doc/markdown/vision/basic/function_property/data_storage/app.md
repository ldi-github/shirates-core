# app (Vision)

You can configure apps data in **apps.json** file, and you can get properties of apps with **app**
function.

## Sample code

[Getting samples](../../getting_samples.md)

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

Set the path of `apps.json` to "**apps**" in **dataset** section of the testConfig.json file.

```
{
  "testConfigName": "testConfig",

  "dataset": {
    "apps": "path/to/apps.json"
  },

...
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
                    it.launchApp("[Settings]")
                        .tap("Search settings")
                        .screenIs("[Android Settings Search Screen]")
                        .tap("Search settings")
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

- [index](../../../../index.md)

