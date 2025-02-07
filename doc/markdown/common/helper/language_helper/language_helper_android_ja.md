# LanguageHelperAndroid (Vision)

これらの関数を使用してデバイスの言語を設定することができます。<br>
`LanguageHelper`の`setLanguageAndLocale`関数がうまく機能しない場合はこちらで代替できる場合があります。

## 関数

| 関数                   | 説明                             |
|:---------------------|:-------------------------------|
| setLanguageAndRegion | Androidの設定アプリを操作して言語と地域を設定します。 |

## サンプルコード

[サンプルの入手](../../../vision/getting_samples_ja.md)

### SetLanguageOnAndroid1.kt

(`src/test/kotlin/tutorial/basic/SetLanguageOnAndroid2.kt`)

```kotlin
    @Test
    fun setLanguageAndRegion() {

        scenario {
            case(1) {
                action {
                    LanguageHelperAndroid.setLanguageAndRegion(languageAndRegion = "English(United States)")
                }.expectation {
                    it.setOCRLanguage("ja")
                        .exist("Languages")
                }
            }
            case(2) {
                action {
                    LanguageHelperAndroid.setLanguageAndRegion(languageAndRegion = "日本語(日本)")
                }.expectation {
                    it.setOCRLanguage("ja")
                        .exist("言語")
                }
            }
        }
    }
```

### 注意

この関数はAndroidの設定アプリを操作します。今後のバージョンのAndroidで設定アプリが変更された場合は動作しない可能性があります。

### Link

- [index](../../../index_ja.md)
