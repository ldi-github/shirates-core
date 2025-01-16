package shirates.core.utility.file

import org.apache.commons.io.FileUtils
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * copyFile
 */
fun String.copyFile(
    destination: String
): String {

    val src = this.toPath().toFile()
    val dst = destination.toPath().toFile()
    FileUtils.copyFile(src, dst)

    return this
}

/**
 * copyFile
 */
fun String.copyFile(
    destinationPath: Path
): String {

    val src = this.toPath().toFile()
    val dst = destinationPath.toFile()
    FileUtils.copyFile(src, dst)

    return this
}

/**
 * copyFileIntoDirectory
 */
fun String.copyFileIntoDirectory(
    directory: String
): String {

    val destination = directory.resolve(this.toFile().name)
    this.copyFile(destination)

    return this
}

/**
 * copyFileIntoDirectory
 */
fun String.copyFileIntoDirectory(
    directoryPath: Path
): String {

    val destination = directoryPath.resolve(this.toFile().name)
    this.copyFile(destination)

    return this
}

/**
 * resolve
 */
fun String.resolve(
    other: String
): String {

    return this.toPath().resolve(other).toString()
}

/**
 * toFile
 */
fun String.toFile(): File {

    return this.toPath().toFile()
}

/**
 * exists
 */
fun String.exists(): Boolean {

    return Files.exists(this.toPath())
}

/**
 * parent
 */
fun String.parent(): String {

    return this.toPath().parent.toString()
}