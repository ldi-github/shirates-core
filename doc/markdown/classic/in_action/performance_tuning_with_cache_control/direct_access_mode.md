# Direct access mode (Classic)

You can access elements with direct access mode.

### Pros.

- Can avoid severe performance problem of getPageSource in iOS
- Fast to get **a few** elements in the screen with many elements

### Cons.

- Slower than cache mode when get many elements
- Direction based/Flow based relative selector is not available
- `currentScreen` is not set automatically. You have to set `currentScreen` with `screenIs`/`switchScreen` function
  manually
  before using selector nickname
- Other functions may be restricted

## Function

| function          | description                                         |
|:------------------|:----------------------------------------------------|
| disableCache      | Disables cache                                      |
| enableCache       | Enables cache                                       |
| suppressCache { } | Suppress cache {code block}                         |
| useCache { }      | Use cache {code block}                              |
| switchScreen      | Set currentScreen to switch screen context manually |

## useCache argument

You can specify useCache argument with these functions of UITest.

- scenario
- case
- condition
- action
- expectation

| useCache             | description                  |
|:---------------------|:-----------------------------|
| null (not specified) | Depends on ambient condition |
| true                 | use cache                    |
| false                | direct access                |

### DirectAccessModeIos.kt

(`kotlin/tutorial/inaction/DirectAccessModeIos.kt`)

```kotlin
    @Test
    @Order(10)
    fun enableCacheTest() {

//        enableCache()   // This should not be specified if cache mode is default

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                        .tap("[General]")
                        .screenIs("[General Screen]")
                }.action {
                    it.tap("[About]")
                }.expectation {
                    it.screenIs("[About Screen]")
                        .exist("[Name]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun disableCacheTest() {

        disableCache()

        scenario {
            case(1) {
                condition {
                    it.switchScreen("[iOS Settings Top Screen]")
                        .tap("[General]")
                        .switchScreen("[General Screen]")
                }.action {
                    it.tap("[About]")
                }.expectation {
                    it.switchScreen("[About Screen]")
                        .exist("[Name]")
                }
            }
        }
    }

    @Test
    @Order(25)
    fun appiumDriverTest() {

        disableCache()

        scenario {
            case(1) {
                condition {
                    appiumDriver.findElement(By.id("General")).click()
                }.action {
                    appiumDriver.findElement(By.id("About")).click()
                }.expectation {
                    appiumDriver.findElement(By.id("Name"))
                        .toTestElement().isFound.thisIsTrue("<Name > exists")
                }
            }
        }

    }

    @Test
    @DisableCache
    @Order(30)
    fun disableCacheTest2() {

        scenario {
            case(1) {
                condition {
                    it.switchScreen("[iOS Settings Top Screen]")
                        .tap("[General]")
                        .switchScreen("[General Screen]")
                }.action {
                    it.tap("[About]")
                }.expectation {
                    it.switchScreen("[About Screen]")
                        .exist("[Name]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun performanceComparison() {

        fun process(count: Int) {
            invalidateCache()

            val sw1 = StopWatch("cache mode")
            useCache {
                for (i in 1..count) {
                    it.select("General")  // cache mode
                }
            }
            sw1.stop()

            val sw2 = StopWatch("direct access mode")
            suppressCache {
                for (i in 1..count) {
                    it.select("General")  // direct access mode
                }
            }
            sw2.stop()

            output("$count element(s)")
            output("$sw1 cache mode")
            output("$sw2 direct access mode")
        }

        scenario {
            case(1) {
                expectation {
                    process(1)
                }
            }
            case(2) {
                expectation {
                    process(5)
                }
            }
            case(3) {
                expectation {
                    process(10)
                }
            }
            case(4) {
                expectation {
                    process(50)
                }
            }
        }
    }

    @Test
    @Order(50)
    fun useCacheArgument() {

        // useCache argument can be specified with these functions

        printUseCache("testMethod")

        scenario(useCache = true) {
            printUseCache("scenario")

            case(1, useCache = false) {
                printUseCache("case")

                condition(useCache = true) {
                    printUseCache("condition")

                }.action(useCache = false) {
                    printUseCache("action")

                }.expectation(useCache = true) {
                    printUseCache("expectation")

                }
            }
        }
    }

    private fun printUseCache(funcName: String) {

        println("($funcName) testContext.useCache=${testContext.useCache}")
    }
```

### Link

- [Performance problem of getPageSource in iOS](performance_problem_of_getpagesource_in_ios.md)


- [index](../../index.md)
