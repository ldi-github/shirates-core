# Appiumとdriverのインストール/アップデート

## Appium 2.0

### インストール

```
npm install -g appium@next
appium -v
```

**注意:** Appium 2.0 はリリース予定です。 2.0をインストールするには "@next"が必要です。

### 再インストール

```
appium -v
npm uninstall -g appium
npm install -g appium@next
appium -v
```

**注意:** Appium 2.0 はリリース予定です。 2.0をインストールするには "@next"が必要です。

参照 [Tested Environments](../environments.md) to get tested version.

<hr>

## UIAutomator2 driver

### インストール

```
appium driver install uiautomator2
appium driver list
```

### 再インストール

```
appium driver list
appium driver uninstall uiautomator2
appium driver install uiautomator2
appium driver list
```

参照 [Tested Environments](../environments.md) to get tested version.

<hr>

## XCUITest driver(Macのみ)

### インストール

```
appium driver install xcuitest
appium driver list
```

### 再インストール

```
appium driver list
appium driver uninstall xcuitest
appium driver install xcuitest
appium driver list
```

参照 [Tested Environments](../environments.md) to get tested version.

<hr>

## 例

### 全てを最新化する

以下をコピーしてターミナルにペーストしてください。

```
appium -v
appium driver list

npm uninstall -g appium
npm install -g appium@next

appium driver uninstall uiautomator2
appium driver uninstall xcuitest

appium driver install uiautomator2
appium driver install xcuitest

appium -v
appium driver list
```

#### コンソール

```
2.0.0-beta.64
✔ Listing available drivers
- uiautomator2@2.15.0 [installed (npm)]
- xcuitest@4.21.33 [installed (npm)]
- mac2 [not installed]
- espresso [not installed]
- safari [not installed]
- gecko [not installed]
- chromium [not installed]

removed 480 packages in 608ms

added 480 packages in 3s

48 packages are looking for funding
  run `npm fund` for details
✔ Successfully uninstalled driver 'uiautomator2'
✔ Successfully uninstalled driver 'xcuitest'
✔ Installing 'uiautomator2' using NPM install spec 'appium-uiautomator2-driver'
ℹ Driver uiautomator2@2.15.0 successfully installed
- automationName: UiAutomator2
- platformNames: ["Android"]
✔ Installing 'xcuitest' using NPM install spec 'appium-xcuitest-driver'
ℹ Driver xcuitest@4.21.33 successfully installed
- automationName: XCUITest
- platformNames: ["iOS","tvOS"]
2.0.0-beta.64
✔ Listing available drivers
- uiautomator2@2.15.0 [installed (npm)]
- xcuitest@4.21.33 [installed (npm)]
- mac2 [not installed]
- espresso [not installed]
- safari [not installed]
- gecko [not installed]
- chromium [not installed]
```

### Link

- [index](../index_ja.md)
