package shirates.core.utility.misc

import org.json.JSONObject
import shirates.core.configuration.NicknameUtility
import shirates.core.driver.TestDriver.testContext
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.rootElement
import shirates.core.storage.App
import shirates.core.storage.app

/**
 * AppNameUtility
 */
object AppNameUtility {

    /**
     * getAppNameWithoutExtension
     */
    fun getAppNameWithoutExtension(appIconName: String): String {

        if (appIconName.contains(".")) {
            return appIconName.substring(0, appIconName.lastIndexOf("."))
        } else {
            return appIconName
        }
    }

    /**
     * getPackageOrBundleId
     */
    fun getPackageOrBundleId(appNameOrAppIdOrActivityName: String): String {

        if (appNameOrAppIdOrActivityName == testContext.profile.appIconName) {
            return testContext.profile.packageOrBundleId!!
        }

        if (NicknameUtility.isValidNickname(appNameOrAppIdOrActivityName)) {
            return app("${appNameOrAppIdOrActivityName}.packageOrBundleId", throwsException = false)
        }

        if (appNameOrAppIdOrActivityName.contains("/")) {
            return appNameOrAppIdOrActivityName.split("/").first()
        }

        val packageOrBundleId = app("[$appNameOrAppIdOrActivityName].packageOrBundleId", throwsException = false)
        if (packageOrBundleId.isNotBlank()) {
            return packageOrBundleId
        }

        return appNameOrAppIdOrActivityName
    }

    /**
     * getAppNameFromPackageName
     */
    fun getAppNameFromPackageName(packageName: String): String {

        if (packageName == testContext.profile.packageOrBundleId) {
            return testContext.profile.appIconName!!
        }

        val appNickName = getAppNickNameFromPackageName(packageName = packageName)
        return appNickName.trimStart('[').trimEnd(']')
    }

    /**
     * getAppNickNameFromPackageName
     */
    fun getAppNickNameFromPackageName(packageName: String): String {

        if (App.repository == null) {
            return ""
        }

        /**
         * Get from apps.json
         */
        val jsonObject = App.repository!!.jsonObject
        for (key in jsonObject.keySet().filter { it != "key" }) {
            val e = jsonObject[key] as JSONObject? ?: continue
            if (e.keySet().contains("packageOrBundleId").not()) continue
            if (e["packageOrBundleId"] == packageName) {
                return key
            }
        }
        return ""
    }

    /**
     * getCurrentAppName
     */
    fun getCurrentAppName(): String {

        if (TestMode.isNoLoadRun) {
            return ""
        }

        if (isiOS) {
            return rootElement.label
        }

        val appNickName = getAppNickNameFromPackageName(packageName = rootElement.packageName)
        if (appNickName.isNotBlank()) {
            return NicknameUtility.getNicknameText(appNickName)
        }

        return rootElement.packageName
    }


}