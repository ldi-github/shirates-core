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

        val instanceKey = "1"
        val profile = "profile1"

        /**
         * registerInstance
         */
        run {
            val resultString = StubProxy.registerInstance(instanceKey = instanceKey, profile = profile).resultString
            val jso = JSONObject(resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey)
            assertThat(jso.get("profile")).isEqualTo(profile)
        }

        /**
         * getInstanceInfo
         */
        run {
            val jso = JSONObject(StubProxy.getInstanceInfo(profile = profile).resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey)
            assertThat(jso.get("profile")).isEqualTo(profile)
        }
        run {
            val jso = JSONObject(StubProxy.getInstanceInfo(profile = instanceKey).resultString)
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
            assertThat(r.resultString).isEqualTo("Reset executed. (profile=profile1)")
        }

        /**
         * removeInstance
         */
        run {
            val r = StubProxy.removeInstance(profile = instanceKey)
            assertThat(r.resultString).isEqualTo("Removed. (profile=$instanceKey)")
        }
    }

    @Test
    fun dataPattern() {

        val instanceKey = "1"
        val profile = "profile1"
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


        StubProxy.removeInstance(profile = profile)
    }

    @Test
    fun complex() {

        val instanceKey1 = "1"
        val profile1 = "profile1"
        val instanceKey2 = "2"
        val profile2 = "profile2"

        StubProxy.resetDataPattern(profile = "")
        StubProxy.removeInstance(profile = profile1)
        StubProxy.removeInstance(profile = profile2)

        /**
         * registerInstance
         */
        run {
            val resultString = StubProxy.registerInstance(instanceKey = instanceKey1, profile = profile1).resultString
            val jso = JSONObject(resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey1)
            assertThat(jso.get("profile")).isEqualTo(profile1)
        }
        run {
            val resultString = StubProxy.registerInstance(instanceKey = instanceKey2, profile = profile2).resultString
            val jso = JSONObject(resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey2)
            assertThat(jso.get("profile")).isEqualTo(profile2)
        }
        StubProxy.profile = null

        /**
         * getInstanceInfo
         */
        run {
            val jso = JSONObject(StubProxy.getInstanceInfo(profile = profile1).resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey1)
            assertThat(jso.get("profile")).isEqualTo(profile1)
        }
        run {
            val jso = JSONObject(StubProxy.getInstanceInfo(profile = instanceKey1).resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey1)
            assertThat(jso.get("profile")).isEqualTo(profile1)
        }
        run {
            val jso = JSONObject(StubProxy.getInstanceInfo(profile = profile2).resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey2)
            assertThat(jso.get("profile")).isEqualTo(profile2)
        }
        run {
            val jso = JSONObject(StubProxy.getInstanceInfo(profile = instanceKey2).resultString)
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey2)
            assertThat(jso.get("profile")).isEqualTo(profile2)
        }

        /**
         * getInstanceProfileMap
         */
        run {
            val jso = JSONObject(StubProxy.getInstanceProfileMap().resultString)
            assertThat(jso.has(instanceKey1)).isTrue()
            assertThat(jso.get(instanceKey1)).isEqualTo(profile1)
            assertThat(jso.has(instanceKey2)).isTrue()
            assertThat(jso.get(instanceKey2)).isEqualTo(profile2)
        }

        /**
         * resetInstance
         */
        run {
            // Arrange
            StubProxy.profile = null
            assertThat(StubProxy.getDataPattern("CustomerList", profile = profile1).resultString).isEqualTo("default")
            // Act
            StubProxy.setDataPattern("CustomerList", "customer/00")
            StubProxy.setDataPattern("CustomerList", "customer/01", profile = profile1)
            StubProxy.setDataPattern("CustomerList", "customer/02", profile = profile2)
            // Assert
            assertThat(StubProxy.getDataPattern("CustomerList").resultString)
                .isEqualTo("customer/00")
            assertThat(StubProxy.getDataPattern("CustomerList", profile = profile1).resultString)
                .isEqualTo("customer/01")
            assertThat(StubProxy.getDataPattern("CustomerList", profile = profile2).resultString)
                .isEqualTo("customer/02")

            // Act
            val r = StubProxy.resetInstance(profile = profile1)
            // Assert
            assertThat(r.resultString).isEqualTo("Reset executed. (profile=$profile1)")
            assertThat(StubProxy.getDataPattern("CustomerList").resultString)
                .isEqualTo("customer/00")
            assertThat(StubProxy.getDataPattern("CustomerList", profile = profile1).resultString)
                .isEqualTo("default")
            assertThat(StubProxy.getDataPattern("CustomerList", profile = profile2).resultString)
                .isEqualTo("customer/02")
        }
        run {
            val r = StubProxy.resetInstance(profile = profile1)
            assertThat(r.resultString).isEqualTo("Reset executed. (profile=$profile1)")
        }
        run {
            val r = StubProxy.resetInstance(profile = "")
            assertThat(r.resultString).isEqualTo("Reset executed. (profile=null)")
        }

        /**
         * removeInstance
         */
        run {
            val r = StubProxy.removeInstance(profile = instanceKey1)
            assertThat(r.resultString).isEqualTo("Removed. (profile=$instanceKey1)")
        }
        run {
            val r = StubProxy.removeInstance(profile = instanceKey1)
            assertThat(r.resultString).isEqualTo("Has no entry. (profile=$instanceKey1)")
        }
        run {
            val r = StubProxy.removeInstance(profile = instanceKey2)
            assertThat(r.resultString).isEqualTo("Removed. (profile=$instanceKey2)")
        }
    }
}