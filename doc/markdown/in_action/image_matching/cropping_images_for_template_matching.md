# Cropping images for template matching

When you want to use **template matching**(image matching), you have to prepare image files in advance.
You can use `cropImage` function for capturing and saving image of specified element.

## Cropping sample

### CroppingImages1.kt

```kotlin
package tutorial.inaction

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/clock/testrun.properties")
class CroppingImages1 : UITest() {

    @Test
    fun croppingClockImages() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Clock]")
                        .tap("[Alarm Tab]")
                        .screenIs("[Alarm Screen]")
                        .wait()     // wait for animation to complete
                }.expectation {
                    it.select("[Alarm Tab]").cropImage("Alarm(selected).png")
                    it.select("[Clock Tab]").cropImage("Clock.png")
                    it.select("[Timer Tab]").cropImage("Timer.png")
                    it.select("[Stopwatch Tab]").cropImage("Stopwatch.png")
                    it.select("[Bedtime Tab]").cropImage("Bedtime.png")
                }
            }
            case(2) {
                action {
                    it.tap("[Clock Tab]")
                        .screenIs("[Clock Screen]")
                        .wait()     // wait for animation to complete
                }.expectation {
                    it.select("[Alarm Tab]").cropImage("Alarm.png")
                    it.select("[Clock Tab]").cropImage("Clock(selected).png")
                }
            }
            case(3) {
                action {
                    it.tap("[Timer Tab]")
                        .screenIs("[Timer Screen]")
                        .wait()     // wait for animation to complete
                }.expectation {
                    it.select("[Timer Tab]").cropImage("Timer(selected).png")
                }
            }
            case(4) {
                action {
                    it.tap("[Stopwatch Tab]")
                        .screenIs("[Stopwatch Screen]")
                        .wait()     // wait for animation to complete
                }.expectation {
                    it.select("[Stopwatch Tab]").cropImage("Stopwatch(selected).png")
                }
            }
            case(5) {
                action {
                    it.tap("[Bedtime Tab]")
                        .screenIs("[Bedtime Screen]")
                        .wait()     // wait for animation to complete
                }.expectation {
                    it.select("[Bedtime Tab]").cropImage("Bedtime(selected).png")
                }
            }
        }
    }

}
```

### Cropping images

Above program select elements and crops images to file.

![cropping images](../_images/cropping_images.png)

### Cropped images

![cropped images](../_images/cropped_images.png)

### Images directory

Put cropped image files in image directory.

![cropped images](../_images/put_cropped_images_in_images_directory.png)

### Screen nickname file

Define image nicknames in screen nickname file. File name must be unique.

#### [Clock(shared)].json

```
{
  "key": "[Clock(shared)]",

  "selectors": {
    "[Alarm Tab]": "#tab_menu_alarm",
    "[Alarm Image]": "Alarm.png",
    "[Alarm Image(selected)]": "Alarm(selected).png",

    "[Clock Tab]": "#tab_menu_clock",
    "[Clock Image]": "Clock.png",
    "[Clock Image(selected)]": "Clock(selected).png",

    "[Timer Tab]": "#tab_menu_timer",
    "[Timer Image]": "Timer.png",
    "[Timer Image(selected)]": "Timer(selected).png",

    "[Stopwatch Tab]": "#tab_menu_stopwatch",
    "[Stopwatch Image]": "Stopwatch.png",
    "[Stopwatch Image(selected)]": "Stopwatch(selected).png",

    "[Bedtime Tab]": "#tab_menu_bedtime",
    "[Bedtime Image]": "Bedtime.png",
    "[Bedtime Image(selected)]": "Bedtime(selected).png"
  }

}
```

### Using image matching

See [Image Assertion](../../basic/function_property/asserting_image/image_assertion.md)

### Link

- [index](../../index.md)
