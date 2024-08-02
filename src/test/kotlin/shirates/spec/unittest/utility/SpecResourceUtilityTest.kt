package shirates.spec.unittest.utility

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.spec.utilily.SpecResourceUtility

class SpecResourceUtilityTest : UnitTest() {

    @Test
    @Order(10)
    fun setup() {

        val u = SpecResourceUtility
        u.setup(logLanguage = "")

        // Assert
        assertThat(u.bullet).isEqualTo("- ")
        assertThat(u.backslash).isEqualTo("\\")
        assertThat(u.isDisplayed).isEqualTo("is displayed")
        assertThat(u.noLoadRunMode).isEqualTo("No-Load-Run Mode")

        assertThat(u.OK).isEqualTo("OK")
        assertThat(u.NG).isEqualTo("NG")
        assertThat(u.ERROR).isEqualTo("ERROR")
        assertThat(u.SUSPENDED).isEqualTo("SUSPENDED")
        assertThat(u.NONE).isEqualTo("N/A")
        assertThat(u.COND_AUTO).isEqualTo("COND_AUTO")
        assertThat(u.MANUAL).isEqualTo("MANUAL")
        assertThat(u.SKIP).isEqualTo("SKIP")
        assertThat(u.NOTIMPL).isEqualTo("NOTIMPL")
        assertThat(u.EXCLUDED).isEqualTo("EXCLUDED")
        assertThat(u.DELETED).isEqualTo("DELETED")

        // Act
        u.setup(logLanguage = "ja")
        // Assert
        assertThat(u.bullet).isEqualTo("・")
        assertThat(u.backslash).isEqualTo("¥")
        assertThat(u.isDisplayed).isEqualTo("表示されること")
        assertThat(u.noLoadRunMode).isEqualTo("無負荷実行モード")

        assertThat(u.OK).isEqualTo("OK")
        assertThat(u.NG).isEqualTo("NG")
        assertThat(u.ERROR).isEqualTo("ERROR")
        assertThat(u.SUSPENDED).isEqualTo("保留")
        assertThat(u.NONE).isEqualTo("未実施")
        assertThat(u.COND_AUTO).isEqualTo("COND_AUTO")
        assertThat(u.MANUAL).isEqualTo("手動で実施")
        assertThat(u.SKIP).isEqualTo("SKIP")
        assertThat(u.NOTIMPL).isEqualTo("NOTIMPL")
        assertThat(u.EXCLUDED).isEqualTo("実施対象外")
        assertThat(u.DELETED).isEqualTo("削除予定")
    }

    @Test
    @Order(20)
    fun getString() {

        val u = SpecResourceUtility

        // Arrange
        u.setup(logLanguage = "")
        // Act, Assert
        assertThat(u.getString("result.NONE")).isEqualTo("N/A")

        // Arrange
        u.setup(logLanguage = "ja")
        // Act, Assert
        assertThat(u.getString("result.NONE")).isEqualTo("未実施")
    }

    @Test
    @Order(30)
    fun getAltResult() {

        val u = SpecResourceUtility

        // Arrange
        u.setup(logLanguage = "")
        // Act, Assert
        assertThat(u.getAltResult("NONE")).isEqualTo("N/A")

        // Arrange
        u.setup(logLanguage = "ja")
        // Act, Assert
        assertThat(u.getAltResult("NONE")).isEqualTo("未実施")
    }

    @Test
    @Order(40)
    fun getResultFromAltResult() {

        val u = SpecResourceUtility

        // Arrange
        u.setup(logLanguage = "")
        // Act, Assert
        assertThat(u.getResultFromAltResult("N/A")).isEqualTo("NONE")
        assertThat(u.getResultFromAltResult("NONE")).isEqualTo("NONE")

        // Arrange
        u.setup(logLanguage = "ja")
        // Act, Assert
        assertThat(u.getResultFromAltResult("未実施")).isEqualTo("NONE")
        assertThat(u.getResultFromAltResult("NONE")).isEqualTo("NONE")
    }
}