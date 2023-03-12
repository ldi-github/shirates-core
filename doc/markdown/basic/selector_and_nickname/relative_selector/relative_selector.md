# Relative selector

**Relative selector** is additional operation to select an element relatively.

In the below example, TestDriver selects the first element that text is "First Name", then select next input element in
right direction.

```kotlin
it.select("<First Name>:rightInput")
```

## Relative selector format

```
<selector expression>:command(args)
```

or

```
[Nickname]:command(args)
```

**command** is relative selector command.

**args** is selector expression (optional).


<br>

Command can be chained like this.

```
[Nickname]:command(args):command(args):command(args)
```

## Example

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

## Relative nickname

You can define nickname for relative selector.

```
"[:Right button]": ":rightButton"
```

You can select an element using relative nickname.

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

- [Relative selector command (Direction based)](relative_selector_direction.md)

- [Relative selector command (Widget flow based)](relative_selector_flow.md)

- [Relative selector command (XML based)](relative_selector_xml.md)

- [Relative selector more example](relative_selector_more_example.md)


- [index](../../../index.md)

