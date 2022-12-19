package tutorial.inaction

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.utility.toPath
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@Testrun("testConfig/android/androidSettings/testrun.properties")
class CroppingImages2 : UITest() {

    @Test
    fun croppingImages() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.select("[Network & internet]").cropAndCopy("[Network & internet].png")
                    it.selectWithScrollDown("[Display]").cropAndCopy("[Display].png")
                    it.selectWithScrollDown("[Tips & support]").cropAndCopy("[Tips & support].png")
                }
            }
        }
    }

    private fun TestElement.cropAndCopy(fileName: String) {

        this.cropImage(fileName = fileName)

        val targetDir = "testConfig/android/androidSettings/screens/images".toPath()
        if (Files.exists(targetDir).not()) {
            Files.createDirectory(targetDir)
        }
        val source = TestLog.directoryForLog.resolve(fileName)
        val target = targetDir.resolve(fileName)
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
    }

}