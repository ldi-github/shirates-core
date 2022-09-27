# Image assertion

You can assert image using these functions.

## functions

| function                |
|:------------------------|
| imageIs                 |
| imageContains           |
| isImage                 |
| isContainingImage       |
| exist                   |
| existWithScrollDown     |
| existWithScrollUp       |
| existWithScrollRight    |
| existWithScrollLeft     |
| dontExist               |
| dontExistWithScrollDown |
| dontExistWithScrollUp   |

## Example

### Getting image files

Get image files for template matching.

See [Cropping images for template matching](../../../in_action/image_matching/cropping_images_for_template_matching.md).

### Images directory

1. Create image directory under
   Put images files in images directory.

![cropped images](../../../in_action/_images/put_cropped_images_in_images_directory.png)

### Screen nickname file

Define image nicknames in screen nickname file. File name must be unique.

#### [Clock(shared)].json

(`testConfig/android/clock/screens/[Clock(shared)].json`)

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

## Sample program

### AssertingImage1.kt

(`kotlin/tutorial/basic/AssertingImage1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/clock/testrun.properties")
class AssertingImage1 : UITest() {

    @Test
    fun imageIs_isImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image(selected)]")     // OK
                    it.select("[Alarm Tab]").isImage("[Alarm Image(selected)]").thisIsTrue()      // OK
                }
            }
            case(2) {
                expectation {
                    it.select("[Clock Tab]").imageIs("[Clock Image]")     // NG
                }
            }
        }
    }

    @Test
    fun checkingTabState() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image(selected)]")
                    it.select("[Clock Tab]").imageIs("[Clock Image]")
                    it.select("[Timer Tab]").imageIs("[Timer Image]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image]")
                }
            }
            case(2) {
                action {
                    it.tap("[Clock Tab]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image]")
                    it.select("[Clock Tab]").imageIs("[Clock Image(selected)]")
                    it.select("[Timer Tab]").imageIs("[Timer Image]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image]")
                }
            }
            case(3) {
                action {
                    it.tap("[Timer Tab]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image]")
                    it.select("[Clock Tab]").imageIs("[Clock Image]")
                    it.select("[Timer Tab]").imageIs("[Timer Image(selected)]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image]")
                }
            }
            case(4) {
                action {
                    it.tap("[Stopwatch Tab]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image]")
                    it.select("[Clock Tab]").imageIs("[Clock Image]")
                    it.select("[Timer Tab]").imageIs("[Timer Image]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image(selected)]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image]")
                }
            }
            case(5) {
                action {
                    it.tap("[Bedtime Tab]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image]")
                    it.select("[Clock Tab]").imageIs("[Clock Image]")
                    it.select("[Timer Tab]").imageIs("[Timer Image]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image(selected)]")
                }
            }
        }
    }

}
```

### On unexpected NG occurs

You can see **template_image.png** (expected image) and **cropped_image.png** (actual image) in the log directory.

### Note

See [WARNING: An illegal reflective access operation has occurred](../../../troubleshooting/warnings/anIllegalRefrectiveAccessOperationHasOccured.md)

### Link

- [index](../../../index.md)
