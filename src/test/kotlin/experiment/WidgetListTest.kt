package experiment

import org.junit.jupiter.api.Test
import shirates.core.UserVar
import shirates.core.driver.TestElementCache
import shirates.core.testcode.UnitTest
import shirates.core.utility.listFiles

class WidgetListTest : UnitTest() {

    @Test
    fun getList() {

        val inputDir = shirates.core.UserVar.downloads.resolve("sourcexml")
        val files = inputDir.listFiles().filter { it.name.endsWith(".xml") }
        for (file in files) {
            val text = file.readText()
            TestElementCache.loadXml(xmlData = text)
            val widgets = TestElementCache.allElements.filter { it.isWidget }
            for (w in widgets) {
                val textOrLabel = if ((w.text + w.label).isNotBlank()) " text=${w.text + w.label}" else ""
                val access = if (w.access.isNotBlank()) " access=${w.access}" else ""
                println(".${w.classOrType}$textOrLabel$access")
            }
        }

    }
}