package shirates.core.testcode

/**
 * NoLoadRun
 */
@Deprecated("Use 'Manual' attribute instead")
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class NoLoadRun(
    val description: String = ""
) {

}