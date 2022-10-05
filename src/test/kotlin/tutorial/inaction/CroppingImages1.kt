package tutorial.inaction

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.rootElement
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.utility.toPath
import java.nio.file.Files
import java.nio.file.StandardCopyOption

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
                }.action {
                    rootElement.cropImage("[Alarm Screen].png")
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
                        .wait()
                }.action {
                    it.select("[Alarm Tab]").cropImage("Alarm.png")
                    it.select("[Clock Tab]").cropImage("Clock(selected).png")
                }
            }
            case(3) {
                action {
                    it.tap("[Timer Tab]")
                        .screenIs("[Timer Screen]")
                        .wait()     // wait for animation to complete
                }.action {
                    it.select("[Timer Tab]").cropImage("Timer(selected).png")
                }
            }
            case(4) {
                action {
                    it.tap("[Stopwatch Tab]")
                        .screenIs("[Stopwatch Screen]")
                        .wait()     // wait for animation to complete
                }.action {
                    it.select("[Stopwatch Tab]").cropImage("Stopwatch(selected).png")
                }
            }
            case(5) {
                action {
                    it.tap("[Bedtime Tab]")
                        .screenIs("[Bedtime Screen]")
                        .wait()     // wait for animation to complete
                }.action {
                    it.select("[Bedtime Tab]").cropImage("Bedtime(selected).png")
                }
            }

            copy("Alarm.png")
            copy("Alarm(selected).png")
            copy("Bedtime.png")
            copy("Bedtime(selected).png")
            copy("Clock.png")
            copy("Clock(selected).png")
            copy("Stopwatch.png")
            copy("Stopwatch(selected).png")
            copy("Timer.png")
            copy("Timer(selected).png")
        }
    }

    private fun copy(fileName: String) {

        val source = TestLog.directoryForLog.resolve(fileName)
        val target = "testConfig/android/clock/screens/images".toPath().resolve(fileName)
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
    }

}