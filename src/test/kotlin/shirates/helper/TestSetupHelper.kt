package shirates.helper

import shirates.core.driver.*
import shirates.core.driver.branchextension.emulator
import shirates.core.driver.branchextension.realDevice
import shirates.core.driver.commandextension.*
import shirates.core.logging.TestLog
import shirates.core.utility.image.saveImage
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object TestSetupHelper : TestDrive {

    /**
     * Setup Image Android Settings Top Screen
     */
    fun setupImageAndroidSettingsTopScreen() {

        if (TestMode.isNoLoadRun) {
            return
        }

        val path = "testConfig/android/androidSettings/screens/images/androidSettingsTopScreen".toPath()

        fun crop(nickname: String) {
            val suffix = testDrive.imageProfile
            it.selectWithScrollDown(nickname)
                .cropImage()
                .lastCropInfo!!.croppedImage!!
                .saveImage(path.resolve("$nickname$suffix").toString())
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
     * Setup Image Android Settings Top Screen
     */
    fun setupImageAndroidSettingsNetworkAndInternetScreen() {

        if (TestMode.isNoLoadRun) {
            return
        }

        val path = "testConfig/android/androidSettings/screens/images/androidSettingsNetworkAndInternetScreen".toPath()
        val dir = TestLog.testResults.resolve(path)
        if (Files.exists(dir).not()) File(dir.toUri()).mkdirs()

        fun crop(nickname: String) {
            val suffix = testDrive.imageProfile
            it.selectWithScrollDown(nickname)
                .cropImage()
                .lastCropInfo!!.croppedImage!!
                .saveImage(path.resolve("$nickname$suffix").toString())
        }

        it.macro("[Network & internet Screen]")

        crop("[Internet Icon]")
        crop("[Calls & SMS Icon]")
        crop("[SIMs Icon]")
        crop("[Airplane mode Icon]")
        crop("[Hotspot & tethering Icon]")
        crop("[Data Saver Icon]")
        crop("[VPN Icon]")
    }

    /**
     * Setup Image Calendar Week Screen
     */
    fun setupImageCalendarWeekScreen() {

        val path = "testConfig/android/calendar/screens/images/calendarWeekScreen".toPath()
        val dir = TestLog.testResults.resolve(path)
        if (Files.exists(dir).not()) File(dir.toUri()).mkdirs()

        fun TestElement.crop(fileName: String) {

            cropImage()
                .lastCropInfo!!.croppedImage!!
                .saveImage(path.resolve(fileName).toString())
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

    fun croppingImagesInNetworkAndInternetScreen() {

        it.macro("[Android Settings Top Screen]")
        val d = "testConfig/android/androidSettings/screens/images"
        it.select("[Network & internet]").cropAndCopy("[Network & internet].png", directory = d)
        it.selectWithScrollDown("[Display]").cropAndCopy("[Display].png", directory = d)
        it.selectWithScrollDown("[Tips & support]").cropAndCopy("[Tips & support].png", directory = d)
    }

    fun croppingMapsImages() {

        it.macro("[Maps Top Screen]")
            .screenIs("[Maps Top Screen]")
            .wait()     // wait for animation to complete
        val d = "testConfig/android/maps/screens/images"

        rootElement.cropImage("[Maps Top Screen].png")
        it.select("[Explore Tab]").cropAndCopy("[Explore Tab Image(selected)].png", directory = d)
        it.select("[Go Tab]").cropAndCopy("[Go Tab Image].png", directory = d)
        it.select("[Saved Tab]").cropAndCopy("[Saved Tab Image].png", directory = d)
        it.select("[Contribute Tab]").cropAndCopy("[Contribute Tab Image].png", directory = d)
        it.select("[Updates Tab]").cropAndCopy("[Updates Tab Image].png", directory = d)

        it.tap("[Go Tab]")
            .screenIs("[Maps Go Screen]")
            .wait()
        it.select("[Explore Tab]").cropAndCopy("[Explore Tab Image].png", directory = d)
        it.select("[Go Tab]").cropAndCopy("[Go Tab Image(selected)].png", directory = d)

        it.tap("[Saved Tab]")
            .wait()     // wait for animation to complete
        it.select("[Saved Tab]").cropAndCopy("[Saved Tab Image(selected)].png", directory = d)

        it.tap("[Contribute Tab]")
            .wait()     // wait for animation to complete
        it.select("[Contribute Tab]").cropAndCopy("[Contribute Tab Image(selected)].png", directory = d)

        it.tap("[Updates Tab]")
            .wait()     // wait for animation to complete
        it.select("[Updates Tab]").cropAndCopy("[Updates Tab Image(selected)].png", directory = d)
    }

    private fun TestElement.cropAndCopy(fileName: String, directory: String) {

        this.cropImage(fileName = fileName)

        val targetDir = directory.toPath()
        if (Files.exists(targetDir).not()) {
            Files.createDirectory(targetDir)
        }
        val source = TestLog.directoryForLog.resolve(fileName)
        val target = targetDir.resolve(fileName)
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
    }

}