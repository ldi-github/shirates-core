package shirates.spec.utilily

import shirates.core.logging.TestLog
import shirates.core.utility.file.ResourceUtility
import shirates.spec.SpecConst
import java.util.*

object SpecResourceUtility {

    /**
     * setup
     */
    fun setup(
        baseName: String = SpecConst.SPEC_BASE_NAME,
        logLanguage: String
    ) {
        specResrouceField = ResourceUtility.getProperties(baseName = baseName, logLanguage = logLanguage)
    }

    /**
     * specResource
     */
    val specResource: Properties
        get() {
            if (specResrouceField == null) {
                setup(logLanguage = TestLog.logLanguage)
            }
            return specResrouceField!!
        }
    private var specResrouceField: Properties? = null

    /**
     * getString
     */
    fun getString(key: String, throwsException: Boolean = true): String {

        val r = specResource
        if (r.containsKey(key).not()) {
            if (throwsException) {
                throw IllegalAccessException("key not found in resource. key=$key")
            }
            return ""
        }

        return r.getProperty(key)
    }

    /**
     * getAltResult
     */
    fun getAltResult(result: String): String {

        val key = "result.$result"
        val replace = getString(key, throwsException = false)
        if (replace.isNotBlank()) {
            return replace
        }
        return result
    }

    /**
     * getResultFromAltResult
     */
    fun getResultFromAltResult(altResult: String): String {

        val entries = specResource.map { it.key.toString() to it.value.toString() }
            .filter { it.first.startsWith("result.") && it.second == altResult }
        val key = entries.firstOrNull()?.first?.removePrefix("result.") ?: ""
        if (key.isNotEmpty()) {
            return key
        }

        val result = altResult
        val alt = getAltResult(result)
        if (alt.isNotBlank()) {
            return result
        }

        return ""
    }

    /**
     * bullet
     */
    val bullet: String
        get() {
            return getString("bullet")
        }

    /**
     * backslash
     */
    val backslash: String
        get() {
            return getString("backslash")
        }

    /**
     * isDisplayed
     */
    val isDisplayed: String
        get() {
            return getString("message.isDisplayed")
        }

    /**
     * noLoadRunMode
     */
    val noLoadRunMode: String
        get() {
            return getString("message.noLoadRunMode")
        }

    val OK: String
        get() {
            return getString("result.OK")
        }

    val NG: String
        get() {
            return getString("result.NG")
        }

    val ERROR: String
        get() {
            return getString("result.ERROR")
        }

    val SUSPENDED: String
        get() {
            return getString("result.SUSPENDED")
        }

    val NONE: String
        get() {
            return getString("result.NONE")
        }

    val COND_AUTO: String
        get() {
            return getString("result.COND_AUTO")
        }

    val MANUAL: String
        get() {
            return getString("result.MANUAL")
        }

    val SKIP: String
        get() {
            return getString("result.SKIP")
        }

    val NOTIMPL: String
        get() {
            return getString("result.NOTIMPL")
        }

    val EXCLUDED: String
        get() {
            return getString("result.EXCLUDED")
        }

    val DELETED: String
        get() {
            return getString("result.DELETED")
        }
}