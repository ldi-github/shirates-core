package shirates.core.utility.file

import org.apache.commons.io.FileUtils
import shirates.core.utility.toPath
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path

/**
 * copyFileTo
 */
fun String.copyFileTo(
    destination: String
): String {

    val src = this.toPath().toFile()
    val dst = destination.toPath().toFile()
    FileUtils.copyFile(src, dst)

    return this
}

/**
 * copyFileTo
 */
fun String.copyFileTo(
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
    this.copyFileTo(destination)

    return this
}

/**
 * copyFileIntoDirectory
 */
fun String.copyFileIntoDirectory(
    directoryPath: Path
): String {

    val destination = directoryPath.resolve(this.toFile().name)
    this.copyFileTo(destination)

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

    if (this.isBlank()) {
        throw FileNotFoundException()
    }
    return Files.exists(this.toPath())
}

/**
 * parent
 */
fun String.parent(): String {

    return this.toPath().parent.toString()
}