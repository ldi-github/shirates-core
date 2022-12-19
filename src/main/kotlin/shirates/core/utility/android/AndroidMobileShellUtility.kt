package shirates.core.utility.android

import com.google.common.collect.ImmutableMap
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message

object AndroidMobileShellUtility {

    /**
     * getMap
     */
    fun getMap(command: String, vararg args: String): Map<String, Any> {

        return ImmutableMap.of(
            "command", command,
            "args", args.asList()
        )
    }

    private fun getMapForPut(scope: String, name: String, value: String): Map<String, Any> {

        return getMap(command = "settings", "put", scope, name, value)
    }

    private fun getMapForGet(scope: String, name: String): Map<String, Any> {

        return getMap(command = "settings", "get", scope, name)
    }

    /**
     * getValue
     */
    fun getValue(name: String, scope: String = "global"): Any {

        val map = getMapForGet(name = name, scope = scope)
        val value = executeMobileShell(map)
        return value
    }

    /**
     * setValue
     */
    fun setValue(name: String, value: String, scope: String = "global"): Any {

        if (TestMode.isNoLoadRun) {
            return "[noLoadRun]"
        }

        val map = getMapForPut(name = name, value = value, scope = scope)
        executeMobileShell(map)

        val map2 = getMapForGet(scope = "global", name = name)
        executeMobileShell(map2)
        val newValue = getValue(name = name)

        return newValue
    }

    /**
     * executeMobileShell
     */
    fun executeMobileShell(map: Map<String, Any>): Any {

        try {
            return TestDriver.androidDriver.executeScript("mobile: shell", map)
        } catch (t: Throwable) {
            if ((t.message ?: "").contains("Potentially insecure feature")) {
                throw TestConfigException(message(id = "relaxedSecurityNotAllowed", arg1 = "$map"))
            }
            throw t
        }
    }

    /**
     * getDefaultInputMethod
     */
    fun getDefaultInputMethod(): String {

        return getValue(name = "default_input_method", scope = "secure").toString()
    }

    /**
     * setDefaultInputMethod
     */
    fun setDefaultInputMethod(value: String) {

        setValue(name = "default_input_method", value = value, scope = "secure")
    }
}