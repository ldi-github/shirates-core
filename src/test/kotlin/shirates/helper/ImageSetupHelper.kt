package shirates.helper

import shirates.core.driver.*
import shirates.core.driver.branchextension.emulator
import shirates.core.driver.branchextension.realDevice
import shirates.core.driver.commandextension.*
import shirates.core.utility.image.saveImage
import shirates.core.utility.toPath
import java.nio.file.Files
import java.nio.file.Path

object ImageSetupHelper : TestDrive {

    fun TestElement.cropTo(dir: Path, fileName: String? = null) {
        val baseFileName = selector.toString()
        val suffix = testDrive.imageProfile
        val fname = fileName ?: "$baseFileName$suffix"
        this.cropImage()
            .lastCropInfo!!.croppedImage!!
            .saveImage(dir.resolve(fname).toString())
    }

    /**
     * Setup Image Android Settings Top Screen
     */
    fun setupImageAndroidSettingsTopScreen() {

        if (TestMode.isNoLoadRun) {
            return
        }

        val dir = "testConfig/android/androidSettings/screens/images/androidSettingsTopScreen".toPath()
        if(Files.exists(dir).not()) dir.toFile().mkdirs()

        it.macro("[Android Settings Top Screen]")

        withScrollDown {
            select("[Network & internet Icon]").cropTo(dir)
            select("[Connected devices Icon]").cropTo(dir)
            select("[Apps Icon]").cropTo(dir)
            select("[Notifications Icon]").cropTo(dir)
            select("[Battery Icon]").cropTo(dir)
            select("[Storage Icon]").cropTo(dir)
            select("[Sound & vibration Icon]").cropTo(dir)
            select("[Display Icon]").cropTo(dir)
            select("[Wallpaper & style Icon]").cropTo(dir)
            select("[Accessibility Icon]").cropTo(dir)
            select("[Security Icon]").cropTo(dir)
            select("[Privacy Icon]").cropTo(dir)
            select("[Location Icon]").cropTo(dir)
            select("[Safety & emergency Icon]").cropTo(dir)
            select("[Passwords & accounts Icon]").cropTo(dir)
            select("[Google Icon]").cropTo(dir)
            select("[System Icon]").cropTo(dir)
            realDevice {
                select("[About phone]").cropTo(dir)
            }
            emulator {
                select("[About emulated device Icon]").cropTo(dir)
            }
            select("[Tips & support Icon]").cropTo(dir)
        }
    }

    /**
     * Setup Image Android Settings Top Screen
     */
    fun setupImageAndroidSettingsNetworkAndInternetScreen() {

        if (TestMode.isNoLoadRun) {
            return
        }

        val dir = "testConfig/android/androidSettings/screens/images/androidSettingsNetworkAndInternetScreen".toPath()
        if (Files.exists(dir).not()) dir.toFile().mkdirs()

        it.macro("[Network & internet Screen]")

        withScrollDown {
            select("[Internet Icon]").cropTo(dir)
            select("[Calls & SMS Icon]").cropTo(dir)
            select("[SIMs Icon]").cropTo(dir)
            select("[Airplane mode Icon]").cropTo(dir)
            select("[Hotspot & tethering Icon]").cropTo(dir)
            select("[Data Saver Icon]").cropTo(dir)
            select("[VPN Icon]").cropTo(dir)
        }
    }

    /**
     * Setup Image Calendar Week Screen
     */
    fun setupImageCalendarWeekScreen() {

        val dir = "testConfig/android/calendar/screens/images/calendarWeekScreen".toPath()
        if (Files.exists(dir).not()) dir.toFile().mkdirs()

        fun TestElement.crop(fileName: String) {

            cropImage()
                .lastCropInfo!!.croppedImage!!
                .saveImage(dir.resolve(fileName).toString())
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
        val dir = "testConfig/android/androidSettings/screens/images".toPath()
        if(Files.exists(dir).not()) dir.toFile().mkdirs()
        it.select("[Network & internet]").cropTo(dir)
        it.selectWithScrollDown("[Display]").cropTo(dir)
        it.selectWithScrollDown("[Tips & support]").cropTo(dir)
    }

    fun croppingMapsImages() {

        it.macro("[Maps Top Screen]")
            .screenIs("[Maps Top Screen]")
            .wait()     // wait for animation to complete
        val dir = "testConfig/android/maps/screens/images".toPath()

        rootElement.cropImage("[Maps Top Screen].png")
        it.select("[Explore Tab]").cropTo(dir, "[Explore Tab Image(selected)].png")
        it.select("[Go Tab]").cropTo(dir,"[Go Tab Image].png")
        it.select("[Saved Tab]").cropTo(dir,"[Saved Tab Image].png")
        it.select("[Contribute Tab]").cropTo(dir, "[Contribute Tab Image].png")
        it.select("[Updates Tab]").cropTo(dir, "[Updates Tab Image].png")

        it.tap("[Go Tab]")
            .screenIs("[Maps Go Screen]")
            .wait()
        it.select("[Explore Tab]").cropTo(dir, "[Explore Tab Image].png")
        it.select("[Go Tab]").cropTo(dir, "[Go Tab Image(selected)].png")

        it.tap("[Saved Tab]")
            .wait()     // wait for animation to complete
        it.select("[Saved Tab]").cropTo(dir, "[Saved Tab Image(selected)].png")

        it.tap("[Contribute Tab]")
            .wait()     // wait for animation to complete
        it.select("[Contribute Tab]").cropTo(dir, "[Contribute Tab Image(selected)].png")

        it.tap("[Updates Tab]")
            .wait()     // wait for animation to complete
        it.select("[Updates Tab]").cropTo(dir, "[Updates Tab Image(selected)].png")
    }

}