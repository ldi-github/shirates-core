package shirates.core.task

import shirates.spec.code.model.CodeGenerationExecutor

object CodeGeneratorExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        CodeGenerationExecutor().execute()
    }
}

