package shirates.core.utility

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

/**
 * toRelativePath
 * (from project root)
 */
fun Path.toRelativePath(): Path {

    return this.toString().toRelativePath()
}

/**
 * listFiles
 */
fun Path.listFiles(): List<File> {

    return this.toFile().listFiles()?.toList() ?: mutableListOf()
}

/**
 * exists
 */
fun Path.exists(): Boolean {

    return Files.exists(this)
}

/**
 * replaceExtension
 */
fun Path.replaceExtension(extension: String): Path {

    return this.parent.resolve("${this.nameWithoutExtension}$extension")
}

/**
 * getSiblingPath
 */
fun Path.getSiblingPath(fileName: String): Path {

    val nameWithoutExtension = fileName.toPath().nameWithoutExtension
    return this.parent.resolve(nameWithoutExtension)
}

/**
 * deleteFilesNonRecursively
 */
fun Path.deleteFilesNonRecursively() {

    val files = this.listFiles().filter { it.isFile }
    for (file in files) {
        file.delete()
    }
}

