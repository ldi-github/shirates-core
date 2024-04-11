# 任意の内容の検証(verify関数)

verify関数を使用して任意の内容の検証を行うことができます。

### 任意の検証ロジックの実装

```kotlin
it.verify("The packageName is \"com.android.settings\"") {
    if (rootElement.packageName == "com.android.settings") {
        OK()
    } else {
        NG()
    }
}
```

verify関数内で任意の検証ロジックを実装します。
検証結果はOK関数またはNG関数を呼び出すことでverify関数に通知します。

### 既存の検証関数の組み合わせ

```kotlin
it.verify("The app is Settings and the screen is [Android Settings Top Screen]") {
    it.appIs("Settings")
    it.screenIs("[Android Settings Top Screen]")
}
```

verify関数内で既存の検証関数を使用する場合はOK関数、NG関数の呼び出しは不要です。

### 出力例

```
141	[00:00:18]	2024/04/12 02:46:17.832	{ok1-1}	0	-	[EXPECTATION]	+196	C	()	expectation
142	[00:00:18]	2024/04/12 02:46:17.835	{ok1-1}	0	-	[OK]	+3	C	(verify)	The packageName is "com.android.settings"
143	[00:00:18]	2024/04/12 02:46:17.840	{ok1-1}	0	-	[OK]	+5	C	(verify)	The app is Settings and the screen is [Android Settings Top Screen]
```

## 例

### AssertingAnything1.kt

(`kotlin/tutorial/basic/AssertingAnything1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElementCache.rootElement
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.verify
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AssertingAnything1 : UITest() {

    @Test
    @Order(10)
    fun ok() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The packageName is \"com.android.settings\"") {
                        if (rootElement.packageName == "com.android.settings") {
                            OK()
                        } else {
                            NG()
                        }
                    }
                    it.verify("The app is Settings and the screen is [Android Settings Top Screen]") {
                        it.appIs("Settings")
                        it.screenIs("[Android Settings Top Screen]")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun ng() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The app is Settings and the screen is [Android Settings Top Screen]") {
                        it.appIs("Settings2")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun notImplemented() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The app is Settings and the screen is [Android Settings Top Screen]") {
                    }
                }
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

