package shirates.core.vision.configration.repository

import shirates.core.driver.TestMode
import shirates.core.utility.toPath
import kotlin.io.path.name

class LabelFileInfo(
    val label: String,
) {
    val files: MutableList<LearningImageFileEntry> = mutableListOf()

    val primaryFile: String?
        get() {
            val annotation = TestMode.platformAnnotation
            val annotatedFile = files.firstOrNull() { it.learningImageFile.toPath().name.contains(annotation) }
            if (annotatedFile != null) {
                return annotatedFile.learningImageFile
            }
            return files.firstOrNull()?.learningImageFile
        }
}
