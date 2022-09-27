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
                it.macro("[Timer Screen]")
            }.expectation {
                it.select("<1>:rightButton").textIs("2")
                it.select("<1>:rightButton:rightButton").textIs("3")
                it.select("<1>:rightButton(2)").textIs("3")
                it.select("[1]:rightButton(2)").textIs("3")
            }
        }
    }
}
```

### [Timer Screen].json

(`testConfig/android/clock/screens/[Timer Screen].json`)

```
{
  "key": "[Timer Screen]",

  "include": [
    "[Clock(shared)]"
  ],

  "identity": "~title=Timer",

  "selectors": {
    "[Timer setup time]": "#timer_setup_time",
    "[1]": "#timer_setup_digit_1",
    "[2]": "#timer_setup_digit_2",
    "[3]": "#timer_setup_digit_3",
    "[4]": "#timer_setup_digit_4",
    "[5]": "#timer_setup_digit_5",
    "[6]": "#timer_setup_digit_6",
    "[7]": "#timer_setup_digit_7",
    "[8]": "#timer_setup_digit_8",
    "[9]": "#timer_setup_digit_9",
    "[00]": "#timer_setup_digit_00",
    "[0]": "#timer_setup_digit_0",
    "[Delete]": "#timer_setup_delete",

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
                it.macro("[Timer Screen]")
            }.expectation {
                it.select("<1>[:Right button]").textIs("2")
                it.select("[1][:Below button]").textIs("4")
                it.select("[1]:rightButton(2)[:Left button]").textIs("2")
            }
        }
        case(2) {
            expectation {
                it.select("[5]").select("[:Right button]").textIs("6")
                it.select("[5]").select("[:Below button]").textIs("8")
                it.select("[5]").select("[:Left button]").textIs("4")
                it.select("[5]").select("[:Above button]").textIs("2")
            }
        }
        case(3) {
            expectation {
                it.select("[5]").apply {
                    select("[:Right button]").textIs("6")
                    select("[:Below button]").textIs("8")
                    select("[:Left button]").textIs("4")
                    select("[:Above button]").textIs("2")
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

