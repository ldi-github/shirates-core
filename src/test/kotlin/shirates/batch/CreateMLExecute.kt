package task.createml

import shirates.core.vision.batch.CreateMLUtility

object CreateMLExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        CreateMLUtility.runLearning(force = true)
    }

}