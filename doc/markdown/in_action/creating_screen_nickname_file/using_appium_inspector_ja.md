# Appium Inspectorを使用する

画面ニックネームを作成したい場合、要素の情報を調査するために **Appium Inspector** を使用します。

## インストール

1. [appium/appium-inspector](https://github.com/appium/appium-inspector/releases)からインストールパッケージを入手します。
    - `Appium-Inspector-mac-202x.x.x.dmg` (Mac用)
    - `Appium-Inspector-windows-202x.x.x.exe` (Windows用)
2. パッケージをインストールします。
3. Appium Inspectorを起動します。

## Appium Serverの起動

1. ターミナルを起動します。
2. Appium Serverのプロセスを起動します。 ```appium``` と入力してからEnterを入力してください。

```
wave1008@SNB-M1 ~ % appium
info Appium Setting NODE_PATH to '/opt/homebrew/lib/node_modules'
[Appium] Welcome to Appium v2.0.0-beta.44 (REV 0b030b74e2ac518bd0bc4158f96c449198f9957f)
[Appium] Attempting to load driver xcuitest...
[debug] [Appium] Requiring driver at /Users/wave1008/.appium/node_modules/appium-xcuitest-driver
[Appium] Attempting to load driver uiautomator2...
[debug] [Appium] Requiring driver at /Users/wave1008/.appium/node_modules/appium-uiautomator2-driver
[Appium] Appium REST http interface listener started on 0.0.0.0:4723
[Appium] Available drivers:
[Appium]   - xcuitest@4.11.1 (automationName 'XCUITest')
[Appium]   - uiautomator2@2.4.6 (automationName 'UiAutomator2')
[Appium] No plugins have been installed. Use the "appium plugin" command to install the one(s) you want to use.
```

Appiumのインストールは[クイックスタート](../../quick-start_ja.md)を参照してください。

## Appium Inspectorの起動

1. Appium Inspectorを起動します。
2. `Desired Capabilities`タブを表示します。
3. パラメーターを以下のように編集します。
   <br>![](../_images/desired_capability_android.png)<br>

```
{
  "appium:automationName": "UiAutomator2",
  "platformName": "Android",
  "appium:platformVersion": "12",
  "appium:appPackage": "com.android.settings",
  "appium:appActivity": "com.android.settings.Settings"
}
```

4. Android 12のエミュレーターを起動します。(Android 12用のAVDの設定については[クイックスタート](../../quick-start_ja.md))
   を参照してください。
5. `[Start Session]`をクリックします。
6. デバイス内で設定アプリが起動します。<br>画面イメージがキャプチャされます。画面要素を調査することができます。
   <br>![](../_images/screen_captured_in_inspector.png)

## ユニークになる属性を探す

画面要素を識別するにはユニークキーになる情報が必要です。外部のエディタを使用して作業することができます。

1. 調査したい画面をAppium Inspectorでキャプチャします。
2. `Copy XML Source to Clipboard`をクリックします。
   <br>![](../_images/copy_xml_source_to_clipboard.png)
3. 好みのエディタを開いてペーストします。ユニークな属性を調査できます。
   <br>![](../_images/finding_unique_attributes_in_editor.png)

### Link

- [画面ニックネームファイルの作成](creating_screen_nickname_file_ja.md)
- [Screen Builderを使用する](using_screen_builder.md)


- [index](../../index_ja.md)
