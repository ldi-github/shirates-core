package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Filter
import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.toPath
import java.io.FileNotFoundException

class FilterTest2 : UnitTest() {

    @Test
    fun text() {

        run {
            // Arrange, Act
            val filter = Filter("text=text1")
            // Assert
            assertThat(filter.name).isEqualTo("text")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("text1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("text=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("text1")
            assertThat(filter.evaluate("text1")).isEqualTo(true)
            assertThat(filter.evaluate("text2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("text!=text1")
            // Assert
            assertThat(filter.name).isEqualTo("text")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!text1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("text!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!text1")
            assertThat(filter.evaluate("text1")).isEqualTo(false)
            assertThat(filter.evaluate("text2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("text1")
            // Assert
            assertThat(filter.name).isEqualTo("text")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("text1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("text=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("text1")
            assertThat(filter.evaluate("text1")).isEqualTo(true)
            assertThat(filter.evaluate("text2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!text1")
            // Assert
            assertThat(filter.name).isEqualTo("text")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!text1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("text!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!text1")
            assertThat(filter.evaluate("text1")).isEqualTo(false)
            assertThat(filter.evaluate("text2")).isEqualTo(true)
        }
    }

    @Test
    fun textContains() {

        run {
            // Arrange, Act
            val filter = Filter("textContains=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textContains")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("*text1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textContains=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("*text1*")
            assertThat(filter.evaluate("---text1---")).isEqualTo(true)
            assertThat(filter.evaluate("---text2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("textContains!=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textContains")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!*text1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textContains!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!*text1*")
            assertThat(filter.evaluate("---text1---")).isEqualTo(false)
            assertThat(filter.evaluate("---text2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("*text1*")
            // Assert
            assertThat(filter.name).isEqualTo("textContains")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("*text1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textContains=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("*text1*")
            assertThat(filter.evaluate("---text1---")).isEqualTo(true)
            assertThat(filter.evaluate("---text2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!*text1*")
            // Assert
            assertThat(filter.name).isEqualTo("textContains")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!*text1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textContains!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!*text1*")
            assertThat(filter.evaluate("---text1---")).isEqualTo(false)
            assertThat(filter.evaluate("---text2---")).isEqualTo(true)
        }
    }

    @Test
    fun textStartsWith() {

        run {
            // Arrange, Act
            val filter = Filter("textStartsWith=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textStartsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("text1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textStartsWith=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("text1*")
            assertThat(filter.evaluate("text1---")).isEqualTo(true)
            assertThat(filter.evaluate("text2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("textStartsWith!=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textStartsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!text1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textStartsWith!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!text1*")
            assertThat(filter.evaluate("text1---")).isEqualTo(false)
            assertThat(filter.evaluate("text2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("text1*")
            // Assert
            assertThat(filter.name).isEqualTo("textStartsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("text1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textStartsWith=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("text1*")
            assertThat(filter.evaluate("text1---")).isEqualTo(true)
            assertThat(filter.evaluate("text2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!text1*")
            // Assert
            assertThat(filter.name).isEqualTo("textStartsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!text1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textStartsWith!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!text1*")
            assertThat(filter.evaluate("text1---")).isEqualTo(false)
            assertThat(filter.evaluate("text2---")).isEqualTo(true)
        }
    }

    @Test
    fun textEndsWith() {

        run {
            // Arrange, Act
            val filter = Filter("textEndsWith=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textEndsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("*text1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textEndsWith=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("*text1")
            assertThat(filter.evaluate("---text1")).isEqualTo(true)
            assertThat(filter.evaluate("---text2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("textEndsWith!=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textEndsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!*text1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textEndsWith!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!*text1")
            assertThat(filter.evaluate("---text1")).isEqualTo(false)
            assertThat(filter.evaluate("---text2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("*text1")
            // Assert
            assertThat(filter.name).isEqualTo("textEndsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("*text1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textEndsWith=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("*text1")
            assertThat(filter.evaluate("---text1")).isEqualTo(true)
            assertThat(filter.evaluate("---text2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!*text1")
            // Assert
            assertThat(filter.name).isEqualTo("textEndsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!*text1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textEndsWith!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!*text1")
            assertThat(filter.evaluate("---text1")).isEqualTo(false)
            assertThat(filter.evaluate("---text2")).isEqualTo(true)
        }
    }

    @Test
    fun textMatches() {

        run {
            // Arrange, Act
            val filter = Filter("textMatches=^value1\$")
            // Assert
            assertThat(filter.name).isEqualTo("textMatches")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Matches")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("^value1\$")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("textMatches=^value1\$")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textMatches=^value1\$")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("^value1\$")
            assertThat(filter.evaluate("value1")).isEqualTo(true)
            assertThat(filter.evaluate("value2")).isEqualTo(false)
        }
    }

    @Test
    fun id() {

        run {
            // Arrange, Act
            val filter = Filter("id=id1")
            // Assert
            assertThat(filter.name).isEqualTo("id")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#id1")
            assertThat(filter.evaluate("id1")).isEqualTo(true)
            assertThat(filter.evaluate("id2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("id!=id1")
            // Assert
            assertThat(filter.name).isEqualTo("id")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#id1")
            assertThat(filter.evaluate("id1")).isEqualTo(false)
            assertThat(filter.evaluate("id2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("#id1")
            // Assert
            assertThat(filter.name).isEqualTo("id")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#id1")
            assertThat(filter.evaluate("id1")).isEqualTo(true)
            assertThat(filter.evaluate("id2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!#id1")
            // Assert
            assertThat(filter.name).isEqualTo("id")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#id1")
            assertThat(filter.evaluate("id1")).isEqualTo(false)
            assertThat(filter.evaluate("id2")).isEqualTo(true)
        }
    }

    @Test
    fun idContains() {

        run {
            // Arrange, Act
            val filter = Filter("idContains=id1")
            // Assert
            assertThat(filter.name).isEqualTo("idContains")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#*id1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("idContains=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("#")
            assertThat(filter.abbreviationExpression).isEqualTo("#*id1*")
            assertThat(filter.evaluate("---id1---")).isEqualTo(true)
            assertThat(filter.evaluate("---id2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("idContains!=id1")
            // Assert
            assertThat(filter.name).isEqualTo("idContains")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#*id1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("idContains!=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("!#")
            assertThat(filter.abbreviationExpression).isEqualTo("!#*id1*")
            assertThat(filter.evaluate("---id1---")).isEqualTo(false)
            assertThat(filter.evaluate("---id2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("#*id1*")
            // Assert
            assertThat(filter.name).isEqualTo("idContains")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#*id1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("idContains=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("#")
            assertThat(filter.abbreviationExpression).isEqualTo("#*id1*")
            assertThat(filter.evaluate("---id1---")).isEqualTo(true)
            assertThat(filter.evaluate("---id2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!#*id1*")
            // Assert
            assertThat(filter.name).isEqualTo("idContains")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#*id1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("idContains!=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("!#")
            assertThat(filter.abbreviationExpression).isEqualTo("!#*id1*")
            assertThat(filter.evaluate("---id1---")).isEqualTo(false)
            assertThat(filter.evaluate("---id2---")).isEqualTo(true)
        }
    }

    @Test
    fun idStartsWith() {

        run {
            // Arrange, Act
            val filter = Filter("idStartsWith=id1")
            // Assert
            assertThat(filter.name).isEqualTo("idStartsWith")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#id1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("idStartsWith=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("#")
            assertThat(filter.abbreviationExpression).isEqualTo("#id1*")
            assertThat(filter.evaluate("id1---")).isEqualTo(true)
            assertThat(filter.evaluate("id2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("idStartsWith!=id1")
            // Assert
            assertThat(filter.name).isEqualTo("idStartsWith")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#id1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("idStartsWith!=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("!#")
            assertThat(filter.abbreviationExpression).isEqualTo("!#id1*")
            assertThat(filter.evaluate("id1---")).isEqualTo(false)
            assertThat(filter.evaluate("id2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("#id1*")
            // Assert
            assertThat(filter.name).isEqualTo("idStartsWith")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#id1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("idStartsWith=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("#")
            assertThat(filter.abbreviationExpression).isEqualTo("#id1*")
            assertThat(filter.evaluate("id1---")).isEqualTo(true)
            assertThat(filter.evaluate("id2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!#id1*")
            // Assert
            assertThat(filter.name).isEqualTo("idStartsWith")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#id1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("idStartsWith!=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("!#")
            assertThat(filter.abbreviationExpression).isEqualTo("!#id1*")
            assertThat(filter.evaluate("id1---")).isEqualTo(false)
            assertThat(filter.evaluate("id2---")).isEqualTo(true)
        }
    }

    @Test
    fun idEndsWith() {

        run {
            // Arrange, Act
            val filter = Filter("idEndsWith=id1")
            // Assert
            assertThat(filter.name).isEqualTo("idEndsWith")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#*id1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("idEndsWith=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("#")
            assertThat(filter.abbreviationExpression).isEqualTo("#*id1")
            assertThat(filter.evaluate("---id1")).isEqualTo(true)
            assertThat(filter.evaluate("---id2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("idEndsWith!=id1")
            // Assert
            assertThat(filter.name).isEqualTo("idEndsWith")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#*id1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("idEndsWith!=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("!#")
            assertThat(filter.abbreviationExpression).isEqualTo("!#*id1")
            assertThat(filter.evaluate("---id1")).isEqualTo(false)
            assertThat(filter.evaluate("---id2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("#*id1")
            // Assert
            assertThat(filter.name).isEqualTo("idEndsWith")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#*id1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("idEndsWith=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("#")
            assertThat(filter.abbreviationExpression).isEqualTo("#*id1")
            assertThat(filter.evaluate("---id1")).isEqualTo(true)
            assertThat(filter.evaluate("---id2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!#*id1")
            // Assert
            assertThat(filter.name).isEqualTo("idEndsWith")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#*id1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("idEndsWith!=id1")
            assertThat(filter.abbreviationOperator).isEqualTo("!#")
            assertThat(filter.abbreviationExpression).isEqualTo("!#*id1")
            assertThat(filter.evaluate("---id1")).isEqualTo(false)
            assertThat(filter.evaluate("---id2")).isEqualTo(true)
        }
    }

    @Test
    fun idMatches() {

        run {
            // Arrange, Act
            val filter = Filter("idMatches=^value1\$")
            // Assert
            assertThat(filter.name).isEqualTo("idMatches")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("Matches")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("^value1\$")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("idMatches=^value1\$")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("idMatches=^value1\$")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1")).isEqualTo(true)
            assertThat(filter.evaluate("value2")).isEqualTo(false)
        }
    }

    @Test
    fun access() {

        run {
            // Arrange, Act
            val filter = Filter("access=access1")
            // Assert
            assertThat(filter.name).isEqualTo("access")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@access1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("access=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@access1")
            assertThat(filter.evaluate("access1")).isEqualTo(true)
            assertThat(filter.evaluate("access2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("access!=access1")
            // Assert
            assertThat(filter.name).isEqualTo("access")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@access1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("access!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@access1")
            assertThat(filter.evaluate("access1")).isEqualTo(false)
            assertThat(filter.evaluate("access2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("@access1")
            // Assert
            assertThat(filter.name).isEqualTo("access")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@access1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("access=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@access1")
            assertThat(filter.evaluate("access1")).isEqualTo(true)
            assertThat(filter.evaluate("access2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!@access1")
            // Assert
            assertThat(filter.name).isEqualTo("access")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@access1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("access!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@access1")
            assertThat(filter.evaluate("access1")).isEqualTo(false)
            assertThat(filter.evaluate("access2")).isEqualTo(true)
        }
    }

    @Test
    fun accessContains() {

        run {
            // Arrange, Act
            val filter = Filter("accessContains=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessContains")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@*access1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessContains=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@*access1*")
            assertThat(filter.evaluate("---access1---")).isEqualTo(true)
            assertThat(filter.evaluate("---access2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("accessContains!=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessContains")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@*access1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessContains!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@*access1*")
            assertThat(filter.evaluate("---access1---")).isEqualTo(false)
            assertThat(filter.evaluate("---access2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("@*access1*")
            // Assert
            assertThat(filter.name).isEqualTo("accessContains")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@*access1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessContains=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@*access1*")
            assertThat(filter.evaluate("---access1---")).isEqualTo(true)
            assertThat(filter.evaluate("---access2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!@*access1*")
            // Assert
            assertThat(filter.name).isEqualTo("accessContains")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@*access1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessContains!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@*access1*")
            assertThat(filter.evaluate("---access1---")).isEqualTo(false)
            assertThat(filter.evaluate("---access2---")).isEqualTo(true)
        }
    }

    @Test
    fun accessStartsWith() {

        run {
            // Arrange, Act
            val filter = Filter("accessStartsWith=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessStartsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@access1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessStartsWith=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@access1*")
            assertThat(filter.evaluate("access1---")).isEqualTo(true)
            assertThat(filter.evaluate("access2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("accessStartsWith!=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessStartsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@access1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessStartsWith!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@access1*")
            assertThat(filter.evaluate("access1---")).isEqualTo(false)
            assertThat(filter.evaluate("access2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("@access1*")
            // Assert
            assertThat(filter.name).isEqualTo("accessStartsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@access1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessStartsWith=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@access1*")
            assertThat(filter.evaluate("access1---")).isEqualTo(true)
            assertThat(filter.evaluate("access2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!@access1*")
            // Assert
            assertThat(filter.name).isEqualTo("accessStartsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@access1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessStartsWith!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@access1*")
            assertThat(filter.evaluate("access1---")).isEqualTo(false)
            assertThat(filter.evaluate("access2---")).isEqualTo(true)
        }
    }

    @Test
    fun accessEndsWith() {

        run {
            // Arrange, Act
            val filter = Filter("accessEndsWith=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessEndsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@*access1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessEndsWith=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@*access1")
            assertThat(filter.evaluate("---access1")).isEqualTo(true)
            assertThat(filter.evaluate("---access2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("accessEndsWith!=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessEndsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@*access1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessEndsWith!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@*access1")
            assertThat(filter.evaluate("---access1")).isEqualTo(false)
            assertThat(filter.evaluate("---access2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("@*access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessEndsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@*access1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessEndsWith=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@*access1")
            assertThat(filter.evaluate("---access1")).isEqualTo(true)
            assertThat(filter.evaluate("---access2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!@*access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessEndsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@*access1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessEndsWith!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@*access1")
            assertThat(filter.evaluate("---access1")).isEqualTo(false)
            assertThat(filter.evaluate("---access2")).isEqualTo(true)
        }
    }

    @Test
    fun value() {

        run {
            // Arrange, Act
            val filter = Filter("value=value1")
            // Assert
            assertThat(filter.name).isEqualTo("value")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("value=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("value=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1")).isEqualTo(true)
            assertThat(filter.evaluate("value=value2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("value!=value1")
            // Assert
            assertThat(filter.name).isEqualTo("value")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("value!=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("value!=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1")).isEqualTo(false)
            assertThat(filter.evaluate("value=value2")).isEqualTo(true)
        }
    }

    @Test
    fun valueContains() {

        run {
            // Arrange, Act
            val filter = Filter("valueContains=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueContains")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("valueContains=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueContains=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("---value1---")).isEqualTo(true)
            assertThat(filter.evaluate("---value2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("valueContains!=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueContains")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("valueContains!=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueContains!=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("---value1---")).isEqualTo(false)
            assertThat(filter.evaluate("---value2---")).isEqualTo(true)
        }
    }

    @Test
    fun valueStartsWith() {

        run {
            // Arrange, Act
            val filter = Filter("valueStartsWith=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueStartsWith")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("valueStartsWith=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueStartsWith=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1---")).isEqualTo(true)
            assertThat(filter.evaluate("value2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("valueStartsWith!=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueStartsWith")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("valueStartsWith!=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueStartsWith!=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1---")).isEqualTo(false)
            assertThat(filter.evaluate("value2---")).isEqualTo(true)
        }
    }

    @Test
    fun valueEndsWith() {

        run {
            // Arrange, Act
            val filter = Filter("valueEndsWith=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueEndsWith")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("valueEndsWith=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueEndsWith=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("---value1")).isEqualTo(true)
            assertThat(filter.evaluate("---value2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("valueEndsWith!=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueEndsWith")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("valueEndsWith!=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueEndsWith!=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("---value1")).isEqualTo(false)
            assertThat(filter.evaluate("---value2")).isEqualTo(true)
        }
    }

    @Test
    fun focusable() {

        run {
            // Arrange, Act
            val filter = Filter("focusable=true")
            assertThat(filter.name).isEqualTo("focusable")
            assertThat(filter.noun).isEqualTo("focusable")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("true")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("focusable=true")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("focusable=true")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("true")).isEqualTo(true)
            assertThat(filter.evaluate("false")).isEqualTo(false)
            assertThat(filter.evaluate("a")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("focusable!=true")
            assertThat(filter.name).isEqualTo("focusable")
            assertThat(filter.noun).isEqualTo("focusable")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("true")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("focusable!=true")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("focusable!=true")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("true")).isEqualTo(false)
            assertThat(filter.evaluate("false")).isEqualTo(true)
            assertThat(filter.evaluate("a")).isEqualTo(true)
        }
    }

    @Test
    fun selected() {

        run {
            // Arrange, Act
            val filter = Filter("selected=true")
            assertThat(filter.name).isEqualTo("selected")
            assertThat(filter.noun).isEqualTo("selected")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("true")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("selected=true")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("selected=true")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("true")).isEqualTo(true)
            assertThat(filter.evaluate("false")).isEqualTo(false)
            assertThat(filter.evaluate("a")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("selected!=true")
            assertThat(filter.name).isEqualTo("selected")
            assertThat(filter.noun).isEqualTo("selected")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("true")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("selected!=true")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("selected!=true")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("true")).isEqualTo(false)
            assertThat(filter.evaluate("false")).isEqualTo(true)
            assertThat(filter.evaluate("a")).isEqualTo(true)
        }
    }

    @Test
    fun scrollable() {

        TestMode.runAsAndroid {
            run {
                // Arrange, Act
                val filter = Filter("scrollable=true")
                assertThat(filter.name).isEqualTo("scrollable")
                assertThat(filter.noun).isEqualTo("scrollable")
                assertThat(filter.verb).isEqualTo("")
                assertThat(filter.operator).isEqualTo("=")
                assertThat(filter.value).isEqualTo("true")
                assertThat(filter.isNegation).isEqualTo(false)
                assertThat(filter.toString()).isEqualTo("scrollable=true")
                assertThat(filter.isAbbreviation).isEqualTo(false)
                assertThat(filter.fullExpression).isEqualTo("scrollable=true")
                assertThat(filter.abbreviationOperator).isEqualTo("")
                assertThat(filter.abbreviationExpression).isEqualTo("")
            }
            run {
                // Arrange, Act
                val filter = Filter("scrollable!=true")
                assertThat(filter.name).isEqualTo("scrollable")
                assertThat(filter.noun).isEqualTo("scrollable")
                assertThat(filter.verb).isEqualTo("")
                assertThat(filter.operator).isEqualTo("!=")
                assertThat(filter.value).isEqualTo("true")
                assertThat(filter.isNegation).isEqualTo(true)
                assertThat(filter.toString()).isEqualTo("scrollable!=true")
                assertThat(filter.isAbbreviation).isEqualTo(false)
                assertThat(filter.fullExpression).isEqualTo("scrollable!=true")
                assertThat(filter.abbreviationOperator).isEqualTo("")
                assertThat(filter.abbreviationExpression).isEqualTo("")
            }
        }
        TestMode.runAsIos {
            run {
                // Arrange, Act
                val filter = Filter("scrollable=true")
                assertThat(filter.toString()).isEqualTo("scrollable=true")
            }
        }
    }

    @Test
    fun visible() {

        run {
            // Arrange, Act
            val selector = Selector("visible=*")
            val filter = selector.filterMap["visible"]!!
            assertThat(filter.name).isEqualTo("visible")
            assertThat(filter.noun).isEqualTo("visible")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("*")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("visible=*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("visible=*")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
        }
        run {
            // Arrange, Act
            val selector = Selector("visible=true")
            val filter = selector.filterMap["visible"]!!
            assertThat(filter.name).isEqualTo("visible")
            assertThat(filter.noun).isEqualTo("visible")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("true")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("visible=true")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("visible=true")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
        }
        run {
            // Arrange, Act
            val selector = Selector("visible=false")
            val filter = selector.filterMap["visible"]!!
            assertThat(filter.name).isEqualTo("visible")
            assertThat(filter.noun).isEqualTo("visible")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("false")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("visible=false")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("visible=false")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
        }
    }

    @Test
    fun ignoreTypes() {

        val selector = Selector("ignoreTypes=Class1,Class2,Class3")

        run {
            // Arrange, Act
            val filter = selector.filterMap["ignoreTypes"]!!
            assertThat(filter.name).isEqualTo("ignoreTypes")
            assertThat(filter.noun).isEqualTo("ignoreTypes")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("Class1,Class2,Class3")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("ignoreTypes=Class1,Class2,Class3")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("ignoreTypes=Class1,Class2,Class3")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("Class4")).isEqualTo(true)
            assertThat(filter.evaluate("Class1")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("ignoreTypes!=Class1,Class2,Class3")
            assertThat(filter.name).isEqualTo("ignoreTypes")
            assertThat(filter.noun).isEqualTo("ignoreTypes")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("Class1,Class2,Class3")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("ignoreTypes!=Class1,Class2,Class3")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("ignoreTypes!=Class1,Class2,Class3")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("Class4")).isEqualTo(false)
            assertThat(filter.evaluate("Class1")).isEqualTo(true)
        }
    }

    @Test
    fun pos() {

        run {
            // Arrange, Act
            val filter = Filter("pos=1")
            // Assert
            assertThat(filter.name).isEqualTo("pos")
            assertThat(filter.noun).isEqualTo("pos")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("[1]")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("pos=1")
            assertThat(filter.abbreviationOperator).isEqualTo("[n]")
            assertThat(filter.abbreviationExpression).isEqualTo("[1]")
        }
        run {
            // Arrange, Act
            val filter = Filter("[1]")
            // Assert
            assertThat(filter.name).isEqualTo("pos")
            assertThat(filter.noun).isEqualTo("pos")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("[]")
            assertThat(filter.value).isEqualTo("1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("[1]")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("pos=1")
            assertThat(filter.abbreviationOperator).isEqualTo("[n]")
            assertThat(filter.abbreviationExpression).isEqualTo("[1]")
        }
        run {
            // Arrange, Act
            val filter = Filter("pos!=1")
            // Assert
            assertThat(filter.name).isEqualTo("pos")
            assertThat(filter.noun).isEqualTo("pos")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("![1]")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("pos!=1")
            assertThat(filter.abbreviationOperator).isEqualTo("![n]")
            assertThat(filter.abbreviationExpression).isEqualTo("![1]")
        }
    }

    @Test
    fun registeredNoun() {

        run {
            // Arrange
            val filter = Filter("access=access1")
            // Assert
            assertThat(filter.noun).isEqualTo("access")
        }

        run {
            // Arrange
            val filter = Filter("unknown=unknown1")
            // Assert
            assertThat(filter.noun).isEqualTo("text")
        }
    }

    @Test
    fun matchText() {

        run {
            assertThat(
                Filter.matchText(
                    "a ",
                    "a"
                )
            ).isTrue()        // criteria are interpreted as "a"
            assertThat(
                Filter.matchText(
                    "(a)",
                    "(a)"
                )
            ).isTrue()    // criteria are expanded to as "(a)"
            assertThat(
                Filter.matchText(
                    "(a|b)",
                    "(a|b)"
                )
            ).isFalse()// criteria are interpreted as "a or b"
        }
        run {
            // Act, Assert
            assertThat(
                Filter.matchText(
                    "a",
                    "(a|b)"
                )
            ).isTrue() // criteria are interpreted as "a or b"
            assertThat(
                Filter.matchText(
                    "b",
                    "(a|b)"
                )
            ).isTrue() // criteria are interpreted as "a or b"
            assertThat(
                Filter.matchText(
                    "c",
                    "(a|b)"
                )
            ).isFalse()    // criteria are interpreted as "a or b"
            assertThat(
                Filter.matchText(
                    "",
                    "(a|b)"
                )
            ).isFalse()     // criteria are interpreted as "a or b"
            assertThat(Filter.matchText("", "")).isFalse()
        }
    }

    @Test
    fun matchLiteral() {

        run {
            assertThat(Filter.matchLiteral("a", "a")).isTrue()
            assertThat(
                Filter.matchLiteral(
                    "a",
                    "(a)"
                )
            ).isFalse()  // criteria are interpreted literally
            assertThat(
                Filter.matchLiteral(
                    "(a)",
                    "(a)"
                )
            ).isTrue()  // criteria are interpreted literally
            assertThat(
                Filter.matchLiteral(
                    "(a|b)",
                    "(a|b)"
                )
            ).isTrue()// criteria are interpreted literally
        }
    }

    @Test
    fun matchVisible() {

        TestMode.runAsIos {
            TestElementCache.loadXml(XmlDataIos.iOS1)

            run {
                // Arrange
                val e = TestElementCache.select("value=First Name")
                assertThat(e.visible).isEqualTo("true")
                val sel = Selector("visible=true")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()

                // Arrange
                sel.visible = null
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()

                // Arrange
                sel.visible = "*"
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("value=First Name")
                assertThat(e.visible).isEqualTo("true")
                val sel = Selector("visible=false")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isFalse()

                // Arrange
                sel.visible = null
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()

                // Arrange
                sel.visible = "*"
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("!.XCUIElementTypeImage&&visible=false")
                assertThat(e.visible).isEqualTo("false")
                val sel = Selector("visible=false")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()

                // Arrange
                sel.visible = null
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isFalse()

                // Arrange
                sel.visible = "*"
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("visible=false")
                assertThat(e.visible).isEqualTo("false")
                val sel = Selector("visible=true")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isFalse()

                // Arrange
                sel.visible = null
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isFalse()

                // Arrange
                sel.visible = "*"
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("value=First Name")
                assertThat(e.visible).isEqualTo("true")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e)).isTrue()

            }
        }
    }

    @Test
    fun isNotIgnoreTypes() {

        run {
            TestMode.runAsAndroid {
                assertThat(Filter.isNotIgnoreTypes(classOrType = "android.widget.TextView")).isTrue()
                assertThat(
                    Filter.isNotIgnoreTypes(
                        classOrType = "android.widget.TextView",
                        ignoreTypes = "android.widget.EditText,android.widget.TextView"
                    )
                ).isFalse()
            }
            TestMode.runAsIos {
                assertThat(Filter.isNotIgnoreTypes(classOrType = "XCUIElementTypeStaticText")).isTrue()
                assertThat(Filter.isNotIgnoreTypes(classOrType = "XCUIElementTypeCell")).isFalse()
            }
        }
    }

    @Test
    fun evaluateImageEqualsTo_error() {

        run {
            // Arrange
            ImageFileRepository.setup("unitTestConfig/android/image/screens".toPath())
            val image = BufferedImageUtility
                .getBufferedImage("unitTestConfig/android/image/screens/images/tower_of_the_sun_middle.png")
            val filter = Filter("image=tower_of_the_sun_face.png")
            ImageFileRepository.clear()
            // Act
            val r = filter.evaluateImageEqualsTo(image)
            // Assert
            assertThat(r.templateImageFile).isNull()
            assertThat(r.result).isFalse()
        }
    }

    @Test
    fun evaluateImageContainedIn_error() {

        run {
            // Arrange
            ImageFileRepository.setup("unitTestConfig/android/image/screens".toPath())
            val image = BufferedImageUtility
                .getBufferedImage("unitTestConfig/android/image/screens/images/tower_of_the_sun_middle.png")
            val filter = Filter("image=tower_of_the_sun_face.png")
            ImageFileRepository.clear()
            assertThatThrownBy {
                filter.evaluateImageContainedIn(image)
            }.isInstanceOf(FileNotFoundException::class.java)
        }
    }

}