package shirates.core.utility

import okhttp3.internal.trimSubstring
import shirates.core.driver.TestMode
import java.io.File
import java.nio.file.Path

/**
 * replacePathSeparators
 */
fun String.replacePathSeparators(): String {

    return if (TestMode.isRunningOnWindows) this.replace("/", File.separator)
    else this.replace("\\", File.separator)
}

/**
 * replaceUserHome
 */
fun String.replaceUserHome(): String {

    return this.replace("{userhome}", shirates.core.UserVar.USER_HOME)
}

/**
 * toPath
 */
fun String?.toPath(): Path {

    val text = (this ?: "").replaceUserHome()
    return Path.of(text).toAbsolutePath()
}

/**
 * toRelativePath
 * (from project root)
 */
fun String?.toRelativePath(): Path {

    val pathString = this.toPath().toString()
    if (pathString.contains(shirates.core.UserVar.PROJECT).not())
        throw IllegalArgumentException("Failed to convert the string to relative path from project root. (string=\"$this\", projectRoot=${shirates.core.UserVar.PROJECT})")
    val relPath = pathString.replace(shirates.core.UserVar.PROJECT, "").trimStart('/').trimStart('\\')
    return Path.of(relPath)
}

/**
 * escapeFileName
 */
fun String.escapeFileName(): String {

    var result = this.replace(File.separator, "_")
        .replace("\\", "_")
        .replace("/", "_")
        .replace(":", "")
        .replace("*", "_")
        .replace("?", "")
        .replace("\"", "_")
        .replace("<", "_")
        .replace(">", "_")
        .replace("|", "")
        .replace("\r\n", "_")
        .replace("\r", "_")
        .replace("\n", "_")

    if (result.length > 100) {
        result = result.trimSubstring(0, 100)
    }

    return result
}