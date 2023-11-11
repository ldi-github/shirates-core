# 相対セレクター

**相対セレクター** は相対的に要素を選択するための追加操作です。

以下の例では`TestDriver`はまず textが"First Name"である要素を選択し、次にそこから右方向にあるinput要素を選択します。

```kotlin
it.select("<First Name>:rightInput")
```

## 相対セレクターの書式

```
<セレクター式>:コマンド名(引数)
```

または

```
[ニックネーム]:コマンド名(引数)
```

**コマンド名** は相対セレクターのコマンドです。

**引数** はセレクター式（省略可）です。


<br>

コマンドは以下のようにチェーンして指定することができます。

```
[ニックネーム]:コマンド名(引数):コマンド名(引数):コマンド名(引数)
```

## 例

### RelativeSelector1.kt

(`kotlin/tutorial/basic/RelativeSelector1.kt`)

```kotlin
    @Test
    fun select() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.expectation {
                    it.select("<@1>:rightButton").accessIs("2")
                    it.select("<@1>:rightButton:rightButton").accessIs("3")
                    it.select("<@1>:rightButton(2)").accessIs("3")
                    it.select("[1]:rightButton(2)").accessIs("3")
                }
            }
        }
    }
```

### [Calculator Main Screen].json

(`testConfig/android/calculator/screens/[Calculator Main Screen].json`)

```
{
  "key": "[Calculator Main Screen]",

  "screens": {
    "import": [
      "testConfig/android/misc/screens"
    ]
  },

  "identity": "[AC][()]",

  "selectors": {
    "[formula]": "#formula",
    "[result final]": "#result_final",
    "[result preview]": "#result_preview",

    "[√]": "#op_sqrt",
    "[π]": "#const_pi",
    "[^]": "#op_pow",
    "[!]": "#op_fact",

    "[AC]": "#clr",
    "[()]": "#parens",
    "[%]": "#op_pct",

    "[÷]": "#op_div",
    "[×]": "#op_mul",
    "[-]": "#op_sub",
    "[+]": "#op_add",
    "[=]": "#eq",
    "[⌫]": "#del",

    "[0]": "#digit_0",
    "[1]": "#digit_1",
    "[2]": "#digit_2",
    "[3]": "#digit_3",
    "[4]": "#digit_4",
    "[5]": "#digit_5",
    "[6]": "#digit_6",
    "[7]": "#digit_7",
    "[8]": "#digit_8",
    "[9]": "#digit_9",
    "[.]": "#dec_point",

    "[:Right button]": ":rightButton",
    "[:Below button]": ":belowButton",
    "[:Left button]": ":leftButton",
    "[:Above button]": ":aboveButton"
  }

}
```

## 相対ニックネーム

相対セレクターをニックネームで定義することができます。

```
"[:Right button]": ":rightButton"
```

相対セレクターを使用して要素を選択することができます。

```
it.select("<Label1>[:Right button]")
```

### RelativeSelector1.kt

(`kotlin/tutorial/basic/RelativeSelector1.kt`)

```kotlin
    @Test
    fun select_with_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.expectation {
                    it.select("<@1>[:Right button]").accessIs("2")
                    it.select("[1][:Below button]").accessIs("0")
                    it.select("[1]:rightButton(2)[:Left button]").accessIs("2")
                }
            }
            case(2) {
                expectation {
                    it.select("[5]").select("[:Right button]").accessIs("6")
                    it.select("[5]").select("[:Below button]").accessIs("2")
                    it.select("[5]").select("[:Left button]").accessIs("4")
                    it.select("[5]").select("[:Above button]").accessIs("8")
                }
            }
            case(3) {
                expectation {
                    it.select("[5]").apply {
                        select("[:Right button]").accessIs("6")
                        select("[:Below button]").accessIs("2")
                        select("[:Left button]").accessIs("4")
                        select("[:Above button]").accessIs("8")
                    }
                }
            }
        }
    }
```

### Link

- [相対セレクター方向ベース)](relative_selector_direction_ja.md)
- [相対セレクター(ウィジェットフローベース)](relative_selector_flow_ja.md)
- [相対セレクター(XMLベース)](relative_selector_xml_ja.md)
- [相対セレクターの追加サンプル](relative_selector_more_example_ja.md)
- [index](../../../index_ja.md)

