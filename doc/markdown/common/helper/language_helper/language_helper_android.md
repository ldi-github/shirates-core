# LanguageHelperAndroid (Vision)

You can set device language using these functions.<br>
If `setLanguageAndLocale` function of `LanguageHelper` does not work well, this may be an alternative.

## Functions

| function             | description                                                       |
|:---------------------|:------------------------------------------------------------------|
| setLanguageAndRegion | Set language and region by manipulating the Android Settings app. |

## Sample code

[Getting samples](../../../vision/getting_samples.md)

### SetLanguageOnAndroid1.kt

(`src/test/kotlin/tutorial/basic/SetLanguageOnAndroid2.kt`)

```kotlin
    @Test
    fun setLanguageAndRegion() {

        scenario {
            case(1) {
                action {
                    LanguageHelperAndroid.setLanguageAndRegion(languageAndRegion = "日本語(日本)")
                }.expectation {
                    it.setOCRLanguage("ja")
                        .exist("言語")
                }
            }
            case(2) {
                action {
                    LanguageHelperAndroid.setLanguageAndRegion(languageAndRegion = "English(United States)")
                }.expectation {
                    it.setOCRLanguage("en")
                        .exist("Languages")
                }
            }
        }
    }
```

### Note

This function manipulates Settings app of Android. This may fail in the future version of Android because of
modification of Settings app.

### Link

- [index](../../../index.md)
