# キーボードが表示されていることの検証

こららの関数を使用してキーボードの表示状態を検証することができます。

## 関数

| 関数                 |
|:-------------------|
| keyboardIsShown    |
| keyboardIsNotShown |
| isKeyboardShown    |

## 例

### AssertingOthers1.kt

(`kotlin/tutorial/basic/AssertingOthers1.kt`)

```kotlin
@Test
@Order(30)
fun keyboardIsShown_OK() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                output("isKeyboardShown=$isKeyboardShown")
                it.keyboardIsNotShown()
            }
        }

        case(2) {
            action {
                it.tap("[Search settings]")
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
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.keyboardIsNotShown()
                    .keyboardIsShown()
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)
