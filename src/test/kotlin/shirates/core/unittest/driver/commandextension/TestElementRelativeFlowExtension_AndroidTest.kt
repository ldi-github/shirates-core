package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Selector
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class TestElementRelativeFlowExtension_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest)
        TestElementCache.synced = true
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun flow() {

        /**
         * |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         * |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         * |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#TextView2-1")

        // flow()
        run {
            // Act, Assert
            val flow = e.flow()
            assertThat(flow.id).isEqualTo("EditText2-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow")
        }
        run {
            // Act, Assert
            val flow = e.flow("[1]")
            assertThat(flow.id).isEqualTo("EditText2-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow")
        }

        // flow(pos)
        for (i in 1..10) {
            val x = e.flow(i)
            println("[${x.bounds.centerX},${x.bounds.centerY}] " + x)
        }
        run {
            // Act, Assert
            val flow = e.flow(1)
            assertThat(flow.id).isEqualTo("EditText2-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow")
        }
        run {
            // Act, Assert
            val flow = e.flow(2)
            assertThat(flow.id).isEqualTo("ImageView2-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(2)")
        }
        run {
            // Act, Assert
            val flow = e.flow(3)
            assertThat(flow.id).isEqualTo("ImageButton2-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(3)")
        }
        run {
            // Act, Assert
            val flow = e.flow(4)
            assertThat(flow.id).isEqualTo("Switch2-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(4)")
        }
        run {
            // Act, Assert
            val flow = e.flow(5)
            assertThat(flow.id).isEqualTo("CheckBox2-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(5)")
        }
        run {
            // Act, Assert
            val flow = e.flow(6)
            assertThat(flow.id).isEqualTo("TextView3-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(6)")
        }
        run {
            // Act, Assert
            val flow = e.flow(7)
            assertThat(flow.id).isEqualTo("EditText3-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(7)")
        }
        run {
            // Act, Assert
            val flow = e.flow(8)
            assertThat(flow.id).isEqualTo("ImageView3-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(8)")
        }
        run {
            // Act, Assert
            val flow = e.flow(9)
            assertThat(flow.id).isEqualTo("ImageButton3-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(9)")
        }
        run {
            // Act, Assert
            val flow = e.flow(10)
            assertThat(flow.id).isEqualTo("Switch3-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(10)")
        }
        run {
            // Act, Assert
            val flow = e.flow(11)
            assertThat(flow.id).isEqualTo("CheckBox3-1")
            assertThat(flow.selector.toString()).isEqualTo("<#TextView2-1>:flow(11)")
        }
    }

    @Test
    fun vflow() {

        /**
         * |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         * |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         * |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#EditText2-1")

        // flow()
        run {
            // Act, Assert
            val vflow = e.vflow()
            assertThat(vflow.id).isEqualTo("EditText3-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow")
        }
        run {
            // Act, Assert
            val vflow = e.vflow("[1]")
            assertThat(vflow.id).isEqualTo("EditText3-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow")
        }
        run {
            // Arrange
            val targetElements = TestElementCache.rootElement.descendants.filter { it.id.contains("EditText") }
            // Act, Assert
            assertThat(
                e.vflow(
                    Selector("[1]"),
                    TestElementCache.allElements,
                    margin = 0,
                    frame = null
                ).id
            ).isEqualTo("EditText3-1")
            assertThat(
                e.vflow(
                    Selector("[2]"),
                    TestElementCache.allElements,
                    margin = 0,
                    frame = null
                ).id
            ).isEqualTo("ImageView1-1")
            assertThat(e.vflow(Selector("[1]"), targetElements, margin = 0, frame = null).id).isEqualTo("EditText3-1")
            assertThat(e.vflow(Selector("[2]"), targetElements, margin = 0, frame = null).isEmpty).isTrue()
        }

        // flow(pos)
        for (i in 1..10) {
            val vflow = e.vflow(i)
            println("[${vflow.bounds.centerX},${vflow.bounds.centerY}] " + vflow)
        }
        run {
            // Act, Assert
            val vflow = e.vflow(1)
            assertThat(vflow.id).isEqualTo("EditText3-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(2)
            assertThat(vflow.id).isEqualTo("ImageView1-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(2)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(3)
            assertThat(vflow.id).isEqualTo("ImageView2-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(3)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(4)
            assertThat(vflow.id).isEqualTo("ImageView3-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(4)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(5)
            assertThat(vflow.id).isEqualTo("ImageButton1-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(5)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(6)
            assertThat(vflow.id).isEqualTo("ImageButton2-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(6)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(7)
            assertThat(vflow.id).isEqualTo("ImageButton3-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(7)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(8)
            assertThat(vflow.id).isEqualTo("Switch1-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(8)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(9)
            assertThat(vflow.id).isEqualTo("Switch2-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(9)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(10)
            assertThat(vflow.id).isEqualTo("Switch3-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(10)")
        }
        run {
            // Act, Assert
            val vflow = e.vflow(11)
            assertThat(vflow.id).isEqualTo("CheckBox1-1")
            assertThat(vflow.selector.toString()).isEqualTo("<#EditText2-1>:vflow(11)")
        }
    }

    @Test
    fun flowLabel() {
        /**
         * |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         * |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         * |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#TextView2-1")

        run {
            // Act, Assert
            val flowLabel = e.flowLabel()
            assertThat(flowLabel.id).isEqualTo("TextView3-1")
            assertThat(flowLabel.selector.toString()).isEqualTo("<#TextView2-1>:flowLabel")
        }
        run {
            // Act, Assert
            val flowLabel = e.flowLabel("[1]")
            assertThat(flowLabel.id).isEqualTo("TextView3-1")
            assertThat(flowLabel.selector.toString()).isEqualTo("<#TextView2-1>:flowLabel")
        }
        run {
            // Act, Assert
            val flowLabel = e.flowLabel(1)
            assertThat(flowLabel.id).isEqualTo("TextView3-1")
            assertThat(flowLabel.selector.toString()).isEqualTo("<#TextView2-1>:flowLabel")
        }
        run {
            // Act, Assert
            val flowLabel = e.flowLabel(2)
            assertThat(flowLabel.isEmpty).isEqualTo(true)
            assertThat(flowLabel.selector.toString()).isEqualTo("<#TextView2-1>:flowLabel(2)")
        }
    }

    @Test
    fun flowInput() {
        /**
         * |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         * |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         * |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#TextView2-1")

        run {
            // Act, Assert
            val flowInput = e.flowInput()
            assertThat(flowInput.id).isEqualTo("EditText2-1")
            assertThat(flowInput.selector.toString()).isEqualTo("<#TextView2-1>:flowInput")
        }
        run {
            // Act, Assert
            val flowInput = e.flowInput("[1]")
            assertThat(flowInput.id).isEqualTo("EditText2-1")
            assertThat(flowInput.selector.toString()).isEqualTo("<#TextView2-1>:flowInput")
        }
        run {
            // Act, Assert
            val flowInput = e.flowInput(1)
            assertThat(flowInput.id).isEqualTo("EditText2-1")
            assertThat(flowInput.selector.toString()).isEqualTo("<#TextView2-1>:flowInput")
        }
        run {
            // Act, Assert
            val flowInput = e.flowInput(2)
            assertThat(flowInput.id).isEqualTo("EditText3-1")
            assertThat(flowInput.selector.toString()).isEqualTo("<#TextView2-1>:flowInput(2)")
        }
        run {
            // Act, Assert
            val flowInput = e.flowInput(3)
            assertThat(flowInput.isEmpty).isTrue()
            assertThat(flowInput.selector.toString()).isEqualTo("<#TextView2-1>:flowInput(3)")
        }
    }

    @Test
    fun flowImage() {
        /**
         * |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         * |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         * |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#TextView2-1")

        run {
            // Act, Assert
            val flowImage = e.flowImage()
            assertThat(flowImage.id).isEqualTo("ImageView2-1")
            assertThat(flowImage.selector.toString()).isEqualTo("<#TextView2-1>:flowImage")
        }
        run {
            // Act, Assert
            val flowImage = e.flowImage("[1]")
            assertThat(flowImage.id).isEqualTo("ImageView2-1")
            assertThat(flowImage.selector.toString()).isEqualTo("<#TextView2-1>:flowImage")
        }
        run {
            // Act, Assert
            val flowImage = e.flowImage(1)
            assertThat(flowImage.id).isEqualTo("ImageView2-1")
            assertThat(flowImage.selector.toString()).isEqualTo("<#TextView2-1>:flowImage")
        }
        run {
            // Act, Assert
            val flowImage = e.flowImage(2)
            assertThat(flowImage.id).isEqualTo("ImageView3-1")
            assertThat(flowImage.selector.toString()).isEqualTo("<#TextView2-1>:flowImage(2)")
        }
        run {
            // Act, Assert
            val flowImage = e.flowImage(3)
            assertThat(flowImage.isEmpty).isTrue()
            assertThat(flowImage.toString()).isEqualTo("(empty)")
            assertThat(flowImage.selector.toString()).isEqualTo("<#TextView2-1>:flowImage(3)")
        }
    }

    @Test
    fun flowButton() {
        /**
         * |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         * |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         * |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#TextView2-1")

        run {
            // Act, Assert
            val flowButton = e.flowButton()
            assertThat(flowButton.id).isEqualTo("ImageButton2-1")
            assertThat(flowButton.selector.toString()).isEqualTo("<#TextView2-1>:flowButton")
        }
        run {
            // Act, Assert
            val flowButton = e.flowButton("[1]")
            assertThat(flowButton.id).isEqualTo("ImageButton2-1")
            assertThat(flowButton.selector.toString()).isEqualTo("<#TextView2-1>:flowButton")
        }
        run {
            // Act, Assert
            val flowButton = e.flowButton(1)
            assertThat(flowButton.id).isEqualTo("ImageButton2-1")
            assertThat(flowButton.selector.toString()).isEqualTo("<#TextView2-1>:flowButton")
        }
        run {
            // Act, Assert
            val flowButton = e.flowButton(2)
            assertThat(flowButton.id).isEqualTo("CheckBox2-1")
            assertThat(flowButton.selector.toString()).isEqualTo("<#TextView2-1>:flowButton(2)")
        }
        run {
            // Act, Assert
            val flowButton = e.flowButton(3)
            assertThat(flowButton.id).isEqualTo("ImageButton3-1")
            assertThat(flowButton.selector.toString()).isEqualTo("<#TextView2-1>:flowButton(3)")
        }
        run {
            // Act, Assert
            val flowButton = e.flowButton(4)
            assertThat(flowButton.id).isEqualTo("CheckBox3-1")
            assertThat(flowButton.selector.toString()).isEqualTo("<#TextView2-1>:flowButton(4)")
        }
        run {
            // Act, Assert
            val flowButton = e.flowButton(5)
            assertThat(flowButton.isEmpty).isTrue()
            assertThat(flowButton.selector.toString()).isEqualTo("<#TextView2-1>:flowButton(5)")
        }
    }

    @Test
    fun flowSwitch() {
        /**
         * |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         * |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         * |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#TextView2-1")

        run {
            // Act, Assert
            val flowSwitch = e.flowSwitch()
            assertThat(flowSwitch.id).isEqualTo("Switch2-1")
            assertThat(flowSwitch.selector.toString()).isEqualTo("<#TextView2-1>:flowSwitch")
        }
        run {
            // Act, Assert
            val flowSwitch = e.flowSwitch("[1]")
            assertThat(flowSwitch.id).isEqualTo("Switch2-1")
            assertThat(flowSwitch.selector.toString()).isEqualTo("<#TextView2-1>:flowSwitch")
        }
        run {
            // Act, Assert
            val flowSwitch = e.flowSwitch(1)
            assertThat(flowSwitch.id).isEqualTo("Switch2-1")
            assertThat(flowSwitch.selector.toString()).isEqualTo("<#TextView2-1>:flowSwitch")
        }
        run {
            // Act, Assert
            val flowSwitch = e.flowSwitch(2)
            assertThat(flowSwitch.id).isEqualTo("Switch3-1")
            assertThat(flowSwitch.selector.toString()).isEqualTo("<#TextView2-1>:flowSwitch(2)")
        }
        run {
            // Act, Assert
            val flowSwitch = e.flowSwitch(3)
            assertThat(flowSwitch.isEmpty).isEqualTo(true)
            assertThat(flowSwitch.selector.toString()).isEqualTo("<#TextView2-1>:flowSwitch(3)")
        }
    }

    @Test
    fun flowScrollable() {
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

        // Arrange
        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest2)
        TestElementCache.synced = true
        val e = TestElementCache.select("#TextView1-2")

        run {
            // Act, Assert
            val flowScrollable = e.flowScrollable()
            assertThat(flowScrollable.id).isEqualTo("Scrollable1-1")
            assertThat(flowScrollable.selector.toString()).isEqualTo("<#TextView1-2>:flowScrollable")
        }
        run {
            // Act, Assert
            val flowScrollable = e.flowScrollable("[1]")
            assertThat(flowScrollable.id).isEqualTo("Scrollable1-1")
            assertThat(flowScrollable.selector.toString()).isEqualTo("<#TextView1-2>:flowScrollable")
        }
        run {
            // Act, Assert
            val flowScrollable = e.flowScrollable(1)
            assertThat(flowScrollable.id).isEqualTo("Scrollable1-1")
            assertThat(flowScrollable.selector.toString()).isEqualTo("<#TextView1-2>:flowScrollable")
        }
        run {
            // Act, Assert
            val flowScrollable = e.flowScrollable(2)
            assertThat(flowScrollable.id).isEqualTo("Scrollable1-2")
            assertThat(flowScrollable.selector.toString()).isEqualTo("<#TextView1-2>:flowScrollable(2)")
        }
        run {
            // Act, Assert
            val flowScrollable = e.flowScrollable(3)
            assertThat(flowScrollable.id).isEqualTo("Scrollable1-3")
            assertThat(flowScrollable.selector.toString()).isEqualTo("<#TextView1-2>:flowScrollable(3)")
        }
        run {
            // Act, Assert
            val flowScrollable = e.flowScrollable(4)
            assertThat(flowScrollable.id).isEqualTo("Scrollable2-1")
            assertThat(flowScrollable.selector.toString()).isEqualTo("<#TextView1-2>:flowScrollable(4)")
        }
        run {
            // Act, Assert
            val flowScrollable = e.flowScrollable(99)
            assertThat(flowScrollable.isEmpty).isEqualTo(true)
            assertThat(flowScrollable.selector.toString()).isEqualTo("<#TextView1-2>:flowScrollable(99)")
        }
    }
}