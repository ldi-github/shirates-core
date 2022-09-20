package shirates.spec.code.entity.exntension

import shirates.core.customobject.CustomFunctionRepository
import shirates.spec.code.custom.DefaultTranslator
import shirates.spec.code.entity.Case
import shirates.spec.code.entity.Target

/**
 * escape
 */
internal fun String.escape(): String {

    val funcName = "escape"
    if (CustomFunctionRepository.hasFunction(funcName)) {
        return CustomFunctionRepository.call(funcName, this).toString()
    }
    return DefaultTranslator.escapeForCode(message = this)
}

/**
 * format
 */
internal fun String.format(): String {

    val funcName = "format"
    if (CustomFunctionRepository.hasFunction(funcName)) {
        return CustomFunctionRepository.call(funcName, this).toString()
    }
    return DefaultTranslator.formatArg(message = this)
}

/**
 * getSubject
 */
internal fun String.getSubject(): String {

    val funcName = "getSubject"
    if (CustomFunctionRepository.hasFunction(funcName)) {
        return CustomFunctionRepository.call(funcName, this).toString()
    }
    return DefaultTranslator.getSubject(message = this)
}

/**
 * getScreenNickName
 */
internal fun String.getScreenNickName(): String {

    val funcName = "getScreenNickName"
    if (CustomFunctionRepository.hasFunction(funcName)) {
        return CustomFunctionRepository.call(funcName, this).toString()
    }
    return DefaultTranslator.getScreenNickName(message = this)
}

/**
 * conditionMessageToFunction
 */
internal fun String.conditionMessageToFunction(
    case: Case,
    defaultFunc: String = "manual"
): String {

    val funcName = "conditionMessageToFunction"
    if (CustomFunctionRepository.hasFunction(funcName)) {
        return CustomFunctionRepository.call(funcName, case, this, defaultFunc).toString()
    }
    return DefaultTranslator.conditionMessageToFunction(case = case, message = this, defaultFunc = defaultFunc)
}

/**
 * actionMessageToFunction
 */
internal fun String.actionMessageToFunction(
    case: Case,
    defaultFunc: String = "manual"
): String {

    val funcName = "actionMessageToFunction"
    if (CustomFunctionRepository.hasFunction(funcName)) {
        return CustomFunctionRepository.call(funcName, case, this, defaultFunc).toString()
    }
    return DefaultTranslator.actionMessageToFunction(case = case, message = this, defaultFunc = defaultFunc)
}

internal fun String.targetToFunction(): String {

    val funcName = "targetToFunction"
    if (CustomFunctionRepository.hasFunction(funcName)) {
        return CustomFunctionRepository.call(funcName, this).toString()
    }
    return DefaultTranslator.targetToFunction(target = this)
}

/**
 * expectationMessageToFunction
 */
internal fun String.expectationMessageToFunction(
    target: Target? = null,
    defaultFunc: String = "manual"
): String {

    val funcName = "expectationMessageToFunction"
    if (CustomFunctionRepository.hasFunction(funcName)) {
        return CustomFunctionRepository.call(funcName, target, this, defaultFunc).toString()
    }
    return DefaultTranslator.expectationMessageToFunction(
        target = target,
        message = this,
        defaultFunc = defaultFunc
    )
}
