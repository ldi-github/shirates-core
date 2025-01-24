# findImage (Shirates/Vision)

You can find an image using these functions.

[Selector expression](../../selector_and_nickname/selector_expression.md) is accepted as argument.

The function returns `ImageMatchResult` object instead of TestElement.

## Functions

| function                 | description                                                    |
|:-------------------------|:---------------------------------------------------------------|
| findImage                | Finds an image that matches the selector in current screen.    |
| findImageWithScrollDown  | Finds an image that matches the selector with scrolling down.  |
| findImageWithScrollUp    | Finds an image that matches the selector with scrolling up.    |
| findImageWithScrollRight | Finds an image that matches the selector with scrolling right. |
| findImageWithScrollLeft  | Finds an image that matches the selector with scrolling left.  |

### FindImage1.kt

(`kotlin/tutorial/basic/FindImage1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/androidSettings/testrun.properties")
class FindImage1 : UITest() {

    @Test
    @Order(10)
    fun croppingImages() {

        scenario {
            ImageSetupHelper.setupImageAndroidSettingsTopScreen()
        }
    }

    @Test
    @Order(20)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    withScrollDown {
                        it.findImage("[Network & internet Icon].png")
                        it.findImage("[Display Icon].png")
                        it.findImage("[Tips & support Icon].png")
                    }
                    withScrollUp {
                        it.findImage("[Display Icon].png")
                        it.findImage("[Network & internet Icon].png")
                    }
                }.expectation {
                    withScrollDown {
                        it.existImage("[Network & internet Icon].png")
                        it.existImage("[Display Icon].png")
                        it.existImage("[Tips & support Icon].png")
                    }
                    withScrollUp {
                        it.existImage("[Display Icon].png")
                        it.existImage("[Network & internet Icon].png")
                    }
                }
            }
        }
    }

}
```

Icons are captured and cropped as image files.

![](../../_images/crop_icon.png)

Image files are copied into `testConfig/android/androidSettings/screens/images/androidSettingsTopScreen` directory.

![img.png](../../_images/prepare_image.png)

### Note

See [Due to JRE encapsulation, low level data structures needed for fast conversion of BufferedImages are no longer accessible.](../../../troubleshooting/errors/dueToJREencapsulationLowLevelDataStructuresNeededForFastConversionOfBufferedImagesAreNoLongerAccessible.md)

### Link

- [index](../../../../index.md)
