package shirates.spec.utilily

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.core.utility.file.ResourceUtility
import java.nio.file.Path


object ExcelUtility {

    /**
     * getWorkbook
     */
    fun getWorkbook(filePath: Path): XSSFWorkbook {

        fun xssfWorkbook(): XSSFWorkbook {
            return lockFile(filePath) {
                val f = filePath.toAbsolutePath().toString()
                val pkg = OPCPackage.open(f)
                val wb = XSSFWorkbook(pkg)
                return@lockFile wb
            }
        }

        try {
            return xssfWorkbook()
        } catch (t: Throwable) {
            val msg = message(id = "failedToOpenFile", file = "$filePath")
            TestLog.warn(msg)
            Thread.sleep(1000)
            try {
                return xssfWorkbook()
            } catch (t: Throwable) {
                throw Exception(msg, t)
            }
        }
    }

    /**
     * getWorkbook
     */
    fun getWorkbook(baseName: String, logLanguage: String = TestLog.logLanguage): XSSFWorkbook {

        return XSSFWorkbook(ResourceUtility.getResourceAsStream(fileName = baseName, logLanguage = logLanguage))
    }

    /**
     * copyCellStyle
     */
    fun copyCellStyle(workbook: Workbook, originalStyle: XSSFCellStyle): XSSFCellStyle {

        val newFont = workbook.createFont()
        with(newFont) {
            val o = originalStyle.font
            bold = o.bold
            color = o.color
            fontHeight = o.fontHeight
            fontName = o.fontName
            italic = o.italic
            strikeout = o.strikeout
            typeOffset = o.typeOffset
            underline = o.underline
        }

        val newStyle = workbook.createCellStyle() as XSSFCellStyle
        with(newStyle) {
            setFont(newFont)
            val o = originalStyle
            alignment = o.alignment
            borderBottom = o.borderBottom
            borderLeft = o.borderLeft
            borderRight = o.borderRight
            borderTop = o.borderTop
            dataFormat = o.dataFormat
            setFillBackgroundColor(o.fillBackgroundColorColor)
            setFillForegroundColor(o.fillForegroundColorColor)
            fillPattern = o.fillPattern
            hidden = o.hidden
            indention = o.indention
            locked = o.locked
            rotation = o.rotation
            verticalAlignment = o.verticalAlignment
            wrapText = o.wrapText
        }

        return newStyle
    }

}