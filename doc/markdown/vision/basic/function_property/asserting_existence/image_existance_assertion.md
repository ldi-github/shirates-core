# Image existence assertion (Vision)

You can check existence of image using these functions.

## Functions

|   group   | function                    | description                                                                                              | return value                                                  |
|:---------:|:----------------------------|:---------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------|
|   exist   | existImage                  | Assert that the image exists in current screen <br> Scrolling occurs within `withScroll` function        | VisionElement(if exists)<br>TestNGException(if doesn't exist) |
|   exist   | existImageWithScrollDown    | Assert that the image exists with scrolling down                                                         | (same as above)                                               |
|   exist   | existImageWithScrollUp      | Assert that the image exists with scrolling up                                                           | (same as above)                                               |
|   exist   | existImageWithoutScroll     | Assert that the image exists without scrolling                                                           | (same as above)                                               |
| dontExist | dontExistImage              | Assert that the image doesn't exist in current screen <br> Scrolling occurs within `withScroll` function | empty element(if doesn't exist)<br>TestNGException(if exists) |
| dontExist | dontExistImageWithoutScroll | Assert that the image doesn't exist without scrolling                                                    | (same as above)                                               |

## Sample code

[Getting samples](../../getting_samples.md)

### ExistImageDontExistImage1.kt

(`src/test/kotlin/tutorial/basic/ExistImageDontExistImage1.kt`)

```kotlin
    @Test
    @Order(10)
    fun existImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Apps Icon]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun withScrollDown_existImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.existImage("[System Icon]")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun withScrollDown_existImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .existImage("[Network & internet Icon]")
                        .scrollDown()
                }.expectation {
                    withScrollDown {
                        it.existImage("[Network & internet Icon]")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun withScrollDown_existImageWithoutScroll_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.existImageWithoutScroll("[Network & internet Icon]")     // OK
                        it.existImageWithoutScroll("[System Icon]")     // NG
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExistImage("[System Icon]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun withScrollDown_dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.dontExistImage("[VPN Icon]")
                    }
                }
            }
        }
    }

    @Test
    @Order(50)
    fun withScrollDown_dontExistImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.dontExistImage("[System Icon]")
                    }
                }
            }
        }
    }

    @Test
    @Order(60)
    fun withScrollDown_dontExistImageWithoutScroll_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.dontExistWithoutScroll("Accessibility")    // OK
                        it.dontExistWithoutScroll("Notifications")    // NG
                    }
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)


