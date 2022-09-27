# wait

Sometimes, waiting for some seconds is required in particular situation.

You can use **wait** function.

Calling without `waitSeconds` argument uses default duration of `shortWaitSeconds`.

See Also [Parameters](../parameter/parameters.md)

### Wait1.kt

(`kotlin/tutorial/basic/Wait1.kt`)

```kotlin
@Test
@Order(10)
fun wait1() {

    scenario {
        case(1) {
            action {
                describe("Wait for a short time.")
                    .wait()
            }
        }

        case(2) {
            action {
                describe("Wait for 3.0 seconds.")
                    .wait(3.0)
            }
        }
    }
}
```

You can configure parameter `shortWaitSeconds` in testrun files.

### Link

- [index](../../index.md)
