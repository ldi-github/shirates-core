package shirates.core.driver

class ErrorEventArgs(
    val error: Throwable,
    var canceled: Boolean = false
) {

    val errorMessage: String
        get() {
            return error.message ?: error.javaClass.typeName
        }

    fun cancel() {
        canceled = true
    }
}