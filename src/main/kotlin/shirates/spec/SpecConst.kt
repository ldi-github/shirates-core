package shirates.spec

import java.text.SimpleDateFormat
import java.util.*

object SpecConst {

    val USER_HOME = System.getProperty("user.home")
    const val SPEC_BASE_NAME = "spec"

    /**
     * yyyy/MM/dd HH:mm:ss.SSS
     */
    val DATE_FORMAT = SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.ENGLISH)

    val SPEC_INPUT = "${USER_HOME}/Downloads/SpecInput"

    val CODEGEN_OUTPUT = "${USER_HOME}/Downloads/CodeOutput"

    val TEST_RESULTS = "${USER_HOME}/Downloads/TestResults"

}