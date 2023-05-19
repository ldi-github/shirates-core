# 独自のデータ関数を作成する

`DatasetRepositoryManager`を使用すると独自のデータ関数を作成することができます。

## 例

customerに関するデータ関数を作成したい場合

1. `dataset`ディレクトリの下に`customers.json`という名前のデータセットjsonファイルを作成します。

![](_images/creating_data_function_1.png)

```json
{
  "[customer1]": {
    "name": "customer1",
    "email": "customer1@example.com"
  },
  "[customer2]": {
    "name": "customer2",
    "email": "customer2@example.com"
  }
}
```

2. `testConfig.json`に`dataset`セクションを追加し、データセット名`customer`をデータセットjsonファイルに紐付けます。

**androidSettingsConfig.json**

```
  "dataset": {
    "customers": "testConfig/android/androidSettings/dataset/customers.json"
  },
```

3. データ関数用のクラスファイルを作成します。

![](_images/creating_data_function_2.png)

4. 以下のようにCustomerオブジェクトを実装します。

```kotlin
package exercise

import shirates.core.configuration.repository.DatasetRepository
import shirates.core.configuration.repository.DatasetRepositoryManager

/**
 * Customer
 */
object Customer {

    /**
     * repository
     */
    var repository: DatasetRepository? = null

    /**
     * getValue
     *
     * key format: [datasetName].attributeName
     */
    fun getValue(key: String): String {

        if (repository == null) {
            repository = DatasetRepositoryManager.getRepository("customers")
        }

        val value = repository!!.getValue(longKey = key)
        return value
    }

}

/**
 * customer
 */
fun customer(key: String): String {

    return Customer.getValue(key = key)
}
```

customerデータ関数を以下のように使用することができます。

### CustomerTest.kt

```kotlin
package exercise

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.output
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class CustomerTest : UITest() {

    @Test
    fun test() {

        scenario {
            case(1) {
                action {
                    output(customer("[customer1].name"))
                    output(customer("[customer1].email"))
                }
            }
        }
    }
}
```

#### コンソール

```
96	2022/09/28 21:16:24.736	{test}	[SCENARIO]	(scenario)	test()
97	2022/09/28 21:16:24.736	{test-1}	[CASE]	(case)	(1)
98	2022/09/28 21:16:24.736	{test-1}	[ACTION]	(action)	action
99	2022/09/28 21:16:24.737	{test-1}	[output]	(output)	customer1
100	2022/09/28 21:16:24.738	{test-1}	[output]	(output)	customer1@example.com
```

### Link

- [index](../index_ja.md)
