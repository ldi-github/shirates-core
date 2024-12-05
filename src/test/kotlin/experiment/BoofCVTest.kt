package experiment

import boofcv.alg.filter.binary.GThresholdImageOps
import boofcv.gui.ListDisplayPanel
import boofcv.gui.binary.VisualizeBinaryData
import boofcv.gui.image.ShowImages
import boofcv.io.image.ConvertBufferedImage
import boofcv.io.image.UtilImageIO
import boofcv.struct.ConfigLength
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayU8
import org.junit.jupiter.api.Test

class BoofCVTest {

    @Test
    fun threthold() {

        val imageName = "unitTestData/files/srvision/android/[Android Settings Screen].png"
        val image = UtilImageIO.loadImageNotNull(imageName)


        // convert into a usable format
        val input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32::class.java)
        val binary = GrayU8(input.width, input.height)


        // Display multiple images in the same window
        val gui = ListDisplayPanel()


        // Global Methods
//        GThresholdImageOps.threshold(input, binary, ImageStatistics.mean(input).toDouble(), true)
//        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Global: Mean")
//        GThresholdImageOps.threshold(input, binary, GThresholdImageOps.computeOtsu(input, 0.0, 255.0), true)
//        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Global: Otsu")
//        GThresholdImageOps.threshold(input, binary, GThresholdImageOps.computeEntropy(input, 0.0, 255.0), true)
//        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Global: Entropy")


        // Local method
        GThresholdImageOps.localMean(input, binary, ConfigLength.fixed(57.0), 1.0, true, null, null, null)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Local: Mean")
        GThresholdImageOps.localGaussian(input, binary, ConfigLength.fixed(85.0), 1.0, true, null, null)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Local: Gaussian")
        GThresholdImageOps.localNiblack(input, binary, ConfigLength.fixed(11.0), 0.30f, true)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Local: Niblack")
        GThresholdImageOps.localSauvola(input, binary, ConfigLength.fixed(11.0), 0.30f, true)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Local: Sauvola")
        GThresholdImageOps.localWolf(input, binary, ConfigLength.fixed(11.0), 0.30f, true)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Local: Wolf")
        GThresholdImageOps.localNick(input, binary, ConfigLength.fixed(11.0), -0.2f, true)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Local: NICK")
        GThresholdImageOps.blockMinMax(input, binary, ConfigLength.fixed(21.0), 1.0, true, 15.0)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Block: Min-Max")
        GThresholdImageOps.blockMean(input, binary, ConfigLength.fixed(21.0), 1.0, true)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Block: Mean")
        GThresholdImageOps.blockOtsu(input, binary, false, ConfigLength.fixed(21.0), 0.5, 1.0, true)
        gui.addImage(VisualizeBinaryData.renderBinary(binary, false, null), "Block: Otsu")

        gui.addImage(ConvertBufferedImage.convertTo(input, null), "Input Image")

        val fileName: String = imageName.substring(imageName.lastIndexOf('/') + 1)
        ShowImages.showWindow(gui, fileName)

        do {
            Thread.sleep(1000)
        } while (true)
    }
}