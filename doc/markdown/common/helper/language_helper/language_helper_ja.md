# 言語ヘルパー (Vision)

これらの関数を使用してデバイスの言語を設定することができます。

## 関数

| 関数                   | 説明                                  |
|:---------------------|:------------------------------------|
| setLanguageAndLocale | 言語とロケールを設定します。Appiumのセッションが再起動されます。 |

## サンプルコード

[サンプルの入手](../../../vision/getting_samples_ja.md)

### SetLanguageOnAndroid1.kt

(`src/test/kotlin/tutorial/basic/SetLanguageOnAndroid1.kt`)

```kotlin
    @Test
    @Order(10)
    fun setLanguage_getLanguage_removeLanguage1() {

        scenario {
            case(1) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "ja", locale = "JP")
                }.expectation {
                    it.exist("設定")
                }
            }
            case(2) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "en", locale = "US")
                }.expectation {
                    it.exist("Settings")
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
                    it.launchApp("com.apple.Preferences")
                        .wait()
                }.expectation {
                    it.screenIs("[iOS Settings Top Screen]")
                        .exist("設定")
                }
            }
            case(2) {
                action {
                    LanguageHelper.setLanguageAndLocale(language = "en", locale = "US")
                    it.launchApp("com.apple.Preferences")
                        .wait()
                }.expectation {
                    it.screenIs("[iOS Settings Top Screen]")
                        .exist("Settings")
                }
            }
        }
    }
```

### language と locale

language と locale の詳細については以下のURLを参照してください。<br>

- [appium-uiautomator2-driver](https://github.com/appium/appium-uiautomator2-driver)<br>
- [XCUITest Driver](https://appium.github.io/appium-xcuitest-driver/4.16/capabilities/)

### Link

- [index](../../../index_ja.md)
