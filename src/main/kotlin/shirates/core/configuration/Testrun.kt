package shirates.core.configuration

@Target(AnnotationTarget.CLASS)
annotation class Testrun(
    val testrunFile: String,
    val profile: String = "",
    val platform: String = "",
) {

}