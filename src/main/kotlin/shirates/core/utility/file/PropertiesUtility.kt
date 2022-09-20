package shirates.core.utility.file

import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

object PropertiesUtility {

    /**
     * getProperties
     */
    fun getProperties(propertiesFile: Path): Properties {

        if (Files.exists(propertiesFile).not()) {
            throw FileNotFoundException(propertiesFile.toString())
        }

        val file = propertiesFile.toFile()
        val reader = InputStreamReader(file.inputStream(), "UTF-8")
        val props = Properties()
        props.load(reader)
        return props
    }

}