package shirates.core.testcode

/**
 * Manual
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Manual(
    val description: String = ""
) {

}