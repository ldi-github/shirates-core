package shirates.core.vision.configration.repository

class VisionClassifier(
    val classifierName: String,
    val visionClassifierRepository: VisionClassifierRepository
) {

    /**
     * classifierShards
     */
    val classifierShards = mutableListOf<VisionClassifierShard>()

    /**
     * shardCount
     */
    val shardCount: Int
        get() {
            return visionClassifierRepository.getShardNodeCount(classifierName = classifierName)
        }

    /**
     * setup
     */
    fun setup(force: Boolean) {

        for (shardID in 1..shardCount) {

            val classifierShard = VisionClassifierShard(shardID = shardID, classifier = this)
            classifierShards.add(classifierShard)
            classifierShard.setup(force = force)
        }
    }


    /**
     * runLearning
     */
    fun runLearning(
        force: Boolean
    ) {

        setup(force = force)

        for (shardID in 1..shardCount) {
            val classifierShard = classifierShards.firstOrNull { it.shardID == shardID }!!
            classifierShard.runLearning()
        }
    }

    /**
     * getLabelInfoMap
     */
    fun getLabelInfoMap(): Map<String, LabelFileInfo> {

        val list = classifierShards.flatMap { it.getLabelInfoList() }
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

        for (shard in classifierShards) {
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

        for (shard in classifierShards) {
            val files = shard.getFiles(label = label)
            if (files.any()) {
                return files
            }
        }
        return listOf()
    }
}