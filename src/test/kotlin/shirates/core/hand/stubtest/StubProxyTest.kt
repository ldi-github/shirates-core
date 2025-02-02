package shirates.core.hand.stubtest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.proxy.StubProxy
import shirates.core.testcode.UITest
import shirates.core.utility.misc.toJSONObject

@Testrun(testrunFile = "unitTestConfig/others/stubConfig/testrun.properties", profile = "Android *")
class StubProxyTest : UITest() {

    @Test
    fun isProfileActivated() {

        run {
            // Arrange
            StubProxy.clearActiveProfile()
            // Act, Assert
            assertThat(StubProxy.isProfileActivated).isFalse()
        }
        run {
            // Arrange
            StubProxy.setActiveProfile("profile1")
            // Act, Assert
            assertThat(StubProxy.isProfileActivated).isTrue()
        }
    }

    @Test
    fun activateProfile() {

        run {
            // Arrange
            StubProxy.clearActiveProfile()
            assertThat(StubProxy.isProfileActivated).isFalse()
            // Act
            StubProxy.setActiveProfile("profile1")
            // Assert
            assertThat(StubProxy.isProfileActivated).isTrue()
        }
    }

    @Test
    fun noInstance() {

        val profile1 = "profile1"
        StubProxy.removeInstance(profile = profile1)

        run {
            val jso = StubProxy.getInstanceInfo(profile = profile1).resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }
        run {
            val jso = StubProxy.resetInstance(profile = profile1).resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }
        run {
            val jso = StubProxy.removeInstance(profile = profile1).resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }
        run {
            val jso = StubProxy.resetDataPattern(profile = profile1).resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }
        run {
            val jso = StubProxy.listDataPattern(profile = profile1).resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }
        run {
            val jso = StubProxy.setDataPattern(
                profile = profile1,
                urlPathOrApiName = "API1",
                dataPatternName = "dataPattern1"
            ).resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }
        run {
            val jso = StubProxy.getDataPattern(
                profile = profile1,
                urlPathOrApiName = "API1"
            ).resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }
    }

    @Test
    fun instance() {

        val instanceKey1 = "1"
        val profile1 = "profile1"

        StubProxy.clearActiveProfile()
        assertThat(StubProxy.activeProfile).isEqualTo("")

        /**
         * registerInstance
         */
        run {
            val jso = StubProxy.registerInstance(instanceKey = instanceKey1, profile = profile1)
                .resultString.toJSONObject()
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey1)
            assertThat(jso.get("profile")).isEqualTo(profile1)
        }

        /**
         * getInstanceInfo by profile
         */
        run {
            val jso = StubProxy.getInstanceInfo(profile = profile1).resultString.toJSONObject()
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey1)
            assertThat(jso.get("profile")).isEqualTo(profile1)
        }
        /**
         * getInstanceInfo by instanceKey
         */
        run {
            val jso = StubProxy.getInstanceInfo(profile = instanceKey1).resultString.toJSONObject()
            assertThat(jso.get("instanceKey")).isEqualTo(instanceKey1)
            assertThat(jso.get("profile")).isEqualTo(profile1)
        }

        /**
         * removeInstance
         */
        run {
            // Act
            val jso = StubProxy.removeInstance(profile = profile1).resultString.toJSONObject()
            // Assert
            assertThat(jso.get("message")).isEqualTo("Removed. (profile=$profile1)")
        }
        run {
            val jso = StubProxy.removeInstance(profile = profile1).resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }
    }

    @Test
    fun getInstanceProfileMap() {

        val instanceKey1 = "1"
        val profile1 = "profile1"
        val instanceKey2 = "2"
        val profile2 = "profile2"

        StubProxy.removeInstance(profile = profile1)
        StubProxy.removeInstance(profile = profile2)
        StubProxy.clearActiveProfile()

        /**
         * getInstanceProfileMap
         */
        run {
            // Act
            val jso = StubProxy.getInstanceProfileMap().resultString.toJSONObject()
            // Assert
            assertThat(jso.has(instanceKey1)).isFalse()
        }
        run {
            // Arrange
            StubProxy.registerInstance(instanceKey = instanceKey1, profile = profile1)
            // Act
            val jso = StubProxy.getInstanceProfileMap().resultString.toJSONObject()
            // Assert
            assertThat(jso.has(instanceKey1)).isTrue()
            assertThat(jso.get(instanceKey1)).isEqualTo(profile1)
            assertThat(jso.has(instanceKey2)).isFalse()
        }
        run {
            // Arrange
            StubProxy.registerInstance(instanceKey = instanceKey2, profile = profile2)
            // Act
            val jso = StubProxy.getInstanceProfileMap().resultString.toJSONObject()
            // Assert
            assertThat(jso.has(instanceKey1)).isTrue()
            assertThat(jso.get(instanceKey1)).isEqualTo(profile1)
            assertThat(jso.has(instanceKey2)).isTrue()
            assertThat(jso.get(instanceKey2)).isEqualTo(profile2)
        }
    }

    @Test
    fun resetInstance() {

        val instanceKey1 = "1"
        val profile1 = "profile1"
        val instanceKey2 = "2"
        val profile2 = "profile2"

        StubProxy.removeInstance(profile = profile1)
        StubProxy.removeInstance(profile = profile2)
        StubProxy.clearActiveProfile()

        /**
         * resetInstance
         */
        run {
            val jso = StubProxy.resetInstance()
                .resultString
                .toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("Reset executed. (profile=null)")
        }
        run {
            val jso = StubProxy.resetInstance(profile = profile1)
                .resultString.toJSONObject()
            assertThat(jso.getString("message")).isEqualTo("No instance. (profile=profile1)")
        }

        /**
         * setDataPattern
         */
        run {
            // Arrange
            StubProxy.registerInstance(instanceKey = instanceKey1, profile = profile1)
            StubProxy.registerInstance(instanceKey = instanceKey2, profile = profile2)
            // Act
            val j0 = StubProxy.setDataPattern("CustomerList", "customer/00").resultString.toJSONObject()
            val j1 =
                StubProxy.setDataPattern("CustomerList", "customer/01", profile = profile1).resultString.toJSONObject()
            val j2 =
                StubProxy.setDataPattern("CustomerList", "customer/02", profile = profile2).resultString.toJSONObject()
            // Assert
            assertThat(j0.get("dataPatternName")).isEqualTo("customer/00")
            assertThat(j1.get("dataPatternName")).isEqualTo("customer/01")
            assertThat(j2.get("dataPatternName")).isEqualTo("customer/02")
        }

        /**
         * getDataPattern
         */
        run {
            // Arrange
            StubProxy.clearActiveProfile()
            // Act
            val j0 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList")
                .resultString.toJSONObject()
            val j1 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList", profile = profile1)
                .resultString.toJSONObject()
            val j2 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList", profile = profile2)
                .resultString.toJSONObject()
            // Assert
            assertThat(j0.get("dataPatternName")).isEqualTo("customer/00")
            assertThat(j1.get("dataPatternName")).isEqualTo("customer/01")
            assertThat(j2.get("dataPatternName")).isEqualTo("customer/02")
        }

        /**
         * resetInstance(default)
         */
        run {
            // Act
            val jso = StubProxy.resetInstance().resultString.toJSONObject()
            // Assert
            assertThat(jso.get("message")).isEqualTo("Reset executed. (profile=null)")
        }
        run {
            // Assert
            val j0 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList")
                .resultString.toJSONObject()
            val j1 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList", profile = profile1)
                .resultString.toJSONObject()
            val j2 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList", profile = profile2)
                .resultString.toJSONObject()
            assertThat(j0.get("dataPatternName")).isEqualTo("default")
            assertThat(j1.get("dataPatternName")).isEqualTo("customer/01")
            assertThat(j2.get("dataPatternName")).isEqualTo("customer/02")
        }

        /**
         * resetInstance(profile1)
         */
        run {
            // Act
            val jso = StubProxy.resetInstance(profile = profile1).resultString.toJSONObject()
            // Assert
            assertThat(jso.get("message")).isEqualTo("Reset executed. (profile=profile1)")
        }
        run {
            // Assert
            val j0 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList")
                .resultString.toJSONObject()
            val j1 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList", profile = profile1)
                .resultString.toJSONObject()
            val j2 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList", profile = profile2)
                .resultString.toJSONObject()
            assertThat(j0.get("dataPatternName")).isEqualTo("default")
            assertThat(j1.get("dataPatternName")).isEqualTo("default")
            assertThat(j2.get("dataPatternName")).isEqualTo("customer/02")
        }

        /**
         * resetInstance(profile2)
         */
        run {
            // Act
            val jso = StubProxy.resetInstance(profile = profile2).resultString.toJSONObject()
            // Assert
            assertThat(jso.get("message")).isEqualTo("Reset executed. (profile=profile2)")
        }
        run {
            // Assert
            val j0 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList")
                .resultString.toJSONObject()
            val j1 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList", profile = profile1)
                .resultString.toJSONObject()
            val j2 = StubProxy.getDataPattern(urlPathOrApiName = "CustomerList", profile = profile2)
                .resultString.toJSONObject()
            assertThat(j0.get("dataPatternName")).isEqualTo("default")
            assertThat(j1.get("dataPatternName")).isEqualTo("default")
            assertThat(j2.get("dataPatternName")).isEqualTo("default")
        }
    }

    @Test
    fun dataPattern() {

        val instanceKey1 = "1"
        val profile1 = "profile1"
        val instanceKey2 = "2"
        val profile2 = "profile2"

        StubProxy.removeInstance(profile = profile1)
        StubProxy.registerInstance(instanceKey = instanceKey1, profile = profile1)
        StubProxy.removeInstance(profile = profile2)
        StubProxy.registerInstance(instanceKey = instanceKey2, profile = profile2)
        StubProxy.clearActiveProfile()

        /**
         * setDataPattern
         */
        run {
            val jso = StubProxy.setDataPattern(urlPathOrApiName = "ProductList", dataPatternName = "product/00")
                .resultString.toJSONObject()
            assertThat(jso.get("dataPatternName")).isEqualTo("product/00")
            assertThat(jso.get("message")).isEqualTo("Data file found.")
            assertThat(jso.get("urlPath")).isEqualTo("/product/list")
        }
        /**
         * setDataPattern(profile1)
         */
        run {
            val jso = StubProxy.setDataPattern(
                urlPathOrApiName = "ProductList",
                dataPatternName = "product/01",
                profile = profile1
            ).resultString.toJSONObject()
            assertThat(jso.get("dataPatternName")).isEqualTo("product/01")
            assertThat(jso.get("message")).isEqualTo("Data file found.")
            assertThat(jso.get("urlPath")).isEqualTo("/product/list")
        }
        /**
         * setDataPattern(profile2)
         */
        run {
            val jso = StubProxy.setDataPattern(
                urlPathOrApiName = "ProductList",
                dataPatternName = "product/02",
                profile = profile2
            ).resultString.toJSONObject()
            assertThat(jso.get("dataPatternName")).isEqualTo("product/02")
            assertThat(jso.get("message")).isEqualTo("Data file found.")
            assertThat(jso.get("urlPath")).isEqualTo("/product/list")
        }

        /**
         * listDataPattern
         */
        run {
            val r = StubProxy.listDataPattern().resultString
            val expected = """
[
  {
    "urlPath": "/customer/list",
    "dataPatternName": "default"
  },
  {
    "urlPath": "/product/list",
    "dataPatternName": "product/00"
  },
  {
    "urlPath": "/supplier/list",
    "dataPatternName": "default"
  }
]
""".trimIndent()
            assertThat(r).isEqualTo(expected)
        }
        /**
         * listDataPattern(profile1)
         */
        run {
            val r = StubProxy.listDataPattern(profile = profile1).resultString
            val expected = """
[
  {
    "urlPath": "/customer/list",
    "dataPatternName": "default"
  },
  {
    "urlPath": "/product/list",
    "dataPatternName": "product/01"
  },
  {
    "urlPath": "/supplier/list",
    "dataPatternName": "default"
  }
]
""".trimIndent()
            assertThat(r).isEqualTo(expected)
        }
        /**
         * listDataPattern(profile2)
         */
        run {
            val r = StubProxy.listDataPattern(profile = profile2).resultString
            val expected = """
[
  {
    "urlPath": "/customer/list",
    "dataPatternName": "default"
  },
  {
    "urlPath": "/product/list",
    "dataPatternName": "product/02"
  },
  {
    "urlPath": "/supplier/list",
    "dataPatternName": "default"
  }
]
""".trimIndent()
            assertThat(r).isEqualTo(expected)
        }

        /**
         * getDataPattern
         */
        run {
            val jso = StubProxy.getDataPattern(urlPathOrApiName = "/product/list")
                .resultString.toJSONObject()
            assertThat(jso.get("dataPatternName")).isEqualTo("product/00")
        }
        run {
            val jso = StubProxy.getDataPattern(urlPathOrApiName = "/product/list", profile = profile1)
                .resultString.toJSONObject()
            assertThat(jso.get("dataPatternName")).isEqualTo("product/01")
        }
        run {
            val jso = StubProxy.getDataPattern(urlPathOrApiName = "/product/list", profile = profile2)
                .resultString.toJSONObject()
            assertThat(jso.get("dataPatternName")).isEqualTo("product/02")
        }

    }

}