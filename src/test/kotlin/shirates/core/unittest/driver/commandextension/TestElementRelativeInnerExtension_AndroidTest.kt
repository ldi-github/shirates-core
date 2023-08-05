package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.logging.printInfo
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class TestElementRelativeInnerExtension_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.RelativeCoordinateTest)
        TestElementCache.synced = true
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun innerWidget() {
        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerWidget = e.innerWidget()
            assertThat(innerWidget.id).isEqualTo("TextView1-1")
            assertThat(innerWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerWidget")
        }
        run {
            // Act, Assert
            val innerWidget = e.innerWidget("[1]")
            assertThat(innerWidget.id).isEqualTo("TextView1-1")
            assertThat(innerWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerWidget")
        }
        run {
            // Act, Assert
            val innerWidget = e.innerWidget(2)
            assertThat(innerWidget.id).isEqualTo("EditText1-1")
            assertThat(innerWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerWidget(2)")
        }
        run {
            // Act, Assert
            val innerWidget = e.innerWidget(6)
            assertThat(innerWidget.id).isEqualTo("CheckBox1-1")
            assertThat(innerWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerWidget(6)")
        }
        run {
            // Act, Assert
            val innerWidget = e.innerWidget(7)
            assertThat(innerWidget.id).isEqualTo("TextView2-1")
            assertThat(innerWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerWidget(7)")
        }
        run {
            // Act, Assert
            val innerWidget = e.innerWidget(18)
            assertThat(innerWidget.id).isEqualTo("CheckBox3-1")
            assertThat(innerWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerWidget(18)")
        }
        run {
            // Act, Assert
            val innerWidget = e.innerWidget(19)
            assertThat(innerWidget.isEmpty).isEqualTo(true)
            assertThat(innerWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerWidget(19)")
        }

        e.innerWidget("*Text*&&[1]").printInfo()
        e.innerWidget("*Text*&&[2]").printInfo()
        e.innerWidget("*Text*&&[3]").printInfo()
        e.innerWidget("*Text*&&[4]").printInfo()
        e.innerWidget("*Text*&&[5]").printInfo()
        e.innerWidget("*Text*&&[6]").printInfo()

        run {
            // Act, Assert
            val innerWidget = e.innerWidget("*Text*&&[4]")
            assertThat(innerWidget.id).isEqualTo("EditText2-1")
            assertThat(innerWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerWidget(*Text*&&[4])")
        }
    }

    @Test
    fun innerLabel() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerLabel = e.innerLabel()
            assertThat(innerLabel.id).isEqualTo("TextView1-1")
            assertThat(innerLabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerLabel")
        }
        run {
            // Act, Assert
            val innerLabel = e.innerLabel(1)
            assertThat(innerLabel.id).isEqualTo("TextView1-1")
            assertThat(innerLabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerLabel")
        }
        run {
            // Act, Assert
            val innerLabel = e.innerLabel(2)
            assertThat(innerLabel.id).isEqualTo("TextView2-1")
            assertThat(innerLabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerLabel(2)")
        }
        run {
            // Act, Assert
            val innerLabel = e.innerLabel(3)
            assertThat(innerLabel.id).isEqualTo("TextView3-1")
            assertThat(innerLabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerLabel(3)")
        }
        run {
            // Act, Assert
            val innerLabel = e.innerLabel(4)
            assertThat(innerLabel.isEmpty).isTrue()
            assertThat(innerLabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerLabel(4)")
        }

        e.innerLabel("*Text*&&[1]").printInfo()
        e.innerLabel("*Text*&&[2]").printInfo()
        e.innerLabel("*Text*&&[3]").printInfo()
        e.innerLabel("*Text*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerLabel = e.innerLabel("*Text*&&[3]")
            assertThat(innerLabel.id).isEqualTo("TextView3-1")
            assertThat(innerLabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerLabel(*Text*&&[3])")
        }

    }

    @Test
    fun innerInput() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerInput = e.innerInput()
            assertThat(innerInput.id).isEqualTo("EditText1-1")
            assertThat(innerInput.selector.toString()).isEqualTo("<#FrameLayout1>:innerInput")
        }
        run {
            // Act, Assert
            val innerInput = e.innerInput(1)
            assertThat(innerInput.id).isEqualTo("EditText1-1")
            assertThat(innerInput.selector.toString()).isEqualTo("<#FrameLayout1>:innerInput")
        }
        run {
            // Act, Assert
            val innerInput = e.innerInput(2)
            assertThat(innerInput.id).isEqualTo("EditText2-1")
            assertThat(innerInput.selector.toString()).isEqualTo("<#FrameLayout1>:innerInput(2)")
        }
        run {
            // Act, Assert
            val innerInput = e.innerInput(3)
            assertThat(innerInput.id).isEqualTo("EditText3-1")
            assertThat(innerInput.selector.toString()).isEqualTo("<#FrameLayout1>:innerInput(3)")
        }
        run {
            // Act, Assert
            val innerInput = e.innerInput(4)
            assertThat(innerInput.isEmpty).isTrue()
            assertThat(innerInput.selector.toString()).isEqualTo("<#FrameLayout1>:innerInput(4)")
        }

        e.innerInput("*Text*&&[1]").printInfo()
        e.innerInput("*Text*&&[2]").printInfo()
        e.innerInput("*Text*&&[3]").printInfo()
        e.innerInput("*Text*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerInput = e.innerInput("*Text*&&[3]")
            assertThat(innerInput.id).isEqualTo("EditText3-1")
            assertThat(innerInput.selector.toString()).isEqualTo("<#FrameLayout1>:innerInput(*Text*&&[3])")
        }

    }

    @Test
    fun innerImage() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerImage = e.innerImage()
            assertThat(innerImage.id).isEqualTo("ImageView1-1")
            assertThat(innerImage.selector.toString()).isEqualTo("<#FrameLayout1>:innerImage")
        }
        run {
            // Act, Assert
            val innerImage = e.innerImage(1)
            assertThat(innerImage.id).isEqualTo("ImageView1-1")
            assertThat(innerImage.selector.toString()).isEqualTo("<#FrameLayout1>:innerImage")
        }
        run {
            // Act, Assert
            val innerImage = e.innerImage(2)
            assertThat(innerImage.id).isEqualTo("ImageView2-1")
            assertThat(innerImage.selector.toString()).isEqualTo("<#FrameLayout1>:innerImage(2)")
        }
        run {
            // Act, Assert
            val innerImage = e.innerImage(3)
            assertThat(innerImage.id).isEqualTo("ImageView3-1")
            assertThat(innerImage.selector.toString()).isEqualTo("<#FrameLayout1>:innerImage(3)")
        }
        run {
            // Act, Assert
            val innerImage = e.innerImage(4)
            assertThat(innerImage.isEmpty).isTrue()
            assertThat(innerImage.selector.toString()).isEqualTo("<#FrameLayout1>:innerImage(4)")
        }

        e.innerImage("*Image*&&[1]").printInfo()
        e.innerImage("*Image*&&[2]").printInfo()
        e.innerImage("*Image*&&[3]").printInfo()
        e.innerImage("*Image*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerImage = e.innerImage("*Image*&&[3]")
            assertThat(innerImage.id).isEqualTo("ImageView3-1")
            assertThat(innerImage.selector.toString()).isEqualTo("<#FrameLayout1>:innerImage(*Image*&&[3])")
        }

    }

    @Test
    fun innerButton() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerButton = e.innerButton()
            assertThat(innerButton.id).isEqualTo("ImageButton1-1")
            assertThat(innerButton.selector.toString()).isEqualTo("<#FrameLayout1>:innerButton")
        }
        run {
            // Act, Assert
            val innerButton = e.innerButton(1)
            assertThat(innerButton.id).isEqualTo("ImageButton1-1")
            assertThat(innerButton.selector.toString()).isEqualTo("<#FrameLayout1>:innerButton")
        }
        run {
            // Act, Assert
            val innerButton = e.innerButton(2)
            assertThat(innerButton.id).isEqualTo("CheckBox1-1")
            assertThat(innerButton.selector.toString()).isEqualTo("<#FrameLayout1>:innerButton(2)")
        }
        run {
            // Act, Assert
            val innerButton = e.innerButton(3)
            assertThat(innerButton.id).isEqualTo("ImageButton2-1")
            assertThat(innerButton.selector.toString()).isEqualTo("<#FrameLayout1>:innerButton(3)")
        }
        run {
            // Act, Assert
            val innerButton = e.innerButton(7)
            assertThat(innerButton.isEmpty).isTrue()
            assertThat(innerButton.selector.toString()).isEqualTo("<#FrameLayout1>:innerButton(7)")
        }

        e.innerButton("*Button*&&[1]").printInfo()
        e.innerButton("*Button*&&[2]").printInfo()
        e.innerButton("*Button*&&[3]").printInfo()
        e.innerButton("*Button*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerButton = e.innerButton("*Button*&&[3]")
            assertThat(innerButton.id).isEqualTo("ImageButton3-1")
            assertThat(innerButton.selector.toString()).isEqualTo("<#FrameLayout1>:innerButton(*Button*&&[3])")
        }

    }

    @Test
    fun innerSwitch() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerSwitch = e.innerSwitch()
            assertThat(innerSwitch.id).isEqualTo("Switch1-1")
            assertThat(innerSwitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerSwitch")
        }
        run {
            // Act, Assert
            val innerSwitch = e.innerSwitch(1)
            assertThat(innerSwitch.id).isEqualTo("Switch1-1")
            assertThat(innerSwitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerSwitch")
        }
        run {
            // Act, Assert
            val innerSwitch = e.innerSwitch(2)
            assertThat(innerSwitch.id).isEqualTo("Switch2-1")
            assertThat(innerSwitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerSwitch(2)")
        }
        run {
            // Act, Assert
            val innerSwitch = e.innerSwitch(3)
            assertThat(innerSwitch.id).isEqualTo("Switch3-1")
            assertThat(innerSwitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerSwitch(3)")
        }
        run {
            // Act, Assert
            val innerSwitch = e.innerSwitch(7)
            assertThat(innerSwitch.isEmpty).isTrue()
            assertThat(innerSwitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerSwitch(7)")
        }

        e.innerSwitch("*Switch*&&[1]").printInfo()
        e.innerSwitch("*Switch*&&[2]").printInfo()
        e.innerSwitch("*Switch*&&[3]").printInfo()
        e.innerSwitch("*Switch*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerSwitch = e.innerSwitch("*Switch*&&[3]")
            assertThat(innerSwitch.id).isEqualTo("Switch3-1")
            assertThat(innerSwitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerSwitch(*Switch*&&[3])")
        }

    }

    @Test
    fun innerVWidget() {
        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget()
            assertThat(innerVWidget.id).isEqualTo("TextView1-1")
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget")
        }
        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget("[1]")
            assertThat(innerVWidget.id).isEqualTo("TextView1-1")
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget")
        }
        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget(2)
            assertThat(innerVWidget.id).isEqualTo("TextView2-1")
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget(2)")
        }
        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget(3)
            assertThat(innerVWidget.id).isEqualTo("TextView3-1")
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget(3)")
        }
        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget(4)
            assertThat(innerVWidget.id).isEqualTo("EditText1-1")
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget(4)")
        }
        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget(6)
            assertThat(innerVWidget.id).isEqualTo("EditText3-1")
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget(6)")
        }
        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget(7)
            assertThat(innerVWidget.id).isEqualTo("ImageView1-1")
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget(7)")
        }
        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget(18)
            assertThat(innerVWidget.id).isEqualTo("CheckBox3-1")
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget(18)")
        }
        run {
            // Act, Assert
            val innerVWidget = e.innerVWidget(19)
            assertThat(innerVWidget.isEmpty).isTrue()
            assertThat(innerVWidget.selector.toString()).isEqualTo("<#FrameLayout1>:innerVWidget(19)")
        }
    }

    @Test
    fun innerVlabel() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerVlabel = e.innerVlabel()
            assertThat(innerVlabel.id).isEqualTo("TextView1-1")
            assertThat(innerVlabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerVlabel")
        }
        run {
            // Act, Assert
            val innerVlabel = e.innerVlabel(1)
            assertThat(innerVlabel.id).isEqualTo("TextView1-1")
            assertThat(innerVlabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerVlabel")
        }
        run {
            // Act, Assert
            val innerVlabel = e.innerVlabel(2)
            assertThat(innerVlabel.id).isEqualTo("TextView2-1")
            assertThat(innerVlabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerVlabel(2)")
        }
        run {
            // Act, Assert
            val innerVlabel = e.innerVlabel(3)
            assertThat(innerVlabel.id).isEqualTo("TextView3-1")
            assertThat(innerVlabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerVlabel(3)")
        }
        run {
            // Act, Assert
            val innerVlabel = e.innerVlabel(4)
            assertThat(innerVlabel.isEmpty).isTrue()
            assertThat(innerVlabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerVlabel(4)")
        }

        e.innerVlabel("*Text*&&[1]").printInfo()
        e.innerVlabel("*Text*&&[2]").printInfo()
        e.innerVlabel("*Text*&&[3]").printInfo()
        e.innerVlabel("*Text*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerVlabel = e.innerVlabel("*Text*&&[3]")
            assertThat(innerVlabel.id).isEqualTo("TextView3-1")
            assertThat(innerVlabel.selector.toString()).isEqualTo("<#FrameLayout1>:innerVlabel(*Text*&&[3])")
        }

    }

    @Test
    fun innerVinput() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerVinput = e.innerVinput()
            assertThat(innerVinput.id).isEqualTo("EditText1-1")
            assertThat(innerVinput.selector.toString()).isEqualTo("<#FrameLayout1>:innerVinput")
        }
        run {
            // Act, Assert
            val innerVinput = e.innerVinput(1)
            assertThat(innerVinput.id).isEqualTo("EditText1-1")
            assertThat(innerVinput.selector.toString()).isEqualTo("<#FrameLayout1>:innerVinput")
        }
        run {
            // Act, Assert
            val innerVinput = e.innerVinput(2)
            assertThat(innerVinput.id).isEqualTo("EditText2-1")
            assertThat(innerVinput.selector.toString()).isEqualTo("<#FrameLayout1>:innerVinput(2)")
        }
        run {
            // Act, Assert
            val innerVinput = e.innerVinput(3)
            assertThat(innerVinput.id).isEqualTo("EditText3-1")
            assertThat(innerVinput.selector.toString()).isEqualTo("<#FrameLayout1>:innerVinput(3)")
        }
        run {
            // Act, Assert
            val innerVinput = e.innerVinput(4)
            assertThat(innerVinput.isEmpty).isTrue()
            assertThat(innerVinput.selector.toString()).isEqualTo("<#FrameLayout1>:innerVinput(4)")
        }

        e.innerVinput("*Text*&&[1]").printInfo()
        e.innerVinput("*Text*&&[2]").printInfo()
        e.innerVinput("*Text*&&[3]").printInfo()
        e.innerVinput("*Text*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerVinput = e.innerVinput("*Text*&&[3]")
            assertThat(innerVinput.id).isEqualTo("EditText3-1")
            assertThat(innerVinput.selector.toString()).isEqualTo("<#FrameLayout1>:innerVinput(*Text*&&[3])")
        }

    }

    @Test
    fun innerVimage() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerVimage = e.innerVimage()
            assertThat(innerVimage.id).isEqualTo("ImageView1-1")
            assertThat(innerVimage.selector.toString()).isEqualTo("<#FrameLayout1>:innerVimage")
        }
        run {
            // Act, Assert
            val innerVimage = e.innerVimage(1)
            assertThat(innerVimage.id).isEqualTo("ImageView1-1")
            assertThat(innerVimage.selector.toString()).isEqualTo("<#FrameLayout1>:innerVimage")
        }
        run {
            // Act, Assert
            val innerVimage = e.innerVimage(2)
            assertThat(innerVimage.id).isEqualTo("ImageView2-1")
            assertThat(innerVimage.selector.toString()).isEqualTo("<#FrameLayout1>:innerVimage(2)")
        }
        run {
            // Act, Assert
            val innerVimage = e.innerVimage(3)
            assertThat(innerVimage.id).isEqualTo("ImageView3-1")
            assertThat(innerVimage.selector.toString()).isEqualTo("<#FrameLayout1>:innerVimage(3)")
        }
        run {
            // Act, Assert
            val innerVimage = e.innerVimage(4)
            assertThat(innerVimage.isEmpty).isTrue()
            assertThat(innerVimage.selector.toString()).isEqualTo("<#FrameLayout1>:innerVimage(4)")
        }

        e.innerVimage("*Image*&&[1]").printInfo()
        e.innerVimage("*Image*&&[2]").printInfo()
        e.innerVimage("*Image*&&[3]").printInfo()
        e.innerVimage("*Image*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerVimage = e.innerVimage("*Image*&&[3]")
            assertThat(innerVimage.id).isEqualTo("ImageView3-1")
            assertThat(innerVimage.selector.toString()).isEqualTo("<#FrameLayout1>:innerVimage(*Image*&&[3])")
        }

    }

    @Test
    fun innerVbutton() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerVbutton = e.innerVbutton()
            assertThat(innerVbutton.id).isEqualTo("ImageButton1-1")
            assertThat(innerVbutton.selector.toString()).isEqualTo("<#FrameLayout1>:innerVbutton")
        }
        run {
            // Act, Assert
            val innerVbutton = e.innerVbutton(1)
            assertThat(innerVbutton.id).isEqualTo("ImageButton1-1")
            assertThat(innerVbutton.selector.toString()).isEqualTo("<#FrameLayout1>:innerVbutton")
        }
        run {
            // Act, Assert
            val innerVbutton = e.innerVbutton(2)
            assertThat(innerVbutton.id).isEqualTo("ImageButton2-1")
            assertThat(innerVbutton.selector.toString()).isEqualTo("<#FrameLayout1>:innerVbutton(2)")
        }
        run {
            // Act, Assert
            val innerVbutton = e.innerVbutton(3)
            assertThat(innerVbutton.id).isEqualTo("ImageButton3-1")
            assertThat(innerVbutton.selector.toString()).isEqualTo("<#FrameLayout1>:innerVbutton(3)")
        }
        run {
            // Act, Assert
            val innerVbutton = e.innerVbutton(4)
            assertThat(innerVbutton.id).isEqualTo("CheckBox1-1")
            assertThat(innerVbutton.selector.toString()).isEqualTo("<#FrameLayout1>:innerVbutton(4)")
        }

        e.innerVbutton("*Button*&&[1]").printInfo()
        e.innerVbutton("*Button*&&[2]").printInfo()
        e.innerVbutton("*Button*&&[3]").printInfo()
        e.innerVbutton("*Button*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerVbutton = e.innerVbutton("*Button*&&[3]")
            assertThat(innerVbutton.id).isEqualTo("ImageButton3-1")
            assertThat(innerVbutton.selector.toString()).isEqualTo("<#FrameLayout1>:innerVbutton(*Button*&&[3])")
        }

    }

    @Test
    fun innerVswitch() {

        /**
         * |FrameLayout1|
         *   |FrameLayout1-1|TextView1-1|EditText1-1|ImageView1-1|ImageButton1-1|Switch1-1|CheckBox1-1|LinearLayout1-1|
         *   |FrameLayout2-1|TextView2-1|EditText2-1|ImageView2-1|ImageButton2-1|Switch2-1|CheckBox2-1|LinearLayout2-1|
         *   |FrameLayout3-1|TextView3-1|EditText3-1|ImageView3-1|ImageButton3-1|Switch3-1|CheckBox3-1|LinearLayout3-1|
         */

        // Arrange
        val e = TestElementCache.select("#FrameLayout1")

        run {
            // Act, Assert
            val innerVswitch = e.innerVswitch()
            assertThat(innerVswitch.id).isEqualTo("Switch1-1")
            assertThat(innerVswitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerVswitch")
        }
        run {
            // Act, Assert
            val innerVswitch = e.innerVswitch(1)
            assertThat(innerVswitch.id).isEqualTo("Switch1-1")
            assertThat(innerVswitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerVswitch")
        }
        run {
            // Act, Assert
            val innerVswitch = e.innerVswitch(2)
            assertThat(innerVswitch.id).isEqualTo("Switch2-1")
            assertThat(innerVswitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerVswitch(2)")
        }
        run {
            // Act, Assert
            val innerVswitch = e.innerVswitch(3)
            assertThat(innerVswitch.id).isEqualTo("Switch3-1")
            assertThat(innerVswitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerVswitch(3)")
        }
        run {
            // Act, Assert
            val innerVswitch = e.innerVswitch(4)
            assertThat(innerVswitch.isEmpty).isTrue()
            assertThat(innerVswitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerVswitch(4)")
        }

        e.innerVswitch("*Switch*&&[1]").printInfo()
        e.innerVswitch("*Switch*&&[2]").printInfo()
        e.innerVswitch("*Switch*&&[3]").printInfo()
        e.innerVswitch("*Switch*&&[4]").printInfo()

        run {
            // Act, Assert
            val innerVswitch = e.innerVswitch("*Switch*&&[3]")
            assertThat(innerVswitch.id).isEqualTo("Switch3-1")
            assertThat(innerVswitch.selector.toString()).isEqualTo("<#FrameLayout1>:innerVswitch(*Switch*&&[3])")
        }

    }
}