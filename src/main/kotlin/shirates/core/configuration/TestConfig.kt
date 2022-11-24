package shirates.core.configuration

import org.apache.commons.io.FilenameUtils
import org.json.JSONArray
import org.json.JSONObject
import shirates.core.Const
import shirates.core.configuration.repository.DatasetRepositoryManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.JsonUtility
import shirates.core.utility.misc.ReflectionUtility
import shirates.core.utility.toPath
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

/**
 * TestConfig
 */
class TestConfig(val testConfigFile: String) {

    val testConfigPath: Path = testConfigFile.toPath()
    var testConfigName: String? = null
    val commonProfile = TestProfile("common")
    lateinit var jso: JSONObject
    val profileMap: MutableMap<String, TestProfile> = mutableMapOf()
    val importScreenDirectories = mutableListOf<Path>()

    /**
     * companion object
     */
    companion object {

        var lastCreated: TestConfig? = null

        /**
         * profProps
         */
        var profProps = TestProfile::class.memberProperties
            .filter { p -> p.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()
            .map { it.name to it }.toMap()
    }

    /**
     * init
     */
    init {
        loadFromFile()
        lastCreated = this
    }

    override fun toString(): String {
        return testConfigFile
    }

    /**
     * loadFromFile
     */
    private fun loadFromFile() {

        jso = JsonUtility.getJSONObjectFromFile(file = testConfigPath.toString())
        if (jso.has("testConfigName").not()) {
            throw TestConfigException(
                message(id = "requiredInFile", subject = "testConfigName", file = "$testConfigPath")
            )
        }
        testConfigName = jso.getString("testConfigName")

        val baseFineName = FilenameUtils.getBaseName(testConfigPath.fileName.toString())
        if (testConfigName != baseFineName) {
            throw TestConfigException(
                message(id = "setTestConfigNameToTheFileName", subject = testConfigName, file = "$testConfigPath")
            )
        }

        setPropertyFromJSON(commonProfile, jso)

        val defaultProfile = createProfileFromProfjso(profileName = "_default", jso)
        defaultProfile.profileName = "_default"

        if (jso.has("profiles")) {
            val jsoProfiles = jso.getJSONArray("profiles")
            for (i in 0 until jsoProfiles.length()) {
                val profjso = jsoProfiles[i] as JSONObject
                if (profjso.has("profileName").not()) {
                    val ex = TestConfigException("profileName is required.(index=$i) $profjso")
                    TestLog.error(ex)
                    throw ex
                }
                val profileName = profjso["profileName"].toString()
                createProfileFromProfjso(profileName, profjso)
            }
        }
    }

    private fun createProfileFromProfjso(profileName: String, profjso: JSONObject): TestProfile {
        val profile = TestProfile(profileName)
        this.profileMap[profileName] = profile
        setProfileFromJson(profile = profile, profjso = profjso)
        profile.testConfig = this
        profile.testConfigPath = testConfigPath

        if (profile.appiumServerUrl.isNullOrBlank()) {
            profile.appiumServerUrl = Const.APPIUM_SERVER_ADDRESS
        }

        return profile
    }

    private fun setProfileFromJson(profile: TestProfile, profjso: JSONObject) {

        profile.testConfig = this
        profile.testConfigPath = testConfigPath

        // specialTags
        if (profjso.has("specialTags")) {
            profile.specialTags = profjso.getString("specialTags")
        }

        // copy common to profile
        copyValues(from = commonProfile, to = profile)
        copyCapabilities(from = commonProfile, to = profile)
        copySettings(from = commonProfile, to = profile)

        // set property from JSON
        this.setPropertyFromJSON(profile, profjso)

        // startupPackageOrBundleId
        val startupPackageOrBundleId = profile.startupPackageOrBundleId ?: profile.packageOrBundleId
        if (startupPackageOrBundleId != null) {
            if (profile.isAndroid) {
                profile.capabilities["appPackage"] = startupPackageOrBundleId
            } else if (profile.isiOS) {
                profile.capabilities["bundleId"] = startupPackageOrBundleId
            }
        }

        // startupActivity
        if (profile.capabilities.containsKey("appActivity").not() && profile.startupActivity != null) {
            profile.capabilities["appActivity"] = profile.startupActivity!!
        }

        setDefaultValuesFromPropertiesToTestProfile(profile = profile)
        overwriteValuesFromPropertiesToTestProfile(profile = profile)
    }

    /**
     * copyValues
     */
    private fun copyValues(from: TestProfile, to: TestProfile) {

        for (p in profProps) {
            if (profProps.containsKey(p.key)) {
                val value = p.value.getter.call(from)
                val toProps = profProps[p.key]
                toProps!!.setter.call(to, value)
            }
        }
    }

    private fun setDefaultValuesFromPropertiesToTestProfile(profile: TestProfile) {

        val props = PropertiesManager.properties

        /**
         * Set default value
         */
        for (prop in props) {
            val pinfo = TestProfile::class.memberProperties.firstOrNull() { it.name == prop.key }
            if (pinfo == null) {
                TestLog.trace(message(id = "notFound", subject = "Property", value = prop.key.toString()))
            } else {
                val value = pinfo.getter.call(profile)
                if (value == null) {
                    ReflectionUtility.setValue(obj = profile, propertyName = pinfo.name, value = prop.value)
                }
            }
        }
    }

    private fun overwriteValuesFromPropertiesToTestProfile(profile: TestProfile) {

        val props = PropertiesManager.properties

        /**
         * Override settings section
         */
        val settings = props.filter { it.key.toString().startsWith("settings_") }
        for (setting in settings) {
            val key = setting.key.toString().substring("settings_".length)
            profile.settings[key] = setting.value.toString()
        }

        /**
         * Override capabilities section
         */
        val caps = props.filter { it.key.toString().startsWith("capabilities_") }
        for (cap in caps) {
            val key = cap.key.toString().substring("capabilities_".length)
            profile.capabilities[key] = cap.value
        }
    }

    private fun copyCapabilities(from: TestProfile, to: TestProfile) {

        for (key in from.capabilities.keys) {
            to.capabilities[key] = from.capabilities[key]
        }
    }

    private fun copySettings(from: TestProfile, to: TestProfile) {

        for (key in from.settings.keys) {
            to.settings[key] = from.settings[key] as String
        }
    }

    private fun setPropertyFromJSON(target: TestProfile, jsonObject: JSONObject) {

        val jso = jsonObject
        val profile = target

        for (key in jso.keys()) {
            if (key == "dataset") {
                val datasetSection = jso.getJSONObject("dataset")
                setDatasetSection(datasetSection)
            } else if (key == "screens") {
                val screenSection = jso.getJSONObject("screens")
                setScreenSection(screenSection)
            } else if (key == "capabilities") {
                val caps = jso.getJSONObject("capabilities")
                setCapabilities(profile, caps)
            } else if (key == "settings") {
                val settings = jso.getJSONObject("settings")
                setSettings(profile, settings)
            } else {
                setProperty(profile, jso, key)
            }
        }
    }

    private fun setDatasetSection(datasetSection: JSONObject) {
        for (datasetName in datasetSection.keys()) {
            if (datasetSection.get(datasetName) is String) {
                val file = datasetSection.getString(datasetName).toString()
                if (Files.exists(file.toPath()).not()) {
                    throw TestConfigException(message(id = "datasetFileNotFound", dataset = datasetName, file = file))
                }
                if (datasetName != file.toPath().toFile().nameWithoutExtension) {
                    throw TestConfigException(
                        message(
                            id = "datasetNameMustBeSameAsFileName",
                            dataset = datasetName,
                            file = file
                        )
                    )
                }
                DatasetRepositoryManager.loadFromFile(file = file)
            }
        }
    }

    private fun setScreenSection(screenSection: JSONObject) {
        if (screenSection.has("import").not()) {
            return
        }

        importScreenDirectories.clear()
        val impList = screenSection.get("import")
        val importList =
            if (impList is JSONArray) impList
            else throw TestConfigException("import must be JSONArray. (file=$testConfigPath)")
        for (path in importList.map { it.toString() }) {
            if (Files.exists(path.toPath()).not()) {
                throw TestConfigException(message(id = "screensDirectoryNotFound", file = path))
            }
            importScreenDirectories.add(path.toPath())
        }
    }

    private fun setCapabilities(target: TestProfile, jsonObject: JSONObject) {

        jsonObject.keys().forEach {
            target.capabilities[it] = jsonObject.get(it)
        }
    }

    private fun setSettings(targetProfile: TestProfile, jsonObject: JSONObject) {

        jsonObject.keys().forEach {
            targetProfile.settings[it] = jsonObject.getString(it)
        }
    }

    private fun setProperty(
        target: TestProfile,
        jsonObject: JSONObject,
        propertyName: String
    ) {

        // comment(# or //)
        if (propertyName.startsWith("#") || propertyName.startsWith("//")) {
            return
        }

        // profiles
        if (propertyName == "profiles") {
            return
        }

        // Warn when property does not exist
        if (profProps.containsKey(propertyName).not()) {
            TestLog.warn(message(id = "failedToSetProperty", subject = propertyName))
            return
        }

        val prop = profProps[propertyName]
        val value = jsonObject.getString(propertyName)
        prop!!.setter.call(target, value)
    }

    /**
     * get
     */
    operator fun get(profileName: String): TestProfile {

        val p = profileMap[profileName]
        return p ?: throw TestConfigException(
            message(id = "notFound", subject = "profileName", value = profileName)
        )
    }

}