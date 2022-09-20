package macro.android.macro

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.*
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object TapAppIconMacro : TestDrive {

    @Macro("[TapAppIconMacro1]")
    fun tapAppIconMacro1(appIconName: String) {

        it.pressHome()
        it.flickBottomToTop()

        if (it.canSelectWithScrollDown(appIconName)) {
            it.tap()
                .wait()
        }
    }

}