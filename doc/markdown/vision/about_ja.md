# Shirates/Visionとは

**Shirates/Vision** はmacOSのVision Frameworkを活用したコンピュータービジョン(CV)
駆動のモバイルアプリテストフレームワークです。<br>

### 真のマルチプラットフォーム

CV駆動なのでAndroidとiOS上の全てのアプリケーションプラットフォームでテストを実行できます。

- Androidネイティブ
- iOSネイティブ
- Flutter
- Compose Multiplatform
- 他

### キーテクノロジー

- AI-OCRによるテキスト認識
- 機械学習による画像分類およびマッチング

### **直感的なAPIと容易なコンフィグレーション**<br>

- 面倒な構成（Shirates/Classicにおける[画面名].jsonなど）を行うことなくモバイルアプリの操作や検証ができます
- 必要なことはスクリーンショット（画面およびアイコン）をキャプチャしてディレクトリに格納し、その画像を利用してテストコードを実装し、テストを実行することだけです

### iOSにおける顕著なパフォーマンス改善<br>

コンピュータービジョン駆動のAPIはDOMへのアクセス（この処理が特にiOSでは非常に遅い）をバイパスします。 Shirates/Vision
のパフォーマンスは旧バージョン(Shirates/Classic)と比較してiOSにおけるパフォーマンスが劇的に改善されています。

### Shirates/Classic

[**Shirates/Classic**](classic/index.md) は古いバージョンです。従来は単に**Shirates**
と表記していました。appiumを使用したDOMアクセスによる従来型の方式によるテストフレームワークです。<br>
**Shirates/Vision** はShiratesの新しいバージョンです。<br>

### Link

- [index](../index_ja.md)