package shirates.core.vision.configration.repository

import shirates.core.utility.file.exists
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.toPath

class VisionClassifier(
    val classifierName: String,
    val visionClassifierRepository: VisionClassifierRepository
) {

    /**
     * classifierShardsMap
     */
    val classifierShardsMap = mutableMapOf<Int, VisionClassifierShard>()

    /**
     * shardCount
     */
    val shardCount: Int
        get() {
            return visionClassifierRepository.getShardNodeCount(classifierName = classifierName)
        }

    /**
     * runLearning
     */
    fun runLearning(
        force: Boolean,
        setupOnly: Boolean = false,
    ) {
        /**
         * setup
         */
        val shardDirectories = getShardDirectories()
        val shardRecreateRequired = shardDirectories.count() != shardCount
        if (shardRecreateRequired) {
            val classifierDirectory = visionClassifierRepository.buildClassifiersDirectory.resolve(classifierName)
            val thisProjectDirectory = "".toPath().toString()
            if (classifierDirectory.contains(thisProjectDirectory)) {
                classifierDirectory.toFile().deleteRecursively()
            }
        }
        for (shardID in 1..shardCount) {
            val classifierShard = VisionClassifierShard(shardID = shardID, classifier = this)
            classifierShardsMap[shardID] = classifierShard
            classifierShard.setup(force = force || shardRecreateRequired)
        }
        val fileListFile = visionClassifierRepository.buildClassifiersDirectory.resolve("fileList.txt")
        if (fileListFile.exists()) {
            fileListFile.toFile().delete()
        }
        if (setupOnly) {
            return
        }
        /**
         * run
         */
        for (shardID in 1..shardCount) {
            val classifierShard = classifierShardsMap[shardID]!!
            classifierShard.runLearning()
        }
    }

    /**
     * getShardDirectories
     */
    fun getShardDirectories(): List<String> {

        val list = mutableListOf<String>()
        for (i in 1..100) {
            val directory = visionClassifierRepository.buildClassifiersDirectory.resolve("$classifierName/$i")
            if (directory.exists()) {
                list.add(directory)
            } else {
                break
            }
        }
        return list
    }

    /**
     * getLabelInfoMap
     */
    fun getLabelInfoMap(): Map<String, LabelFileInfo> {

        val list = classifierShardsMap.flatMap { it.value.getLabelInfoList() }.sortedBy { it.label }
        val map = mutableMapOf<String, LabelFileInfo>()
        for (info in list) {
            map[info.label] = info
        }
        return map
    }

    /**
     * getFile
     */
    fun getFile(label: String): String? {

        val shards = classifierShardsMap.values.sortedBy { it.shardID }
        for (shard in shards) {
            val file = shard.getFile(label)
            if (file != null) {
                return file
            }
        }
        return null
    }

    /**
     * getFiles
     */
    fun getFiles(label: String): List<String> {

        val shards = classifierShardsMap.values.sortedBy { it.shardID }
        for (shard in shards) {
            val files = shard.getFiles(label = label)
            if (files.any()) {
                return files
            }
        }
        return listOf()
    }
}