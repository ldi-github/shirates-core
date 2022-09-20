package shirates.spec.utilily

import org.apache.poi.openxml4j.opc.OPCPackage
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

}