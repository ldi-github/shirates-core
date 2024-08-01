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
    fun getString(key: String): String {

        val r = specResource
        if (r.containsKey(key).not()) {
            throw IllegalAccessException("key not found in resource. key=$key")
        }

        return r.getProperty(key)
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