# テキストインデックスによる画面認識精度の向上 (Vision)

[ScreenClassifier](../../basic/text_and_image_recognition/using_screen_classifier_ja.md)
を使用すると機械学習によって画面の画像と画面名の紐付け行い、これを利用してスクリーンショットから対応する画面名を取得することができます。各画面が特徴的なレイアウトである場合は
ScreenClassifier はうまく機能します。
一方で、似たようなレイアウトの画面が多い場合は誤認識が発生しやすく、うまくいかないケースがあります。<br>

学習させる画像を追加すれば特定の状況では認識精度が上がる場合もありますが、期待したほど認識精度が上がらない場合もあります。たくさんの画像を機械学習すると学習に必要な時間が長くなるため、これはあまり良い方法ではありません。
大規模な自動テストを安定的に運用するには別のアプローチが必要です。

Shirates/Visionではこの問題を解決するために **テキストインデックス** を利用することができます。

## テキストインデックス

画面のテキストをAI-OCRで抽出し、これを画面認識のためのインデックスとして利用します。

### テキストインデックスの要件

テキストインデックスとして利用するテキストは、その画面において固定的に表示されているもの、つまり
画面のコンテンツが書き変わったりスクロールして表示状態が変化したりしても常に表示されているテキストを選択する必要があります。

例えばAndroidの設定アプリの`開発者オプション画面`は画面が上下にスクロールしますが、常に表示されるテキストがあります。

![](_images/text_index_example_1_ja.png)

したがって、この画面の適切なテキストインデックスは以下のテキストの組み合わせになります。

- `開発者向けオプション`
- `開発者向けオプションを`
- `使用`

※ 改行箇所で分割します

<br>

### テキストインデックスの作成例

1. ScreenClassifier配下のディレクトリに `[開発者向けオプション画面]` という名前のディレクトリを作成します。
2. 画面のスクリーンショットを取得し、画面名のディレクトリ下に画像ファイルを置きます。画像ファイルには`#`
   から開始する名前をつけます。

![](_images/text_index_base_image_file_ja.png)

3. 画像を右クリックし、`Open In > Open In Associated Application` でPreviewで表示します。

![](_images/editing_image_with_preview_app_1_ja.png)

4. 編集モードに切り替えます。

![](_images/editing_image_with_preview_app_2_ja.png)

5. 矩形のシェイプを挿入します。

![](_images/editing_image_with_preview_app_3_ja.png)

5. テキストインデックス部分以外をマスクして保存します。
   ![](_images/masking_image_ja.png)

4. Shiratesで任意のテストを実行します。AI-OCRが画像からテキストを読み取り、テキストインデックスファイルが作成されます。

![](_images/text_index_file_ja.png)

5. テキストインデックスファイルの中身は以下のようになります。

![](_images/text_index_file_content_ja.png)

## サンプルコード

[サンプルの入手](../../getting_samples_ja.md)

### in_action/TextIndex1.kt

`src/test/kotlin/tutorial/inaction/TextIndex1.kt`

```kotlin
    @Test
    fun textIndexScenario1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    it.flickAndGoDown()
                        .tapWithScrollDown("システム")
                        .tapWithScrollDown("開発者向けオプション")
                }.expectation {
                    it.screenIs("[開発者向けオプション画面]")
                }
            }
        }
    }
```

このサンプルコードは以下を実行します。

1. Android設定アプリを起動
2. `システム` をタップする
3. `開発者向けオプション` をタップする
4. 表示された画面の画面名が `[開発者向けオプション画面]` であることを検証する

サンプルコードを実行すると、以下のようなログが出力されます。

```
...
127	[00:00:38]	2025/04/17 05:29:59.468	{textIndexScenario1}	0	-	[info]	+223	!	(silent)	Running device found. (udid=emulator-5554, avd=Pixel_8_Android_14)
128	[00:00:38]	2025/04/17 05:29:59.469	{textIndexScenario1}	0	-	[info]	+1	!	(silent)	接続された端末が見つかりました。(Pixel_8_Android_14:5554, Android 14, emulator-5554)
129	[00:00:39]	2025/04/17 05:29:59.844	{textIndexScenario1-1}	0	-	[CASE]	+375	!	()	(1)
130	[00:00:39]	2025/04/17 05:29:59.846	{textIndexScenario1-1}	0	-	[CONDITION]	+2	!	()	事前条件
131	[00:00:39]	2025/04/17 05:29:59.847	{textIndexScenario1-1}	0	-	[operate]	+1	!	()	[Android設定トップ画面]
132	[00:00:41]	2025/04/17 05:30:02.153	{textIndexScenario1-1}	1	[Android設定トップ画面]	[screenshot]	+2306	!	()	screenshot: 132.png
133	[00:00:42]	2025/04/17 05:30:02.751	{textIndexScenario1-1}	1	[Android設定トップ画面]	[info]	+598	!	()	132_[132.png]_recognizeText_rectangles.png
134	[00:00:42]	2025/04/17 05:30:02.881	{textIndexScenario1-1}	1	[Android設定トップ画面]	[info]	+130	!	()	[Android設定トップ画面] found by matchTextScoreRate
135	[00:00:43]	2025/04/17 05:30:03.609	{textIndexScenario1-1}	0	-	[info]	+728	!	()	Syncing screen.(isSame: true, changed: false, matchRate: 0.999458315607626, distance=5.416843923740089E-4)
136	[00:00:43]	2025/04/17 05:30:03.611	{textIndexScenario1-1}	0	-	[ACTION]	+2	!	()	アクション
137	[00:00:43]	2025/04/17 05:30:03.613	{textIndexScenario1-1}	0	-	[operate]	+2	!	(flickAndGoDown)	フリックして下方向へ進む
138	[00:00:43]	2025/04/17 05:30:04.384	{textIndexScenario1-1}	0	-	[info]	+771	!	()	Syncing screen.(isSame: false, changed: false, matchRate: 0.8892182111740112, distance=0.11078178882598877)
139	[00:00:44]	2025/04/17 05:30:05.276	{textIndexScenario1-1}	0	-	[info]	+892	!	()	Syncing screen.(isSame: false, changed: true, matchRate: 0.9026634842157364, distance=0.09733651578426361)
140	[00:00:45]	2025/04/17 05:30:06.166	{textIndexScenario1-1}	0	-	[info]	+890	!	()	Syncing screen.(isSame: true, changed: true, matchRate: 1.0, distance=0.0)
141	[00:00:45]	2025/04/17 05:30:06.171	{textIndexScenario1-1}	0	-	[screenshot]	+5	!	()	screenshot: 141.png
142	[00:00:46]	2025/04/17 05:30:06.967	{textIndexScenario1-1}	0	-	[info]	+796	!	()	141_[141.png]_recognizeText_rectangles.png
143	[00:00:47]	2025/04/17 05:30:08.103	{textIndexScenario1-1}	0	-	[info]	+1136	!	()	[recognizeScreen] in 1.921 sec
144	[00:00:47]	2025/04/17 05:30:08.104	{textIndexScenario1-1}	0	-	[info]	+1	!	()	currentScreen=[Android設定トップ画面]
145	[00:00:47]	2025/04/17 05:30:08.105	{textIndexScenario1-1}	0	-	[operate]	+1	!	(tapWithScrollDown)	<システム>をタップする（下スクロール）
146	[00:00:48]	2025/04/17 05:30:09.356	{textIndexScenario1-1}	0	-	[info]	+1251	!	(tapWithScrollDown)	145_[85, 1653, 400, 1732](w=316, h=80).png
147	[00:00:48]	2025/04/17 05:30:09.486	{textIndexScenario1-1}	0	-	[info]	+130	!	(tapWithScrollDown)	146_[145_[85, 1653, 400, 1732](w=316, h=80).png]_recognizeText_rectangles.png
148	[00:00:50]	2025/04/17 05:30:10.799	{textIndexScenario1-1}	0	-	[screenshot]	+1313	!	(tapWithScrollDown)	screenshot: 148.png
149	[00:00:51]	2025/04/17 05:30:11.600	{textIndexScenario1-1}	0	-	[info]	+801	!	(tapWithScrollDown)	148_[148.png]_recognizeText_rectangles.png
150	[00:00:53]	2025/04/17 05:30:14.367	{textIndexScenario1-1}	0	-	[screenshot]	+2767	!	(tapWithScrollDown)	screenshot: 150.png
151	[00:00:54]	2025/04/17 05:30:14.933	{textIndexScenario1-1}	0	-	[info]	+566	!	(tapWithScrollDown)	150_[150.png]_recognizeText_rectangles.png
152	[00:00:54]	2025/04/17 05:30:15.054	{textIndexScenario1-1}	0	-	[info]	+121	!	(tapWithScrollDown)	[システム画面] found by matchTextScoreRate
153	[00:00:54]	2025/04/17 05:30:15.057	{textIndexScenario1-1}	0	-	[operate]	+3	!	(tapWithScrollDown)	<開発者向けオプション>をタップする（下スクロール）
154	[00:00:55]	2025/04/17 05:30:15.564	{textIndexScenario1-1}	0	-	[info]	+507	!	(tapWithScrollDown)	Syncing screen.(isSame: false, changed: false, matchRate: 0.9924810254015028, distance=0.007518974598497152)
155	[00:00:55]	2025/04/17 05:30:16.422	{textIndexScenario1-1}	0	-	[info]	+858	!	(tapWithScrollDown)	Syncing screen.(isSame: true, changed: true, matchRate: 1.0, distance=0.0)
156	[00:00:55]	2025/04/17 05:30:16.424	{textIndexScenario1-1}	0	-	[screenshot]	+2	!	(tapWithScrollDown)	screenshot: 156.png
157	[00:00:56]	2025/04/17 05:30:17.011	{textIndexScenario1-1}	0	-	[info]	+587	!	(tapWithScrollDown)	156_[156.png]_recognizeText_rectangles.png
158	[00:00:56]	2025/04/17 05:30:17.142	{textIndexScenario1-1}	0	-	[info]	+131	!	(tapWithScrollDown)	[システム画面] found by matchTextScoreRate
159	[00:00:56]	2025/04/17 05:30:17.142	{textIndexScenario1-1}	0	-	[info]	+0	!	(tapWithScrollDown)	[recognizeScreen] in 0.717 sec
160	[00:00:56]	2025/04/17 05:30:17.142	{textIndexScenario1-1}	0	-	[info]	+0	!	(tapWithScrollDown)	currentScreen=[システム画面]
161	[00:00:56]	2025/04/17 05:30:17.148	{textIndexScenario1-1}	0	-	[operate]	+6	!	(tapWithScrollDown)	下方向へスクロールする
162	[00:00:56]	2025/04/17 05:30:17.151	{textIndexScenario1-1}	0	-	[info]	+3	!	(tapWithScrollDown)	scrollableRect: [0,0][1079,2399] width=1080, height=2400, centerX=540, centerY=1200
163	[00:01:00]	2025/04/17 05:30:20.582	{textIndexScenario1-1}	0	-	[info]	+3431	!	(tapWithScrollDown)	Syncing screen.(isSame: false, changed: false, matchRate: 0.9407601356506348, distance=0.059239864349365234)
164	[00:01:00]	2025/04/17 05:30:21.484	{textIndexScenario1-1}	0	-	[info]	+902	!	(tapWithScrollDown)	Syncing screen.(isSame: false, changed: true, matchRate: 0.9521534293889999, distance=0.04784657061100006)
165	[00:01:01]	2025/04/17 05:30:22.378	{textIndexScenario1-1}	0	-	[info]	+894	!	(tapWithScrollDown)	Syncing screen.(isSame: true, changed: true, matchRate: 1.0, distance=0.0)
166	[00:01:01]	2025/04/17 05:30:22.379	{textIndexScenario1-1}	0	-	[screenshot]	+1	!	(tapWithScrollDown)	screenshot: 166.png
167	[00:01:02]	2025/04/17 05:30:22.980	{textIndexScenario1-1}	0	-	[info]	+601	!	(tapWithScrollDown)	166_[166.png]_recognizeText_rectangles.png
168	[00:01:02]	2025/04/17 05:30:23.107	{textIndexScenario1-1}	0	-	[info]	+127	!	(tapWithScrollDown)	[システム画面] found by matchTextScoreRate
169	[00:01:02]	2025/04/17 05:30:23.107	{textIndexScenario1-1}	0	-	[info]	+0	!	(tapWithScrollDown)	[recognizeScreen] in 0.727 sec
170	[00:01:02]	2025/04/17 05:30:23.108	{textIndexScenario1-1}	0	-	[info]	+1	!	(tapWithScrollDown)	currentScreen=[システム画面]
171	[00:01:02]	2025/04/17 05:30:23.203	{textIndexScenario1-1}	0	-	[info]	+95	!	(tapWithScrollDown)	endOfScroll=false
172	[00:01:03]	2025/04/17 05:30:23.568	{textIndexScenario1-1}	0	-	[info]	+365	!	(tapWithScrollDown)	Syncing screen.(isSame: true, changed: false, matchRate: 1.0, distance=0.0)
173	[00:01:03]	2025/04/17 05:30:24.202	{textIndexScenario1-1}	0	-	[info]	+634	!	(tapWithScrollDown)	172_[192, 2069, 717, 2124](w=526, h=56).png
174	[00:01:03]	2025/04/17 05:30:24.311	{textIndexScenario1-1}	0	-	[info]	+109	!	(tapWithScrollDown)	173_[172_[192, 2069, 717, 2124](w=526, h=56).png]_recognizeText_rectangles.png
175	[00:01:05]	2025/04/17 05:30:25.549	{textIndexScenario1-1}	0	-	[screenshot]	+1238	!	(tapWithScrollDown)	screenshot: 175.png
176	[00:01:05]	2025/04/17 05:30:26.150	{textIndexScenario1-1}	0	-	[info]	+601	!	(tapWithScrollDown)	175_[175.png]_recognizeText_rectangles.png
177	[00:01:05]	2025/04/17 05:30:26.277	{textIndexScenario1-1}	0	-	[info]	+127	!	(tapWithScrollDown)	[システム画面] found by matchTextScoreRate
178	[00:01:08]	2025/04/17 05:30:28.819	{textIndexScenario1-1}	0	-	[screenshot]	+2542	!	(tapWithScrollDown)	screenshot: 178.png
179	[00:01:08]	2025/04/17 05:30:29.400	{textIndexScenario1-1}	0	-	[info]	+581	!	(tapWithScrollDown)	178_[178.png]_recognizeText_rectangles.png
180	[00:01:08]	2025/04/17 05:30:29.495	{textIndexScenario1-1}	0	-	[info]	+95	!	(tapWithScrollDown)	[開発者向けオプション画面] found by textIndex: [開発者向けオプション, 開発者向けオプションを, 使用]
181	[00:01:08]	2025/04/17 05:30:29.496	{textIndexScenario1-1}	0	-	[EXPECTATION]	+1	!	()	期待結果
182	[00:01:08]	2025/04/17 05:30:29.499	{textIndexScenario1-1}	0	-	[OK]	+3	!	(screenIs)	[開発者向けオプション画面]が表示されること
183	[00:01:08]	2025/04/17 05:30:29.499	{textIndexScenario1-1}	0	-	[info]	+0	!	()	シナリオの実行が完了しました。(処理時間: 36.5 sec)
184	[00:01:08]	2025/04/17 05:30:29.501	{}	0	-	[info]	+2	!	()	テスト関数の実行が完了しました。(処理時間: 68.7 sec)
185	[00:01:08]	2025/04/17 05:30:29.501	{}	0	-	[info]	+0	!	()	End of Test function: textIndexScenario1 [textIndexScenario1()]
...
```

<br>

`[開発者向けオプション画面]`がテキストインデックスを使用して認識されたことがわかります。

```
[開発者向けオプション画面] found by textIndex: [開発者向けオプション, 開発者向けオプションを, 使用]
```

<br>

なお、他の画面はScreenClassifierで認識されています。

```
[Android設定トップ画面] found by matchTextScoreRate

[システム画面] found by matchTextScoreRate
```

## テキストインデックスの優先度

テキストインデックスの元になる画像ファイルは`#`から開始するファイル名ですが、`#`の数を増やすと優先度が高くなります。

#a.txt // 優先度1<br>
##a.txt // 優先度2<br>
###a.txt // 優先度3<br>

※優先度の値が大きい方が優先されます。

ファイルの優先度が同じ場合は、テキストインデックスを構成する項目数が多い画面が優先されます。

- 項目数が3の画面は項目数が2の項目より優先されます
- 項目数が同じ画面の場合は不定です。優先度に差をつけたい場合は項目数を調整するように運用してください

## 画面認識は必要なのか？

画面認識機能の利用は任意です。
画像の学習あるいはテキストインデックスの作成などのセットアップに手間のかかる画面認識機能を利用するのはやめて、淡々とテスト手順をテストコードとして実装するのも悪いアイデアではありません。

ただし、画面認識を正しく行うと以下のメリットがあります。

- アクションを実行した時に表示されている画面が何なのかをログ上で容易に把握でき、デバッグが容易になる
- **onScreen関数** によるコールバックが利用できる。非同期で表示されるポップアップダイアログのような画面遷移の処理を簡単かつ確実に行える

特に **onScreen関数** は一度使うとその有効性が理解でき、手間をかけてテキストインデックスファイルを整備しようという動機が生まれる思います。

### Link

- [index](../../../index_ja.md)

