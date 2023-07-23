# ダイレクトアクセスモード

ダイレクトアクセスモードで要素にアクセスすることができます。

### メリット

- iOSにおいてgetPageSourceを実行した場合に発生する深刻なパフォーマンス問題を回避することができます
- 非常に多くの要素が存在する画面において数個程度の要素を取得する場合は高速です

### デメリット

- 多くの要素を取得する場合はキャッシュモードよりも低速です
- 方向ベース/フローベースの相対セレクターは使用できません
- currentScreenが自動で設定されません（画面を自動判定できません）。セレクターニックネームを使用する前に`screenIs`/`switchScreen`
  関数を手動で使用してcurrentScreenを設定する必要があります
- 他の関数についても制約が発生する場合があります

## 関数

| 関数                | 説明                                    |
|:------------------|:--------------------------------------|
| disableCache      | キャッシュを無効にする                           |
| enableCache       | キャッシュを有効にする                           |
| suppressCache { } | コードブロック内でキャッシュを抑制する                   |
| useCache { }      | コードブロック内でキャッシュを使用する                   |
| switchScreen      | currentScreenを設定して画面のコンテキストを手動で切り替えます |

## 引数useCache

UITestのこれらの関数で引数useCacheを指定することができます。

- scenario
- case
- condition
- action
- expectation

| useCache   | 説明                 |
|:-----------|:-------------------|
| null (未指定) | 周囲の状況に依存します        |
| true       | キャッシュモードで動作します     |
| false      | ダイレクトアクセスモードで動作します |

### DirectAccessModeIos.kt

(`kotlin/tutorial/inaction/DirectAccessModeIos.kt`)

```kotlin
package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.utility.time.StopWatch

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class DirectAccessModeIos : UITest() {

    @Test
    @Order(10)
    fun enableCacheTest() {

//        enableCache()   // This should not be specified if cache mode is default

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                        .tap("[General]")
                        .screenIs("[General Screen]")
                }.action {
                    it.tap("[About]")
                }.expectation {
                    it.screenIs("[About Screen]")
                        .exist("[Name]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun disableCacheTest() {

        disableCache()

        scenario {
            case(1) {
                condition {
                    it.switchScreen("[iOS Settings Top Screen]")
                        .tap("[General]")
                        .switchScreen("[General Screen]")
                }.action {
                    it.tap("[About]")
                }.expectation {
                    it.switchScreen("[About Screen]")
                        .exist("[Name]")
                }
            }
        }
    }

    @Test
    @DisableCache
    @Order(30)
    fun disableCacheTest2() {

        scenario {
            case(1) {
                condition {
                    it.switchScreen("[iOS Settings Top Screen]")
                        .tap("[General]")
                        .switchScreen("[General Screen]")
                }.action {
                    it.tap("[About]")
                }.expectation {
                    it.switchScreen("[About Screen]")
                        .exist("[Name]")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun performanceComparison() {

        fun process(count: Int) {
            val sw1 = StopWatch()
            val sw2 = StopWatch()
            invalidateCache()

            sw1.start()
            useCache {
                for (i in 1..count) {
                    it.select("General")  // cache mode
                }
            }
            sw1.stop()

            sw2.start()
            suppressCache {
                for (i in 1..count) {
                    it.select("General")  // direct access mode
                }
            }
            sw2.stop()

            output("$count element(s)")
            output("$sw1 cache mode")
            output("$sw2 direct access mode")
        }

        scenario {
            case(1) {
                expectation {
                    process(1)
                }
            }
            case(2) {
                expectation {
                    process(5)
                }
            }
            case(3) {
                expectation {
                    process(10)
                }
            }
            case(4) {
                expectation {
                    process(50)
                }
            }
        }
    }
}
```

### Link

- [iOSにおけるgetSource実行時のパフォーマンス問題](performance_problem_of_getpagesource_in_ios_ja.md)
- [index](../../index_ja.md)
