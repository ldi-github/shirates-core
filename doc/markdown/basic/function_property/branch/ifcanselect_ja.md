# 分岐関数 (ifCanSelect, ifCanSelectNot)

画面要素に対してこれらの分岐関数を使用することができます。

## 関数

| 関数             | 説明                                |
|:---------------|:----------------------------------|
| ifCanSelect    | 指定した要素が画面上に存在する場合にコードブロックが実行されます  |
| ifCanSelectNot | 指定した要素が画面上に存在しない場合にコードブロックが実行されます |

### IfCanSelect1.kt

(`kotlin/tutorial/basic/IfCanSelect1.kt`)

```kotlin
@Test
@Order(10)
fun ifCanSelectTest() {

    scenario {
        case(1) {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                ifCanSelect("[Network & internet]") {
                    OK("ifCanSelect called")
                }.ifElse {
                    NG()
                }

                ifCanSelectNot("[System]") {
                    OK("ifCanSelectNot called")
                }.ifElse {
                    NG()
                }
            }
        }
        case(2) {
            action {
                it.scrollToBottom()
            }.expectation {
                ifCanSelect("[Network & internet]") {
                    NG()
                }.ifElse {
                    OK("ifElse called")
                }

                ifCanSelectNot("[System]") {
                    NG()
                }.ifElse {
                    OK("ifElse called")
                }
            }
        }
    }
}
```

### Link

- [index](../../../index_ja.md)

