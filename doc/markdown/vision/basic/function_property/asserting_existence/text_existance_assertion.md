# Text existence assertion (Vision)

You can check existence of text using these functions.

## Functions

|   group   | function               | description                                                                                             | return value                                                  |
|:---------:|:-----------------------|:--------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------|
|   exist   | exist                  | Assert that the text exists in current screen <br> Scrolling occurs within `withScroll` function        | VisionElement(if exists)<br>TestNGException(if doesn't exist) |
|   exist   | existWithScrollDown    | Assert that the text exists with scrolling down                                                         | (same as above)                                               |
|   exist   | existWithScrollUp      | Assert that the text exists with scrolling up                                                           | (same as above)                                               |
|   exist   | existWithoutScroll     | Assert that the text exists without scrolling                                                           | (same as above)                                               |
| dontExist | dontExist              | Assert that the text doesn't exist in current screen <br> Scrolling occurs within `withScroll` function | empty element(if doesn't exist)<br>TestNGException(if exists) |
| dontExist | dontExistWithoutScroll | Assert that the text doesn't exist without scrolling                                                    | (same as above)                                               |

## Sample code

[Getting samples](../../getting_samples.md)

### ExistDontExist1.kt

(`src/test/kotlin/tutorial/basic/ExistDontExist1.kt`)

```kotlin
    @Test
    @Order(10)
    fun exist_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun exist_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("System")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun existWithScrollDown_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("System")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun existWithScrollDown_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("Network business")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun existWithScrollUp_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    it.existWithScrollUp("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(60)
    fun existWithScrollUp_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    it.existWithScrollUp("Network business")
                }
            }
        }
    }

    @Test
    @Order(70)
    fun dontExist_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExist("System")
                }
            }
        }
    }

    @Test
    @Order(80)
    fun dontExist_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExist("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(90)
    fun withScrollDown_dontExist_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.dontExist("Network business")
                    }
                }
            }
        }
    }

    @Test
    @Order(100)
    fun withScrollDown_dontExist_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.dontExist("System")
                    }
                }
            }
        }
    }

    @Test
    @Order(110)
    fun dontExistWithScrollUp_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExist("Network business")
                    }
                }
            }
        }
    }

    @Test
    @Order(120)
    fun dontExistWithScrollUp_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExist("System")
                    }
                }
            }
        }
    }

    @Test
    @Order(130)
    fun withScrollDown_existWithoutScroll_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.existWithoutScroll("Network & internet")    // OK
                        it.existWithoutScroll("System")    // NG
                    }
                }
            }
        }
    }

    @Test
    @Order(140)
    fun withScrollUp_dontExistWithoutScroll_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickAndGoDown()
                }.expectation {
                    withScrollUp {
                        it.dontExistWithoutScroll("Display")    // OK
                        it.dontExistWithoutScroll("System")    // NG
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)


