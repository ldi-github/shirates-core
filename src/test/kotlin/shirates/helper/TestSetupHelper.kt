package shirates.helper

import shirates.core.driver.*
import shirates.core.driver.branchextension.emulator
import shirates.core.driver.branchextension.realDevice
import shirates.core.driver.commandextension.*
import shirates.core.logging.TestLog
import shirates.core.utility.image.saveImage
import java.io.File
import java.nio.file.Files

object TestSetupHelper : TestDrive {

    /**
     * Setup Image Android Settings Top Screen
     */
    fun setupImageAndroidSettingsTopScreen() {

        if (TestMode.isNoLoadRun) {
            return
        }

        val path = "images/androidSettingsTopScreen"
        val dir = TestLog.testResults.resolve(path)
        if (Files.exists(dir).not()) File(dir.toUri()).mkdirs()

        fun crop(nickname: String) {
            val suffix = testDrive.imageSizeProfile
            it.selectWithScrollDown(nickname)
                .cropImage()
                .lastCropInfo!!.croppedImage!!
                .saveImage(TestLog.testResults.resolve("$path/$nickname$suffix").toString())
        }

        it.macro("[Android Settings Top Screen]")

        crop("[Network & internet Icon]")
        crop("[Connected devices Icon]")
        crop("[Apps Icon]")
        crop("[Notifications Icon]")
        crop("[Battery Icon]")
        crop("[Storage Icon]")
        crop("[Sound & vibration Icon]")
        crop("[Display Icon]")
        crop("[Wallpaper & style Icon]")
        crop("[Accessibility Icon]")
        crop("[Security Icon]")
        crop("[Privacy Icon]")
        crop("[Location Icon]")
        crop("[Safety & emergency Icon]")
        crop("[Passwords & accounts Icon]")
        crop("[Google Icon]")
        crop("[System Icon]")
        realDevice {
            crop("[About phone]")
        }
        emulator {
            crop("[About emulated device Icon]")
        }
        crop("[Tips & support Icon]")
    }

    /**
     * Setup Image Calendar Week Screen
     */
    fun setupImageCalendarWeekScreen() {

        val path = "images/calendarWeekScreen"
        val dir = TestLog.testResults.resolve(path)
        if (Files.exists(dir).not()) File(dir.toUri()).mkdirs()

        fun TestElement.crop(fileName: String) {

            cropImage()
                .lastCropInfo!!.croppedImage!!
                .saveImage(TestLog.testResults.resolve("$path/$fileName").toString())
        }

        fun TestElement.cropItems(dayN: String): List<TestElement> {

            val items = this.children.filter { it.contentDesc.contains("Open Schedule View") }
            for (i in 1..items.count()) {
                items[i - 1].crop("[Day$dayN-$i].png")
            }
            return items
        }

        it.macro("[Calendar Week Screen]")
        it.select(".android.support.v7.widget.RecyclerView") {
            cropItems("1")
        }
        it.flickToLeft()
        it.select(".android.support.v7.widget.RecyclerView") {
            cropItems("2")
        }
        it.flickToLeft()
        it.select(".android.support.v7.widget.RecyclerView") {
            cropItems("3")
        }
    }

}