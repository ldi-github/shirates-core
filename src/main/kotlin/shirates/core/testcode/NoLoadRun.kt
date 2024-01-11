package shirates.core.testcode

/**
 * NoLoadRun
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class NoLoadRun(
    val description: String = ""
) {

}