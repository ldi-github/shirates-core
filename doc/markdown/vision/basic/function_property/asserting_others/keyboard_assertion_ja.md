# キーボードが表示されていることの検証

こららの関数を使用してキーボードの表示状態を検証することができます。

## 関数

| 関数                 |
|:-------------------|
| keyboardIsShown    |
| keyboardIsNotShown |
| isKeyboardShown    |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### AssertingOthers1.kt

(`kotlin/tutorial/basic/AssertingOthers1.kt`)

```kotlin
    @Test
    @Order(30)
    fun keyboardIsShown_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    output("isKeyboardShown=$isKeyboardShown")
                    it.keyboardIsNotShown()
                }
            }
            case(2) {
                action {
                    it.tap("設定を検索")
                }.expectation {
                    it.keyboardIsShown()
                }
            }
        }
    }

    @Test
    @Order(40)
    fun keyboardIsShown_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.expectation {
                    it.keyboardIsNotShown()
                        .keyboardIsShown()
                }
            }
        }
    }
```

### Link

- [index](../../../../index_ja.md)
