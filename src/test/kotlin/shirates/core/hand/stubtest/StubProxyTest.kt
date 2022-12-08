package shirates.core.hand.stubtest

import org.assertj.core.api.Assertions.assertThat
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.proxy.StubProxy
import shirates.core.testcode.UITest

@Testrun(testrunFile = "unitTestConfig/others/stubConfig/testrun.properties", profile = "Android *")
class StubProxyTest : UITest() {

    @Test
    fun isProfileActivated() {

        run {
            // Arrange
            StubProxy.profile = null
            // Act, Assert
            assertThat(StubProxy.isProfileActivated).isFalse()
        }
        run {
            // Arrange
            StubProxy.profile = "profile1"
            // Act, Assert
            assertThat(StubProxy.isProfileActivated).isTrue()
        }
    }

    @Test
    fun activateProfile() {

        run {
            // Arrange
            StubProxy.profile = null
            assertThat(StubProxy.isProfileActivated).isFalse()
            // Act
            StubProxy.activateProfile("profile1")
            // Assert
            assertThat(StubProxy.isProfileActivated).isTrue()
        }
    }

    @Test
    fun instance() {

        val instanceKey = "i1"
        val profile = "test"

        /**
         * registerInstance
         */
        run {
            val jso = JSONObject(StubProxy.registerInstance(instanceKey = instanceKey, profile = profile).resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey)
            assertThat(jso.get("profile")).isEqualTo(profile)
        }

        /**
         * getInstanceInfo
         */
        run {
            val jso = JSONObject(StubProxy.getInstanceInfo(profileOrInstanceKeyPrefix = profile).resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey)
            assertThat(jso.get("profile")).isEqualTo(profile)
        }
        run {
            val jso = JSONObject(StubProxy.getInstanceInfo(profileOrInstanceKeyPrefix = instanceKey).resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey)
            assertThat(jso.get("profile")).isEqualTo(profile)
        }

        /**
         * getInstanceProfileMap
         */
        run {
            val jso = JSONObject(StubProxy.getInstanceProfileMap().resultString)
            assertThat(jso.has(instanceKey)).isTrue()
            assertThat(jso.get(instanceKey)).isEqualTo(profile)
        }

        /**
         * resetInstance
         */
        run {
            val r = StubProxy.resetInstance()
            assertThat(r.resultString).isEqualTo("")
        }

        /**
         * removeInstance
         */
        run {
            val r = StubProxy.removeInstance(profileOrInstanceKeyPrefix = instanceKey)
            assertThat(r.resultString).isEqualTo("")
        }
    }

    @Test
    fun dataPattern() {

        val instanceKey = "dataPattern-instanceKey"
        val profile = "dataPattern-profile"
        StubProxy.registerInstance(instanceKey = instanceKey, profile = profile)

        /**
         * listDataPattern
         */
        val r = StubProxy.listDataPattern()
        val dataPatterns = JSONArray(r.resultString)
        val defaultItem = dataPatterns.first() as JSONObject
        val urlPath = defaultItem.getString("urlPath")
        val dataPatternName = defaultItem.getString("dataPatternName")

        /**
         * getDataPattern
         */
        val r2 = StubProxy.getDataPattern(urlPathOrApiName = urlPath)
        assertThat(r2.resultString).isEqualTo(dataPatternName)

        /**
         * setDataPattern
         */
        val r3 = StubProxy.setDataPattern(urlPathOrApiName = urlPath, dataPatternName = dataPatternName)
        val setDataPatternResult = JSONObject(r3.resultString)
        assertThat(setDataPatternResult.getString("urlPath")).isEqualTo(urlPath)
        assertThat(setDataPatternResult.getString("dataPatternName")).isEqualTo(dataPatternName)
        assertThat(setDataPatternResult.getString("message")).isEqualTo("data file found.")


        StubProxy.removeInstance(profileOrInstanceKeyPrefix = profile)
    }

}