package macro

import shirates.core.driver.TestDrive
import shirates.core.driver.commandextension.launchApp
import shirates.core.driver.commandextension.tapAppIcon
import shirates.core.driver.testContext
import shirates.core.macro.Macro
import shirates.core.macro.MacroObject

@MacroObject
object LaunchMacroObject : TestDrive {

    @Macro("[My Launch Macro]")
    fun myLaunchMacro(
        appNameOrAppIdOrActivityName: String = testContext.appIconName
    ) {

        if (testContext.profile.profileName.startsWith("SO-02J")) {
            /**
             * Custom launching procedure for device SO-02J
             */
            launchApp(appNameOrAppIdOrActivityName = appNameOrAppIdOrActivityName, launchAppMethod = "shell")
        } else {
            tapAppIcon(appIconName = appNameOrAppIdOrActivityName)
        }
    }

}