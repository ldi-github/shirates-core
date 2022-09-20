package shirates.core.macro

/**
 * Macro
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Macro(val macroName: String) {
}