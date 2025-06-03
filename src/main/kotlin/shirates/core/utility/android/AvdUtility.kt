package shirates.core.utility.android

import org.apache.commons.io.FileUtils
import shirates.core.Const
import shirates.core.UserVar
import shirates.core.driver.testProfile
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.utility.android.AndroidDeviceUtility.escapeAvdName
import shirates.core.utility.exists
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
        avdDir: String = getAvdDir(),
        overwrite: Boolean = false,
        timeoutSeconds: Double = Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
        waitSecondsAfterStartup: Double = Const.DEVICE_WAIT_SECONDS_AFTER_STARTUP
    ): AndroidDeviceInfo {
        copy(
            sourceAvdName = sourceAvdName,
            newAvdName = newAvdName,
            avdDir = avdDir,
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
     * getAvdDir
     */
    fun getAvdDir(): String {

        return UserVar.userHome.resolve(".android/avd").toString()
    }

    /**
     * deleteAvd
     */
    fun deleteAvd(
        avdName: String,
    ) {
        AndroidDeviceUtility.shutdownEmulatorByAvdName(avdName)

        val avdFileBase = avdName.escapeAvdName().trimEnd('_')
        val targetDir = getAvdDir().resolve("$avdFileBase.avd")
        if (Files.exists(targetDir.toPath())) {
            FileUtils.deleteDirectory(targetDir.toFile())
        }
        val targetIniFile = getAvdDir().resolve("$avdFileBase.ini")
        Files.deleteIfExists(targetIniFile.toPath())
    }

    /**
     * copy
     */
    fun copy(
        sourceAvdName: String,
        newAvdName: String,
        avdDir: String = getAvdDir(),
        overwrite: Boolean = false
    ) {
        if (sourceAvdName == newAvdName) {
            throw IllegalArgumentException("newAvdName is the same as sourceAvdName: $newAvdName")
        }
        if (avdDir.toPath().exists().not()) {
            throw FileNotFoundException("avdDir: $avdDir")
        }

        var sourceAvdFileBase = avdDir.toPath().resolve(sourceAvdName).toString().escapeAvdName()
        var sourceAvdDir = "$sourceAvdFileBase.avd"
        var sourceIniFile = "$sourceAvdFileBase.ini"

        if (sourceAvdDir.exists().not()) {
            sourceAvdFileBase = sourceAvdFileBase.trimEnd('_')
            sourceAvdDir = "$sourceAvdFileBase.avd"
            sourceIniFile = "$sourceAvdFileBase.ini"
            if (sourceAvdDir.exists().not()) {
                throw FileNotFoundException("$sourceAvdDir not found in $avdDir")
            }
        }
        if (sourceIniFile.exists().not()) {
            throw FileNotFoundException("$sourceIniFile not found in $avdDir")
        }

        val newAvdFileBase = avdDir.toPath().resolve(newAvdName).toString().escapeAvdName().trimEnd('_')
        val newAvdDir = "$newAvdFileBase.avd"

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
        val originalAvd = sourceAvdFileBase.toPath().fileName.toString()
        val newAvd = newAvdFileBase.toPath().fileName.toString()
        run {
            val newIniFile = "$newAvdFileBase.ini"
            val sourceIniContent = sourceIniFile.toFile().readText()
            val newIniContent = sourceIniContent
                .replace(originalAvd, newAvd)
            newIniFile.toFile().writeText(newIniContent)
        }
        /**
         * Rewrite config.ini
         */
        run {
            val sourceIniFile = sourceAvdDir.resolve("config.ini")
            val sourceIniContent = sourceIniFile.toFile().readText()
            val newIniContent = sourceIniContent
                .replace(originalAvd, newAvd)
                .replace(sourceAvdName, newAvdName)
            val newIniFile = newAvdDir.resolve("config.ini")
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