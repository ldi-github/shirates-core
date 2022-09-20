package shirates.spec.utilily

import shirates.spec.SpecConst
import shirates.core.logging.TestLog
import shirates.core.utility.file.ResourceUtility
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
     * notApplicable
     */
    val notApplicable: String
        get() {
            return getString("result.notApplicable")
        }

    /**
     * suspended
     */
    val suspended: String
        get() {
            return getString("result.suspended")
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

}