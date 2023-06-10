package shirates.core

import shirates.core.utility.toPath

object UserVar {

    /**
     * User directories
     */
    val USER_HOME: String = System.getProperty("user.home")
    val userHome = USER_HOME.toPath()

    val DOWNLOADS = userHome.resolve("Downloads").toString()
    val downloads = DOWNLOADS.toPath()

    val PROJECT = System.getProperty("user.dir")
    val project = PROJECT.toPath()

    val TEST_RESULTS = downloads.resolve("TestResults").toString()
    val testResults = TEST_RESULTS.toPath()
}