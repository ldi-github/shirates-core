package shirates.core.logging

/**
 * LogType
 */
enum class LogType(val label: String) {

    /**
     * --------------------------------------------------
     * Generic
     * --------------------------------------------------
     */

    /**
     * NONE
     */
    NONE("-"),

    /**
     * TRACE
     */
    TRACE("trace"),

    /**
     * INFO
     */
    INFO("info"),

    /**
     * WARN
     */
    WARN("WARN"),

    /**
     * ERROR
     */
    ERROR("ERROR"),

    /**
     * DELETED
     */
    DELETED("DELETED"),

    /**
     * IMPORTANT
     */
    IMPORTANT("IMPORTANT"),

    /**
     * --------------------------------------------------
     * for test results
     * --------------------------------------------------
     */

    /**
     * OK（for EXPECTATION）
     */
    OK("OK"),

    /**
     * ok(for CONDITION, ACTION)
     */
    ok("ok"),

    /**
     * NG
     */
    NG("NG"),

    /**
     * MANUAL
     */
    MANUAL("MANUAL"),

    /**
     * COND_AUTO
     */
    COND_AUTO("COND_AUTO"),

    /**
     * SKIP_SCENARIO
     */
    SKIP_SCENARIO("SKIP_SCENARIO"),

    /**
     * SKIP_CASE
     */
    SKIP_CASE("SKIP_CASE"),

    /**
     * SKIP
     */
    SKIP("SKIP"),

    /**
     * NOTIMPL
     */
    NOTIMPL("NOTIMPL"),

    /**
     * KNOWNISSUE
     */
    KNOWNISSUE("KNOWNISSUE"),

    /**
     * --------------------------------------------------
     * for test description
     * --------------------------------------------------
     */

    /**
     * SCENARIO
     */
    SCENARIO("SCENARIO"),

    /**
     * CASE
     */
    CASE("CASE"),

    /**
     * CONDITION
     */
    CONDITION("CONDITION"),

    /**
     * ACTION
     */
    ACTION("ACTION"),

    /**
     * TARGET
     */
    TARGET("TARGET"),

    /**
     * EXPECTATION
     */
    EXPECTATION("EXPECTATION"),

    /**
     * SELECT
     */
    SELECT("select"),

    /**
     * BOOLEAN
     */
    BOOLEAN("boolean"),

    /**
     * BRANCH
     */
    BRANCH("branch"),

    /**
     * OPERATE
     */
    OPERATE("operate"),

    /**
     * SCREENSHOT
     */
    SCREENSHOT("screenshot"),

    /**
     * CHECK
     */
    CHECK("check"),

    /**
     * PROCEDURE
     */
    PROCEDURE("procedure"),

    /**
     * SILENT
     */
    SILENT("silent"),

    /**
     * WITHSCROLL
     */
    WITHSCROLL("withscroll"),

    /**
     * CAPTION
     */
    CAPTION("caption"),

    /**
     * DESCRIBE
     */
    DESCRIBE("describe"),

    /**
     * OUTPUT
     */
    OUTPUT("output"),

    /**
     * COMMENT
     */
    COMMENT("comment"),
    ;

    /**
     * isCaePatternType
     */
    val isCaePatternType: Boolean
        get() {
            return CAEPatternTypes.contains(this)
        }

    /**
     * isOKType
     */
    val isOKType: Boolean
        get() {
            return OKTypes.contains(this)
        }

    /**
     * isEffectiveType
     */
    val isEffectiveType: Boolean
        get() {
            return EffectiveTypes.contains(this)
        }

    /**
     * isFailType
     */
    val isFailType: Boolean
        get() {
            return FailTypes.contains(this)
        }

    /**
     * isSkipType
     */
    val isSkipType: Boolean
        get() {
            return SkipTypes.contains(this)
        }

    /**
     * isInconclusiveType
     */
    val isInconclusiveType: Boolean
        get() {
            return InconclusiveTypes.contains(this)
        }

    companion object {

        /**
         * AllTypes
         */
        val AllTypes: List<LogType> = mutableListOf(

            /**
             * Generic
             */
            NONE,
            TRACE,
            INFO,
            WARN,
            ERROR,

            /**
             * Test Result
             */
            OK,
            ok,
            NG,
            MANUAL,
            SKIP,
            NOTIMPL,
            KNOWNISSUE,

            /**
             * Test Description
             */
            SCENARIO,
            CASE,
            CONDITION,
            ACTION,
            TARGET,
            EXPECTATION,
            SELECT,
            BOOLEAN,
            BRANCH,
            SKIP_SCENARIO,
            SKIP_CASE,
            OPERATE,
            SCREENSHOT,
            CHECK,
            PROCEDURE,
            SILENT,
            WITHSCROLL,
            CAPTION,
            DESCRIBE,
            OUTPUT,
            COMMENT,
        )

        /**
         * EffectiveTypes
         */
        val EffectiveTypes: List<LogType> = mutableListOf(
            ERROR,
            OK,
            NG,
            SKIP,
            MANUAL,
            NOTIMPL,
            KNOWNISSUE
        )

        /**
         * OKTypes
         */
        val OKTypes: List<LogType> = mutableListOf(
            OK
        )

        /**
         * FailTypes
         */
        val FailTypes: List<LogType> = mutableListOf(
            ERROR,
            NG
        )

        /**
         * SkipTypes
         */
        val SkipTypes: List<LogType> = mutableListOf(
            SKIP_SCENARIO,
            SKIP_CASE,
            SKIP
        )

        /**
         * InconclusiveTypes
         */
        val InconclusiveTypes: List<LogType> = mutableListOf(
            SKIP,
            MANUAL,
            NOTIMPL,
            KNOWNISSUE
        )

        /***
         * CAEPatternTypes
         */
        val CAEPatternTypes: List<LogType> = mutableListOf<LogType>(
            CONDITION,
            ACTION,
            EXPECTATION
        )

    }

}