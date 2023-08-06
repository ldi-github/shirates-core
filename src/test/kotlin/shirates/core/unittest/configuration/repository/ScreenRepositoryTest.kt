package shirates.core.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.ScreenInfo
import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Path

class ScreenRepositoryTest : UnitTest() {

    private fun check(selector: Selector?, property: String, expected: String?) {

        val s = selector!!

        if (property == "text") {
            assertThat(s.text).isEqualTo(expected)
        }

        if (property == "textStartsWith") {
            assertThat(s.textStartsWith).isEqualTo(expected)
        }

        if (property == "textContains") {
            assertThat(s.textContains).isEqualTo(expected)
        }

        if (property == "textEndsWith") {
            assertThat(s.textEndsWith).isEqualTo(expected)
        }

        if (property == "textMatches") {
            assertThat(s.textMatches).isEqualTo(expected)
        }

        if (property == "id") {
            assertThat(s.id).isEqualTo(expected)
        }

        if (property == "access") {
            assertThat(s.access).isEqualTo(expected)
        }

        if (property == "accessStartsWith") {
            assertThat(s.accessStartsWith).isEqualTo(expected)
        }

        if (property == "accessContains") {
            assertThat(s.accessContains).isEqualTo(expected)
        }

        if (property == "accessEndsWith") {
            assertThat(s.accessEndsWith).isEqualTo(expected)
        }

        if (property == "accessMatches") {
            assertThat(s.accessMatches).isEqualTo(expected)
        }

        if (property == "value") {
            assertThat(s.value).isEqualTo(expected)
        }

        if (property == "valueStartsWith") {
            assertThat(s.valueStartsWith).isEqualTo(expected)
        }

        if (property == "valueContains") {
            assertThat(s.valueContains).isEqualTo(expected)
        }

        if (property == "valueEndsWith") {
            assertThat(s.valueEndsWith).isEqualTo(expected)
        }

        if (property == "valueMatches") {
            assertThat(s.valueMatches).isEqualTo(expected)
        }

        if (property == "className") {
            assertThat(s.className).isEqualTo(expected)
        }

        if (property == "xpath") {
            assertThat(s.xpath).isEqualTo(expected)
        }
    }

    @Test
    fun setup_with_args() {

        // Act
        ScreenRepository.setup("unitTestData/testConfig/nicknames1/screens".toPath())
        // Assert
        assertThat(ScreenRepository.screensDirectory).isEqualTo("unitTestData/testConfig/nicknames1/screens".toPath())
        assertThat(ScreenRepository.screenInfoMap.count()).isEqualTo(11)
        assertThat(ScreenRepository.screenInfoSearchList.count()).isEqualTo(11)

        /**
         * global
         */
        run {
            // Assert
            val screenInfo = ScreenRepository["[A Screen]"]
            assertThat(screenInfo.selectors.containsKey("[Global Selector 1]")).isTrue()
            assertThat(screenInfo.selectors.containsKey("[Global Selector 2]")).isTrue()
        }

        /**
         * Common Footer
         */
        run {
            // Assert
            val screenInfo = ScreenRepository["[Common Footer]"]
            assertThat(screenInfo.key).isEqualTo("[Common Footer]")
            assertThat(screenInfo.includes.count()).isEqualTo(0)
            assertThat(screenInfo.selectors.count()).isEqualTo(13)
            assertThat(screenInfo.screenFile).endsWith(
                "unitTestData/testConfig/nicknames1/screens/shared/[Common Footer].json".replace(
                    "/",
                    File.separator
                )
            )

            val s = screenInfo.selectors

            assertThat(s.containsKey("[Menu X]")).isTrue()
            check(s["[Menu X]"], "id", "idX")

            assertThat(s.containsKey("[Menu Y]")).isTrue()
            check(s["[Menu Y]"], "xpath", "xpathY")

            assertThat(s.containsKey("[Menu Z]")).isTrue()
            check(s["[Menu Z]"], "text", "textZ")
        }

        /**
         * A Screen
         */
        run {
            // Assert
            val screenInfo = ScreenRepository["[A Screen]"]
            assertThat(screenInfo.includes.count()).isEqualTo(1)
            assertThat(
                screenInfo.screenFile?.replace(File.separator, "/")
            ).endsWith("unitTestData/testConfig/nicknames1/screens/FunctionA/[A Screen].json")
            assertThat(screenInfo.identitySelectors.count()).isEqualTo(2)
            assertThat(screenInfo.scrollInfo.scrollable).isEqualTo("[Scrollable]")
            assertThat(screenInfo.scrollInfo.startElements)
                .containsAll(
                    listOf(
                        "[Start Element 1]",
                        "[Start Element 2]",
                        "[start-elementA]",
                        "[start-elementB]"
                    )
                )
            assertThat(screenInfo.scrollInfo.endElements)
                .containsAll(
                    listOf(
                        "[End Element 1]",
                        "[End Element 2]",
                        "[end-elementA]",
                        "[end-elementB]"
                    )
                )
            assertThat(screenInfo.scrollInfo.overlayElements)
                .containsAll(
                    listOf(
                        "[Overlay Element 1]",
                        "[Overlay Element 1]",
                        "[overlay-elementA]",
                        "[overlay-elementB]",
                        "[Menu X]",
                        "[Menu Y]",
                        "[Menu Z]"
                    )
                )

            assertThat(screenInfo.key).isEqualTo("[A Screen]")

            val s = screenInfo.selectors

            /**
             * include
             */
            run {
                assertThat(s.containsKey("[Menu X]")).isTrue()
                check(s["[Menu X]"], "id", "idX")

                assertThat(s.containsKey("[Menu Y]")).isTrue()
                check(s["[Menu Y]"], "xpath", "xpathY")

                assertThat(s.containsKey("[Menu Z]")).isTrue()
                check(s["[Menu Z]"], "text", "textZ")
            }

            /**
             * selectors
             */
            run {
                assertThat(s.containsKey("[Button A]")).isTrue()
                check(s["[Button A]"], "id", "idA")
                assertThat(s["[Button A]"]?.section).isEqualTo("selectors")

                assertThat(s.containsKey("{TextBox A}")).isTrue()
                check(s["{TextBox A}"], "xpath", "xpathA")
                assertThat(s["{TextBox A}"]?.section).isEqualTo("selectors")

                assertThat(s.containsKey("[Label A]")).isTrue()
                check(s["[Label A]"], "text", "textA")

                assertThat(s.containsKey("[Label A2]")).isTrue()
                check(s["[Label A2]"], "textStartsWith", "textA2")

                assertThat(s.containsKey("[Label A3]")).isTrue()
                check(s["[Label A3]"], "textContains", "textA3")

                assertThat(s.containsKey("[Label A4]")).isTrue()
                check(s["[Label A4]"], "textMatches", "textA4")

                assertThat(s.containsKey("[empty]")).isTrue()
                assertThat(s["[empty]"]?.text).isEqualTo("empty")
                assertThat(s["[empty]"]?.textContains).isNull()
                assertThat(s["[empty]"]?.textStartsWith).isNull()
                assertThat(s["[empty]"]?.textEndsWith).isNull()
                assertThat(s["[empty]"]?.textMatches).isNull()
                assertThat(s["[empty]"]?.id).isNull()
                assertThat(s["[empty]"]?.access).isNull()
                assertThat(s["[empty]"]?.accessStartsWith).isNull()
                assertThat(s["[empty]"]?.className).isNull()
                assertThat(s["[empty]"]?.xpath).isNull()
                assertThat(s["[empty]"]?.section).isEqualTo("selectors")

                assertThat(s.containsKey("[by id]")).isTrue()
                check(s["[by id]"], "id", "idA")

                assertThat(s.containsKey("[by xpath]")).isTrue()
                check(s["[by xpath]"], "xpath", "xpathA")

                assertThat(s.containsKey("[by text]")).isTrue()
                check(s["[by text]"], "text", "textA")

                assertThat(s.containsKey("[by textStartsWith]")).isTrue()
                check(s["[by textStartsWith]"], "textStartsWith", "textA")

                assertThat(s.containsKey("[by textContains]")).isTrue()
                check(s["[by textContains]"], "textContains", "textA")

                assertThat(s.containsKey("[by textEndsWith]")).isTrue()
                check(s["[by textEndsWith]"], "textEndsWith", "textA")

                assertThat(s.containsKey("[by textMatches]")).isTrue()
                check(s["[by textMatches]"], "textMatches", "textA")

                assertThat(s.containsKey("[by access]")).isTrue()
                check(s["[by access]"], "access", "accessA")

                assertThat(s.containsKey("[by access 2]")).isTrue()
                check(s["[by access 2]"], "access", "accessA2")

                assertThat(s.containsKey("[by accessStartsWith]")).isTrue()
                check(s["[by accessStartsWith]"], "accessStartsWith", "accessA")

                assertThat(s.containsKey("[by accessStartsWith 2]")).isTrue()
                check(s["[by accessStartsWith 2]"], "accessStartsWith", "accessA2")

                assertThat(s.containsKey("[by accessContains]")).isTrue()
                check(s["[by accessContains]"], "accessContains", "accessA")

                assertThat(s.containsKey("[by accessContains 2]")).isTrue()
                check(s["[by accessContains 2]"], "accessContains", "accessA2")

                assertThat(s.containsKey("[by accessEndsWith]")).isTrue()
                check(s["[by accessEndsWith]"], "accessEndsWith", "accessA")

                assertThat(s.containsKey("[by accessEndsWith 2]")).isTrue()
                check(s["[by accessEndsWith 2]"], "accessEndsWith", "accessA2")

                assertThat(s.containsKey("[by accessMatches]")).isTrue()
                check(s["[by accessMatches]"], "accessMatches", "accessA")

                assertThat(s.containsKey("[by className]")).isTrue()
                check(s["[by className]"], "className", "className1")

                assertThat(s.containsKey("[by access,text,className]")).isTrue()
                check(s["[by access,text,className]"], "access", "access1")
                check(s["[by access,text,className]"], "text", "text1")
                check(s["[by access,text,className]"], "className", "className1")
            }
        }
    }

    @Test
    fun setup_screen_directory_is_not_directory() {

        run {
            // Arrange
            val path = "unitTestConfig/android/androidSettings/androidSettingsConfig.json".toPath().toAbsolutePath()
            // Act, Assert
            assertThatThrownBy {
                ScreenRepository.setup(screensDirectory = path)
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("screens is not directory. ($path)")
        }
    }

    @Test
    fun setup_screen_duplicated() {

        run {
            // Act
            ScreenRepository.setup("unitTestData/testConfig/errorScreens/screenDuplicated".toPath())
            // Assert
            val msg = TestLog.lastTestLog?.message
            assertThat(msg?.startsWith("Screen nickname [A Screen] is duplicated. Registered file"))
        }
    }

    @Test
    fun seup_includeNotRegisteredScreen() {

        run {
            // Act, Assert
            val directory = "unitTestData/testConfig/errorScreens/includeNotRegisteredScreen".toPath().toAbsolutePath()
            val notRegisteredScreenPath = directory.resolve("[includeNotRegisteredScreen].json")
            assertThatThrownBy {
                ScreenRepository.setup(directory)
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessageStartingWith("Failed to include non registered screen.(key=[not.registered.screen], file=$notRegisteredScreenPath)")
        }
    }

    @Test
    fun screensDirectory_directory() {

        // Arrange
        val notExitDirectory = "not/exist/directory".toPath().toAbsolutePath()
        // Act
        ScreenRepository.setup(notExitDirectory)
        // Assert
        assertThat(TestLog.lastTestLog?.message)?.isEqualTo("screens directory not found. ($notExitDirectory)")
    }

    @Test
    fun setup_importDirectories() {

        // Arrange
        val importDirectories = mutableListOf<Path>()
        importDirectories.add("unitTestConfig/android/androidSettings/screens".toPath())
        // Act
        ScreenRepository.setup(
            "unitTestData/testConfig/nicknames1/screens".toPath(),
            importDirectories = importDirectories
        )
        // Assert
        assertThat(ScreenRepository.importDirectories).isEqualTo(importDirectories)
        assertThat(ScreenRepository.has("[Sample Accessibility Screen]"))
    }

    @Test
    fun has() {

        // Arrange
        ScreenRepository.setup("unitTestData/testConfig/nicknames1/screens".toPath())
        // Act, Assert
        assertThat(ScreenRepository.has("screenName1")).isFalse()

        // Arrange
        ScreenRepository.set(
            "screenName1",
            ScreenInfo()
        )
        // Act, Assert
        assertThat(ScreenRepository.has("screenName1")).isTrue()

    }

    @Test
    fun get_test() {

        // Arrange
        ScreenRepository.setup("unitTestData/testConfig/nicknames1/screens".toPath())
        // Act, Assert
        assertThatThrownBy {
            ScreenRepository.get("screenName1")
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("key is not registered.(key='screenName1')")

        // Arrange
        ScreenRepository.set(
            "screenName1",
            ScreenInfo()
        )
        // Act, Assert
        assertThat(ScreenRepository.get("screenName1")).isNotNull()
    }

    @Test
    fun getScreenInfo() {

        // Arrange
        ScreenRepository.setup("unitTestData/testConfig/nicknames1/screens".toPath())
        run {
            // Act
            val screenInfo = ScreenRepository.getScreenInfo(screenName = "[A Screen]")
            // Assert
            assertThat(screenInfo.key).isEqualTo("[A Screen]")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                ScreenRepository.getScreenInfo(screenName = "[Not Registered Screen]")
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("Screen not registered (screenName=[Not Registered Screen]). Use registered nickname or setup screen nickname file.")
        }
    }

    @Test
    fun getSelector() {

        // Arrange
        ScreenRepository.setup("unitTestData/testConfig/nicknames1/screens".toPath())
        run {
            // Act
            val selector = ScreenRepository.getSelector(screenName = "[A Screen]", nickName = "[Label A]")
            // Assert
            assertThat(selector.expression).isEqualTo("text=textA")
        }

    }

    @Test
    fun nicknameIndex() {

        // Arrange, Act
        ScreenRepository.setup("unitTestData/testConfig/weight/screens".toPath())
        run {
            val nicknameIndex = ScreenRepository.nicknameIndex
            // Assert
            assertThat(nicknameIndex.count()).isEqualTo(18)
        }

    }
}