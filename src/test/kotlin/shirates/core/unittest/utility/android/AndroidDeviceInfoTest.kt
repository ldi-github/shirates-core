package shirates.core.unittest.utility.android

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.android.AndroidDeviceInfo

class AndroidDeviceInfoTest {

    @Test
    fun initTest() {

        /**
         * emulator
         */
        run {
            val a =
                AndroidDeviceInfo(line = "emulator-5554          device product:sdk_gphone64_arm64 model:sdk_gphone64_arm64 device:emulator64_arm64 transport_id:98")
            assertThat(a.udid).isEqualTo("emulator-5554")
            assertThat(a.port).isEqualTo("5554")
            assertThat(a.status).isEqualTo("device")

            assertThat(a.info).isEqualTo("product:sdk_gphone64_arm64 model:sdk_gphone64_arm64 device:emulator64_arm64 transport_id:98")
            assertThat(a.usb).isEqualTo("")
            assertThat(a.product).isEqualTo("sdk_gphone64_arm64")
            assertThat(a.model).isEqualTo("sdk_gphone64_arm64")
            assertThat(a.device).isEqualTo("emulator64_arm64")
            assertThat(a.transportId).isEqualTo("98")

            assertThat(a.version).isEqualTo("")
            assertThat(a.pid).isEqualTo("")
            assertThat(a.psResult).isEqualTo("")
            assertThat(a.cmd).isEqualTo("")
            assertThat(a.avdName).isEqualTo("")
        }
        run {
            val a = AndroidDeviceInfo(line = "emulator-5554          offline transport_id:99")
            assertThat(a.udid).isEqualTo("emulator-5554")
            assertThat(a.port).isEqualTo("5554")
            assertThat(a.status).isEqualTo("offline")

            assertThat(a.info).isEqualTo("transport_id:99")
            assertThat(a.usb).isEqualTo("")
            assertThat(a.product).isEqualTo("")
            assertThat(a.model).isEqualTo("")
            assertThat(a.device).isEqualTo("")
            assertThat(a.transportId).isEqualTo("99")

            assertThat(a.version).isEqualTo("")
            assertThat(a.pid).isEqualTo("")
            assertThat(a.psResult).isEqualTo("")
            assertThat(a.cmd).isEqualTo("")
            assertThat(a.avdName).isEqualTo("")
        }

        /**
         * real device
         */
        run {
            val a =
                AndroidDeviceInfo(line = "93MAY0CY1M             device usb:17825792X product:sargo model:Pixel_3a device:sargo transport_id:72")
            assertThat(a.udid).isEqualTo("93MAY0CY1M")
            assertThat(a.port).isEqualTo("")
            assertThat(a.status).isEqualTo("device")

            assertThat(a.info).isEqualTo("usb:17825792X product:sargo model:Pixel_3a device:sargo transport_id:72")
            assertThat(a.usb).isEqualTo("17825792X")
            assertThat(a.product).isEqualTo("sargo")
            assertThat(a.model).isEqualTo("Pixel_3a")
            assertThat(a.device).isEqualTo("sargo")
            assertThat(a.transportId).isEqualTo("72")

            assertThat(a.version).isEqualTo("")
            assertThat(a.pid).isEqualTo("")
            assertThat(a.psResult).isEqualTo("")
            assertThat(a.cmd).isEqualTo("")
            assertThat(a.avdName).isEqualTo("")
        }

    }
}