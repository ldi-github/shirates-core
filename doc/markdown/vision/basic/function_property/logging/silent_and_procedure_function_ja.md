# silent, procedure

## silent関数

**silent**関数を使用するとログ出力を抑制することができます。

### SilentAndProcedure1.kt

(`kotlin/tutorial/basic/SilentAndProcedure1.kt`)

```kotlin
    @Test
    @Order(10)
    fun silent1() {

        scenario {
            case(1) {
                condition {
                    macro("[Android Settings Top Screen]")
                }.action {
                    describe("Tap [System]")
                    silent {
                        it.scrollToBottom()
                            .tap("[System]")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }
```

### Html-Report

![](../../_images/silent_and_procedure_1.png)

`describe`は出力されています。<br>
`scrollToEnd`と`tap`は出力されていません。

<br>

# procedure関数

**procedure**関数を使用するとdescribe関数 と silent関数を組み合わせることなく代用できます。

### SilentAndProcedure1.kt

```kotlin
    @Test
    @Order(20)
    fun procedure1() {

        scenario {
            case(1) {
                condition {
                    macro("[Android Settings Top Screen]")
                }.action {
                    procedure("Tap [System]") {
                        it.scrollToBottom()
                            .tap("[System]")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }
```

### Html-Report

![](../../_images/silent_and_procedure_2.png )

`procedure` は出力されています。<br>
`scrollToEnd` と `tap`は出力されていません。

### Link

- [index](../../../index_ja.md)
