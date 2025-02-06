# DefaultClassifierを使用する (Vision)

Shirates/Vision はusing DefaultClassifierを使用して画像を認識します。

### サンプルコード

[サンプルの入手](../../getting_samples_ja.md)

### Classify1.kt

(`src/test/kotlin/tutorial/basic/Classify1.kt`)

```kotlin
    @Test
    @Order(10)
    fun classify() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    v1 = it.detect("ネットワークとインターネット")
                        .leftItem()
                    s1 = v1.classify()
                }.expectation {
                    s1.thisIs("[ネットワークとインターネットアイコン]", message = "label is $s1")
                }
            }
            case(2) {
                action {
                    s2 = v1.classifyFull()
                }.expectation {
                    s2.thisIs(
                        "@a[Android設定アプリ][Android設定トップ画面][ネットワークとインターネットアイコン]",
                        message = "fullLabel is $s2"
                    )
                }
            }
        }
    }
```

`classify()`を右クリックして`debug`を選択してテストを実行します。

### コンソール出力

```
139	[00:00:26]	2025/02/07 01:58:53.564	{classify-1}	0	-	[info]	+270	!	()	138_[137.png]_recognized_text_rectangles.png
140	[00:00:26]	2025/02/07 01:58:53.770	{classify-1}	0	-	[info]	+206	!	()	[detect] in 2.275 sec
2025-02-07 01:58:53.772 java[27836:744198] +[IMKClient subclass]: chose IMKClient_Modern
2025-02-07 01:58:53.772 java[27836:744198] +[IMKInputSession subclass]: chose IMKInputSession_Modern
141	[00:00:27]	2025/02/07 01:58:54.864	{classify-1}	0	-	[info]	+1094	!	()	<ネットワークとインターネット>[_左のアイテム].png
142	[00:00:27]	2025/02/07 01:58:54.904	{classify-1}	0	-	[info]	+40	!	()	[ImageClassifier/classifyImage] in 0.037 sec
143	[00:00:27]	2025/02/07 01:58:54.906	{classify-1}	0	-	[EXPECTATION]	+2	!	()	期待結果
144	[00:00:27]	2025/02/07 01:58:54.907	{classify-1}	0	-	[OK]	+1	!	(thisIs)	label is [ネットワークとインターネットアイコン]
145	[00:00:27]	2025/02/07 01:58:54.908	{classify-2}	0	-	[CASE]	+1	!	()	(2)
146	[00:00:27]	2025/02/07 01:58:54.908	{classify-2}	0	-	[ACTION]	+0	!	()	アクション
147	[00:00:27]	2025/02/07 01:58:54.936	{classify-2}	0	-	[info]	+28	!	()	[ImageClassifier/classifyImage] in 0.027 sec
148	[00:00:27]	2025/02/07 01:58:54.937	{classify-2}	0	-	[EXPECTATION]	+1	!	()	期待結果
149	[00:00:27]	2025/02/07 01:58:54.938	{classify-2}	0	-	[OK]	+1	!	(thisIs)	fullLabel is @a[Android設定アプリ][Android設定トップ画面][ネットワークとインターネットアイコン]
```

### TestResults

TestResults ディレクトリ(デフォルトは`~/Downloads/TestResults`)にテスト結果のファイルが出力されます。

### fullLabel と label

ディレクトリ`build/vision/classifiers/DefaultClassifier/training`を開きます。

![](_images/full_label_and_label_ja.png)

`fullLabel` ラベルの完全な記述です。

```
@a[Android設定アプリ][Android設定トップ画面][ネットワークとインターネットアイコン]
```

`label` は短い記述です。

```
[ネットワークとインターネットアイコン]
```

<br>
<hr>

### サンプルコード

[サンプルの入手](../../getting_samples_ja.md)

### ImageIs1.kt

(`src/test/kotlin/tutorial/basic/ImageIs1.kt`)

```kotlin
    @Test
    @Order(10)
    fun imageIs() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android設定トップ画面]")
                }.action {
                    v1 = it.detect("ネットワークとインターネット")
                        .leftItem()
                }.expectation {
                    v1.imageIs("[ネットワークとインターネットアイコン]")
                }
            }
            case(2) {
                expectation {
                    v1.imageFullLabelIs("@a[Android設定アプリ][Android設定トップ画面][ネットワークとインターネットアイコン]")
                }
            }
        }
    }
```

`imageIs()`を右クリックし`debug`を選択してテストを実行します。

### コンソール出力

```
141	[00:00:27]	2025/02/07 02:03:40.128	{imageIs-1}	0	-	[EXPECTATION]	+1253	!	()	期待結果
142	[00:00:27]	2025/02/07 02:03:40.132	{imageIs-1}	0	-	[info]	+4	!	(imageIs)	<ネットワークとインターネット>[_左のアイテム].png
143	[00:00:27]	2025/02/07 02:03:40.199	{imageIs-1}	0	-	[info]	+67	!	(imageIs)	[ImageClassifier/classifyImage] in 0.064 sec
144	[00:00:27]	2025/02/07 02:03:40.201	{imageIs-1}	0	-	[info]	+2	!	(imageIs)	label: [ネットワークとインターネットアイコン]
145	[00:00:27]	2025/02/07 02:03:40.204	{imageIs-1}	0	-	[info]	+3	!	(imageIs)	144_text_ "", bounds_ [84,879][146,925] width=63, height=47, centerX=115, centerY=902, rect_ [84, 879, 146, 925](w=63, h=47).png
146	[00:00:27]	2025/02/07 02:03:40.210	{imageIs-1}	0	-	[OK]	+6	!	(imageIs)	<ネットワークとインターネット>[:左のアイテム]の画像が"[ネットワークとインターネットアイコン]"であること
147	[00:00:27]	2025/02/07 02:03:40.211	{imageIs-2}	0	-	[CASE]	+1	!	()	(2)
148	[00:00:27]	2025/02/07 02:03:40.212	{imageIs-2}	0	-	[EXPECTATION]	+1	!	()	期待結果
149	[00:00:27]	2025/02/07 02:03:40.278	{imageIs-2}	0	-	[info]	+66	!	(imageFullLabelIs)	[ImageClassifier/classifyImage] in 0.063 sec
150	[00:00:27]	2025/02/07 02:03:40.279	{imageIs-2}	0	-	[info]	+1	!	(imageFullLabelIs)	fullLabel: @a[Android設定アプリ][Android設定トップ画面][ネットワークとインターネットアイコン]
151	[00:00:27]	2025/02/07 02:03:40.280	{imageIs-2}	0	-	[info]	+1	!	(imageFullLabelIs)	150_text_ "", bounds_ [84,879][146,925] width=63, height=47, centerX=115, centerY=902, rect_ [84, 879, 146, 925](w=63, h=47).png
152	[00:00:27]	2025/02/07 02:03:40.282	{imageIs-2}	0	-	[OK]	+2	!	(imageFullLabelIs)	<ネットワークとインターネット>[:左のアイテム]の画像フルラベルが@a[Android設定アプリ][Android設定トップ画面][ネットワークとインターネットアイコン]であること
```

<br>
<hr>

### FindImage1.kt

(`src/test/kotlin/tutorial/basic/FindImage1.kt`)

```kotlin
    @Test
    @Order(10)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android設定トップ画面]")
                }.action {
                    withScrollDown {
                        v1 = it.findImage("[ネットワークとインターネットアイコン]")
                        v2 = it.findImage("[ディスプレイアイコン]")
                    }
                }.expectation {
                    v1.isFound.thisIsTrue("[ネットワークとインターネットアイコン]が見つかりました。")
                    v2.isFound.thisIsTrue("[ディスプレイアイコン]が見つかりました。")
                }
            }
        }
    }
```

### テストを実行する

1. Androidで実行されるように`testrun.global.properties`の`os`を設定します (デフォルトは`android`
   なので単にコメントアウトします)。

```properties
## OS --------------------
#os=ios
```

2. `findImage()`を右クリックし`debug`を選択してテストを実行します。

### コンソール出力

```
129	[00:00:24]	2025/02/07 02:05:46.080	{findImage-1}	0	-	[ACTION]	+4575	!	()	アクション
130	[00:00:24]	2025/02/07 02:05:46.082	{findImage-1}	0	-	[info]	+2	!	()	Trying to find image with scroll. (label="[ネットワークとインターネットアイコン]")
131	[00:00:24]	2025/02/07 02:05:46.225	{findImage-1}	0	-	[info]	+143	!	()	130_[0, 0, 1079, 2399](w=1080, h=2400).png
132	[00:00:24]	2025/02/07 02:05:46.574	{findImage-1}	0	-	[info]	+349	!	()	normalized_template_img.png
2025-02-07 02:05:46.780 java[28200:754113] +[IMKClient subclass]: chose IMKClient_Modern
2025-02-07 02:05:46.780 java[28200:754113] +[IMKInputSession subclass]: chose IMKInputSession_Modern
133	[00:00:27]	2025/02/07 02:05:49.540	{findImage-1}	0	-	[info]	+2966	!	()	[findImage] in 3.458 sec
134	[00:00:27]	2025/02/07 02:05:49.541	{findImage-1}	0	-	[info]	+1	!	()	Trying to find image with scroll. (label="[ディスプレイアイコン]")
135	[00:00:27]	2025/02/07 02:05:49.647	{findImage-1}	0	-	[info]	+106	!	()	134_[0, 0, 1079, 2399](w=1080, h=2400).png
136	[00:00:27]	2025/02/07 02:05:49.751	{findImage-1}	0	-	[info]	+104	!	()	normalized_template_img.png
137	[00:00:30]	2025/02/07 02:05:52.090	{findImage-1}	0	-	[info]	+2339	!	()	findImage("[ディスプレイアイコン]") not found. (distance:0.6129697 > threshold:0.1)
138	[00:00:30]	2025/02/07 02:05:52.092	{findImage-1}	0	-	[operate]	+2	!	(scrollDown)	下方向へスクロールする
139	[00:00:34]	2025/02/07 02:05:56.306	{findImage-1}	0	-	[info]	+4214	!	()	endOfScroll=false
140	[00:00:34]	2025/02/07 02:05:56.307	{findImage-1}	0	-	[info]	+1	!	()	139_[0, 0, 1079, 2399](w=1080, h=2400).png
141	[00:00:34]	2025/02/07 02:05:56.385	{findImage-1}	0	-	[info]	+78	!	()	normalized_template_img.png
142	[00:00:37]	2025/02/07 02:05:59.316	{findImage-1}	0	-	[info]	+2931	!	()	[findImage] in 9.775 sec
143	[00:00:37]	2025/02/07 02:05:59.317	{findImage-1}	0	-	[EXPECTATION]	+1	!	()	期待結果
144	[00:00:37]	2025/02/07 02:05:59.318	{findImage-1}	0	-	[OK]	+1	!	(thisIsTrue)	[ネットワークとインターネットアイコン]が見つかりました。
145	[00:00:37]	2025/02/07 02:05:59.319	{findImage-1}	0	-	[OK]	+1	!	(thisIsTrue)	[ディスプレイアイコン]が見つかりました。
```

### Link

- [index](../../../index_ja.md)
