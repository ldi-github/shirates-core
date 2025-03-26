# memo (Vision)

You can write and read memo using these functions.

**memo** is temporary storage in memory.

## functions

| function             | description                                               |
|:---------------------|:----------------------------------------------------------|
| writeMemo(key, text) | Take a memo.                                              |
| readMemo(key)        | Read remo of the key                                      |
| memoTextAs(key)      | Get text value of the element and take a memo as the key. |

## Sample code

[Getting samples](../../../getting_samples.md)

### Memo1.kt

(`src/test/kotlin/tutorial/basic/Memo1.kt`)

```kotlin
    @Order(10)
    @Test
    fun writeMemo_readMemo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .tap("Storage")
                        .waitForDisplay("GB")
                }.action {
                    writeMemo("System", it.detect("System").rightText().text)
                    writeMemo("Apps", it.detect("Apps").rightText().text)
                }.expectation {
                    readMemo("System").printInfo()
                    readMemo("Apps").printInfo()
                }
            }
        }
    }

    @Order(20)
    @Test
    fun memoTextAs_readMemo() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    it.detect("SIMs").belowText().memoTextAs("SIMs")
                    it.detect("VPN").belowText().memoTextAs("VPN")
                }.expectation {
                    it.readMemo("SIMs").thisIs("T-Mobile")
                    it.readMemo("VPN").thisIs("None")
                }
            }
        }

    }
```

### Link

- [index](../../../../index.md)

