# data function

You can configure any data in **data.json** file, and you can get properties of data with **data** function.

## Example

### data.json

Create `data.json` file, and describe data items. Property names(product_name, product_code, unit_price, etc) are
arbitrary.

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

Set the path of `data.json` to "**data**" in **dataset** section of the testConfig.json file.

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
                    .tap("[Search Box]")
            }.action {
                it.sendKeys(data("[product1].product_name"))
            }.expectation {
                it.textIs("Super High Tension")
            }
        }
    }

}
```

### See also

[Creating your own data function](../../../advanced/creating_you_own_data_function.md)

### Link

- [index](../../../index.md)
