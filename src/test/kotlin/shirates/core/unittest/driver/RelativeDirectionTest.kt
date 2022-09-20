package shirates.core.unittest.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.*
import shirates.core.testcode.UnitTest

class RelativeDirectionTest : UnitTest() {

    @Test
    fun isDirection() {

        assertThat(RelativeDirection.right.isRight).isTrue()
        assertThat(RelativeDirection.right.isLeft).isFalse()

        assertThat(RelativeDirection.left.isLeft).isTrue()
        assertThat(RelativeDirection.left.isRight).isFalse()

        assertThat(RelativeDirection.above.isAbove).isTrue()
        assertThat(RelativeDirection.below.isAbove).isFalse()

        assertThat(RelativeDirection.below.isBelow).isTrue()
        assertThat(RelativeDirection.above.isBelow).isFalse()
    }

}