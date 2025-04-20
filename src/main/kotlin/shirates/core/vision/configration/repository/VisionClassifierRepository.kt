package shirates.core.vision.configration.repository

import okio.FileNotFoundException
import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.printInfo
import shirates.core.utility.file.exists
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.toPath
import java.nio.file.Files
import java.security.MessageDigest
import kotlin.io.path.name

object VisionClassifierRepository {

    /**
     * classifierMap
     */
    val classifierMap = mutableMapOf<String, VisionClassifier>()

    /**
     * visionDirectory
     */
    var visionDirectory: String
        get() {
            if (_visionDirectory != null) return _visionDirectory!!
            return PropertiesManager.visionDirectory
        }
        set(value) {
            _visionDirectory = value
        }
    private var _visionDirectory: String? = null

    /**
     * createBinary
     */
    var createBinary: Boolean? = null

    /**
     * buildVisionDirectory
     */
    var buildVisionDirectory: String
        get() {
            if (_buildVisionDirectory != null) return _buildVisionDirectory!!
            return PropertiesManager.visionBuildDirectory.resolve("vision")
        }
        set(value) {
            _buildVisionDirectory = value
        }
    private var _buildVisionDirectory: String? = null

    /**
     * buildClassifiersDirectory
     */
    val buildClassifiersDirectory: String
        get() {
            return "$buildVisionDirectory/classifiers"
        }

    /**
     * visionClassifiersDirectory
     */
    val visionClassifiersDirectory: String
        get() {
            return visionDirectory.resolve("classifiers")
        }

    /**
     * defaultClassifier
     */
    val defaultClassifier: VisionClassifier
        get() {
            return getClassifier("DefaultClassifier")
        }

    /**
     * screenClassifier
     */
    val screenClassifier: VisionClassifier
        get() {
            return getClassifier("ScreenClassifier")
        }

    /**
     * buttonStateClassifier
     */
    val buttonStateClassifier: VisionClassifier
        get() {
            return getClassifier("ButtonStateClassifier")
        }

    /**
     * checkStateClassifier
     */
    val checkStateClassifier: VisionClassifier
        get() {
            return getClassifier("CheckStateClassifier")
        }

//    /**
//     * radioButtonStateClassifier
//     */

//    /**
//     * switchStateClassifier
//     */

    /**
     * clear
     */
    fun clear() {

        classifierMap.clear()
        visionDirectory = ""
    }

    /**
     * runLearning
     */
    fun runLearning(
        visionDirectory: String,
        classifierName: String,
        createBinary: Boolean?,
        force: Boolean = false,
    ) {
        this.visionDirectory = visionDirectory
        this.createBinary = createBinary

        /**
         * Setup classifier
         */
        val classifier = setupClassifier(
            classifierName = classifierName,
            createBinary = createBinary,
            force = force,
        )

        /**
         * Run learning
         */
        classifier.runLearning(force = force)
    }

    /**
     * setupClassifier
     */
    fun setupClassifier(
        classifierName: String,
        createBinary: Boolean?,
        force: Boolean,
        visionDirectory: String? = null,
        buildVisionDirectory: String? = null,
    ): VisionClassifier {
        this.createBinary = createBinary
        if (visionDirectory != null) {
            this.visionDirectory = visionDirectory
        }
        if (buildVisionDirectory != null) {
            this.buildVisionDirectory = buildVisionDirectory
        }
        if (this.visionDirectory.exists().not()) {
            throw FileNotFoundException("vision directory not found. (visionDirectory=$visionDirectory)")
        }
        if (Files.exists(buildClassifiersDirectory.toPath()).not()) {
            buildClassifiersDirectory.toFile().mkdirs()
        }

        if (classifierMap.containsKey(classifierName).not()) {
            classifierMap[classifierName] =
                VisionClassifier(classifierName = classifierName, visionClassifierRepository = this)
        }
        val classifier = classifierMap[classifierName]!!
        classifier.setup(force = force)

        return classifier
    }


    /**
     * hasClassifier
     */
    fun hasClassifier(classifierName: String): Boolean {

        return classifierMap.containsKey(classifierName)
    }

    /**
     * getClassifierNames
     */
    fun getClassifierNames(
        visionDirectory: String
    ): List<String> {

        val dirs = visionDirectory.resolve("classifiers").toPath().toFile().walkTopDown()
            .filter { it.name.endsWith("Classifier.swift") }.map { it.parentFile.toString() }.toList()
        return dirs.map { it.toPath().name }
    }

    /**
     * getClassifier
     */
    fun getClassifier(
        classifierName: String
    ): VisionClassifier {
        if (classifierMap.containsKey(classifierName).not()) {
            throw TestConfigException("Classifier not found. (classifierName=$classifierName, buildClassifiersDirectory=$buildClassifiersDirectory)")
        }
        return classifierMap[classifierName]!!
    }

    /**
     * getClassifiers
     */
    fun getClassifiers(): List<VisionClassifier> {

        return classifierMap.values.toList()
    }

    private fun printLastLearningResultOnWarning(
        classifierName: String
    ) {
        val logFile = buildClassifiersDirectory.resolve(classifierName).resolve("createML.log")
        if (logFile.exists()) {
            val content = logFile.toFile().readText()
            val accuracy100 = checkAccuracy(content = content)
            if (accuracy100.not()) {
                println(content)
            }
        }
    }

    internal fun checkAccuracy(content: String): Boolean {

        val accuracy100 = content.contains("Accuracy: 100.00%")
        if (accuracy100.not()) {
            printInfo("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            printInfo("!!                                                                  !!")
            printInfo("!! CAUTION!  Learning has a problem. Accuracy is not 100%.          !!")
            printInfo("!!                                                                  !!")
            printInfo("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }
        return accuracy100
    }

    /**
     * getShardNodeCount
     */
    fun getShardNodeCount(
        classifierName: String
    ): Int {

        val tokens = PropertiesManager.visionClassifierShardNodeCount.split(",", ";", ":")
        val nameValue = tokens.firstOrNull() { it.contains(classifierName) }?.split("=")
        if (nameValue == null) {
            throw TestConfigException("classifierName is missing. visionClassifierShardNodeCount is invalid. (classifiername: $classifierName, visionClassifierShardNodeCount: ${PropertiesManager.visionClassifierShardNodeCount})")
        }
        if (nameValue.count() < 2) {
            throw TestConfigException("visionClassifierShardNodeCount is invalid. (visionClassifierShardNodeCount: ${PropertiesManager.visionClassifierShardNodeCount})")
        }
        val value = nameValue[1].toIntOrNull()
        if (value == null) {
            throw TestConfigException("visionClassifierShardNodeCount is invalid. (visionClassifierShardNodeCount: ${PropertiesManager.visionClassifierShardNodeCount})")
        }

        return value
    }

    /**
     * getShardID
     */
    fun getShardID(
        classifierName: String,
        label: String,
    ): Int {

        val md5 = MessageDigest.getInstance("MD5")
        val hashBytes = md5.digest(label.toByteArray())
        val hashValue = hashBytes.fold(0) { acc, byte -> (acc shl 8) + (byte.toInt() and 0xFF) }
        val shardNodeCount = getShardNodeCount(classifierName = classifierName)
        return Math.abs(hashValue) % shardNodeCount + 1
    }

}