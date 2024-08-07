package shirates.core.testcode

/**
 * environment
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Environment(
    val environment: String = ""
) {

}