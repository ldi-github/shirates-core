package shirates.core.configuration

import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.utility.toPath
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path

class TestConfigContainer(val testConfigPattern: String) {

    val testConfigDirectory: Path
    val testConfigs = mutableListOf<TestConfig>()
    val profileMap = mutableMapOf<String, TestProfile>()

    companion object {

        var lastCreated: TestConfigContainer? = null
    }

    init {

        testConfigDirectory = File(testConfigPattern).parent.toPath()

        if (testConfigPattern.toList().count() { it == '*' } >= 2)
            throw IllegalArgumentException(message(id = "multipleWildcardNotAllowed", arg1 = testConfigPattern))

        if (Files.exists(testConfigDirectory).not())
            throw FileNotFoundException(message(id = "directoryNotFoundWithPattern", arg1 = testConfigPattern))

        val tokens = File(testConfigPattern).name.toString().split("*")
        val start = tokens.first()
        val end = if (tokens.count() == 1) "" else tokens.last()
        val files = testConfigDirectory.toFile().listFiles()
        val targetFiles = files!!.filter {
            it.name.startsWith(start) && it.name.endsWith(end) && it.name.endsWith(".json")
        }

        fun checkDuplication(endsWith: String) {

            val filteredFiles = targetFiles.map { it.name }.filter { it.endsWith(endsWith) }.sortedBy { it }
            if (filteredFiles.count() >= 2)
                throw TestConfigException(
                    message(
                        id = "multipleConfigurationFileNotAllowed",
                        arg1 = testConfigPattern,
                        arg2 = filteredFiles.joinToString(", ")
                    )
                )
        }
        checkDuplication("@a.json")
        checkDuplication("@i.json")

        for (file in targetFiles) {
            val testConfig = TestConfig(testConfigFile = file.toString())
            testConfigs.add(testConfig)
            for (profile in testConfig.profileMap) {
                if (profileMap.containsKey(profile.key) && profile.key != "_default") {
                    throw TestConfigException(
                        message(
                            id = "profileNameDuplicated",
                            arg1 = profile.key,
                            arg2 = testConfigPattern
                        )
                    )
                }
                profileMap[profile.key] = profile.value
            }
        }

        lastCreated = this
    }

    /**
     * getProfile
     */
    fun getProfile(profileName: String): TestProfile? {

        return if (profileMap.containsKey(profileName)) profileMap[profileName]
        else null
    }
}