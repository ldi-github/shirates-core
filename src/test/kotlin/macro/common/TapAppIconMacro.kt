package macro.common

import shirates.core.driver.TestDrive
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ios
import shirates.core.driver.commandextension.*
import shirates.core.driver.wait
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object TapAppIconMacro : TestDrive {

    @Macro("[TapAppIconMacro1]")
    fun tapAppIconMacro1(appIconName: String) {

        android {
            it.pressHome()
            it.flickBottomToTop()

            if (it.canSelectWithScrollDown(appIconName)) {
                it.tap()
                    .wait()
            }
        }
        ios {
            it.tapAppIcon(appIconName)
        }
    }
}