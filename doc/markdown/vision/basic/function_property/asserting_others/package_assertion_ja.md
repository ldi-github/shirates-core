# 表示中のアプリのパッケージの検証

これらの関数を使用して現在表示されているアプリのパッケージを検証することができます。

## 関数

| 関数        |
|:----------|
| packageIs |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### AssertingOthers1.kt

(`src/test/kotlin/tutorial/basic/AssertingOthers1.kt`)

```kotlin
    @Test
    @Order(50)
    fun packageIs_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.packageIs("com.android.settings")
                }
            }
            case(2) {
                action {
                    it.launchApp("[Chrome]")
                        .waitForDisplay("*検索*")
                }.expectation {
                    it.packageIs("com.android.chrome")
                }
            }
        }
    }

    @Test
    @Order(60)
    fun packageIs_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.packageIs("com.android.chrome")
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
