# OsaifuKeitai function (Vision)

You can use special branch functions for [Osaifu-Keitai](https://en.wikipedia.org/wiki/Osaifu-Keitai).

## functions

| function        | description                                                  |
|:----------------|:-------------------------------------------------------------|
| osaifuKeitai    | This function is executed on Osaifu-Keitai is available.     |
| osaifuKeitaiNot | This function is executed on Osaifu-Keitai is not available. |

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

- [index](../../../../index.md)
