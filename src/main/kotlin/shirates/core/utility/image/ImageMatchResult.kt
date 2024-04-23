package shirates.core.utility.image

import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.Bounds
import shirates.core.driver.TestDrive
import shirates.core.driver.TestElement
import java.awt.image.BufferedImage
import kotlin.math.absoluteValue

class ImageMatchResult(
    var result: Boolean,
    var templateSubject: String?,
    var x: Int = Int.MIN_VALUE,
    var y: Int = Int.MIN_VALUE,
    var score: Double = Double.MIN_VALUE,
    var scale: Double = 1.0,
    var threshold: Double = 0.0,
    var image: BufferedImage? = null,
    var templateImage: BufferedImage? = null,
    var templateImageFile: String? = null,
    var imageFileEntries: List<ImageFileRepository.ImageFileEntry> = mutableListOf()
) : TestDrive {
    override fun toString(): String {
        val file = templateImageFile ?: imageFileEntries.firstOrNull()?.filePath
        return "result=$result, scale=$scale, threshold=$threshold, x=$x, y=$y, score=${score.absoluteValue}, templateImageFile=$file"
    }

    fun getBounds(): Bounds {

        return Bounds(
            left = x,
            top = y,
            width = templateImage?.width ?: 0,
            height = templateImage?.height ?: 0
        )
    }

    fun toDummyElement(selector: Selector): TestElement {

        val de = TestElement.dummyElement
        de.imageMatchResult = this
        de.selector = selector
        de.propertyCache.putIfAbsent("bounds", getBounds().boundString)
        return de
    }
}
