package macro.android

import shirates.core.driver.TestDrive
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

/**
 * This macro is for demonstration
 */
@MacroObject
object TutorialMacro : TestDrive {

    @Macro("[Setup stock]")
    fun setupStock() {

        // Implement procedure
    }

    @Macro("[Login]")
    fun login() {

        // Implement login procedure
    }

    @Macro("[Order Screen]")
    fun orderScreen() {

        // Implement procedure
    }

}