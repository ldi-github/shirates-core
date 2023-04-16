package shirates.core.testcode

import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.PropertiesManager
import kotlin.reflect.KClass

class TestRequiredContext(
    val isRequired: Boolean,
    val clazz: KClass<*>?
)

internal val ExtensionContext.requiredContext: TestRequiredContext
    get() {

        val context = this
        val annotation = context.priorityAnnotation()

        if (annotation == null && PropertiesManager.none.not()) {
            return TestRequiredContext(isRequired = false, clazz = null)

        } else if (annotation is Must && PropertiesManager.must.not()) {
            return TestRequiredContext(isRequired = false, clazz = Must::class)

        } else if (annotation is Should && PropertiesManager.should.not()) {
            return TestRequiredContext(isRequired = false, clazz = Should::class)

        } else if (annotation is Want && PropertiesManager.want.not()) {
            return TestRequiredContext(isRequired = false, clazz = Want::class)
        }

        return TestRequiredContext(isRequired = true, clazz = null)
    }

private fun ExtensionContext.priorityAnnotation(): Annotation? {

    val methodAnnotation =
        this.requiredTestMethod.annotations.firstOrNull { a -> (a is Must || a is Should || a is Want) }
    if (methodAnnotation != null) {
        return methodAnnotation
    }

    return this.requiredTestClass.annotations.firstOrNull { a -> (a is Must || a is Should || a is Want) }
}

internal fun <T : Annotation> ExtensionContext?.isAnnotated(
    annotation: KClass<T>,
): Boolean {

    return isMethodAnnotated(annotation) || isClassAnnotated(annotation)
}

@Suppress("UNCHECKED_CAST")
internal fun <T : Annotation> ExtensionContext?.getMethodAnnotation(
    annotation: KClass<T>,
): T? {

    val context = this ?: return null
    val qualifiedName = annotation.qualifiedName
    val first =
        context.requiredTestMethod.annotations.firstOrNull() { it?.annotationClass?.qualifiedName == qualifiedName }
    return first as T?
}

@Suppress("UNCHECKED_CAST")
internal fun <T : Annotation> ExtensionContext?.getClassAnnotation(
    annotation: KClass<T>,
): T? {

    val context = this ?: return null
    val qualifiedName = annotation.qualifiedName
    val first =
        context.requiredTestClass.annotations.firstOrNull() { it?.annotationClass?.qualifiedName == qualifiedName }
    return first as T?
}

internal fun <T : Annotation> ExtensionContext?.isMethodAnnotated(
    annotation: KClass<T>,
): Boolean {

    val context = this ?: return false

    try {
        val first = context.getMethodAnnotation(annotation)
        if (first != null) {
            return true
        }
    } catch (t: Throwable) {
        return false
    }
    return false
}

internal fun <T : Annotation> ExtensionContext?.isClassAnnotated(
    annotation: KClass<T>,
): Boolean {

    val context = this ?: return false

    try {
        val first = context.getClassAnnotation(annotation)
        if (first != null) {
            return true
        }
    } catch (t: Throwable) {
        return false
    }
    return false
}