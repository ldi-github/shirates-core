# data関数 (Vision)

**data**関数を使用すると**data.json**ファイルに設定したアカウント情報のプロパティを取得することができます。

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

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

Use data function.

```kotlin
    @Test
    @Order(10)
    fun data1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                        .tap("Search settings")
                }.action {
                    it.sendKeys(data("[product1].product_name"))
                }.expectation {
                    it.textIs("Super High Tension")
                }
            }
        }

    }
```

### 参照

[独自のデータ関数を作成する](../../../../common/advanced/creating_you_own_data_function_ja.md)

### Link

- [index](../../../../index_ja.md)
