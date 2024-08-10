# Asserting existence of image（existImage, dontExistImage）

To confirm the existence of an image, use the `existImage` function instead of `exist` function.

To confirm that an image does not exist, use the `dontExistImage` function instead of `dontExist` function.

### ExistDontExist2.kt

(`kotlin/tutorial/basic/ExistDontExist2.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExistImage
import shirates.core.driver.commandextension.existImage
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/maps/testrun.properties")
class ExistDontExist2 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            ImageSetupHelper.SetupImagesMapsTopScreen()
        }
    }

    @Test
    @Order(10)
    fun existImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab(selected)]")
                        .existImage("[You Tab]")
                        .existImage("[Contribute Tab]")
                }
            }
        }

    }

    @Test
    @Order(20)
    fun existImage_WARN() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]")   // WARN
                }
            }
        }
    }

    @Test
    @Order(30)
    fun existImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]", throwsException = true)   // NG
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
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Explore Tab]") // OK
                }
            }
        }
    }

    @Test
    @Order(50)
    fun dontExistImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Explore Tab(selected)]") // NG
                }
            }
        }
    }

}
```

## Sample Description

1. Run `setupImage()` to create files for image matching. Image files will be output to the following directory after
   capturing the maps app screen.<br><br>`testConfig/android/maps/screens/images/mapsTopScreen`
   <br><br> ![img.png](../../_images/setup_image_android_settings_top_screen.png) <br><br> ![img.png](../../_images/setup_image_android_settings_top_screen_2.png)
2. Run `existImage_OK()`. The files described above will be loaded.
   Image matching will be executed on calling `existImage()`.

```kotlin
    @Test
    @Order(10)
    fun existImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab(selected)]")
                        .existImage("[You Tab]")
                        .existImage("[Contribute Tab]")
                }
            }
        }

    }
```

![img_1.png](../../_images/image_assertion_exist_image_maps_top_screen_1.png) <br><br>
![img.png](../../_images/image_assertion_exist_image_existimage_ok.png)

3. Run `existImage_WARN()`. **WARN** message is output because the image does not match.

```kotlin
    @Test
    @Order(20)
    fun existImage_WARN() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]")   // WARN
                }
            }
        }
    }
```

![img.png](../../_images/image_assertion_exist_image_existimage_warn.png)

4. Run `existImage_NG()`. **NG** message is output because the argument of `throwsException = true` specified.

```kotlin
    @Test
    @Order(30)
    fun existImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]", throwsException = true)   // NG
                }
            }
        }
    }
```

![img.png](../../_images/image_assertion_exist_image_existimage_ng.png)

5. Run `dontExistImage_OK()`. **OK** message is output because the image is not exist.

```kotlin
    @Test
    @Order(40)
    fun dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Explore Tab]") // OK
                }
            }
        }
    }
```

![img.png](../../_images/image_assertion_exist_image_dontexistimage_ok.png)

6. Run `dontExistImage_NG()`. **NG** message is output because the image exists.

```kotlin
    @Test
    @Order(50)
    fun dontExistImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Explore Tab(selected)]") // NG
                }
            }
        }
    }
```

![img.png](../../_images/image_assertion_exist_image_dontexistimage_ng.png)

### Link

- [index](../../../index_ja.md)
