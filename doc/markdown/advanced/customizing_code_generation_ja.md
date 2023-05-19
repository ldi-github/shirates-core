# コード生成のカスタマイズ

**Translator** インターフェースを使用するとコード生成をカスタマイズすることができます。

## Translator インターフェース

| 関数                           | 説明                             |
|------------------------------|--------------------------------|
| escape                       | 文字をエスケープします                    |
| format                       | メッセージをフォーマットします                |
| getSubject                   | メッセージからsubjectを取得します           |
| getScreenNickName            | 画面ニックネームをメッセージから取得します          |
| conditionMessageToFunction   | conditionのメッセージを関数の文字列に変換します   |
| actionMessageToFunction      | expectationのメッセージを関数の文字列に変換します |
| targetToFunction             | targetのメッセージを関数の文字列に変換します      |
| expectationMessageToFunction | expectationのメッセージを関数の文字列に変換します |
| atEndOfScenario              | scenarioの行が生成された後の拡張ポイント       |
| atEndOfCase                  | case行が生成された後の拡張ポイント            |
| atEndOfCondition             | condition行が生成された後の拡張ポイント       |
| atEndOfAction                | action行が生成された後の拡張ポイント          |
| atEndOfExpectation           | expectation行が生成された後の拡張ポイント     |
| atEndOfTarget                | target行が生成された後の拡張ポイント          |

### Translator.kt

```kotlin
package shirates.spec.code.custom

import shirates.core.configuration.PropertiesManager
import shirates.spec.code.entity.Case
import shirates.spec.code.entity.Target
import shirates.spec.utilily.*

interface Translator {

    /**
     * escape
     */
    fun escape(message: String): String {

        if (PropertiesManager.logLanguage == "ja") {
            return message.replace("\\", "¥")
        } else {
            return message.replace("\\", "\\\\")
        }
    }

    /**
     * format
     */
    fun format(message: String): String {

        var msg = escape(message)
        msg = msg.removePrefix(SpecResourceUtility.bullet).removeBrackets().removeJapaneseBrackets()
        return msg
    }

    /**
     * getSubject
     */
    fun getSubject(message: String): String {

        val screenNickname = getScreenNickName(message = message)
        if (screenNickname.isNotBlank()) {
            return screenNickname
        }

        val nickname1 = message.getGroupValue(".*(\\[.*]).*".toRegex())
        if (nickname1.isNotBlank()) {
            return nickname1
        }

        val nickname2 = message.getGroupValue(".*(\\{.*}).*".toRegex())
        if (nickname2.isNotBlank()) {
            return nickname2
        }

        val selectorExpression = message.getGroupValue(".*(<.*>).*".toRegex())
        if (selectorExpression.isNotBlank()) {
            return selectorExpression
        }

        if (message.contains("を")) {
            val list = message.split("を").toMutableList()
            list.removeLast()
            val s = format(list.joinToString())
            return "<$s>"
        }

        return ""
    }

    /**
     * getScreenNickName
     */
    fun getScreenNickName(
        message: String
    ): String {

        val replaced = message.removeJapaneseBrackets().removeBrackets()
        for (keyword in Keywords.screenKeywords) {
            val screenName = replaced.getGroupValue(".*(\\.*$keyword.*).*".toRegex())
            if (screenName.isNotBlank()) {
                return "[$screenName]"
            }
        }

        return ""
    }

    /**
     * messageToFunction
     */
    fun messageToFunction(message: String, defaultFunc: String = "manual"): String {

        val subject = getSubject(message)

        if (subject.isNotBlank()) {
            if (message.isDisplayed) {
                if (subject.isScreen) {
                    return "screenIs(\"$subject\")"
                } else {
                    if (message.isAssertion) {
                        return "exist(\"${subject}\")"
                    }
                    return "manual(\"$message\")"
                }
            }
            if (message.contains("アイコンをタップする") || message.lowercase().contains("tap app icon")) {
                return "tapAppIcon(\"$subject\")"
            }
            if (message.contains("タップする") || message.lowercase().contains("tap ")) {
                return "tap(\"$subject\")"
            }
            if (message.contains("がON") || message.lowercase().endsWith(" is on")) {
                return "select(\"$subject\").checkIsON()"
            }
            if (message.contains("がOFF") || message.lowercase().endsWith(" is off")) {
                return "select(\"$subject\").checkIsOFF()"
            }

            if (message.isAssertion) {
                val valueEN = message.getGroupValue(".* is \"(.*)\"".toRegex())
                if (valueEN.isNotBlank()) {
                    return "select(\"$subject\").textIs(\"$valueEN\")"
                }
                val valueJP = message.getGroupValue(".*の値が\"(.*)\"であること.*".toRegex())
                if (valueJP.isNotBlank()) {
                    return "select(\"$subject\").textIs(\"$valueJP\")"
                }
            }
        }

        val arg = format(message)
        return "$defaultFunc(\"$arg\")"
    }

    /**
     * conditionMessageToFunction
     */
    fun conditionMessageToFunction(
        case: Case,
        message: String,
        defaultFunc: String = "manual"
    ): String {

        if (message.startsWith("[") && message.endsWith("]")) {
            return "macro(\"$message\")"
        }

        val screenNickname = getScreenNickName(message = message)
        if (screenNickname.isNotBlank()) {
            return "macro(\"$screenNickname\")"
        }

        return messageToFunction(message = message, defaultFunc = defaultFunc)
    }

    /**
     * actionMessageToFunction
     */
    fun actionMessageToFunction(
        case: Case,
        message: String,
        defaultFunc: String = "manual"
    ): String {

        return messageToFunction(message = message, defaultFunc = defaultFunc)
    }

    /**
     * targetToFunction
     */
    fun targetToFunction(
        target: String
    ): String {

        val screenNickName = getScreenNickName(message = target)
        if (screenNickName.isNotBlank()) {
            return "it.target(\"$screenNickName\")"
        }

        val escaped = escape(target.replace("\n", ""))
        return "it.target(\"$escaped\")"
    }

    /**
     * expectationMessageToFunction
     */
    fun expectationMessageToFunction(
        target: Target? = null,
        message: String,
        defaultFunc: String = "manual"
    ): String {

        if (target?.target != null) {
            if (message.isDisplayed) {
                if (target.target.isScreen) {
                    val screenNickName = getScreenNickName(target.target)
                    return "screenIs(\"$screenNickName\")"
                } else {
                    if (message.isAssertion) {
                        return "exist(\"${target.target}\")"
                    }
                    return "manual(\"$message\")"
                }
            }
            val functionPart = messageToFunction(message = message, defaultFunc = defaultFunc)
            if (functionPart.isNotBlank()) {
                return functionPart
            }
        }

        val arg = format(message)
        return "$defaultFunc(\"$arg\")"
    }

    /**
     * atEndOfScenario
     */
    fun atEndOfScenario(lines: MutableList<String>) {

    }

    /**
     * atEndOfCase
     */
    fun atEndOfCase(lines: MutableList<String>) {

    }

    /**
     * atEndOfCondition
     */
    fun atEndOfCondition(lines: MutableList<String>) {

    }

    /**
     * atEndOfAction
     */
    fun atEndOfAction(lines: MutableList<String>) {

    }

    /**
     * atEndOfExpectation
     */
    fun atEndOfExpectation(lines: MutableList<String>) {

    }

    /**
     * atEndOfTarget
     */
    fun atEndOfTarget(lines: MutableList<String>) {

        // Remove `target` if `screenIs` succeeds
        if (lines.count() >= 2 && lines[1].startsWith("it.screenIs(")) {
            if (lines[0].startsWith("it.target(")) {
                lines.removeAt(0)
            }
        }

        for ((i, line) in lines.withIndex()) {
            if (i > 0) {
                if (lines[i - 1].endsWith("{").not())
                    lines[i] = line.removePrefix("it")
            }
        }
    }

}
```

## 例

1. `AndroidSettingdDemo`を実行して**Spec-Report**(`AndroidSettingsDemo@a.xlsx`)を作成します。  (
   参照 [クイックスタート](../quick-start_ja.md))
2. `ダウンロード`ディレクトリの下に`SpecInput`ディレクトリを作成し、`AndroidSettingsDemo@a.xlsx`を配置します。
3. `CodeGeneratorExecute` を実行して `AndroidSettingsDemo.kt`を生成します。

### AndroidSettingsDemo.kt

```kotlin
package generated

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.*
import shirates.core.driver.commandextension.*
import shirates.core.testcode.*

@SheetName("AndroidSettingsDemo")
class AndroidSettingsDemo : UITest() {

    @NoLoadRun
    @Test
    @DisplayName("airplaneModeSwitch()")
    fun S1010() {

        scenario {
            case(1) {
                condition {
                    it.manual("Tap app icon <Settings>")
                        .screenIs("[Android Settings Top Screen]")
                }.action {
                    it.manual("Tap [Network & internet]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    it.manual("{Airplane mode switch} is OFF")
                }.action {
                    it.manual("Tap {Airplane mode switch}")
                }.expectation {
                    it.manual("{Airplane mode switch} is ON")
                }
            }
            case(3) {
                action {
                    it.manual("Tap {Airplane mode switch}")
                }.expectation {
                    it.manual("{Airplane mode switch} is OFF")
                }
            }
        }
    }

}
```

4. `kotlin/exercise`の下に`CustomTranslator.kt`を作成します。

#### CustomTranslator.kt

```kotlin
package exercise

object CustomTranslator {

}
```

5. 以下のように編集します。
    1. `@CustomObject`アノテーションを`CustomTranslator`に付与します。
    2. `Translator`インターフェースを継承します。
    3. `actionMessageToFunction`関数をオーバーライドして`@CustomFunction`アノテーションを付与します。
    4. カスタムの変換ロジックを実装します。この例ではメッセージが`tap`という文字列と、何らかのニックネームを含む場合にtap関数を出力します。

```kotlin
package exercise

import shirates.core.customobject.CustomFunction
import shirates.core.customobject.CustomObject
import shirates.spec.code.custom.Translator
import shirates.spec.code.entity.Case

@CustomObject
object CustomTranslator : Translator {

    @CustomFunction
    override fun actionMessageToFunction(case: Case, message: String, defaultFunc: String): String {

        val nickname = getNickName(message)
        if (message.lowercase().contains("tap") && nickname.isNotBlank()) {
            return "tap(\"$nickname\")"
        }

        return super.actionMessageToFunction(case, message, defaultFunc)
    }

}
```

6. プロジェクトをビルドします。
7. `CodeGeneratorExecute`を実行して`AndroidSettingsDemo.kt`を生成します。以下のように出力されます。

```kotlin
case(1) {
    condition {
        it.manual("Tap app icon <Settings>")
            .screenIs("[Android Settings Top Screen]")
    }.action {
        it.tap("[Network & internet]")
    }.expectation {
        it.target("[Network & internet Screen]")
        android {
            specialTag(specialTag = "osaifu") {
                it.screenIs("[Network & internet Screen]")
            }
        }
    }
}
```

**Before**

```kotlin
it.manual("Tap [Network & internet]")
```

**After**

```kotlin
it.tap("[Network & internet]")
```

### Link

- [index](../index_ja.md)
