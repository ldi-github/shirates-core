# LanguageHelper (Vision)

You can set android device language using these functions.

## Functions

| function             | description              |
|:---------------------|:-------------------------|
| setLanguageAndLocale | Set language and locale. |

## Sample code

[Getting samples](../../getting_samples.md)

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

### Link

- [index](../../../../index.md)
