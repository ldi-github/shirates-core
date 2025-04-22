# 画像の学習 (Vision)

Shirates/Visionは機械学習ツール(**CreateML**)をテスト実行プロセスに統合します。<br>
画像分類のパワーをテストコードで利用できます。<br>
分類器のラベルのディレクトリにテンプレート画像を配置してください。

## DefaultClassifier

**DefaultClassifier** テンプレートマッチング用の画像分類器です。<br>
画像のディレクトリの名前がラベルに対応します。

![](_images/default_classifier_ja.png)

ラベルのディレクトリには1つ以上の画像ファイルを配置します。基本的には1つのラベルに対して1つの画像を用意すれば十分です。<br>
ラベル名は任意ですが、`[ニックネーム]`のような表記を推奨します。

## CheckStateClassifier

**CheckStateClassifier**はトグル型のウィジェットの状態用の分類器です。この分類器は2つのラベル (`[ON]`と`[OFF]`)のみを持ちます。

![](_images/check_state_classifier.png)

上記の例ではラジオボタンとスイッチがサポートされます。<br>
チェックボックスをサポートしたい場合は[ON]/[OFF]のディレクトリに画像を追加し学習を実行してください。

## 学習の実行

![](_images/running_learning.png)

明示的に学習を実行したい場合は

`CreateMlExecute` (`src/test/kotlin/batch/CreateMLExecute.kt`)を右クリックし`Debug 'CreateMLExecute'`
を選択します。

**注意**<br>
学習を明示的に実行する必要はありません。必要な場合はテストを開始した際に学習が実行されます。

### コンソール出力

```
Connected to the target VM, address: '127.0.0.1:59730', transport: 'socket'
lineNo	[elapsedTime]	logDateTime	{testCaseId}	macroDepth	macroName	[logType]	timeDiff	mode	(group)	message
1	[00:00:00]	2025/04/23 04:11:01.771	{}	0	-	[info]	+0	C	()	Classifier files loaded.(CheckStateClassifier, 2 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/CheckStateClassifier/1)
Copying jar content createml/MLImageClassifier.swift to /Users/wave1008/Downloads/TestResults/2025-04-23_041101
2	[00:00:00]	2025/04/23 04:11:02.141	{}	0	-	[info]	+370	C	()	Starting leaning. [CheckStateClassifier]
3	[00:00:02]	2025/04/23 04:11:04.194	{}	0	-	[info]	+2053	C	()	Learning completed. ([swift /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/CheckStateClassifier/1/MLImageClassifier.swift /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/CheckStateClassifier/1 -noise -blur] in 2.040 sec)
["/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/CheckStateClassifier/1/MLImageClassifier.swift", "/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/CheckStateClassifier/1", "-noise", "-blur"]
----------------------------------
dataSourceName: 1
dataSourcePath: file:///Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/CheckStateClassifier/1/
options: ["-noise", "-blur"]
featureExtractor: Image Feature Print V2
----------------------------------
Number of examples: 16
Number of classes: 2
Accuracy: 100.00%

******CONFUSION MATRIX******
----------------------------------
True\Pred [OFF] [ON]  
[OFF]     8     0     
[ON]      0     8     

******PRECISION RECALL******
----------------------------------
Class Precision(%) Recall(%)
[OFF] 100.00          100.00         
[ON]  100.00          100.00         


Model saved to /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/CheckStateClassifier/1/1.mlmodel
4	[00:00:02]	2025/04/23 04:11:04.210	{}	0	-	[info]	+16	C	()	Classifier files loaded.(ScreenClassifier, 20 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/ScreenClassifier/1)
Copying jar content createml/MLImageClassifier.swift to /Users/wave1008/Downloads/TestResults/2025-04-23_041101
5	[00:00:04]	2025/04/23 04:11:06.136	{}	0	-	[info]	+1926	C	()	Starting leaning. [ScreenClassifier]
6	[00:00:07]	2025/04/23 04:11:08.640	{}	0	-	[info]	+2504	C	()	Learning completed. ([swift /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/ScreenClassifier/1/MLImageClassifier.swift /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/ScreenClassifier/1 -noise -blur] in 2.502 sec)
["/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/ScreenClassifier/1/MLImageClassifier.swift", "/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/ScreenClassifier/1", "-noise", "-blur"]
----------------------------------
dataSourceName: 1
dataSourcePath: file:///Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/ScreenClassifier/1/
options: ["-noise", "-blur"]
featureExtractor: Image Feature Print V2
----------------------------------
Number of examples: 20
Number of classes: 19
Accuracy: 100.00%

******CONFUSION MATRIX******
----------------------------------
True\Pred                          @a_Android_[Androidホーム画面]          @a_Android設定_[Android設定トップ画面]      @a_Android設定_[Android設定検索画面]       @a_Android設定_[インターネット画面]           @a_Android設定_[エミュレートされたデバイスについて画面] @a_Android設定_[システム画面]              @a_Android設定_[ネットワークとインターネット画面]    @a_Android設定_[バッテリー画面]             @a_Android設定_[ユーザー補助画面]            @a_Android設定_[接続設定画面]              @a_Android設定_[通知画面]                @a_ファイル_[ファイルトップ画面]                @a_マップ_[マップトップ画面]                  @a_電卓_[電卓メイン画面]                    @i_iOS_[iOSホーム画面]                  @i_iOS設定_[iOS設定トップ画面]              @i_iOS設定_[デベロッパ画面]                 @i_iOS設定_[一般画面]                    @i_iOS設定_[情報画面]                    
@a_Android_[Androidホーム画面]          1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[Android設定トップ画面]      0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[Android設定検索画面]       0                                  0                                  2                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[インターネット画面]           0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[エミュレートされたデバイスについて画面] 0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[システム画面]              0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[ネットワークとインターネット画面]    0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[バッテリー画面]             0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[ユーザー補助画面]            0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[接続設定画面]              0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_Android設定_[通知画面]                0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_ファイル_[ファイルトップ画面]                0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_マップ_[マップトップ画面]                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  0                                  
@a_電卓_[電卓メイン画面]                    0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  0                                  
@i_iOS_[iOSホーム画面]                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  0                                  
@i_iOS設定_[iOS設定トップ画面]              0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  0                                  
@i_iOS設定_[デベロッパ画面]                 0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  0                                  
@i_iOS設定_[一般画面]                    0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  0                                  
@i_iOS設定_[情報画面]                    0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  0                                  1                                  

******PRECISION RECALL******
----------------------------------
Class                              Precision(%) Recall(%)
@a_Android_[Androidホーム画面]          100.00          100.00         
@a_Android設定_[Android設定トップ画面]      100.00          100.00         
@a_Android設定_[Android設定検索画面]       100.00          100.00         
@a_Android設定_[インターネット画面]           100.00          100.00         
@a_Android設定_[エミュレートされたデバイスについて画面] 100.00          100.00         
@a_Android設定_[システム画面]              100.00          100.00         
@a_Android設定_[ネットワークとインターネット画面]    100.00          100.00         
@a_Android設定_[バッテリー画面]             100.00          100.00         
@a_Android設定_[ユーザー補助画面]            100.00          100.00         
@a_Android設定_[接続設定画面]              100.00          100.00         
@a_Android設定_[通知画面]                100.00          100.00         
@a_ファイル_[ファイルトップ画面]                100.00          100.00         
@a_マップ_[マップトップ画面]                  100.00          100.00         
@a_電卓_[電卓メイン画面]                    100.00          100.00         
@i_iOS_[iOSホーム画面]                  100.00          100.00         
@i_iOS設定_[iOS設定トップ画面]              100.00          100.00         
@i_iOS設定_[デベロッパ画面]                 100.00          100.00         
@i_iOS設定_[一般画面]                    100.00          100.00         
@i_iOS設定_[情報画面]                    100.00          100.00         


Model saved to /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/ScreenClassifier/1/1.mlmodel
7	[00:00:07]	2025/04/23 04:11:08.655	{}	0	-	[info]	+15	C	()	Classifier files loaded.(DefaultClassifier, 32 labels, directory=/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/DefaultClassifier/1)
Copying jar content createml/MLImageClassifier.swift to /Users/wave1008/Downloads/TestResults/2025-04-23_041101
8	[00:00:07]	2025/04/23 04:11:09.158	{}	0	-	[info]	+503	C	()	Starting leaning. [DefaultClassifier]
9	[00:00:10]	2025/04/23 04:11:11.798	{}	0	-	[info]	+2640	C	()	Learning completed. ([swift /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/DefaultClassifier/1/MLImageClassifier.swift /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/DefaultClassifier/1 -noise -blur] in 2.640 sec)
["/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/DefaultClassifier/1/MLImageClassifier.swift", "/Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/DefaultClassifier/1", "-noise", "-blur"]
----------------------------------
dataSourceName: 1
dataSourcePath: file:///Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/DefaultClassifier/1/
options: ["-noise", "-blur"]
featureExtractor: Image Feature Print V2
----------------------------------
Number of examples: 33
Number of classes: 32
Accuracy: 100.00%

******CONFUSION MATRIX******
**** (upperleft 20-by-20) ****
----------------------------------
True\Pred                                          @a_Androidファイル_[サイズの大きいファイルボタン]                    @a_Androidファイル_[ドキュメントボタン]                         @a_Androidファイル_[今週ボタン]                             @a_Androidファイル_[動画ボタン]                             @a_Androidファイル_[画像ボタン]                             @a_Androidファイル_[音声ボタン]                             @a_Android設定_Android設定トップ画面_[アプリアイコン]              @a_Android設定_Android設定トップ画面_[システムアイコン]             @a_Android設定_Android設定トップ画面_[ストレージアイコン]            @a_Android設定_Android設定トップ画面_[ディスプレイアイコン]           @a_Android設定_Android設定トップ画面_[ネットワークとインターネットアイコン]   @a_Android設定_Android設定トップ画面_[バッテリーアイコン]            @a_Android設定_Android設定トップ画面_[位置情報アイコン]             @a_Android設定_Android設定トップ画面_[接続設定アイコン]             @a_Android設定_Android設定トップ画面_[音とバイブレーションアイコン]       @a_Android設定_ネットワークとインターネット画面_[SIMアイコン]            @a_Android設定_ネットワークとインターネット画面_[VPNアイコン]            @a_Android設定_ネットワークとインターネット画面_[アクセスポイントとテザリングアイコン] @a_Android設定_ネットワークとインターネット画面_[インターネットアイコン]        @a_Android設定_ネットワークとインターネット画面_[データセーバーアイコン]        
@a_Androidファイル_[サイズの大きいファイルボタン]                    1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Androidファイル_[ドキュメントボタン]                         0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Androidファイル_[今週ボタン]                             0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Androidファイル_[動画ボタン]                             0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Androidファイル_[画像ボタン]                             0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Androidファイル_[音声ボタン]                             0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[アプリアイコン]              0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[システムアイコン]             0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[ストレージアイコン]            0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[ディスプレイアイコン]           0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[ネットワークとインターネットアイコン]   0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[バッテリーアイコン]            0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[位置情報アイコン]             0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[接続設定アイコン]             0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_Android設定トップ画面_[音とバイブレーションアイコン]       0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_ネットワークとインターネット画面_[SIMアイコン]            0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  0                                                  
@a_Android設定_ネットワークとインターネット画面_[VPNアイコン]            0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  0                                                  
@a_Android設定_ネットワークとインターネット画面_[アクセスポイントとテザリングアイコン] 0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  0                                                  
@a_Android設定_ネットワークとインターネット画面_[インターネットアイコン]        0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  0                                                  
@a_Android設定_ネットワークとインターネット画面_[データセーバーアイコン]        0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  0                                                  1                                                  

******PRECISION RECALL******
----------------------------------
Class                                              Precision(%) Recall(%)
@a_Androidファイル_[サイズの大きいファイルボタン]                    100.00          100.00         
@a_Androidファイル_[ドキュメントボタン]                         100.00          100.00         
@a_Androidファイル_[今週ボタン]                             100.00          100.00         
@a_Androidファイル_[動画ボタン]                             100.00          100.00         
@a_Androidファイル_[画像ボタン]                             100.00          100.00         
@a_Androidファイル_[音声ボタン]                             100.00          100.00         
@a_Android設定_Android設定トップ画面_[アプリアイコン]              100.00          100.00         
@a_Android設定_Android設定トップ画面_[システムアイコン]             100.00          100.00         
@a_Android設定_Android設定トップ画面_[ストレージアイコン]            100.00          100.00         
@a_Android設定_Android設定トップ画面_[ディスプレイアイコン]           100.00          100.00         
@a_Android設定_Android設定トップ画面_[ネットワークとインターネットアイコン]   100.00          100.00         
@a_Android設定_Android設定トップ画面_[バッテリーアイコン]            100.00          100.00         
@a_Android設定_Android設定トップ画面_[位置情報アイコン]             100.00          100.00         
@a_Android設定_Android設定トップ画面_[接続設定アイコン]             100.00          100.00         
@a_Android設定_Android設定トップ画面_[音とバイブレーションアイコン]       100.00          100.00         
@a_Android設定_ネットワークとインターネット画面_[SIMアイコン]            100.00          100.00         
@a_Android設定_ネットワークとインターネット画面_[VPNアイコン]            100.00          100.00         
@a_Android設定_ネットワークとインターネット画面_[アクセスポイントとテザリングアイコン] 100.00          100.00         
@a_Android設定_ネットワークとインターネット画面_[インターネットアイコン]        100.00          100.00         
@a_Android設定_ネットワークとインターネット画面_[データセーバーアイコン]        100.00          100.00         
@a_Android設定_ネットワークとインターネット画面_[機内モードアイコン]          100.00          100.00         
@a_Android設定_ネットワークとインターネット画面_[通話とSMSアイコン]         100.00          100.00         
@a_misc_[←]                                        100.00          100.00         
@a_misc_[ラジオボタン(OFF)]                              100.00          100.00         
@a_misc_[ラジオボタン(ON)]                               100.00          100.00         
@i_iOS設定_[Apple IntelligenceとSiriアイコン]             100.00          100.00         
@i_iOS設定_[アクションボタンアイコン]                            100.00          100.00         
@i_iOS設定_[アクセシビリティアイコン]                            100.00          100.00         
@i_iOS設定_[カメラアイコン]                                 100.00          100.00         
@i_iOS設定_[スクリーンタイムアイコン]                            100.00          100.00         
@i_iOS設定_[デベロッパアイコン]                               100.00          100.00         
@i_iOS設定_[一般アイコン]                                  100.00          100.00         


Model saved to /Users/wave1008/github/ldi-github/shirates-core-vision-samples_ja/build/vision/classifiers/DefaultClassifier/1/1.mlmodel
Disconnected from the target VM, address: '127.0.0.1:59730', transport: 'socket'

Process finished with exit code 0
```

## build/vision/classifiers

結果のファイルは`build/vision/classifiers`へ出力されます。

![](_images/build_vision_classifiers.png)

### 1.mlmodel

出力されたmlmodelファイルです。

### createML.log

コンソールのログです（上記の**コンソール出力**を参照）。

### MLImageClassifier.swift

実行されるスクリプトです。

```swift
import CreateML
import Foundation

let arguments = CommandLine.arguments
print(arguments)
if arguments.count < 2 {
    print("path to data source is required")
    exit(1)
}
let options = arguments.filter { $0.starts(with: "-") }

var revision = 2
if let opt = options.first(where: { $0.starts(with: "-fp:")}) {
    if let r = Int(opt.components(separatedBy: [":","="]).last!) {
        revision = r
    }
}

let dataSourcePath = arguments[1]
let dataSourceURL = URL(fileURLWithPath: dataSourcePath)
let dataSourceName = dataSourceURL.lastPathComponent

print("----------------------------------")
print("dataSourceName: \(dataSourceName)")
print("dataSourcePath: \(dataSourceURL)")
print("options: \(options)")
print("featureExtractor: Image Feature Print V\(revision)")

// data source URLs
let trainingDataURL = dataSourceURL.appending(path: "training")
let testingDataURL = dataSourceURL.appending(path: "test")

// data sources
let trainingData = MLImageClassifier.DataSource.labeledDirectories(at: trainingDataURL)
let testingData = MLImageClassifier.DataSource.labeledDirectories(at: testingDataURL)

// model parameters
var augmentation = MLImageClassifier.ImageAugmentationOptions()
if options.contains("-noise") {
    augmentation.insert(.noise)
}
if options.contains("-blur") {
    augmentation.insert(.blur)
}
if options.contains("-crop") {
    augmentation.insert(.crop)
}
if options.contains("-exposure") {
    augmentation.insert(.exposure)
}
if options.contains("-flip") {
    augmentation.insert(.flip)
}
if options.contains("-rotation") {
    augmentation.insert(.rotation)
}
let modelParameters = MLImageClassifier.ModelParameters(
    validation: .split(strategy: .automatic),
    augmentation: augmentation,
    algorithm: .transferLearning(
        featureExtractor: .scenePrint(revision: revision),
        classifier: .logisticRegressor
    )
)

// training
let classifier = try MLImageClassifier(trainingData: trainingData, parameters: modelParameters)

// evaluation
let evaluation = classifier.evaluation(on: testingData)
print("\(evaluation)")

// save
var parent = dataSourceURL
parent.deleteLastPathComponent()
parent.append(path: "models")
let modelURL = URL(fileURLWithPath: "\(dataSourcePath)/\(dataSourceName).mlmodel")
try classifier.write(to: modelURL)

print("Model saved to \(modelURL.path)")
```

### Link

- [index](../../../index_ja.md)
