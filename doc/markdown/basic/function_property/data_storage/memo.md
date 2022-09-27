# memo

You can write and read memo using these functions.

## functions

| function             | description                                               |
|:---------------------|:----------------------------------------------------------|
| writeMemo(key, text) | Take a memo.                                              |
| readMemo(key)        | Read remo of the key                                      |
| memoTextAs(key)      | Get text value of the element and take a memo as the key. |

## Example 1

### Memo1.kt

(`kotlin/tutorial/basic/Memo1.kt`)

```kotlin
@Order(10)
@Test
fun writeMemo_readMemo() {

    scenario {
        case(1) {
            condition {
                it.macro("[Calculator Main Screen]")
                writeMemo("First key", "[1]")
                writeMemo("Second key", "[+]")
                writeMemo("Third key", "[2]")
                writeMemo("Fourth key", "[=]")
                writeMemo("Expected result", "3")
            }.action {
                it
                    .tap(readMemo("First key"))
                    .tap(readMemo("Second key"))
                    .tap(readMemo("Third key"))
                    .tap(readMemo("Fourth key"))
            }.expectation {
                it.select("[result final]")
                    .textIs(readMemo("Expected result"))
            }
        }
    }
}
```

## Example 2

### Memo1.kt

(`kotlin/tutorial/basic/Memo1.kt`)

```kotlin
@Order(20)
@Test
fun memoTextAs_readMemo() {

    scenario {
        case(1) {
            condition {
                it.macro("[Calculator Main Screen]")
            }.action {
                it.tap("[1]")
                    .tap("[2]")
                    .tap("[+]")
                    .tap("[3]")
                    .tap("[=]")
            }.expectation {
                it.select("[result final]")
                    .textIs("15")
                    .memoTextAs("result1")    // memo TestElement.text as "result1"
            }
        }

        case(2) {
            condition {
                it.tap("[AC]")
            }.action {
                it.readMemo("result1")
                    .forEach { key ->
                        it.tap("[$key]")
                    }
                it.tap("[+]")
                    .tap("[5]")
                    .tap("[=]")
            }.expectation {
                it.select("[result final]")
                    .textIs("20")
            }
        }
    }

}
```

### Link

- [index](../../../index.md)

