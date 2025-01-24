# data関数

**data**関数を使用すると**data.json**ファイルに設定したアカウント情報のプロパティを取得することができます。

## 例

### data.json

`data.json`ファイルを作成し、データアイテムを記述します。プロパティ名（product_name, product_code, unit_priceなど）は任意です。

```
{
  "[product1]": {
    "product_name": "Super Liquid",
    "product_code": "P001",
    "unit_price": "100"
  },

  "[product2]": {
    "product_name": "Ultra High",
    "product_code": "P002",
    "unit_price": "200"
  }
}
```

### androidSettingsConfig.json (testConfig.json)

使用する`data.json`ファイルのパスを`testConfig.json`ファイルの**dataset**セクションの"**data**"で指定します。

```
{
  "testConfigName": "androidSettingsConfig",

  "dataset": {
    "data": "testConfig/android/androidSettings/dataset/data.json"
  },

...
```

### Data1.kt

data関数を使用します。

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.sendKeys
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.textIs
import shirates.core.storage.data
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Data1 : UITest() {

    @Test
    @Order(10)
    fun data1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                        .tap("[Search Box]")
                }.action {
                    it.sendKeys(data("[product1].product_name"))
                }.expectation {
                    it.textIs("Super High Tension")
                }
            }
        }

    }
}
```

### 参照

[独自のデータ関数を作成する](../../../advanced/creating_you_own_data_function_ja.md)

### Link

- [index](../../../index_ja.md)
