# launchApp, terminateApp (Classic)

これらの関数を使用してアプリを起動/終了できます。

## 関数

| 関数           | 設定                                      |
|:-------------|:----------------------------------------|
| launchApp    | アプリを起動します。アプリ名、packageOrBundleIdを指定できます |
| terminateApp | アプリを終了します。アプリ名、packageOrBundleIdを指定できます |

## 例

### LaunchApp1.kt

(`kotlin/tutorial/basic/LaunchApp1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.terminateApp
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class LaunchApp1 : UITest() {

    @Test
    fun launchApp() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()   // Refers packageOrBundleId in testConfig.json
                }.action {
                    it.launchApp()  // Refers packageOrBundleId in testConfig.json
                }.expectation {
                    it.appIs("[Settings]")  // App Nickname in app.json
                    it.appIs("Settings")    // App Nickname in app.json
                    it.appIs("com.android.settings")    // package
                }
            }
            case(2) {
                condition {
                    it.terminateApp("[Chrome]")     // App Nickname in app.json
                }.action {
                    it.launchApp("[Chrome]")
                }.expectation {
                    it.appIs("[Chrome]")
                }
            }
            case(3) {
                condition {
                    it.terminateApp("com.android.chrome")   // package
                }.action {
                    it.launchApp("com.android.chrome")
                }.expectation {
                    it.appIs("com.android.chrome")
                }
            }
            case(4) {
                condition {
                    it.terminateApp("Chrome")   // App Nickname in app.json
                }.action {
                    it.launchApp("Chrome")
                }.expectation {
                    it.appIs("Chrome")
                }
            }
            case(5) {
                condition {
                    it.terminateApp("[Play Store]")     // App Nickname in app.json
                }.action {
                    it.launchApp("[Play Store]")
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
