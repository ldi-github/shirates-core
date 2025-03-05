package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.driver.classic
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class TestElementRelativeCoordinateExtension_AndroidTest1 : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun complex() {

        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest)
        TestElementCache.synced = true
        ScreenRepository.setup(screensDirectory = "unitTestData/testConfig/nicknames1/screens/relative")
        TestDriver.currentScreen = "[RelativeCoordinateTest Screen]"

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|Scrollable1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|Scrollable2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|Scrollable3-1|
         */

        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")
            // Act, Assert
            val e2 = e.right().below().left().above()
            assertThat(e2.selector.toString()).isEqualTo("<#TextView1-1>:right:below:left:above")
            assertThat(e2).isEqualTo(e)
        }
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")
            // Act, Assert
            val e2 = e.relative(":right", margin = 0).relative(":below", margin = 0).relative(":left", margin = 0)
                .relative(":above", margin = 0)
            assertThat(e2.selector.toString()).isEqualTo("<#TextView1-1>:right:below:left:above")
            assertThat(e2).isEqualTo(e)
        }
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")
            // Act, Assert
            val e2 = e.select("[:Right]").select("[:Below]").select("[:Left]").select("[:Above]")
            assertThat(e2.selector.toString()).isEqualTo("<#TextView1-1>[:Right][:Below][:Left][:Above]")
            assertThat(e2).isEqualTo(e)
        }
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")
            // Act, Assert
            val e2 = e.select(":right").select(":below").select(":left").select(":above")
            assertThat(e2.selector.toString()).isEqualTo("<#TextView1-1>:right:below:left:above")
            assertThat(e2).isEqualTo(e)
        }
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")
            // Act, Assert
            val e2 = e.select(":scrollable")
                .select(":belowScrollable")
                .select(":aboveScrollable")
                .select(":flowScrollable")
            assertThat(e2.selector.toString()).isEqualTo("<#TextView1-1>:scrollable:belowScrollable:aboveScrollable:flowScrollable")
            val e3 = e.select(":scrollable(2)")
            assertThat(e2).isEqualTo(e3)
        }

    }

    @Test
    fun right() {
        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest)
        TestElementCache.synced = true
        ScreenRepository.setup(screensDirectory = "unitTestData/testConfig/nicknames1/screens/relative")
        TestDriver.currentScreen = "[RelativeCoordinateTest Screen]"

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|Scrollable1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|Scrollable2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|Scrollable3-1|
         */

        // right()
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")

            run {
                // Act, Assert
                val r1 = e.right()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:right")
                val r2 = e.select(":right")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:right")
                val r3 = e.select("[:Right]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightLabel()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightLabel")
                val r2 = e.select(":rightLabel", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightLabel")
                val r3 = e.select("[:Right label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right label]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
            run {
                // Act, Assert
                val r1 = e.rightInput()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightInput")
                val r2 = e.select(":rightInput")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightInput")
                val r3 = e.select("[:Right input]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightImage()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightImage")
                val r2 = e.select(":rightImage")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightImage")
                val r3 = e.select("[:Right image]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right image]")
                // Assert
                assertThat(e.rightImage().id).isEqualTo("ImageView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightButton()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightButton")
                val r2 = e.select(":rightButton")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightButton")
                val r3 = e.select("[:Right button]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right button]")
                // Assert
                assertThat(e.rightButton().id).isEqualTo("ImageButton1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightSwitch()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightSwitch")
                val r2 = e.select(":rightSwitch")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightSwitch")
                val r3 = e.select("[:Right switch]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightSwitch("[1]"))
            }
        }

        // right(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")
            run {
                // Act, Assert
                val r1 = e.right(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:right")
                val r2 = e.select(":right(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:right")
                val r3 = e.select("[:Right]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:right(2)")
                val r2 = e.select(":right(2)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:right(2)")
                val r3 = e.select("[:Right(2)]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right(2)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[2]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(3)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:right(3)")
                val r2 = e.select(":right(3)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:right(3)")
                val r3 = e.select("[:Right(3)]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right(3)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[3]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(4)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:right(4)")
                val r2 = e.select(":right(4)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:right(4)")
                val r3 = e.select("[:Right(4)]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right(4)]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[4]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(5)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:right(5)")
                val r2 = e.select(":right(5)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:right(5)")
                val r3 = e.select("[:Right(5)]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right(5)]")
                // Assert
                assertThat(r1.id).isEqualTo("CheckBox1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[5]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(6)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:right(6)")
                val r2 = e.select(":right(6)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:right(6)")
                val r3 = e.select("[:Right(6)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right(6)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.right("[7]").isEmpty).isTrue()
            }

        }

        // rightLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")

            run {
                // Act, Assert
                val r1 = e.rightLabel(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightLabel")
                val r2 = e.select(":rightLabel(1)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightLabel")
                val r3 = e.select("[:Right label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right label]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
            run {
                // Act, Assert
                val r1 = e.rightLabel(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightLabel(2)")
                val r2 = e.select(":rightLabel(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightLabel(2)")
                val r3 = e.select("[:Right label(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right label(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.rightLabel("[2]").isEmpty).isTrue()
            }
        }

        // rightInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")

            run {
                // Act, Assert
                val r1 = e.rightInput(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightInput")
                val r2 = e.select(":rightInput(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightInput")
                val r3 = e.select("[:Right input]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightInput(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightInput(2)")
                val r2 = e.select(":rightInput(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightInput(2)")
                val r3 = e.select("[:Right input(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right input(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.rightInput("[2]").isEmpty).isTrue()
            }
        }

        // rightImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")

            run {
                // Act, Assert
                val r1 = e.rightImage(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightImage")
                val r2 = e.select(":rightImage(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightImage")
                val r3 = e.select("[:Right image]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightImage(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightImage(2)")
                val r2 = e.select(":rightImage(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightImage(2)")
                val r3 = e.select("[:Right image(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right image(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.rightImage("[2]").isEmpty).isTrue()
            }
        }

        // rightButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")

            run {
                // Act, Assert
                val r1 = e.rightButton(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightButton")
                val r2 = e.select(":rightButton(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightButton")
                val r3 = e.select("[:Right button]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightButton(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightButton(2)")
                val r2 = e.select(":rightButton(2)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightButton(2)")
                val r3 = e.select("[:Right button(2)]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right button(2)]")
                // Assert
                assertThat(r1.id).isEqualTo("CheckBox1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightButton("[2]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightButton(3)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightButton(3)")
                val r2 = e.select(":rightButton(3)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightButton(3)")
                val r3 = e.select("[:Right button(3)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right button(3)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.rightButton("[3]").isEmpty).isTrue()
            }
        }

        // rightSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")

            run {
                // Act, Assert
                val r1 = e.rightSwitch(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightSwitch")
                val r2 = e.select(":rightSwitch(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightSwitch")
                val r3 = e.select("[:Right switch]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightSwitch("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightSwitch(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:rightSwitch(2)")
                val r2 = e.select(":rightSwitch(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:rightSwitch(2)")
                val r3 = e.select("[:Right switch(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Right switch(2)]")
                // Assert
                assertThat(r1.isEmpty).isEqualTo(true)
                assertThat(r2.isEmpty).isEqualTo(true)
                assertThat(r3.isEmpty).isEqualTo(true)
                assertThat(e.rightSwitch("[2]").isEmpty).isTrue()
            }
        }
    }

    @Test
    fun right2() {
        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest)
        TestElementCache.synced = true
        ScreenRepository.setup(screensDirectory = "unitTestData/testConfig/nicknames1/screens/relative")
        TestDriver.currentScreen = "[RelativeCoordinateTest Screen]"

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|Scrollable1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|Scrollable2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|Scrollable3-1|
         */

        // right2()
        run {
            // Arrange
            val e = TestElementCache.select("#TextView2-1")
            run {
                // Act, Assert
                val r1 = e.right()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:right")
                val r2 = e.select(":right")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:right")
                val r3 = e.select("[:Right]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightInput()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightInput")
                val r2 = e.select(":rightInput")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightInput")
                val r3 = e.select("[:Right input]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightLabel()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightLabel")
                val r2 = e.select(":rightLabel", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightLabel")
                val r3 = e.select("[:Right label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right label]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
            run {
                // Act, Assert
                val r1 = e.rightImage()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightImage")
                val r2 = e.select(":rightImage")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightImage")
                val r3 = e.select("[:Right image]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightButton()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightButton")
                val r2 = e.select(":rightButton")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightButton")
                val r3 = e.select("[:Right button]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightSwitch()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightSwitch")
                val r2 = e.select(":rightSwitch")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightSwitch")
                val r3 = e.select("[:Right switch]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightSwitch("[1]"))
            }
        }

        // right(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView2-1")

            run {
                // Act, Assert
                val r1 = e.right(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:right")
                val r2 = e.select(":right(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:right")
                val r3 = e.select("[:Right]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:right(2)")
                val r2 = e.select(":right(2)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:right(2)")
                val r3 = e.select("[:Right(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right(2)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[2]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(3)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:right(3)")
                val r2 = e.select(":right(3)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:right(3)")
                val r3 = e.select("[:Right(3)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right(3)]")
                // Assert
                assertThat(e.right(3).id).isEqualTo("ImageButton2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[3]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(4)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:right(4)")
                val r2 = e.select(":right(4)")
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:right(4)")
                val r3 = e.select("[:Right(4)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right(4)]")
                // Assert
                assertThat(e.right(4).id).isEqualTo("Switch2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[4]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(5)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:right(5)")
                val r2 = e.select(":right(5)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:right(5)")
                val r3 = e.select("[:Right(5)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right(5)]")
                // Assert
                assertThat(r1.id).isEqualTo("CheckBox2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.right("[5]"))
            }
            run {
                // Act, Assert
                val r1 = e.right(6)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:right(6)")
                val r2 = e.select(":right(6)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:right(6)")
                val r3 = e.select("[:Right(6)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right(6)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }

        // rightLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView2-1")

            run {
                // Act, Assert
                val r1 = e.rightLabel(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightLabel")
                val r2 = e.select(":rightLabel(1)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightLabel")
                val r3 = e.select("[:Right label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right label]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
            run {
                // Act, Assert
                val r1 = e.rightLabel(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightLabel(2)")
                val r2 = e.select(":rightLabel(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightLabel(2)")
                val r3 = e.select("[:Right label(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right label(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }

        // rightInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView2-1")

            run {
                // Act, Assert
                val r1 = e.rightInput(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightInput")
                val r2 = e.select(":rightInput(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightInput")
                val r3 = e.select("[:Right input]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightInput(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightInput(2)")
                val r2 = e.select(":rightInput(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightInput(2)")
                val r3 = e.select("[:Right input(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right input(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }

        // rightImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView2-1")

            run {
                // Act, Assert
                val r1 = e.rightImage(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightImage")
                val r2 = e.select(":rightImage(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightImage")
                val r3 = e.select("[:Right image]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightImage(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightImage(2)")
                val r2 = e.select(":rightImage(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightImage(2)")
                val r3 = e.select("[:Right image(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right image(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.rightImage("[2]").isEmpty).isTrue()
            }
        }

        // rightButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView2-1")

            run {
                // Act, Assert
                val r1 = e.rightButton(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightButton")
                val r2 = e.select(":rightButton(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightButton")
                val r3 = e.select("[:Right button]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightButton(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightButton(2)")
                val r2 = e.select(":rightButton(2)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightButton(2)")
                val r3 = e.select("[:Right button(2)]")
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right button(2)]")
                // Assert
                assertThat(r1.id).isEqualTo("CheckBox2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightButton("[2]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightButton(3)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightButton(3)")
                val r2 = e.select(":rightButton(3)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightButton(3)")
                val r3 = e.select("[:Right button(3)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right button(3)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.rightButton("[3]").isEmpty).isTrue()
            }
        }

        // rightSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView2-1")

            run {
                // Act, Assert
                val r1 = e.rightSwitch(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightSwitch")
                val r2 = e.select(":rightSwitch(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightSwitch")
                val r3 = e.select("[:Right switch]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch2-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.rightSwitch("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.rightSwitch(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView2-1>:rightSwitch(2)")
                val r2 = e.select(":rightSwitch(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView2-1>:rightSwitch(2)")
                val r3 = e.select("[:Right switch(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView2-1>[:Right switch(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.rightSwitch("[2]").isEmpty).isTrue()
            }
        }
    }

    @Test
    fun left() {
        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest)
        TestElementCache.synced = true
        ScreenRepository.setup(screensDirectory = "unitTestData/testConfig/nicknames1/screens/relative")
        TestDriver.currentScreen = "[RelativeCoordinateTest Screen]"

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-1")

            run {
                // Act, Assert
                val r1 = e.left()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:left")
                val r2 = e.select(":left")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:left")
                val r3 = e.select("[:Left]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.left("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftInput()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftInput")
                val r2 = e.select(":leftInput")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftInput")
                val r3 = e.select("[:Left input]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftLabel()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftLabel")
                val r2 = e.select(":leftLabel")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftLabel")
                val r3 = e.select("[:Left label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left label]")
                // Assert
                assertThat(r1.id).isEqualTo("TextView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftLabel("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftImage()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftImage")
                val r2 = e.select(":leftImage")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftImage")
                val r3 = e.select("[:Left image]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftButton()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftButton")
                val r2 = e.select(":leftButton")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftButton")
                val r3 = e.select("[:Left button]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftSwitch()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftSwitch")
                val r2 = e.select(":leftSwitch")
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftSwitch")
                val r3 = e.select("[:Left switch]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftSwitch("[1]"))
            }
        }

        // left(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-1")

            run {
                // Act, Assert
                val r1 = e.left(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:left")
                val r2 = e.select(":left(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:left")
                val r3 = e.select("[:Left]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.left("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.left(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:left(2)")
                val r2 = e.select(":left(2)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:left(2)")
                val r3 = e.select("[:Left(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left(2)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.left("[2]"))
            }
            run {
                // Act, Assert
                val r1 = e.left(3)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:left(3)")
                val r2 = e.select(":left(3)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:left(3)")
                val r3 = e.select("[:Left(3)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left(3)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.left("[3]"))
            }
            run {
                // Act, Assert
                val r1 = e.left(4)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:left(4)")
                val r2 = e.select(":left(4)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:left(4)")
                val r3 = e.select("[:Left(4)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left(4)]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.left("[4]"))
            }
            run {
                // Act, Assert
                val r1 = e.left(5)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:left(5)")
                val r2 = e.select(":left(5)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:left(5)")
                val r3 = e.select("[:Left(5)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left(5)]")
                // Assert
                assertThat(e.left(5).id).isEqualTo("TextView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.left("[5]"))
            }
            run {
                // Act, Assert
                val r1 = e.left(6)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:left(6)")
                val r2 = e.select(":left(6)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:left(6)")
                val r3 = e.select("[:Left(6)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left(6)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.left("[6]").isEmpty).isTrue()
            }
        }

        // leftLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-1")

            run {
                // Act, Assert
                val r1 = e.leftLabel(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftLabel")
                val r2 = e.select(":leftLabel(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftLabel")
                val r3 = e.select("[:Left label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left label]")
                // Assert
                assertThat(r1.id).isEqualTo("TextView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftLabel("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftLabel(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftLabel(2)")
                val r2 = e.select(":leftLabel(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftLabel(2)")
                val r3 = e.select("[:Left label(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left label(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(e.leftLabel("[2]").isEmpty).isTrue()
            }
        }

        // leftInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-1")

            run {
                // Act, Assert
                val r1 = e.leftInput(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftInput")
                val r2 = e.select(":leftInput(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftInput")
                val r3 = e.select("[:Left input]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftInput(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftInput(2)")
                val r2 = e.select(":leftInput(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftInput(2)")
                val r3 = e.select("[:Left input(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left input(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.leftInput("[2]").isEmpty).isTrue()
            }
        }

        // leftImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-1")

            run {
                // Act, Assert
                val r1 = e.leftImage(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftImage")
                val r2 = e.select(":leftImage(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftImage")
                val r3 = e.select("[:Left image]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftImage(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftImage(2)")
                val r2 = e.select(":leftImage(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftImage(2)")
                val r3 = e.select("[:Left image(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left image(2)]")
                // Assert
                assertThat(r1.isEmpty).isEqualTo(true)
                assertThat(r2.isEmpty).isEqualTo(true)
                assertThat(r3.isEmpty).isEqualTo(true)
                assertThat(e.leftImage("[2]").isEmpty).isTrue()
            }
        }

        // leftButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-1")
            run {
                // Act, Assert
                val r1 = e.leftButton(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftButton")
                val r2 = e.select(":leftButton(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftButton")
                val r3 = e.select("[:Left button]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftButton(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftButton(2)")
                val r2 = e.select(":leftButton(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftButton(2)")
                val r3 = e.select("[:Left button(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left button(2)]")
                // Assert
                assertThat(r1.isEmpty).isEqualTo(true)
                assertThat(r2.isEmpty).isEqualTo(true)
                assertThat(r3.isEmpty).isEqualTo(true)
                assertThat(e.leftButton("[2]").isEmpty).isTrue()
            }
        }

        // leftSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-1")
            run {
                // Act, Assert
                val r1 = e.leftSwitch(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftSwitch")
                val r2 = e.select(":leftSwitch(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftSwitch")
                val r3 = e.select("[:Left switch]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-1")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.leftSwitch("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.leftSwitch(3)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-1>:leftSwitch(3)")
                val r2 = e.select(":leftSwitch(3)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-1>:leftSwitch(3)")
                val r3 = e.select("[:Left switch(3)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-1>[:Left switch(3)]")
                // Assert
                assertThat(r1.isEmpty).isEqualTo(true)
                assertThat(r2.isEmpty).isEqualTo(true)
                assertThat(r3.isEmpty).isEqualTo(true)
                assertThat(e.leftSwitch("[3]").isEmpty).isTrue()
            }
        }
    }

    @Test
    fun below() {
        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest2)
        TestElementCache.synced = true
        ScreenRepository.setup(screensDirectory = "unitTestData/testConfig/nicknames1/screens/relative")
        TestDriver.currentScreen = "[RelativeCoordinateTest2 Screen]"

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1 |FrameLayout1-2 |FrameLayout1-3 |
         *   |TextView1-1    |TextView1-2    |TextView1-3    |
         *   |EditText1-1    |EditText1-2    |EditText1-3    |
         *   |ImageView1-1   |ImageView1-2   |ImageView1-3   |
         *   |ImageButton1-1 |ImageButton1-2 |ImageButton1-3 |
         *   |Switch1-1      |Switch1-2      |Switch1-3      |
         *   |CheckBox1-1    |CheckBox1-2    |CheckBox1-3    |
         *   |LinearLayout1-1|LinearLayout1-2|LinearLayout1-3|
         *   |Scrollable1-1  |Scrollable1-2  |Scrollable1-3  |
         *   |Scrollable2-1  |Scrollable2-2  |               |
         */

        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-2")
            run {
                // Act, Assert
                val r1 = e.below()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:below")
                val r2 = e.select(":below")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:below")
                val r3 = e.select("[:Below]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.below("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.below(expression = ".android.widget.LinearLayout")
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:below(.android.widget.LinearLayout)")
                assertThat(r1.isFound).isTrue()
                // Act, Assert
                val r2 = e.below(expression = ".android.widget.LinearLayout&&[1]")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:below(.android.widget.LinearLayout&&[1])")
                assertThat(r2.isFound).isTrue()
            }
            run {
                // Act, Assert
                val r1 = e.belowLabel()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowLabel")
                val r2 = e.select(":belowLabel", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowLabel")
                val r3 = e.select("[:Below label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below label]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
            run {
                // Act, Assert
                val r1 = e.belowInput()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowInput")
                val r2 = e.select(":belowInput")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowInput")
                val r3 = e.select("[:Below input]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowImage()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowImage")
                val r2 = e.select(":belowImage")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowImage")
                val r3 = e.select("[:Below image]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowButton()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowButton")
                val r2 = e.select(":belowButton")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowButton")
                val r3 = e.select("[:Below button]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowSwitch()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowSwitch")
                val r2 = e.select(":belowSwitch")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowSwitch")
                val r3 = e.select("[:Below switch]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowSwitch("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowScrollable()
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowScrollable")
                val r2 = e.select(":belowScrollable")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowScrollable")
                val r3 = e.select("[:Below scrollable]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below scrollable]")
                // Assert
                assertThat(r1.id).isEqualTo("Scrollable1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowScrollable("[1]"))
            }
        }

        // below(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-2")

            run {
                // Act, Assert
                val r1 = e.below(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:below")
                val r2 = e.select(":below(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:below")
                val r3 = e.select("[:Below]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.below("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.below(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:below(2)")
                val r2 = e.select(":below(2)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:below(2)")
                val r3 = e.select("[:Below(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below(2)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.below("[2]"))
            }
            run {
                // Act, Assert
                val r1 = e.below(3)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:below(3)")
                val r2 = e.select(":below(3)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:below(3)")
                val r3 = e.select("[:Below(3)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below(3)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.below("[3]"))
            }
            run {
                // Act, Assert
                val r1 = e.below(4)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:below(4)")
                val r2 = e.select(":below(4)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:below(4)")
                val r3 = e.select("[:Below(4)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below(4)]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.below("[4]"))
            }
            run {
                // Act, Assert
                val r1 = e.below(5)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:below(5)")
                val r2 = e.select(":below(5)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:below(5)")
                val r3 = e.select("[:Below(5)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below(5)]")
                // Assert
                assertThat(r1.id).isEqualTo("CheckBox1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.below("[5]"))
            }
            run {
                // Act, Assert
                val r1 = e.below(6)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:below(6)")
                val r2 = e.select(":below(6)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:below(6)")
                val r3 = e.select("[:Below(6)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below(6)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(e.below("[6]").isEmpty).isTrue()
            }
        }

        // belowLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-1")

            run {
                // Act, Assert
                val r1 = e.belowLabel(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:belowLabel")
                val r2 = e.select(":belowLabel(1)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:belowLabel")
                val r3 = e.select("[:Below label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Below label]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
            run {
                // Act, Assert
                val r1 = e.belowLabel(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-1>:belowLabel(2)")
                val r2 = e.select(":belowLabel(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-1>:belowLabel(2)")
                val r3 = e.select("[:Below label(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-1>[:Below label(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.belowLabel("[2]").isEmpty).isTrue()
            }
        }

        // belowInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-2")

            run {
                // Act, Assert
                val r1 = e.belowInput(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowInput")
                val r2 = e.select(":belowInput(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowInput")
                val r3 = e.select("[:Below input]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowInput(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowInput(2)")
                val r2 = e.select(":belowInput(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowInput(2)")
                val r3 = e.select("[:Below input(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below input(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.belowInput("[2]").isEmpty).isTrue()
            }
        }

        // belowImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-2")

            run {
                // Act, Assert
                val r1 = e.belowImage(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowImage")
                val r2 = e.select(":belowImage(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowImage")
                val r3 = e.select("[:Below image]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowImage(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowImage(2)")
                val r2 = e.select(":belowImage(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowImage(2)")
                val r3 = e.select("[:Below image(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below image(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.belowImage("[2]").isEmpty).isTrue()
            }
        }

        // belowButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-2")

            run {
                // Act, Assert
                val r1 = e.belowButton(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowButton")
                val r2 = e.select(":belowButton(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowButton")
                val r3 = e.select("[:Below button]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowButton(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowButton(2)")
                val r2 = e.select(":belowButton(2)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowButton(2)")
                val r3 = e.select("[:Below button(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below button(2)]")
                // Assert
                assertThat(r1.id).isEqualTo("CheckBox1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowButton("[2]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowButton(3)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowButton(3)")
                val r2 = e.select(":belowButton(3)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowButton(3)")
                val r3 = e.select("[:Below button(3)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below button(3)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.belowButton("[3]").isEmpty).isTrue()
            }
        }

        // belowSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#TextView1-2")
            run {
                // Act, Assert
                val r1 = e.belowSwitch(1)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowSwitch")
                val r2 = e.select(":belowSwitch(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowSwitch")
                val r3 = e.select("[:Below switch]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.belowSwitch("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.belowSwitch(2)
                assertThat(r1.selector.toString()).isEqualTo("<#TextView1-2>:belowSwitch(2)")
                val r2 = e.select(":belowSwitch(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#TextView1-2>:belowSwitch(2)")
                val r3 = e.select("[:Below switch(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#TextView1-2>[:Below switch(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
                assertThat(e.belowSwitch("[2]").isEmpty).isTrue()
            }
        }
    }

    @Test
    fun above() {
        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest2)
        TestElementCache.synced = true
        ScreenRepository.setup(screensDirectory = "unitTestData/testConfig/nicknames1/screens/relative")
        TestDriver.switchScreen("[RelativeCoordinateTest2 Screen]")

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1 |FrameLayout1-2 |FrameLayout1-3 |
         *   |TextView1-1    |TextView1-2    |TextView1-3    |
         *   |EditText1-1    |EditText1-2    |EditText1-3    |
         *   |ImageView1-1   |ImageView1-2   |ImageView1-3   |
         *   |ImageButton1-1 |ImageButton1-2 |ImageButton1-3 |
         *   |Switch1-1      |Switch1-2      |Switch1-3      |
         *   |CheckBox1-1    |CheckBox1-2    |CheckBox1-3    |
         *   |LinearLayout1-1|LinearLayout1-2|LinearLayout1-3|
         *   |Scrollable1-1  |Scrollable1-2  |Scrollable1-3  |
         *   |Scrollable2-1  |Scrollable2-2  |               |
         */

        classic.enableCache()

        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-2")

            run {
                // Act, Assert
                val r1 = e.above()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:above")
                val r2 = e.select(":above")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:above")
                val r3 = e.select("[:Above]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.above("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveLabel()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveLabel")
                val r2 = e.select(":aboveLabel")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveLabel")
                val r3 = e.select("[:Above label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above label]")
                // Assert
                assertThat(e.aboveLabel().id).isEqualTo("TextView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveLabel("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveInput()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveInput")
                val r2 = e.select(":aboveInput")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveInput")
                val r3 = e.select("[:Above input]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveImage()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveImage")
                val r2 = e.select(":aboveImage")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveImage")
                val r3 = e.select("[:Above image]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveButton()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveButton")
                val r2 = e.select(":aboveButton", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveButton")
                val r3 = e.select("[:Above button]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveSwitch()
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveSwitch")
                val r2 = e.select(":aboveSwitch")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveSwitch")
                val r3 = e.select("[:Above switch]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveSwitch("[1]"))
            }
            run {
                val s = TestElementCache.select("#Scrollable2-2")
                // Act, Assert
                val r1 = s.aboveScrollable()
                assertThat(r1.selector.toString()).isEqualTo("<#Scrollable2-2>:aboveScrollable")
                val r2 = s.select(":aboveScrollable")
                assertThat(r2.selector.toString()).isEqualTo("<#Scrollable2-2>:aboveScrollable")
                val r3 = s.select("[:Above scrollable]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#Scrollable2-2>[:Above scrollable]")
                // Assert
                assertThat(r1.id).isEqualTo("Scrollable1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(s.aboveScrollable("[1]"))
            }
        }

        // above(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-2")

            run {
                // Act, Assert
                val r1 = e.above(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:above")
                val r2 = e.select(":above(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:above")
                val r3 = e.select("[:Above]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.above("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.above(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:above(2)")
                val r2 = e.select(":above(2)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:above(2)")
                val r3 = e.select("[:Above(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above(2)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.above("[2]"))
            }
            run {
                // Act, Assert
                val r1 = e.above(3)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:above(3)")
                val r2 = e.select(":above(3)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:above(3)")
                val r3 = e.select("[:Above(3)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above(3)]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.above("[3]"))
            }
            run {
                // Act, Assert
                val r1 = e.above(4)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:above(4)")
                val r2 = e.select(":above(4)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:above(4)")
                val r3 = e.select("[:Above(4)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above(4)]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.above("[4]"))
            }
            run {
                // Act, Assert
                val r1 = e.above(5)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:above(5)")
                val r2 = e.select(":above(5)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:above(5)")
                val r3 = e.select("[:Above(5)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above(5)]")
                // Assert
                assertThat(r1.id).isEqualTo("TextView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.above("[5]"))
            }
            run {
                // Act, Assert
                val r1 = e.above(6)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:above(6)")
                val r2 = e.select(":above(6)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:above(6)")
                val r3 = e.select("[:Above(6)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above(6)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }

        // aboveLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-2")

            run {
                // Act, Assert
                val r1 = e.aboveLabel(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveLabel")
                val r2 = e.select(":aboveLabel(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveLabel")
                val r3 = e.select("[:Above label]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above label]")
                // Assert
                assertThat(r1.id).isEqualTo("TextView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveLabel("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveLabel(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveLabel(2)")
                val r2 = e.select(":aboveLabel(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveLabel(2)")
                val r3 = e.select("[:Above label(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above label(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }

        // aboveInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-2")

            run {
                // Act, Assert
                val r1 = e.aboveInput(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveInput")
                val r2 = e.select(":aboveInput(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveInput")
                val r3 = e.select("[:Above input]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above input]")
                // Assert
                assertThat(r1.id).isEqualTo("EditText1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveInput("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveInput(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveInput(2)")
                val r2 = e.select(":aboveInput(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveInput(2)")
                val r3 = e.select("[:Above input(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above input(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }

        // aboveImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-2")

            run {
                // Act, Assert
                val r1 = e.aboveImage(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveImage")
                val r2 = e.select(":aboveImage(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveImage")
                val r3 = e.select("[:Above image]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above image]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageView1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveImage("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveImage(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveImage(2)")
                val r2 = e.select(":aboveImage(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveImage(2)")
                val r3 = e.select("[:Above image(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above image(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }

        // aboveButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-2")

            run {
                // Act, Assert
                val r1 = e.aboveButton(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveButton")
                val r2 = e.select(":aboveButton(1)")
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveButton")
                val r3 = e.select("[:Above button]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above button]")
                // Assert
                assertThat(r1.id).isEqualTo("ImageButton1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveButton("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveButton(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveButton(2)")
                val r2 = e.select(":aboveButton(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveButton(2)")
                val r3 = e.select("[:Above button(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above button(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }

        // aboveSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#CheckBox1-2")

            run {
                // Act, Assert
                val r1 = e.aboveSwitch(1)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveSwitch")
                val r2 = e.select(":aboveSwitch(1)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveSwitch")
                val r3 = e.select("[:Above switch]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above switch]")
                // Assert
                assertThat(r1.id).isEqualTo("Switch1-2")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1).isEqualTo(r3)
                assertThat(r1).isEqualTo(e.aboveSwitch("[1]"))
            }
            run {
                // Act, Assert
                val r1 = e.aboveSwitch(2)
                assertThat(r1.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveSwitch(2)")
                val r2 = e.select(":aboveSwitch(2)", throwsException = false)
                assertThat(r2.selector.toString()).isEqualTo("<#CheckBox1-2>:aboveSwitch(2)")
                val r3 = e.select("[:Above switch(2)]", throwsException = false)
                assertThat(r3.selector.toString()).isEqualTo("<#CheckBox1-2>[:Above switch(2)]")
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r3.isEmpty).isTrue()
            }
        }
    }
}