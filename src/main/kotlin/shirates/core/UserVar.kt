package shirates.core

import shirates.core.utility.toPath
import java.nio.file.Path

object UserVar {

    /**
     * User directories
     */
    val USER_HOME: String = System.getProperty("user.home")
    val userHome = USER_HOME.toPath()

    val DOWNLOADS = userHome.resolve("Downloads").toString()
    val downloads = DOWNLOADS.toPath()

    val SHIRATES_PROJECT_DIR = "shirates.project.dir"

    val PROJECT: String
        get() {
            val shiratesProjectDir = System.getProperty(SHIRATES_PROJECT_DIR)
            if (shiratesProjectDir.isNullOrBlank()) {
                return System.getProperty("user.dir")
            }
            return shiratesProjectDir
        }
    val project: Path
        get() {
            return PROJECT.toPath()
        }

    val TEST_RESULTS = downloads.resolve("TestResults").toString()
    val testResults = TEST_RESULTS.toPath()
}