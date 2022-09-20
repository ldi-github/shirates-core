package shirates.core.configuration

import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message

class ImageInfo(val imageExpression: String?) {

    val OPTION_START = '?'
    val OPTION_DELIMITER = '&'

    var fileName: String = ""
        private set;

    var scale: Double = PropertiesManager.imageMatchingScale
        private set

    var threshold: Double = PropertiesManager.imageMatchingThreshold
        private set;

    val isImage: Boolean
        get() {
            return fileName.endsWith(".png")
        }

    init {
        val ex = TestConfigException(message(id = "invalidImageExpression", arg1 = imageExpression))

        if (imageExpression?.contains(".png") == true) {
            setup(imageExpression, ex)
        }
    }

    private fun setup(imageExpression: String, ex: TestConfigException) {
        val tokens = imageExpression.split(OPTION_START)
        fileName = tokens.first()
        val option = if (tokens.count() >= 2) tokens[1] else ""
        if (option.isNotBlank()) {
            val options = option.split(OPTION_DELIMITER)
            for (op in options) {
                if (op.startsWith("s=")) {
                    scale = op.substring("s=".length).toDoubleOrNull() ?: throw ex
                } else if (op.startsWith("scale=")) {
                    scale = op.substring("scale=".length).toDoubleOrNull() ?: throw ex
                } else if (op.startsWith("t=")) {
                    threshold = op.substring("t=".length).toDoubleOrNull() ?: throw ex
                } else if (op.startsWith("threshold=")) {
                    threshold = op.substring("threshold=".length).toDoubleOrNull() ?: throw ex
                }
            }
        }
    }

    override fun toString(): String {
        val list = mutableListOf<String>()
        if (scale != 1.0) {
            list.add("scale=$scale")
        }
        if (threshold != 0.0) {
            list.add("threshold=$threshold")
        }
        if (list.size == 0) {
            return fileName
        }

        val option = list.joinToString("$OPTION_DELIMITER")
        return "$fileName${OPTION_START}${option}"
    }
}