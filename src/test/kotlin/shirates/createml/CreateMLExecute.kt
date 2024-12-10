package task.createml

import shirates.core.vision.createml.CreateMLUtility

object CreateMLExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        CreateMLUtility.runLearning()
    }

}