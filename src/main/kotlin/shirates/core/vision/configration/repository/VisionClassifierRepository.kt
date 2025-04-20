package shirates.core.vision.configration.repository

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.file.exists
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.format
import shirates.core.utility.toPath
import java.nio.file.Files
import java.util.*
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
     * defaultClassifierRepository
     */
    val defaultClassifierRepository: VisionClassifier
        get() {
            return getClassifier("DefaultClassifier")
        }

    /**
     * screenClassifierRepository
     */
    val screenClassifierRepository: VisionClassifier
        get() {
            return getClassifier("ScreenClassifier")
        }

    /**
     * buttonStateClassifierRepository
     */
    val buttonStateClassifierRepository: VisionClassifier
        get() {
            return getClassifier("ButtonStateClassifier")
        }

    /**
     * checkStateClassifierRepository
     */
    val checkStateClassifierRepository: VisionClassifier
        get() {
            return getClassifier("CheckStateClassifier")
        }

//    /**
//     * radioButtonStateClassifierRepository
//     */
//    val radioButtonStateClassifierRepository: VisionClassifierRepository
//        get() {
//            return getRepository("RadioButtonStateClassifier")
//        }
//
//    /**
//     * switchStateClassifierRepository
//     */
//    val switchStateClassifierRepository: VisionClassifierRepository
//        get() {
//            return getRepository("SwitchStateClassifier")
//        }

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
         * Check if any file in the classifier directory is updated
         */
        val buildClassifierDirectory = buildClassifiersDirectory.resolve(classifierName)
        val fileListFile = buildClassifierDirectory.resolve("fileList.txt")
        val lastListString = if (fileListFile.exists()) fileListFile.toFile().readText() else ""
        val currentListString = getFileListInVisionClassifierDirectory(classifierName = classifierName)
        val doLearning = currentListString != lastListString || force
        if (doLearning.not()) {
            printLastLearningResultOnWarning(classifierName = classifierName)
            val classifierDirectory = visionClassifiersDirectory.resolve(classifierName)
            TestLog.info("Learning skipped. Updated file not found. (classifierDirectory=${classifierDirectory})")
            loadClassifier(classifierName = classifierName)
            return
        }

        /**
         * Setup classifier
         */
        val classifier = setupClassifier(classifierName = classifierName, createBinary = createBinary)

        /**
         * Run learning
         */
        classifier.runLearning()
    }

    /**
     * setupClassifier
     */
    fun setupClassifier(
        classifierName: String,
        createBinary: Boolean?
    ): VisionClassifier {
        this.createBinary = createBinary

        if (Files.exists(buildClassifiersDirectory.toPath()).not()) {
            buildClassifiersDirectory.toFile().mkdirs()
        }

        val classifier = VisionClassifier()
        classifier.setup(
            visionClassifierDirectory = visionClassifiersDirectory.resolve(classifierName),
            buildClassifierDirectory = buildClassifiersDirectory.resolve(classifierName),
            createBinary = createBinary
        )
        classifierMap[classifierName] = classifier
        return classifier
    }

    /**
     * loadClassifier
     */
    fun loadClassifier(
        classifierName: String,
    ): VisionClassifier {

        val classifier = VisionClassifier()
        classifier.loadLabelInfoMap(buildClassifierDirectory = buildClassifiersDirectory.resolve(classifierName))
        classifierMap[classifierName] = classifier

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
            throw TestConfigException("Classifier not found. (classifierName=$classifierName)")
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

    internal fun getFileListInVisionClassifierDirectory(
        classifierName: String
    ): String {

        val projectRoot = "".toPath().toString()
        val files = visionClassifiersDirectory.resolve(classifierName).toFile().walkTopDown()
            .filter { it.isFile && it.name != ".DS_Store" && it.name.endsWith(".txt").not() }
            .map {
                "${Date(it.lastModified()).format("yyyy/MM/dd HH:mm:ss.SSS")} ${
                    it.toString().removePrefix(projectRoot).trimStart('/')
                }"
            }
        val result = files.joinToString("\n")
        return result
    }

}