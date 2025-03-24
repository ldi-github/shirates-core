# findImage (Vision)

You can find an image using these functions.

## Functions

| function                 | description                                                          |
|:-------------------------|:---------------------------------------------------------------------|
| findImage                | Finds an image that matches the template image in current screen.    |
| findImageWithScrollDown  | Finds an image that matches the template image with scrolling down.  |
| findImageWithScrollUp    | Finds an image that matches the template image with scrolling up.    |
| findImageWithScrollRight | Finds an image that matches the template image with scrolling right. |
| findImageWithScrollLeft  | Finds an image that matches the template image with scrolling left.  |

### Tuning threshold

You may encounter an error in spite of the image is actually existing,
and see message as follows.

```
[info]	+1727	!	()	findImage("[Location Icon]") not found. (distance:0.40028667 > threshold:0.1)
```

In this case, specify argument `threshold` explicitly.

```
findImageWithScrollDown("[Location Icon]", threshold = 0.5)
```

## Sample code

[Getting samples](../../../getting_samples.md)

### Template images

![](_images/template_images.png)

### FindImage1.kt

(`src/test/kotlin/tutorial/basic/FindImage1.kt`)

```kotlin
    @Test
    @Order(10)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    withScrollDown {
                        v1 = it.findImage("[Network & internet Icon]")
                        v2 = it.findImage("[Display Icon]")
                    }
                }.expectation {
                    v1.isFound.thisIsTrue("[Network & internet Icon] is found.")
                    v2.isFound.thisIsTrue("[Display Icon] is found.")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun findImageWithScrollDown_findImageWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    v1 = findImageWithScrollDown("[Location Icon]")
                }.expectation {
                    v1.imageIs("[Location Icon]")
                }
            }
            case(2) {
                action {
                    v1 = findImageWithScrollUp("[Connected devices Icon]")
                }.expectation {
                    v1.imageIs("[Connected devices Icon]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun findImageWithScrollRight_findImageWithScrollLeft() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Files Top Screen]")
                }.action {
                    it.onLineOf("Images") {
                        v1 = findImageWithScrollRight("[This week Button]")
                    }
                }.expectation {
                    v1.imageIs("[This week Button]")
                }
            }
            case(2) {
                action {
                    v1.onLine {
                        v2 = findImageWithScrollLeft("[Audio Button]")
                    }
                }.expectation {
                    v2.imageIs("[Audio Button]")
                }
            }
        }
    }
```

### Link

- [index](../../../../index.md)
