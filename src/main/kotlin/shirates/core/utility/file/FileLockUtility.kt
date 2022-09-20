package shirates.core.utility.file

import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.nio.file.Path
import java.nio.file.StandardOpenOption

object FileLockUtility {

    internal val map = mutableMapOf<String, FileLock>()

    /**
     * lock
     */
    fun <R> lockFile(filePath: Path, action: (filePath: Path) -> R): R {

        val lockFile = filePath.parent.resolve("${filePath.fileName}.lock")
        val key = lockFile.toString()
        if (map.containsKey(key)) {
            return action(filePath)
        }

        val channel: FileChannel
        try {
            channel = FileChannel.open(
                lockFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.DELETE_ON_CLOSE
            )
        } catch (t: Throwable) {
            return action(filePath)
        }

        val lock = channel.tryLock()
        if (lock == null) {
            return action(filePath)
        }

        map[key] = lock
        try {
            return action(filePath)
        } finally {
            try {
                lock.release()
            } catch (t: Throwable) {
                println(t)
            } finally {
                map.remove(key)
            }
        }

    }

}