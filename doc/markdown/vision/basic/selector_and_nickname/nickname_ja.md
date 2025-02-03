# ニックネーム

## ニックネームとは？

**ニックネーム** は自動テストを容易かつ効率的にするためのShiratesのキーコンセプトの一つです。
ニックネームはテストコード内で使用するデータの対象(セレクター, 画面名, アプリ, ユーザーデータ, etc)に対して付与する別名です。
ニックネームを定義し、テストコードで使用することができます。
このメカニズムによってテストコードを書いたり読んだりすることが簡単になります。

## ニックネームファイル

ニックネームはニックネームファイルに記述することができます。

![nickname_files](_images/nickname_files.png)

## ニックネームの種類

以下の種類のニックネームを使用することができます。

- [セレクターニックネーム (classicモード用)](../../../classic/basic/selector_and_nickname/nickname/selector_nickname_ja.md)
- [画面ニックネーム (classicモード用)](../../../classic/basic/selector_and_nickname/nickname/screen_nickname_ja.md)
- [データセットニックネーム](../../../classic/basic/selector_and_nickname/nickname/dataset_nickname_ja.md)

**セレクターニックネーム** と **画面ニックネーム** はClassicモードで使用するためのものです。<br>
**データセットニックネーム** はClassicモード/Visionモードの両方で使用できます。

## ニックネーム宣言 (一般形)

ニックネームはkeyとvalueの組み合わせで定義します。keyはニックネーム、valueは何らかの式です。

ニックネームは **"[ ]"**(square brackets) または **"{ }"**(curly brackets)で囲います。

```
"[nickname]": (何らかの式)
```

```
"{nickname}": (何らかの式)
```

"[ ]" は **静的なコンテンツ**. "{ }" **動的なコンテンツ** に使用することが推奨されます。

<br>

### Link

- [account](../../../vision/basic/function_property/data_storage/account_ja.md)
- [app](basic/function_property/data_storage/app_ja.md)
- [data](basic/function_property/data_storage/data_ja.md)


- [index](../../../index_ja.md)

