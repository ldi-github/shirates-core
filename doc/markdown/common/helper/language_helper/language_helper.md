# LanguageHelper (Vision)

You can set device language using these functions.

## Functions

| function             | description                                                |
|:---------------------|:-----------------------------------------------------------|
| setLanguageAndLocale | Set language and locale. Appium session will be restarted. |

## Sample code

[Getting samples](../../../vision/getting_samples.md)

### SetLanguageOnAndroid1.kt

(`src/test/kotlin/tutorial/basic/SetLanguageOnAndroid1.kt`)

```kotlin
    @Test
    fun setLanguageAndLocale() {

        scenario {
            case(1) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "ja", locale = "JP")
                }.expectation {
                    it.exist("設定", waitSeconds = 15.0)
                }
            }
            case(2) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "en", locale = "US")
                }.expectation {
                    it.exist("Settings", waitSeconds = 15.0)
                }
            }
        }
    }
```

### SetLanguageOnIos1.kt

(`src/test/kotlin/tutorial/basic/SetLanguageOnIos1.kt`)

```kotlin
    @Test
    fun setLanguageAndLocale() {

        scenario {
            case(1) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "ja", locale = "JP")
                }.expectation {
                    it.exist("設定", waitSeconds = 10.0)
                }
            }
            case(2) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "en", locale = "US")
                }.expectation {
                    it.exist("Settings", waitSeconds = 10.0)
                }
            }
        }
    }
```

### language and locale

Refer to the URL below for details on language and locale<br>

- [appium-uiautomator2-driver](https://github.com/appium/appium-uiautomator2-driver)<br>
- [XCUITest Driver](https://appium.github.io/appium-xcuitest-driver/4.16/capabilities/)

## Troubleshooting

The following error may occur on Android.

```
Cannot set the device locale to 'ja_JP'. You may want to apply one of the following locales instead: ja_JP 
```

Check [Cannot set the device locale to '(locale)'](../../troubleshooting/performance_and_stability_android/cannot_set_the_device_locale_to.md)

### Link

- [index](../../../index.md)
