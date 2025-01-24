# データセットニックネーム (Classic)

データセットニックネームはテキストデータのセットに対するニックネームです。

## data.json

`data.json`内にデータセットニックネームを定義し、`[ニックネーム].属性`の組み合わせ名で値を取得することができます。

### data.json

```
{
  "[製品1]": {
    "製品名": "スーパーハイテンション",
    "製品コード": "P001",
    "単価": "100"
  },

  "[製品2]": {
    "製品名": "ウルトラハイ",
    "製品コード": "P002",
    "単価": "200"
  }
}
```

**データセットニックネーム**は"[ ]"(square brackets)を使用して名前を定義します。

`[製品1]`の`製品コード`を`data`関数を使用して取得できます。

```kotlin
it.sendKeys(data("[製品1].製品コード")) // data関数は"P001"を返します
```

<br>

## account.json

`account.json`はデータセットニックネームのアカウント専用スペシャルバージョンです。

### accounts.json

```
{
  "[アカウント1]": {
    "ID": "account1@example.com",
    "パスワード": "p@ssword"
  },

  "[アカウント2]": {
    "ID": "account2@example.com",
    "パスワード": "p@ssword"
  }
}
```

**アカウントニックネーム** は"[ ]"(square brackets)で囲みます。

上記の例では**ID**属性と **パスワード**属性が定義されています。

`[アカウント1]`の`ID`を`account`関数を使用して取得できます。

```kotlin
it.sendKeys(account("[アカウント1].ID"))   // account関数は"account1@example.com"を返します
```

You can add any attribute in account and get it.

```
{
  "[アカウント1]": {
    "ID": "account1@example.com",
    "パスワード": "p@ssword",
    "アバター名": "マウンテンキャット"
  },

  "[アカウント2]": {
    "ID": "account2@example.com",
    "パスワード": "p@ssword",
    "アバター名": "ワイルドタイガー"
  }
}
```

```kotlin
it.sendKeys(account("[アカウント1].アバター名"))  // account関数は"マウンテンキャット"を返します
```

<br>

## apps.json

`apps.json`はデータセットニックネームのアプリ専用スペシャルバージョンです。

### apps.json

```
{
  "[設定]": {
    "packageOrBundleId": "com.android.settings"
  },
  "[電卓]": {
    "packageOrBundleId": "com.google.android.calculator"
  },
  "[Chrome]": {
    "packageOrBundleId": "com.android.chrome"
  },
  "[地図]": {
    "packageOrBundleId": "com.google.android.apps.maps"
  },
  "[Playストア]": {
    "packageOrBundleId": "com.android.vending"
  },
  "[時計]": {
    "packageOrBundleId": "com.google.android.deskclock"
  },
  "[アプリ1]": {
    "packageOrBundleId": "com.example.app1"
  }
}
```

**アプリニックネーム** は"[ ]"(square brackets)で囲みます。

上記の例では **packageOrBundleId** が定義されています。

`[アプリ1]`の`packageOrBundleId`を`app`関数を使用して取得できます。

```kotlin
it.sendKeys(app("[アプリ1].packageOrBundleId"))   // app関数は"com.example.app1"を返します
```

<br>

`isApp`関数を使用すると現在表示されているアプリが期待したものであるかどうかを判定できます。

```kotlin
if (isApp("[地図]")) {
    // do something
}
```

### Link

- [セレクターニックネーム](selector_nickname_ja.md)
- [画面ニックネーム](screen_nickname_ja.md)
- [ニックネーム](nickname_ja.md)
