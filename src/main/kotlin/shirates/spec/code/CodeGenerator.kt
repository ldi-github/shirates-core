package shirates.spec.code

import jdk.jshell.spi.ExecutionControl
import shirates.spec.code.model.CodeGenerationExecutor
import kotlin.system.exitProcess

fun main() {

    try {
        CodeGenerationExecutor().execute()
    } catch (u: ExecutionControl.UserException) {
        println("ERROR: ${u.message}")
        return
    } catch (t: Throwable) {
        println("ERROR: Unexpected error")
        println(t.printStackTrace())
        exitProcess(2)
    }

}