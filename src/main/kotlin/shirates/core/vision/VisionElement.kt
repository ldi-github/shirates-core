package shirates.core.vision

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType

class VisionElement(
    var selector: Selector? = null,
    var lastError: Throwable? = null,
    var lastResult: LogType = LogType.NONE,
    var observation: VisionObservation? = null,
) : VisionDrive {


    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return observation == null
        }

    /**
     * bounds
     */
    val bounds: Bounds
        get() {
            return observation?.rect ?: Bounds.empty
        }

    /**
     * recognizeTextObservation
     */
    val recognizeTextObservation: RecognizeTextObservation?
        get() {
            return observation as? RecognizeTextObservation
        }

    /**
     * text
     */
    val text: String
        get() {
            return recognizeTextObservation?.text ?: return ""
        }

    companion object {

        /**
         * emptyElement means element was not found.
         */
        val emptyElement: VisionElement
            get() {
                return VisionElement()
            }
    }

    /**
     * subject
     */
    val subject: String
        get() {
//            if (altSubject.isNotBlank()) {
//                return altSubject
//            }

            if (selector == null && isEmpty) {
                return "(empty)"
            }
            var s = selector?.nickname
            if (s != null && s.isNotBlank()) {
                return s
            }

            s = selector?.toString()
            if (s != null && s.isNotBlank()) {
                if (CodeExecutionContext.isInCell) {
                    return s.split(">:", "]:").last()
                }
                return s
            }

            if (observation == null) {
                return "empty"
            }
            s = observation.toString()
            return s
        }

}