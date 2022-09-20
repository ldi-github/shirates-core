package shirates.core.utility.image

import boofcv.io.image.UtilImageIO
import boofcv.struct.image.GrayF32
import shirates.core.utility.toPath
import java.io.FileNotFoundException
import java.nio.file.Files

object Gray32ImageUtility {

    /**
     * getImage
     */
    fun getImage(file: String): GrayF32 {

        if (Files.exists(file.toPath()).not()) {
            throw FileNotFoundException(file)
        }
        return UtilImageIO.loadImage(file, GrayF32::class.java)!!
    }

}