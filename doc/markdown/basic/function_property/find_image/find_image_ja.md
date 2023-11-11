# findImage

これらの関数を使用すると画像を検索することができます。

[セレクター式](../../selector_and_nickname/selector_expression_ja.md)を引数として使用します。

関数は`TestElement`オブジェクトではなく`ImageMatchResult`オブジェクトを返却します。

## 関数

| 関数                       | 説明                                 |
|:-------------------------|:-----------------------------------|
| findImage                | selectorにマッチする画像を検索します（現在の画面）      |
| findImageWithScrollDown  | selectorにマッチする画像を検索します（下方向スクロールあり） |
| findImageWithScrollUp    | selectorにマッチする画像を検索します（上方向スクロールあり） |
| findImageWithScrollRight | selectorにマッチする画像を検索します（右方向スクロールあり） |
| findImageWithScrollLeft  | selectorにマッチする画像を検索します（左方向スクロールあり） |

## 例1

### FindImage1_prepare.kt

(`kotlin/tutorial/basic/FindImage1_prepare.kt`)

`prepareImage()`を実行してテスト用の画像ファイルを作成します。

```kotlin
package tutorial.basic

import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import java.io.File
import java.nio.file.Path

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class FindImage1_prepare : UITest() {

    private fun TestElement.cropAndCopy(fileName: String, directory: Path = TestLog.directoryForLog): TestElement {

        this.cropImage(fileName)
        FileUtils.copyFile(
            directory.resolve(fileName).toFile(),
            File("unitTestConfig/android/androidSettings/screens/images/$fileName")
        )
        return this
    }

    @Test
    fun prepareImage() {

        scenario {
            condition {
                it.screenIs("[Android Settings Top Screen]")
            }.action {
                it.select("Network & internet").leftImage().cropAndCopy("Network & internet.png")
                it.selectWithScrollDown("Display").leftImage().cropAndCopy("Display.png")
                it.selectWithScrollDown("Tips & support").leftImage().cropAndCopy("Tips & support.png")
            }
        }
    }
}
```

アイコンがキャプチャされて画像ファイルとして切り出されます。

![](../../_images/crop_icon.png)

画像ファイルは`unitTestConfig/android/androidSettings/screens/images/`ディレクトリにコピーされます。

![](../../_images/prepare_image.png)

### FindImage1.kt

(`kotlin/tutorial/basic/FindImage1.kt`)

`findImage()`を実行して画像マッチングのデモを行います。

```kotlin
@Test
fun findImage() {

    scenario {
        case(1) {
            condition {
                it.screenIs("[Android Settings Top Screen]")
            }.action {
                it.findImage("Network & internet.png")
                it.findImageWithScrollDown("Display.png")
                it.findImageWithScrollDown("Tips & support.png")
                it.findImageWithScrollUp("Display.png")
                it.findImageWithScrollUp("Network & internet.png")
            }.expectation {
                it.exist("Network & internet.png")
                it.existWithScrollDown("Display.png")
                it.existWithScrollDown("Tips & support.png")
                it.existWithScrollUp("Display.png")
                it.existWithScrollUp("Network & internet.png")
            }
        }
    }
}
```

### 注意

参照 [Due to JRE encapsulation, low level data structures needed for fast conversion of BufferedImages are no longer accessible.](../../../troubleshooting/errors/dueToJREencapsulationLowLevelDataStructuresNeededForFastConversionOfBufferedImagesAreNoLongerAccessible.md)

### Link

- [index](../../../index_ja.md)
