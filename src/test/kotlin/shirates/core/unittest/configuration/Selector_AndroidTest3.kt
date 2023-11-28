package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest

class Selector_AndroidTest3 : UnitTest() {

    @Test
    fun init_relative() {

        TestMode.runAsAndroid {

            run {
                // Act
                val sel = Selector(":right(1)")
                // Assert
                assertThat(sel.command).isEqualTo(":right")
                assertThat(sel.pos).isEqualTo(1)
                assertThat(sel.toString()).isEqualTo(":right")
            }
            run {
                // Act
                val sel = Selector(":right([1])")
                // Assert
                assertThat(sel.command).isEqualTo(":right")
                assertThat(sel.pos).isEqualTo(1)
                assertThat(sel.toString()).isEqualTo(":right")
            }
            run {
                // Act
                val sel = Selector(":right(pos=1)")
                // Assert
                assertThat(sel.command).isEqualTo(":right")
                assertThat(sel.pos).isEqualTo(1)
                assertThat(sel.toString()).isEqualTo(":right")
            }
            run {
                // Act
                val sel = Selector(":right(2)")
                // Assert
                assertThat(sel.command).isEqualTo(":right")
                assertThat(sel.pos).isEqualTo(2)
                assertThat(sel.toString()).isEqualTo(":right(2)")
            }
            run {
                // Act
                val sel = Selector(":right([2])")
                // Assert
                assertThat(sel.command).isEqualTo(":right")
                assertThat(sel.pos).isEqualTo(2)
                assertThat(sel.toString()).isEqualTo(":right(2)")
            }
            run {
                // Act
                val sel = Selector(":right(pos=2)")
                // Assert
                assertThat(sel.command).isEqualTo(":right")
                assertThat(sel.pos).isEqualTo(2)
                assertThat(sel.toString()).isEqualTo(":right(2)")
            }
        }

    }

    @Test
    fun isBased() {

        run {
            val sel = Selector()
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isFalse()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector("text1")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isFalse()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":flow")
            assertThat(sel.isFlowBased).isTrue()
            assertThat(sel.isInnerWidgetBased).isFalse()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":flowInput")
            assertThat(sel.isFlowBased).isTrue()
            assertThat(sel.isInnerWidgetBased).isFalse()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerWidget")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerLabel")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerInput")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerImage")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerButton")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerSwitch")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerVWidget")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerVlabel")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerVinput")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerVimage")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerVbutton")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":innerVswitch")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isTrue()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":parent")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isFalse()
            assertThat(sel.isXmlBased).isTrue()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":right")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isFalse()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isTrue()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":rightLabel")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isFalse()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isTrue()
            assertThat(sel.isOtherBased).isFalse()
        }
        run {
            val sel = Selector(":not")
            assertThat(sel.isFlowBased).isFalse()
            assertThat(sel.isInnerWidgetBased).isFalse()
            assertThat(sel.isXmlBased).isFalse()
            assertThat(sel.isCoordinateBased).isFalse()
            assertThat(sel.isOtherBased).isTrue()
        }
    }

    @Test
    fun relativeCommandNames() {

        assertThat(Selector.flowCommandBaseNames).containsExactlyInAnyOrder(
            ":flow", ":label", ":input", ":image", ":button", ":switch",
            ":vflow"
        )
        assertThat(Selector.innerWidgetCommandBaseNames).containsExactlyInAnyOrder(
            ":innerWidget", ":inner",
            ":innerLabel", ":innerInput", ":innerImage", ":innerButton", ":innerSwitch",
            ":innerVWidget", ":innerV",
            ":innerVlabel", ":innerVinput", ":innerVimage", ":innerVbutton", ":innerVswitch",
        )
        assertThat(Selector.xmlCommandBaseNames).containsExactlyInAnyOrder(
            ":parent", ":child", ":sibling", ":ancestor", ":descendant", ":next", ":pre", ":previous"
        )
        assertThat(Selector.coordinateCommandBaseNames).containsExactlyInAnyOrder(
            ":right", ":below", ":left", ":above"
        )
        assertThat(Selector.otherCommandBaseNames).containsExactlyInAnyOrder(
            ":not"
        )
        run {
            val list = mutableListOf<String>()
            list.addAll(Selector.flowCommandBaseNames)
            list.addAll(Selector.innerWidgetCommandBaseNames)
            list.addAll(Selector.xmlCommandBaseNames)
            list.addAll(Selector.coordinateCommandBaseNames)
            list.addAll(Selector.otherCommandBaseNames)
            assertThat(Selector.relativeCommandBaseNames).containsExactlyInAnyOrderElementsOf(list)
        }
        assertThat(Selector.relativeCommandSubjectNames).containsExactlyInAnyOrder(
            "label", "image", "button", "switch", "input", "widget"
        )
    }

    @Test
    fun posMergeEnabledBaseNames() {

        assertThat(Selector.posMergeEnabledBaseNames).containsExactlyInAnyOrder(
            ":flow", ":label", ":input", ":image", ":button", ":switch",
            ":vflow",
            ":ancestor", ":next", ":pre", ":previous",
            ":right", ":below", ":left", ":above"
        )
    }

}