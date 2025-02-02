# 表示中のアプリのパッケージの検証 (Classic)

これらの関数を使用して現在表示されているアプリのパッケージを検証することができます。

## 関数

| 関数        |
|:----------|
| packageIs |

## 例

### AssertingOthers1.kt

(`kotlin/tutorial/basic/AssertingOthers1.kt`)

```kotlin
    @Test
    @Order(50)
    fun packageIs_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.packageIs("com.android.settings")
                }
            }

            case(2) {
                action {
                    it.launchApp("Chrome")
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.packageIs("com.android.chrome")
                }
            }
        }
    }
```

### Link

- [index](../../../index_ja.md)
