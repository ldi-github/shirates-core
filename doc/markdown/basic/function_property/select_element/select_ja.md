# select

これらの関数を使用して要素を選択することができます。

[セレクター式](../../selector_and_nickname/selector_expression_ja.md) is accepted as argument.

関数は`TestElement`を返します。

## 関数

| 関数                    | 説明                                          |
|:----------------------|:--------------------------------------------|
| select                | 現在の画面においてセレクターにマッチする最初の要素を取得します             |
| selectWithScrollDown  | 現在の画面においてセレクターにマッチする最初の要素を取得します（下方向スクロールあり） |
| selectWithScrollUp    | 現在の画面においてセレクターにマッチする最初の要素を取得します（上方向スクロールあり） |
| selectWithScrollRight | 現在の画面においてセレクターにマッチする最初の要素を取得します（右方向スクロールあり） |
| selectWithScrollLeft  | 現在の画面においてセレクターにマッチする最初の要素を取得します（左方向スクロールあり） |
| selectInScanResults   | スキャン結果においてセレクターにマッチする最初の要素を取得します            |

## 例1: select

### Select1.kt

(`kotlin/tutorial/basic/Select1.kt`)

```kotlin
    @Test
    @Order(10)
    fun select() {

        scenario {
            case(1) {
                action {
                    it.select("Settings", log = true)
                    output(it)
                }
            }
            case(2) {
                action {
                    it.selectWithScrollDown("System", log = true)
                    output(it)
                }
            }
            case(3) {
                action {
                    it.selectWithScrollUp("Settings", log = true)
                    output(it)
                }
            }
        }
    }
```

#### 注意

`log = true` はデモンストレーション用です。実際のテストコードでは指定しないでください。デフォルトはfalseです。

## 例2: scanElements

### Select1.kt

(`kotlin/tutorial/basic/Select1.kt`)

```kotlin
    @Test
    @Order(20)
    fun selectInScanElements() {

        scenario {
            case(1) {
                action {
                    it.scanElements()
                        .selectInScanResults("Settings", log = true)
                        .selectInScanResults("Accessibility", log = true)
                        .selectInScanResults("System", log = true)
                }
            }
        }
    }
```

#### 注意

`log = true` はデモンストレーション用です。実際のテストコードでは指定しないでください。デフォルトはfalseです。

### Link

- [index](../../../index_ja.md)
