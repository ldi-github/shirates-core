# 画像が存在することの検証（existImage, dontExistImage）

画像の存在確認は通常の `exist` 関数ではなく、画像専用の `existImage` 関数を使用します。

また、画像が存在しないことの確認は `dontExist` 関数ではなく、画像専用の `dontExistImage` 関数を使用します。

### ExistDontExist2.kt

(`kotlin/tutorial/basic/ExistDontExist2.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExistImage
import shirates.core.driver.commandextension.existImage
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/maps/testrun.properties")
class ExistDontExist2 : UITest() {

    override fun beforeAllAfterSetup(context: ExtensionContext?) {
        ImageSetupHelper.setupImagesMapsTopScreen()
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
                        .existImage("[Contribute Tab]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existImage_WARN_COND_AUTO() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]")   // WARN & COND_AUTO
                }
            }
        }
    }

    @Test
    @Order(30)
    fun dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Contribute Tab(selected)]") // OK
                }
            }
        }
    }

    @Test
    @Order(40)
    fun dontExistImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Contribute Tab]") // NG
                }
            }
        }
    }

}
```

## サンプルの解説

1. `ExistDontExist2`
   を実行すると、画像マッチング用の画像が作成されます。Mapsアプリの画面をキャプチャして以下のディレクトリに画像ファイルが出力されます。<br><br>`testConfig/android/maps/screens/images/mapsTopScreen`
   <br><br> ![img.png](../../_images/setup_image_android_settings_top_screen.png) <br><br> ![img.png](../../_images/setup_image_android_settings_top_screen_2.png)

<br>

2. `existImage_OK()` を実行します。上記で出力した画像ファイルが読み込まれます。`existImage()`を実行すると画像マッチングが実行されます。

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
                        .existImage("[Contribute Tab]")
                }
            }
        }
    }
```

![img_1.png](../../_images/image_assertion_exist_image_maps_top_screen_1.png) <br><br>
![img.png](../../_images/image_assertion_exist_image_existimage_ok.png)

<br>

3. `existImage_WARN_COND_AUTO()` を実行します。画像がマッチしないため警告メッセージ(WARN)
   が出力されるとともに、手動テストが必要であることを示すメッセージ(COND_AUTO)を出力します。

```kotlin
    @Test
    @Order(20)
    fun existImage_WARN_COND_AUTO() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]")   // WARN & COND_AUTO
                }
            }
        }
    }
```

![img.png](../../_images/image_assertion_exist_image_existimage_warn.png)

<br>

4. `dontExistImage_OK()` を実行します。画像が存在しないのでOKメッセージが出力されます。

```kotlin
    @Test
    @Order(30)
    fun dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Contribute Tab(selected)]") // OK
                }
            }
        }
    }
```

![img.png](../../_images/image_assertion_exist_image_dontexistimage_ok.png)

<br>

5. `dontExistImage_NG()` を実行します。画像が存在するのでNGメッセージが出力されます。

```kotlin
    @Test
    @Order(40)
    fun dontExistImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Contribute Tab]") // NG
                }
            }
        }
    }
```

![img.png](../../_images/image_assertion_exist_image_dontexistimage_ng.png)

### Link

- [index](../../../index_ja.md)
