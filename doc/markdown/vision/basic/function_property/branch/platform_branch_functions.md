# Platform function (Vision)

Conditional branches are required on specific test situation. You can use platform functions.

## functions

| function      | description                                    |
|:--------------|:-----------------------------------------------|
| android       | The code block is executed on android          |
| ios           | The code block is executed on iOS              |
| emulator      | The code block is executed on Android emulator |
| simulator     | The code block is executed on iOS simulator    |
| virtualDevice | The code block is executed on virtual device   |
| realDevice    | The code block is executed on real device      |

## Sample code

[Getting samples](../../getting_samples.md)

### BranchFunctions1.kt

(`src/test/kotlin/tutorial/basic/BranchFunctions1.kt`)

```kotlin
    @Test
    @Order(10)
    fun branch_platform_device() {

        scenario {
            case(1) {
                action {
                    android {
                        virtualDevice {
                            describe("This is called on android emulator")
                        }
                        realDevice {
                            describe("This is called on android real device")
                        }
                    }
                    ios {
                        virtualDevice {
                            describe("This is called on iOS simulator")
                        }
                        realDevice {
                            describe("This is called on iOS real device")
                        }
                    }
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }
            case(2) {
                action {
                    emulator {
                        describe("This is called on android emulator")
                    }
                    simulator {
                        describe("This is called on iOS simulator")
                    }
                    realDevice {
                        describe("This is called on real device")
                    }
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
            }
        }
    }
```

#### Spec-Report(Normal mode)

![](_images/branch_functions_normal.png)

#### Spec-Report(No-Load-Run mode)

![](_images/branch_functions_no_load_run.png)

### Link

- [index](../../../../index.md)

