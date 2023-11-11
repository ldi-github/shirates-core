# おサイフケータイ関数

[おサイフケータイ](https://en.wikipedia.org/wiki/Osaifu-Keitai)に関してこれらの分岐関数を使用することができます。

## 関数

| 関数              | 説明                                 |
|:----------------|:-----------------------------------|
| osaifuKeitai    | おサイフケータイが利用可能な場合にコードブロックが実行されます    |
| osaifuKeitaiNot | おサイフケータイが利用可能ではない場合にコードブロックが実行されます |

## Example

### OsaifuKeitai1.kt

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.osaifuKeitai
import shirates.core.driver.branchextension.osaifuKeitaiNot
import shirates.core.driver.commandextension.describe
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class OsaifuKeitai1 : UITest() {

    @Test
    @Order(10)
    fun osaifuKeitai1() {

        scenario {
            case(1) {
                action {
                    osaifuKeitai {
                        describe("Osaifu-Keitai is available")
                    }
                    osaifuKeitaiNot {
                        describe("Osaifu-Keitai is not available")
                    }
                }
            }
        }
    }

}
```

### Link

- [index](../../../index_ja.md)
