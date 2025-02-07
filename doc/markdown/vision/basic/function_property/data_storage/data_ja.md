# data関数 (Vision)

**data**関数を使用すると**data.json**ファイルに設定したアカウント情報のプロパティを取得することができます。

### サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### data.json

`data.json`ファイルを作成し、データアイテムを記述します。プロパティ名（product_name, product_code, unit_priceなど）は任意です。

```
{
  "[製品1]": {
    "製品名": "スーパーハイテンション",
    "製品コード": "P001",
    "単価": "100"
  },

  "[製品2]": {
    "製品名": "ウルトラハイ",
    "製品コード": "P002",
    "単価": "200"
  }
}
```

### testConfig.json

使用する`data.json`ファイルのパスを`testConfig.json`ファイルの**dataset**セクションの"**data**"で指定します。

```
{
  "testConfigName": "testConfig@a",

  "dataset": {
    "data": "testConfig/android/androidSettings/dataset/data.json"
  },

...
```

### Data1.kt

```kotlin
    @Test
    @Order(10)
    fun data1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定検索画面]")
                        .tap("設定を検索")
                }.action {
                    it.sendKeys(data("[製品1].製品名"))
                }.expectation {
                    it.textIs("スーパーハイテンション")
                }
            }
        }

    }
```

### 参照

[独自のデータ関数を作成する](../../../../common/advanced/creating_you_own_data_function_ja.md)

### Link

- [index](../../../../index_ja.md)
