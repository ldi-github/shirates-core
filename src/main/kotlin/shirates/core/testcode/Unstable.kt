package shirates.core.testcode

/**
 * Unstable
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Unstable(
    val message: String
) {
}