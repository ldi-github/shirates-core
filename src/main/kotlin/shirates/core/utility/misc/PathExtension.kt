package shirates.core.utility

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

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