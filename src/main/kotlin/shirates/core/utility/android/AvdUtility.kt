package shirates.core.utility.android

import org.apache.commons.io.FileUtils
import shirates.core.Const
import shirates.core.UserVar
import shirates.core.driver.testProfile
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.utility.android.AndroidDeviceUtility.escapeAvdName
import shirates.core.utility.file.exists
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.WaitUtility
import shirates.core.utility.toPath
import java.io.FileNotFoundException
import java.nio.file.Files
import java.security.MessageDigest

object AvdUtility {

    /**
     * setupAvdAndStartEmulator
     */
    fun setupAvdAndStartEmulator(
        sourceAvdName: String,
        newAvdName: String,
        androidId: String? = null,
        overwrite: Boolean = false,
        timeoutSeconds: Double = Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
        waitSecondsAfterStartup: Double = Const.DEVICE_WAIT_SECONDS_AFTER_STARTUP
    ): AndroidDeviceInfo {
        copy(
            sourceAvdName = sourceAvdName,
            newAvdName = newAvdName,
            overwrite = overwrite
        )
        val androidDeviceInfo = AndroidDeviceUtility.startEmulatorAndWaitDeviceReady(
            avdName = newAvdName,
            timeoutSeconds = timeoutSeconds,
            waitSecondsAfterStartup = waitSecondsAfterStartup
        )
        val newAndroidId = androidId ?: get8BytesSha256(input = newAvdName)
        setAndroidId(udid = androidDeviceInfo.udid, newAndroidId = newAndroidId)

        return androidDeviceInfo
    }

    /**
     * get8BytesSha256
     */
    fun get8BytesSha256(input: String): String {

        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        return hashBytes.take(8).joinToString("") { "%02x".format(it) }
    }

    /**
     * getAvdHome
     */
    fun getAvdHome(): String {

        return UserVar.userHome.resolve(".android/avd").toString()
    }

    /**
     * getAvdIni
     */
    fun getAvdIni(
        avdName: String
    ): String {

        val avdHome = getAvdHome()
        var avdIni = avdHome.resolve(avdName.escapeAvdName()) + ".ini"
        if (avdIni.exists()) {
            return avdIni
        }
        avdIni = avdHome.resolve(avdName.escapeAvdName().trimEnd('_')) + ".ini"
        return avdIni
    }

    /**
     * getPropertyValue
     */
    fun getPropertyValue(file: String, key: String): String {

        if (file.exists().not()) {
            throw FileNotFoundException("File not found. (file=$file)")
        }
        val content = file.toFile().readText()
        val value = content.split("\r\n", "\n").firstOrNull() { it.contains("$key=") }?.split("=")?.last()
            ?: throw TestDriverException("key not found in $file")
        return value
    }

    /**
     * getAvdDir
     */
    fun getAvdDir(
        avdName: String
    ): String? {

        val avdIni = getAvdIni(avdName = avdName)
        if (avdIni.exists().not()) {
            return null
        }
        val avdDir = getPropertyValue(file = avdIni, key = "path")
        return avdDir
    }

    /**
     * deleteAvd
     */
    fun deleteAvd(
        avdName: String,
    ) {
        AndroidDeviceUtility.shutdownEmulatorByAvdName(avdName)

        val targetAvdDir = getAvdDir(avdName)
        if (targetAvdDir != null) {
            FileUtils.deleteDirectory(targetAvdDir.toFile())
        }

        val targetIniFile = getAvdIni(avdName)
        Files.deleteIfExists(targetIniFile.toPath())
    }

    /**
     * copy
     */
    fun copy(
        sourceAvdName: String,
        newAvdName: String,
        overwrite: Boolean = false
    ) {
        if (sourceAvdName == newAvdName) {
            throw IllegalArgumentException("newAvdName is the same as sourceAvdName: $newAvdName")
        }

        val sourceIniFile = getAvdIni(avdName = sourceAvdName)
        if (sourceIniFile.exists().not()) {
            throw FileNotFoundException(".ini file not found. (avdName=$sourceAvdName)")
        }
        val sourceAvdDir = getAvdDir(avdName = sourceAvdName)!!
        if (sourceAvdDir.exists().not()) {
            throw FileNotFoundException(".avd directory not found. (avdName=$sourceAvdName)")
        }

        val avdHome = getAvdHome()
        val escapedAvdName = newAvdName.escapeAvdName().trimEnd('_')
        val newIniFile = avdHome.resolve("${escapedAvdName}.ini")
        val newAvdDir = avdHome.resolve("${escapedAvdName}.avd")

        if (sourceAvdDir == newAvdDir) {
            throw IllegalArgumentException("newAvdDir is the same as sourceAvdDir: $newAvdDir")
        }
        if (overwrite.not()) {
            if (newAvdDir.exists()) {
                throw IllegalArgumentException("AVD already exists. ($newAvdDir)")
            }
        }

        /**
         * Delete avd before copy
         */
        deleteAvd(newAvdName)
        /**
         * Copy
         */
        FileUtils.copyDirectory(sourceAvdDir.toFile(), newAvdDir.toFile())
        /**
         * Write .ini
         */
        run {
            val pathRel = getPropertyValue(file = sourceIniFile, key = "path.rel")
            val sourceIniContent = sourceIniFile.toFile().readText()
            val newIniContent = sourceIniContent
                .replace(sourceAvdDir, newAvdDir)
                .replace(pathRel, "avd/$escapedAvdName.avd")
            newIniFile.toFile().writeText(newIniContent)
        }
        /**
         * Rewrite config.ini
         */
        run {
            val newIniFile = newAvdDir.resolve("config.ini")
            var newIniContent = newIniFile.toFile().readText()
            newIniContent = newIniContent
                .replace(sourceAvdDir, newAvdDir)
                .replace(sourceAvdName, newAvdName)
            newIniFile.toFile().writeText(newIniContent)
        }
    }


    /**
     * getAndroidId
     */
    fun getAndroidId(
        udid: String = testProfile.udid
    ): String {

        val r = ShellUtility.executeCommand(
            "adb",
            "-s",
            udid,
            "shell",
            "settings",
            "get",
            "secure",
            "android_id"
        )
        if (r.hasError) {
            throw TestDriverException(message = r.resultString, cause = r.error)
        }
        return r.resultString
    }

    /**
     * setAndroidId
     */
    fun setAndroidId(
        udid: String = testProfile.udid,
        newAndroidId: String,
    ): String {

        TestLog.info("Setting androidId. (udid=$udid, newAndroidId=$newAndroidId)")

        var r: ShellUtility.ShellResult? = null
        fun action() {
            r = ShellUtility.executeCommand(
                "adb",
                "-s",
                udid,
                "shell",
                "settings",
                "put",
                "secure",
                "android_id",
                newAndroidId
            )
        }
        action()
        WaitUtility.doUntilTrue {
            action()
            val hasError = r!!.hasError
            if (hasError) {
                TestLog.info("Retrying setAndroidId. (udid=$udid, newAndroidId=$newAndroidId)")
            }
            hasError.not()
        }
        val result = r!!
        if (result.hasError) {
            throw TestDriverException(message = result.resultString, cause = result.error)
        }

        val actual = getAndroidId(udid = udid)
        if (actual != newAndroidId) {
            throw TestDriverException(message = "Failed to set android id. (udid=$udid, androidId=$newAndroidId, actual androidId=$actual)")
        }

        return result.resultString
    }

}