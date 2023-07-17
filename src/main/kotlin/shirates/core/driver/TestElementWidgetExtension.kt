package shirates.core.driver

import shirates.core.utility.element.ElementCategoryExpressionUtility

/**
 * classAlias
 */
val TestElement.classAlias: String
    get() {
        val alias = ElementCategoryExpressionUtility.getClassAlias(classOrType = classOrType)
        if (alias.isBlank()) {
            return ""
        }
        return ".$alias"
    }