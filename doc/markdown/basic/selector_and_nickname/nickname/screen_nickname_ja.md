# 画面ニックネーム

画面ニックネームはアプリのビューに対して定義できます。

## 例1: 電卓アプリ

![calculator](../../_images/calculator.png)

### [Calculator Main Screen].json

(`testConfig/android/calculator/screens/[Calculator Main Screen].json`)

```
{
  "key": "[Calculator Main Screen]",

  "screens": {
    "import": [
      "testConfig/android/misc/screens"
    ]
  },

  "identity": "[AC][()]",

  "selectors": {
    "[formula]": "#formula",
    "[result final]": "#result_final",
    "[result preview]": "#result_preview",

    "[√]": "#op_sqrt",
    "[π]": "#const_pi",
    "[^]": "#op_pow",
    "[!]": "#op_fact",

    "[AC]": "#clr",
    "[()]": "#parens",
    "[%]": "#op_pct",

    "[÷]": "#op_div",
    "[×]": "#op_mul",
    "[-]": "#op_sub",
    "[+]": "#op_add",
    "[=]": "#eq",
    "[⌫]": "#del",

    "[0]": "#digit_0",
    "[1]": "#digit_1",
    "[2]": "#digit_2",
    "[3]": "#digit_3",
    "[4]": "#digit_4",
    "[5]": "#digit_5",
    "[6]": "#digit_6",
    "[7]": "#digit_7",
    "[8]": "#digit_8",
    "[9]": "#digit_9",
    "[.]": "#dec_point",

    "[:Right button]": ":rightButton",
    "[:Below button]": ":belowButton",
    "[:Left button]": ":leftButton",
    "[:Above button]": ":aboveButton"
  }

}
```

"**key**" はファイル名（拡張子なし）と一致させる必要があります。

"**identity**" はセレクターまたはニックネームで構成する、ユニークな識別子です。

"**selectors**" セクションにニックネームを定義します。


<br>

## 例2: Android設定アプリのトップ画面

![android_settings_top_screen](../../_images/android_settings_top_screen.png)

### [Android Settings Top Screen].json

(`testConfig/android/androidSettings/screens/[Android Settings Top Screen].json`)

```
{
  "key": "[Android Settings Top Screen]",

  "identity": "#recycler_view",
  "satellites": ["Battery", "Accessibility", "Passwords & accounts", "Tips & support"],

  "selectors": {
    "[Account Avatar]": "#account_avatar",
    "[Settings]": "#homepage_title",

    "[Search Button]": "<#search_action_bar>:inner(1)",
    "[Search settings]": "#search_action_bar_title",

    "[Network & internet]": "",
    "{Network & internet}": "[Network & internet]:label",
    "[Network & internet Icon]": "[Network & internet]:leftImage",

    "[Connected devices]": "",
    "{Connected devices}": "[Connected devices]:label",
    "[Connected devices Icon]": "[Connected devices]:leftImage",

    "[Apps]": "",
    "{Apps}": "[Apps]:label",
    "[Apps Icon]": "[Apps]:leftImage",

    "[Notifications]": "",
    "{Notifications}": "[Notifications]:label",
    "[Notifications Icon]": "[Notifications]:leftImage",

    "[Battery]": "",
    "{Battery}": "[Battery]:label",
    "[Battery Icon]": "[Battery]:leftImage",

    "[Storage]": "",
    "{Storage}": "[Storage]:label",
    "[Storage Icon]": "[Storage]:leftImage",

    "[Sound & vibration]": "",
    "{Sound & vibration}": "[Sound & vibration]:label",
    "[Sound & vibration Icon]": "[Sound & vibration]:leftImage",

    "[Display]": "",
    "{Display}": "[Display]:label",
    "[Display Icon]": "[Display]:leftImage",

    "[Wallpaper & style]": "",
    "{Wallpaper & style}": "[Wallpaper & style]:label",
    "[Wallpaper & style Icon]": "[Wallpaper & style]:leftImage",

    "[Accessibility]": "",
    "{Accessibility}": "[Accessibility]:label",
    "[Accessibility Icon]": "[Accessibility]:leftImage",

    "[Security]": "",
    "{Security}": "[Security]:label",
    "[Security Icon]": "[Security]:leftImage",

    "[Privacy]": "",
    "{Privacy}": "[Privacy]:label",
    "[Privacy Icon]": "[Privacy]:leftImage",

    "[Location]": "",
    "{Location}": "[Location]:label",
    "[Location Icon]": "[Location]:leftImage",

    "[Safety & emergency]": "",
    "{Safety & emergency}": "[Safety & emergency]:label",
    "[Safety & emergency Icon]": "[Safety & emergency]:leftImage",

    "[Passwords & accounts]": "",
    "{Passwords & accounts}": "[Passwords & accounts]:label",
    "[Passwords & accounts Icon]": "[Passwords & accounts]:leftImage",

    "[Google]": "",
    "{Google}": "[Google]:label",
    "[Google Icon]": "[Google]:leftImage",

    "[System]": "",
    "{System}": "[System]:label",
    "[System Icon]": "[System]:leftImage",

    "[About emulated device]": "",
    "{About emulated device}": "[About emulated device]:label",
    "[About emulated device Icon]": "[About emulated device]:leftImage",

    "[About phone]": "",
    "{About phone}": "[About phone]:label",
    "[About phone Icon]": "[About phone]:leftImage",

    "[Tips & support]": "",
    "{Tips & support}": "[Tips & support]:label",
    "[Tips & support Icon]": "[Tips & support]:leftImage",

    "[:Summary]": ":belowLabel"
  },

  "scroll": {
    "start-elements": "[Network & internet]",
    "end-elements": "{Tips & support}",
    "overlay-elements": "[Search Button][Search settings]"
  }
}
```

"**key**" はファイル名（拡張子なし）と一致させる必要があります。

"**identity**" と "**satellites**"の組み合わせは画面をユニークに識別します。
スクロール可能なビューの場合、画面をユニークに識別するための要素が常に表示されるとは限りません。
この場合は"sattelites"で補完してユニークな識別子を構成することができます。
例えば、`#recycler_view&&Battery`と `#recycler_view&&Accessibility` はそれぞれ`[Android Settings Top Screen]`をユニークに識別することができます。

"**selectors**"セクションにおいてセレクターニックネームを定義できます。上記の場合、 [相対セレクター](../relative_selector/relative_selector.md)が使用されています。

## 画面ニックネームの共有

### [screen-base].json

全ての画面ニックネームファイルで共用したい内容を`[screen-base].json`で定義することができます。

![](../../_images/screen_base_json.png)

```
{
  "key": "[screen-base]",

  "include": [
  ],
  "selectors": {
    "[<-]": "@Navigate up",
    "[More options]": "@More options"
  }
}
```

これにより"[<-]"と"[More options]"を画面ニックネームを定義するたびに都度定義することなく使用できます。

### include

`include`を使用すると他の画面ニックネームをインクルードすることができます。

#### [Screen A].json

```
{
  "key": "[Screen A]",

  "include": [
    "[Common Header]",
    "[Common Footer]"
  ],
  "selectors": {
    "[Button A]": "#buttonA",
    "[TextBox A]": "#textA",
    "[Label A]": "#labelA"
  }
}
```

#### [Common Header].json

```
{
  "key": "[Common Header]",

  "selectors": {
    "[Header Title]": "#header-title"
  }
}
```

#### [Common Footer].json

```
{
  "key": "[Common Footer]",

  "selectors": {
    "[Footer Button A]": "#footer-buttonA",
    "[Footer Button B]": "#footer-buttonB"
  }
}
```

これにより画面ニックネーム`[Screen A]`で`[Header Title]`、`[Footer Button A]`、`[Footer Button A]`を使用することができます。

### Link

- [セレクターニックネーム](selector_nickname_ja.md)

- [データセットニックネーム](dataset_nickname_ja.md)

- [ニックネーム](nickname_ja.md)
