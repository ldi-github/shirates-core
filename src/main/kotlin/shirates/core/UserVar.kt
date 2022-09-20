package shirates.core

import shirates.core.utility.toPath

object UserVar {

    /**
     * User directories
     */
    val USER_HOME: String = System.getProperty("user.home")
    val userHome = shirates.core.UserVar.USER_HOME.toPath()

    val DOWNLOADS = shirates.core.UserVar.userHome.resolve("Downloads").toString()
    val downloads = shirates.core.UserVar.DOWNLOADS.toPath()

    val PROJECT = System.getProperty("user.dir")
    val project = shirates.core.UserVar.PROJECT.toPath()

    val TEST_RESULTS = shirates.core.UserVar.downloads.resolve("TestResults").toString()
    val testResults = shirates.core.UserVar.TEST_RESULTS.toPath()
}