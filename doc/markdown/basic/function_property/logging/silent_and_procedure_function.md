# silent and procedure function

You can suppress logging using `silent` function.

## silent function

### SilentAndProcedure1.kt

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
                    it.scrollToEnd()
                        .tapWithScrollUp("[System]")
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

`describe` is logged.<br>
`scrollToEnd` and `tapWithScrollUp` are not logged.

<br>

# procedure function

You can use procedure function instead of describe + silent.

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
                    it.scrollToEnd()
                        .tapWithScrollUp("[System]")
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

`procedure` is logged.<br>
`scrollToEnd` and `tapWithScrollUp` are not logged.

### Link

- [index](../../../index.md)
