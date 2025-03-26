# アプリが表示されていることの検証

これらの関数を使用してアプリが表示されていることを検証することができます。

## 関数

| 関数    |
|:------|
| appIs |
| isApp |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### AssertingOthers1.kt

(`src/test/kotlin/tutorial/basic/AssertingOthers1.kt`)

```kotlin
    @Test
    @Order(10)
    fun appIs_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.appIs("[設定]")
                }
            }
            case(2) {
                condition {
                    it.launchApp("[Chrome]")
                }.expectation {
                    val isApp = it.isApp("[Chrome]")
                    output("isApp(\"[Chrome]\")=$isApp")
                    it.appIs("[Chrome]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun appIs_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.appIs("[Chrome]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
