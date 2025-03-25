# tempSelector, tempValue (Vision)

こららの関数を使用すると一時的な使用のために値を登録したり取得したりすることができます。

## 関数

| 関数                                   | 説明                                          |
|:-------------------------------------|---------------------------------------------|
| tempSelector(_nickname, expression_) | _nickname_ をキーとして _expression_ を登録することができます |
| tempValue(_nickname_)                | _nickname_ をキーとして値を取得することができます              |

## サンプルコード

[サンプルの入手](../../../getting_samples_ja.md)

### TempSelector1.kt

(`src/test/kotlin/tutorial/basic/TempSelector1.kt`)

```kotlin
    @Test
    @Order(10)
    fun tempSelector1() {

        tempSelector("[最初のアイテム]", "ネットワークとインターネット")

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    it.tap("[最初のアイテム]")
                }.expectation {
                    it.screenIs("[ネットワークとインターネット画面]")
                }
            }
        }
    }
```

### 参照

[ニックネーム](../../selector_and_nickname/nickname_ja.md)

### Link

- [index](../../../../index_ja.md)

