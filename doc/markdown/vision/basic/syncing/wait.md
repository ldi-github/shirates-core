# wait (Vision)

Sometimes, waiting for some seconds is required in particular situation.

You can use **wait** function.

Calling without `waitSeconds` argument uses default duration of `shortWaitSeconds`.
You can configure parameter `shortWaitSeconds` in testrun files.

See Also [Parameters](../../../common/parameter/parameters.md)

## Sample code

[Getting samples](../../getting_samples.md)

### Wait1.kt

(`src/test/kotlin/tutorial/basic/Wait1.kt`)

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

### Link

- [index](../../../index.md)
