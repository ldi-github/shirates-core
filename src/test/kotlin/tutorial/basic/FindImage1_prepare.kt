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