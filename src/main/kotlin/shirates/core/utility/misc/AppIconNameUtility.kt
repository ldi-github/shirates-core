package shirates.core.utility.misc

/**
 * AppIconNameUtility
 */
object AppIconNameUtility {

    fun getAppName(appIconName: String): String {

        if (appIconName.contains(".")) {
            return appIconName.substring(appIconName.lastIndexOf("."))
        } else {
            return appIconName
        }

    }
}