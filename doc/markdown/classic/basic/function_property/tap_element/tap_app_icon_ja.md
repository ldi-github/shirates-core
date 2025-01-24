# tapAppIcon (Classic)

**tapAppIcon** 関数を使用するとアプリアイコンをタップしてアプリを起動できます。

## 関数

| 関数         | 説明                                   |
|:-----------|:-------------------------------------|
| tapAppIcon | ホーム画面またはアプリのランチャーメニューのアプリアイコンをタップします |

## 例

### TapAppIcon1.kt

(`kotlin/tutorial/basic/TapAppIcon1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TapAppIcon1 : UITest() {

    @Test
    fun tapAppIcon() {

        scenario {
            case(1) {
                action {
                    it.launchApp("Chrome")
                }.expectation {
                    it.appIs("[Chrome]")
                }
            }
            case(2) {
                action {
                    it.launchApp("Play Store")
                }.expectation {
                    it.appIs("[Play Store]")
                }
            }
        }
    }

}
```

### Link

- [index](../../../index_ja.md)
