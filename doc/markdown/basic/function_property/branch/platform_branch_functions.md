# Platform function

Conditional branches are required on specific test situation. You can use platform functions.

## functions

| function   | description                               |
|:-----------|:------------------------------------------|
| android    | The code block is executed on android     |
| ios        | The code block is executed on iOS         |
| emulator   | The code block is executed on emulator    |
| simulator  | The code block is executed on simulator   |
| realDevice | The code block is executed on real device |

## Example

### BranchFunctions1.kt

```kotlin
@Test
@Order(10)
fun branch_os_platform() {

    scenario {
        case(1) {
            action {
                android {
                    emulator {
                        output("This is called on android emulator")
                    }
                    realDevice {
                        output("This is called on android real device")
                    }
                }
                ios {
                    simulator {
                        output("This is called on iOS Simulator")
                    }
                    realDevice {
                        output("This is called on iOS real device")
                    }
                }
            }.expectation {
                it.screenIs("[Android Settings Top Screen]")
            }
        }
    }
}
```

### Spec-Report(Normal mode)

![](../../_images/branch_functions_normal.png)

### Spec-Report(No-Load-Run mode)

![](../../_images/branch_functions_no_load_run.png)

### Link

- [index](../../../index.md)

