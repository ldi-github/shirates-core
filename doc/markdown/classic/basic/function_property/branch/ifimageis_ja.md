# 分岐関数 (ifImageIs, ifImageIsNot) (Classic)

画像に対してこれらの分岐関数を使用することができます。

## 関数

| 関数           | 説明                                   |
|:-------------|:-------------------------------------|
| ifImageIs    | 指定した画像に要素の画像がマッチする場合にコードブロックが実行されます  |
| ifImageIsNot | 指定した画像に要素の画像がマッチしない場合にコードブロックが実行されます |

### IfImageIs1.kt

(`kotlin/tutorial/basic/IfImageIs1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifImageIs
import shirates.core.driver.branchextension.ifImageIsNot
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.select
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/androidSettings/testrun.properties")
class IfImageIs1 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        ImageSetupHelper.setupImageAndroidSettingsTopScreen()
    }

    @Test
    @Order(10)
    fun ifImageIsTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.select("[Network & internet Icon]")
                        .ifImageIs("[Network & internet Icon].png") {
                            OK("ifImageIs called")
                        }.ifElse {
                            NG()
                        }
                    it.select("[Network & internet Icon]")
                        .ifImageIsNot("[Network & internet Icon].png") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                }
            }
            case(2) {
                expectation {
                    it.select("[Network & internet Icon]")
                        .ifImageIs("[App Icon].png") {
                            NG()
                        }.ifElse {
                            OK("ifElse called")
                        }
                    it.select("[Network & internet Icon]")
                        .ifImageIsNot("[App Icon].png") {
                            OK("ifImageIsNot called")
                        }.ifElse {
                            NG()
                        }
                }
            }
        }
    }

}
```

### Link

- [index](../../../index_ja.md)

