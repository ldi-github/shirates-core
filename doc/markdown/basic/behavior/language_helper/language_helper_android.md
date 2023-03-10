# LanguageHelperAndroid

You can set android device language using these functions.

### Limitation

Android 9 or later are supported. These functions are depends on view, and may not work well on language settings screen
customized by device manufacturer.

## Functions

| function           | description                                                        |
|:-------------------|:-------------------------------------------------------------------|
| setLanguage        | Set language. This function calls other functions below if needed. |
| getLanguage        | Go to Languages screen and returns current language.               |
| addLanguage        | Add language.                                                      |
| removeLanguage     | Remove language.                                                   |
| gotoLocaleSettings | Go to Languages screen.                                            |

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

- [index](../../../index.md)
