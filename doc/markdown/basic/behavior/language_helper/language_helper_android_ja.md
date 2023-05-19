# LanguageHelperAndroid

これらの関数を使用してAndroidデバイスの言語設定を行うことができます。

### 制限事項

Android 9以降がサポートされます。これらの関数は画面に依存するため、デバイスの製造者が言語設定画面をカスタマイズしている場合は正常に動作しない場合があります。

## 関数

| 関数                 | 説明                                 |
|:-------------------|:-----------------------------------|
| setLanguage        | 言語を設定します。必要に応じて以下の関数を実行します         |
| getLanguage        | 言語設定画面へ移動し、現在設定されている言語を戻り値として返却します |
| addLanguage        | 言語の設定を追加します                        |
| removeLanguage     | 言語の設定を解除します                        |
| gotoLocaleSettings | 言語設定画面へ移動します                       |

## Example

### SetLanguageOnAndroid1.kt

(`kotlin/tutorial/basic/SetLanguageOnAndroid1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.befavior.LanguageHelperAndroid
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SetLanguageOnAndroid1 : UITest() {

    @Test
    @Order(10)
    fun setLanguage_getLanguage_removeLanguage1() {

        scenario {
            case(1) {
                action {
                    LanguageHelperAndroid.setLanguage(language = "日本語", region = "日本")
                }
            }
            case(2) {
                action {
                    LanguageHelperAndroid.setLanguage(language = "English", region = "United States")
                }
            }
        }
    }

}
```

### Link

- [index](../../../index_ja.md)
