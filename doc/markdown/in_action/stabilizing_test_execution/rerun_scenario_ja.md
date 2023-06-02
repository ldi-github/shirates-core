# scenarioの再実行

テストの再実行はフレーキーテストを解決するシンプルで強力な方法です。shirates-core に組み込まれたscenarioの再実行の仕組みはテスト結果を安定させます。

## scenarioを再実行する手順

1. Appium Serverを終了します
2. デバイスを再起動します
3. Appium Serverを起動します
4. Appium Serverへ接続します
5. scenarioを再実行します

## scenarioの再実行はいつ行われるのか？

以下のような状況において再実行が実施されます。

| 状況                                                | 説明                                                                |
|---------------------------------------------------|-------------------------------------------------------------------|
| HTTP connection time out ("Read timed out")       | デバイスが反応していない                                                      |
| AppiumProxy.getSource() time out.                 | Appiumのセッションが破損している                                               |
| Could not start a new session. Response code 500. | Appiumのセッションが開始できなかった                                             |
| Terminating app time out. (Android)               | ex. `'com.android.settings' is still running after 500ms timeout` |
| Could not proxy command to the remote server.     | デバイスへのコマンド送信に失敗した (ex. `socket hang up`, `connect ECONNREFUSED`)  |

## scenarioの再実行を手動で要求する

エラーをハンドリングして **RerunScenarioException** をスローすることでscenarioを再実行することができます。

```kotlin
throw RerunScenarioException("App is not responding")
```

## 例

### RerunScenario1.kt

(`kotlin/tutorial/basic/RerunScenario1.kt`)

```kotlin
package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.RerunScenarioException
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class RerunScenario1 : UITest() {

    @Test
    @Order(10)
    fun rerunScenario() {

        var count = 0

        scenario {
            case(1) {
                action {
                    count++
                    output("count=$count")
                    if (count == 1) {
                        throw RerunScenarioException("Request to rerun scenario")
                    }
                }.expectation {
                    count.thisIs(2, "count is 2")
                }
            }
        }
    }
}
```

```
...

128	2023/03/23 05:22:10.324	{}	[info]	()	Running scenario ..................................................
129	2023/03/23 05:22:10.324	{}	[info]	()	Startup package: com.android.settings
130	2023/03/23 05:22:10.325	{rerunScenario}	[SCENARIO]	(scenario)	rerunScenario()
131	2023/03/23 05:22:10.851	{rerunScenario}	[screenshot]	(screenshot)	screenshot
132	2023/03/23 05:22:10.853	{rerunScenario}	[operate]	(launchApp)	Launch app <Settings>
133	2023/03/23 05:22:11.311	{rerunScenario}	[info]	(syncCache)	Syncing (1)
134	2023/03/23 05:22:12.117	{rerunScenario}	[info]	(syncCache)	Synced. (elapsed=0.806, currentScreen=[Android Settings Top Screen])
135	2023/03/23 05:22:12.123	{rerunScenario-1}	[CASE]	(case)	(1)
136	2023/03/23 05:22:12.124	{rerunScenario-1}	[ACTION]	(action)	action
137	2023/03/23 05:22:12.125	{rerunScenario-1}	[output]	(output)	count=1
138	2023/03/23 05:22:12.125	{rerunScenario-1}	[WARN]	()	Request to rerun scenario
139	2023/03/23 05:22:12.126	{rerunScenario-1}	[-]	()	Rerunning scenario ...
140	2023/03/23 05:22:12.126	{rerunScenario-1}	[info]	()	Resetting Appium session.
141	2023/03/23 05:22:35.514	{rerunScenario-1}	[info]	()	Connecting to Appium Server.(http://127.0.0.1:4720/)
142	2023/03/23 05:22:43.096	{rerunScenario-1}	[info]	()	[Health check] start
143	2023/03/23 05:22:43.098	{rerunScenario-1}	[info]	(syncCache)	Syncing (1)
144	2023/03/23 05:22:43.947	{rerunScenario-1}	[info]	(syncCache)	Synced. (elapsed=0.849, currentScreen=[Android Settings Top Screen])
145	2023/03/23 05:22:43.948	{rerunScenario-1}	[info]	()	tap<.label>
146	2023/03/23 05:22:45.335	{rerunScenario-1}	[info]	(syncCache)	Syncing (1)
147	2023/03/23 05:22:45.440	{rerunScenario-1}	[info]	(syncCache)	Synced. (elapsed=0.105, currentScreen=[Android Settings Top Screen])
148	2023/03/23 05:22:45.677	{rerunScenario-1}	[info]	()	[Health check] end
149	2023/03/23 05:22:45.678	{rerunScenario-1}	[info]	()	implicitlyWaitSeconds: 5.0
150	2023/03/23 05:22:45.755	{rerunScenario-1}	[info]	()	(settings) always_finish_activities: 0
151	2023/03/23 05:22:45.787	{rerunScenario-1}	[info]	()	Searching device for the profile. (profileName=Pixel 3a(Android 12))
152	2023/03/23 05:22:45.897	{rerunScenario-1}	[info]	()	Connected device found. (Pixel_3a_Android_12_:5554, Android 12, emulator-5554)
153	2023/03/23 05:22:45.898	{rerunScenario-1}	[info]	()	AppiumDriver initialized.
154	2023/03/23 05:22:45.898	{rerunScenario-1}	[info]	()	Running scenario ..................................................
155	2023/03/23 05:22:45.899	{rerunScenario-1}	[info]	()	Startup package: com.android.settings
156	2023/03/23 05:22:45.899	{rerunScenario-1}	[SCENARIO]	(scenario)	rerunScenario()
157	2023/03/23 05:22:46.173	{rerunScenario-1}	[screenshot]	(screenshot)	screenshot
158	2023/03/23 05:22:46.174	{rerunScenario-1}	[operate]	(launchApp)	Launch app <Settings>
159	2023/03/23 05:22:46.605	{rerunScenario-1}	[info]	(syncCache)	Syncing (1)
160	2023/03/23 05:22:47.485	{rerunScenario-1}	[info]	(syncCache)	Synced. (elapsed=0.881, currentScreen=[Android Settings Top Screen])
161	2023/03/23 05:22:47.487	{rerunScenario-1}	[CASE]	(case)	(1)
162	2023/03/23 05:22:47.487	{rerunScenario-1}	[ACTION]	(action)	action
163	2023/03/23 05:22:47.488	{rerunScenario-1}	[output]	(output)	count=2
164	2023/03/23 05:22:47.489	{rerunScenario-1}	[EXPECTATION]	(expectation)	expectation
165	2023/03/23 05:22:47.491	{rerunScenario-1}	[OK]	(thisIs)	count is 2
166	2023/03/23 05:22:47.492	{rerunScenario-1}	[info]	()	Scenario executed. (duration: 37.2 sec)
167	2023/03/23 05:22:47.494	{}	[info]	()	Test function executed. (duration: 53.3 sec)
168	2023/03/23 05:22:47.494	{}	[info]	()	End of Test function: rerunScenario [rerunScenario()]
169	2023/03/23 05:22:47.498	{}	[info]	()	Logging to file:////Users/wave1008/Downloads/TestResults/androidSettingsConfig/2023-03-23_052153/RerunScenario1/

...
```

### Link

- [フレーキーテストの原因](causes_of_flaky_test_ja.md)
- [index](../../index_ja.md)
