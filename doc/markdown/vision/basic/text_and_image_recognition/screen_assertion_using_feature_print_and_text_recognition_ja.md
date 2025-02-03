# FeaturePrintとテキスト認識による画面の識別 (Vision)

Shirates/Vision combinesは2つのテクニックを使用して画面を識別します。

- Image FeaturePrint
- AI-OCRによるテキスト認識

## Image FeaturePrint

- Image FeaturePrint はVision Frameworkにおいて画像の類似度を計算するためのオブジェクト（ベクトル表現）です
- FeaturePrintは画像から生成することができ、FeaturePrint間の距離(distance)を計算することができます
- 距離が小さいほど類似度は高くなります
- 単純なピクセル比較よりも抽象度の高い比較が可能です

Shirates/Visionでは、最も類似している画像を見つけるためにFeaturePrintを使用します。

## AI-OCRによるテキスト認識

FeaturePrintの距離によって最も類似しているテンプレート画像イメージを取得したとしても、
その画像がスクリーンショットの画面に正確に対応しているかどうかを決定することはできません。
あくまで、最も類似しているというだけです。

これを補うために、AI-OCRを使用したテキストによる類似比較が実行されます。

パフォーマンスの問題で、AI-OCRは全てのテンプレート画面イメージに対して実行されるわけではありません。<br>
一方、FeaturePrintの計算は非常に高速なので、vision-serverを起動した際に全てのテンプレート画像イメージに対して実行することができます。
FeaturePrintはインデックス作成のようにパフォーマンス改善のために使用されます。

## 単にImageClassifierを使用するのではダメなのか？

分類器の精度は100%とは限らないので、単純にImageClassifierを使用するだけで画面を識別することはできません。
この弱点を克服するために、テンプレート画像の中から候補となる画面をいくつか（例えば3つ）取得し、テキストを精査することが必要となります。

## 画面イメージテンプレート

```vision/screens```を確認します。

![](_images/template_screen_images.png)

`vision/screens`ディレクトリ配下にテンプレート画像を追加することができます。

### Link

- [index](../../../index_ja.md)
