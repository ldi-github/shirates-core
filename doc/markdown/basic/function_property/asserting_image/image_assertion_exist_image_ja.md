# 画像の検証（existImage）

画像の存在確認には `exist` 関数が使えますが、画像マッチング用の画像ファイルを作成するのが煩雑です。スクリーンショットをキャプチャして画像ファイルを作成し、画像を切り抜いて
cofig ディレクトリに配置し、あらかじめニックネームファイルにファイル名を設定しておく必要があります。

`existImage` 関数を使えば画像ファイルの切り出しを自動化できます。testConfigディレクトリに配置するだけです。以降はexistImage関数を使用すれば要素の画像を検証することができます。

## 例

### AssertingImage1.kt

(`kotlin/tutorial/basic/AssertingImage2.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.existImage
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.withScrollDown
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.utility.toPath
import shirates.helper.TestSetupHelper
import java.nio.file.Files
import kotlin.io.path.copyTo

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AssertingImage2 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        TestSetupHelper.setupImageAndroidSettingsTopScreen()
        val files = Files.list(TestLog.testResults.resolve("images/androidSettingsTopScreen")).toList()
        val p = "testConfig/android/androidSettings/screens/images/androidSettingsTopScreen".toPath()
        if (Files.exists(p).not()) {
            p.toFile().mkdirs()
        }
        for (file in files) {
            file.copyTo(p.resolve(file.fileName), overwrite = true)
        }
    }

    @Test
    @Order(10)
    fun existImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Network & internet Icon]")
                    withScrollDown {
                        it.existImage("[Display Icon]")
                        it.existImage("[Tips & support Icon]")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existImage_NG_image_not_found() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Tips & support]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun existImage_manual_template_file_not_found() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Settings]")   // manual
                }
            }
        }
    }

}
```

### Link

- [index](../../../index_ja.md)
