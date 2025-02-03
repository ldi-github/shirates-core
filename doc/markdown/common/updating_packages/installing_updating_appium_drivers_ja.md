# Appiumとdriverのインストール/アップデート (Vision/Classic)

## Appium

### インストール

```
npm install -g appium
appium -v
```

### 再インストール

```
appium -v
npm uninstall -g appium
npm install -g appium
appium -v
```

参照 [Tested Environments](../environments.md)

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

参照 [Tested Environments](../environments.md)

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

参照 [Tested Environments](../environments.md)

<hr>

## 例

### 全てを最新化する

以下をコピーしてターミナルにペーストしてください。

```
appium -v
appium driver list

npm uninstall -g appium
npm install -g appium

appium driver uninstall uiautomator2
appium driver uninstall xcuitest

appium driver install uiautomator2
appium driver install xcuitest

appium -v
appium driver list
```

### Link

- [index(Vision)](../../index_ja.md)
- [index(Classic)](../../classic/index_ja.md)
