package shirates.core.testcode

/**
 * Fail
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Fail(
    val message: String,
    val since: String = "",
    val platform: String = ""
) {

}

fun Fail.getMesssage(): String {

    val list = mutableListOf<String>()
    list.add(message)
    if (since.isNotBlank()) {
        list.add("since: $since")
    }
    if (platform.isNotBlank()) {
        list.add("platform: $platform")
    }
    val msg = list.joinToString(", ")
    return msg
}