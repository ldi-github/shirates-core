package shirates.spec.unittest.code

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.testcode.UnitTest
import shirates.spec.code.custom.DefaultTranslator
import shirates.spec.code.entity.Case
import shirates.spec.code.entity.Scenario
import shirates.spec.utilily.SpecResourceUtility

class TranslatorTest : UnitTest() {

    @Test
    fun escapeForCode() {

        // Arrange
        PropertiesManager.logLanguage = "ja"
        // Act, Assert
        assertThat(DefaultTranslator.escapeForCode("\\")).isEqualTo("¥")
        assertThat(DefaultTranslator.escapeForCode("\"")).isEqualTo("\\\"")
        assertThat(DefaultTranslator.escapeForCode("\n")).isEqualTo(" ")

        // Arrange
        PropertiesManager.logLanguage = ""
        // Act, Assert
        assertThat(DefaultTranslator.escapeForCode("\\")).isEqualTo("\\\\")
        assertThat(DefaultTranslator.escapeForCode("\"")).isEqualTo("\\\"")
        assertThat(DefaultTranslator.escapeForCode("\n")).isEqualTo(" ")
    }

    @Test
    fun formatArg() {

        // Arrange
        val arg = SpecResourceUtility.bullet + """item"""
        // Act, Assert
        assertThat(DefaultTranslator.formatArg(arg)).isEqualTo("item")
    }

    @Test
    fun getScreenNickName() {

        // Act, Assert
        assertThat(DefaultTranslator.getScreenNickName("[A画面]")).isEqualTo("[A画面]")
        assertThat(DefaultTranslator.getScreenNickName("- [A画面]")).isEqualTo("[A画面]")
        assertThat(DefaultTranslator.getScreenNickName("- [A Screen]")).isEqualTo("[A Screen]")
        assertThat(DefaultTranslator.getScreenNickName("- [A screen]")).isEqualTo("[A screen]")
        assertThat(DefaultTranslator.getScreenNickName("- [Aダイアログ]")).isEqualTo("[Aダイアログ]")
        assertThat(DefaultTranslator.getScreenNickName("- [A Dialog]")).isEqualTo("[A Dialog]")
        assertThat(DefaultTranslator.getScreenNickName("- [A dialog]")).isEqualTo("[A dialog]")

        // Act, Assert
        assertThat(DefaultTranslator.getScreenNickName("A画面")).isEqualTo("")
        assertThat(DefaultTranslator.getScreenNickName("- A画面")).isEqualTo("")
        assertThat(DefaultTranslator.getScreenNickName("- [A Screen]")).isEqualTo("[A Screen]")
        assertThat(DefaultTranslator.getScreenNickName("- [A screen]")).isEqualTo("[A screen]")
        assertThat(DefaultTranslator.getScreenNickName("- [Aダイアログ]")).isEqualTo("[Aダイアログ]")
        assertThat(DefaultTranslator.getScreenNickName("- [A Dialog]")).isEqualTo("[A Dialog]")
        assertThat(DefaultTranslator.getScreenNickName("- [A dialog]")).isEqualTo("[A dialog]")
    }

    @Test
    fun getSubject() {

        // Act, Assert
        assertThat(DefaultTranslator.getSubject("[A画面]")).isEqualTo("[A画面]")
        assertThat(DefaultTranslator.getSubject("[A Screen]")).isEqualTo("[A Screen]")
        // Act, Assert
        assertThat(DefaultTranslator.getSubject("[ボタン]をタップする")).isEqualTo("[ボタン]")
        assertThat(DefaultTranslator.getSubject("Tap [Button]")).isEqualTo("[Button]")
        // Act, Assert
        assertThat(DefaultTranslator.getSubject("{項目}をタップする")).isEqualTo("{項目}")
        assertThat(DefaultTranslator.getSubject("Tap {Field}")).isEqualTo("{Field}")
        // Act, Assert
        assertThat(DefaultTranslator.getSubject("<ラベル>をタップする")).isEqualTo("<ラベル>")
        assertThat(DefaultTranslator.getSubject("Tap <Label>")).isEqualTo("<Label>")
        // Act, Assert
        assertThat(DefaultTranslator.getSubject("ラベルをタップする")).isEqualTo("<ラベル>")
        assertThat(DefaultTranslator.getSubject("Item1 is displayed")).isEqualTo("<Item1>")
    }

    @Test
    fun messageToFunction() {

        val t = DefaultTranslator

        run {
            // Arrange
            PropertiesManager.logLanguage = "ja"
            // Act, Assert
            assertThat(t.messageToFunction("[A画面]が表示されること")).isEqualTo("screenIs(\"[A画面]\")")
            assertThat(t.messageToFunction("[ラベル]が存在すること")).isEqualTo("exist(\"[ラベル]\")")
            assertThat(t.messageToFunction("[ラベル]が表示されること")).isEqualTo("manual(\"[ラベル]が表示されること\")")
            assertThat(t.messageToFunction("アプリが[Chrome]であること")).isEqualTo("appIs(\"[Chrome]\")")
            assertThat(t.messageToFunction("セットアップする", defaultFunc = "macro"))
                .isEqualTo("macro(\"[セットアップする]\")")
            assertThat(t.messageToFunction("セットアップする")).isEqualTo("manual(\"セットアップする\")")
        }
        run {
            // Arrange
            PropertiesManager.logLanguage = ""
            // Act, Assert
            assertThat(t.messageToFunction("[A Screen] is displayed")).isEqualTo("screenIs(\"[A Screen]\")")
            assertThat(t.messageToFunction("[Label] exists")).isEqualTo("exist(\"[Label]\")")
            assertThat(t.messageToFunction("[Label] is displayed")).isEqualTo("manual(\"[Label] is displayed\")")
            assertThat(t.messageToFunction("App is [Chrome]")).isEqualTo("appIs(\"[Chrome]\")")
            assertThat(t.messageToFunction("Setup", defaultFunc = "macro")).isEqualTo("macro(\"[Setup]\")")
            assertThat(t.messageToFunction("Setup")).isEqualTo("manual(\"Setup\")")
        }
    }

    @Test
    fun matchWithMessageMaster() {

        val t = DefaultTranslator

        run {
            // Arrange
            PropertiesManager.logLanguage = "ja"
            // Act, Assert
            assertThat(t.messageToFunction("アプリが[Chrome]であること")).isEqualTo("appIs(\"[Chrome]\")")
        }
        run {
            // Arrange
            PropertiesManager.logLanguage = ""
            // Act, Assert
            assertThat(t.messageToFunction("App is [Chrome]")).isEqualTo("appIs(\"[Chrome]\")")
        }
    }

    @Test
    fun conditionMessageToFunction() {

        val t = DefaultTranslator
        val case = Case(Scenario())

        run {
            // Act
            val actual = t.conditionMessageToFunction(case = case, message = "[macro]")
            // Assert
            assertThat(actual).isEqualTo("macro(\"[macro]\")")
        }
        run {
            val actual = t.conditionMessageToFunction(case = case, message = "[A Screen] is displayed")
            assertThat(actual).isEqualTo("screenIs(\"[A Screen]\")")
        }
        run {
            val actual = t.conditionMessageToFunction(case = case, message = "[A Screen]")
            assertThat(actual).isEqualTo("macro(\"[A Screen]\")")
        }
        run {
            val actual = t.conditionMessageToFunction(case = case, message = "Tap <Label>")
            assertThat(actual).isEqualTo("tap(\"Label\")")
        }
    }

    @Test
    fun actionMessageToFunction() {

        val t = DefaultTranslator
        val case = Case(Scenario())

        run {
            val actual = t.actionMessageToFunction(case = case, message = "[macro]")
            assertThat(actual).isEqualTo("macro(\"[macro]\")")
        }
        run {
            val actual = t.actionMessageToFunction(case = case, message = "[A Screen] is displayed")
            assertThat(actual).isEqualTo("screenIs(\"[A Screen]\")")
        }
        run {
            val actual = t.actionMessageToFunction(case = case, message = "[A Screen]")
            assertThat(actual).isEqualTo("macro(\"[A Screen]\")")
        }
        run {
            val actual = t.actionMessageToFunction(case = case, message = "Tap <Label>")
            assertThat(actual).isEqualTo("tap(\"Label\")")
        }
    }

    @Test
    fun targetToFunction() {

        val t = DefaultTranslator

        run {
            val actual = t.targetToFunction("[A Screen]")
            assertThat(actual).isEqualTo("target(\"[A Screen]\")")
        }
        run {
            val actual = t.targetToFunction("Item1")
            assertThat(actual).isEqualTo("target(\"Item1\")")
        }
    }

    @Test
    fun expectationMessageToFunction() {

        val t = DefaultTranslator

        /**
         * target != null
         */
        run {
            // Arrange
            val target = shirates.spec.code.entity.Target(case = Case(Scenario()), target = "[A Screen]")
            val message = "is displayed"
            // Act
            val actual = t.expectationMessageToFunction(target = target, message = message)
            // Assert
            assertThat(actual).isEqualTo("screenIs(\"[A Screen]\")")
        }
        run {
            // Arrange
            val target = shirates.spec.code.entity.Target(case = Case(Scenario()), target = "Item1")
            val message = "is displayed"
            // Act
            val actual = t.expectationMessageToFunction(target = target, message = message)
            // Assert
            assertThat(actual).isEqualTo("manual(\"is displayed\")")
        }
        run {
            // Arrange
            val target = shirates.spec.code.entity.Target(case = Case(Scenario()), target = "Header Area")
            val message = "Set Wi-Fi OFF"
            // Act
            val actual = t.expectationMessageToFunction(target = target, message = message)
            // Assert
            assertThat(actual).isEqualTo("manual(\"$message\")")
        }

        /**
         * target == null
         */
        run {
            // Arrange
            val message = "[A Screen] is displayed"
            // Act
            val actual = t.expectationMessageToFunction(message = message)
            // Assert
            assertThat(actual).isEqualTo("screenIs(\"[A Screen]\")")
        }
        run {
            // Arrange
            val message = "Item1 is displayed"
            // Act
            val actual = t.expectationMessageToFunction(message = message)
            // Assert
            assertThat(actual).isEqualTo("exist(\"<Item1>\")")
        }
        run {
            // Arrange
            val message = "Set Wi-Fi OFF"
            // Act
            val actual = t.expectationMessageToFunction(message = message)
            // Assert
            assertThat(actual).isEqualTo("manual(\"$message\")")
        }
    }

    @Test
    fun atEndOfScenario() {

        DefaultTranslator.atEndOfScenario(mutableListOf())
    }

    @Test
    fun atEndOfCase() {

        DefaultTranslator.atEndOfCase(mutableListOf())
    }

    @Test
    fun atEndOfCondition() {

        DefaultTranslator.atEndOfCondition(mutableListOf())
    }

    @Test
    fun atEndOfAction() {

        DefaultTranslator.atEndOfAction(mutableListOf())
    }

    @Test
    fun atEndOfExpectation() {

        DefaultTranslator.atEndOfExpectation(mutableListOf())
    }

    @Test
    fun atEndOfTarget() {

        run {
            val list = mutableListOf<String>()
            list.add("screenIs(\"[A Screen]\")")
            DefaultTranslator.atEndOfTarget(list)
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("screenIs(\"[A Screen]\")")
            list.add("exist(\"<Item1>\")")
            // Act
            DefaultTranslator.atEndOfTarget(list)
            // Assert
            assertThat(list[0]).isEqualTo("it.screenIs(\"[A Screen]\")")
            assertThat(list[1]).isEqualTo(".exist(\"<Item1>\")")
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("target(\"[A Screen]\")")
            list.add("screenIs(\"[A Screen]\")")
            // Act
            DefaultTranslator.atEndOfTarget(list)
            // Assert
            assertThat(list.count()).isEqualTo(1)
            assertThat(list[0]).isEqualTo("it.screenIs(\"[A Screen]\")")
        }
    }

    @Test
    fun reformatLines() {

        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("\"String1\".isEmpty()")
            // Act
            DefaultTranslator.reformatLines(list)
            // Assert
            assertThat(list[0]).isEqualTo("\"String1\".isEmpty()")
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("}")
            // Act
            DefaultTranslator.reformatLines(list)
            // Assert
            assertThat(list[0]).isEqualTo("}")
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("true")
            // Act
            DefaultTranslator.reformatLines(list)
            // Assert
            assertThat(list[0]).isEqualTo("true")
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("false")
            // Act
            DefaultTranslator.reformatLines(list)
            // Assert
            assertThat(list[0]).isEqualTo("false")
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("onScreen(")
            // Act
            DefaultTranslator.reformatLines(list)
            // Assert
            assertThat(list[0]).isEqualTo("onScreen(")
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("}")
            list.add("manual(\"Connect USB\")")
            // Act
            DefaultTranslator.reformatLines(list)
            // Assert
            assertThat(list[0]).isEqualTo("}")
            assertThat(list[1]).isEqualTo("it.manual(\"Connect USB\")")
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("android {")
            list.add("manual(\"Connect USB\")")
            list.add("}")
            // Act
            DefaultTranslator.reformatLines(list)
            // Assert
            assertThat(list[0]).isEqualTo("android {")
            assertThat(list[1]).isEqualTo("it.manual(\"Connect USB\")")
            assertThat(list[2]).isEqualTo("}")
        }
        run {
            // Arrange
            val list = mutableListOf<String>()
            list.add("android {")
            list.add("manual(\"Connect USB\")")
            // Act
            DefaultTranslator.reformatLines(list)
            // Assert
            assertThat(list[0]).isEqualTo("android {")
            assertThat(list[1]).isEqualTo("it.manual(\"Connect USB\")")
            assertThat(list[2]).isEqualTo("}")
        }

    }
}