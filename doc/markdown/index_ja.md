# Shirates/Vision

[in English(英語)](index.md)

### Shiratesのバージョン

**Shirates/Vision** はShiratesの新しいバージョンです。<br>
[Shirates/Classic](classic/index_ja.md) は古いバージョンです。

## リポジトリ

- [shirates-core](https://github.com/ldi-github/shirates-core)

## 概要

- [Shirates/Visionとは](vision/about_ja.md)
- [テスト済みの環境](environments.md)
- [クイックスタート(Shirates/Vision)](quick-start_ja.md)

## チュートリアル(Basic)

### サンプル

- [サンプルの入手](vision/getting_samples_ja.md)

### 推奨設定

- [コードフォーマット (IntelliJ IDEA)](common/tool_settings/code_format_ja.md)
- [右クリックによるテスト実行を有効にする (IntelliJ IDEA)](common/tool_settings/right_click_test_running_ja.md)
- [propertiesファイルのエンコーディング設定 (IntelliJ IDEA)](common/tool_settings/properties_file_encoding_ja.md)
- [Androidデバイスの設定](vision/basic/recommended_settings/recommended_settings_android_ja.md)

### 環境の切り替え

- [Android/iOSの切り替え](vision/basic/switching_environment/switching_android_ios_ja.md)
- [デバイスの切り替え](vision/basic/switching_environment/switching_device_ja.md)
- [デバイスの言語の切り替え](vision/basic/switching_environment/switching_device_language_ja.md)
- [ログ言語の切り替え](common/message_and_language/log_language_ja.md)
- [AI-OCR言語の切り替え](vision/basic/switching_environment/switching_ai_ocr_language_ja.md)

### プロジェクトの作成

- [プロジェクトを作成する](vision/basic/creating_project/creating_project_ja.md)
- [テスト構成テンプレート](vision/basic/creating_project/test_configuration_template_ja.md)

### テストクラスの作成

- [テストクラスを作成する](vision/basic/creating_testclass/creating_testclass_ja.md)
- [テキストを検出して検証する](vision/basic/creating_testclass/detect_and_assert_ja.md)
- [テストコードの構造](vision/basic/creating_testclass/testcode_structure_ja.md)

### コンピュータービジョンによるテキストと画像の認識

- [AI-OCRによるテキスト認識](vision/basic/text_and_image_recognition/text_recognition_by_ai_ocr_ja.md)
- [AI/MLによる画像検索](vision/basic/text_and_image_recognition/finding_image_by_ai_ml_ja.md)
- [画像の学習](vision/basic/text_and_image_recognition/learning_images_ja.md)
- [CheckStateClassifierを使用する](vision/basic/text_and_image_recognition/using_check_state_classifier_ja.md)
- [DefaultClassifierを使用する](vision/basic/text_and_image_recognition/using_default_classifier_ja.md)
- [ScreenClassifierを使用する](vision/basic/text_and_image_recognition/using_screen_classifier_ja.md)

### マクロ

- [マクロ](vision/basic/routine_work/macro_ja.md)

### セレクターとニックネーム

- [セレクター式](vision/basic/selector_and_nickname/selector_expression_ja.md)
- [ニックネーム](vision/basic/selector_and_nickname/nickname_ja.md)
    - [データセットニックネーム](classic/basic/selector_and_nickname/nickname/dataset_nickname_ja.md)

### パラメーター

- [パラメーター構成ファイル](common/parameter/parameter_configuration_files_ja.md)
- [パラメーター](common/parameter/parameters_ja.md)
- [プロファイルの構成](common/parameter/profile_configuration_ja.md)
- [自動デバイス検出](common/parameter/automatic_device_detection_ja.md)

### 関数/プロパティ

- 要素を選択する
    - [detect](vision/basic/function_property/detect_text/detect_ja.md)
    - [canDetect](vision/basic/function_property/detect_text/can_detect_ja.md)
- 画像を見つける
    - [findImage](vision/basic/function_property/getting_image/find_image_ja.md)
    - [canFindImage](vision/basic/function_property/getting_image/can_find_image_ja.md)
- 要素をタップする
    - [tap](vision/basic/function_property/tap_element/tap_ja.md)
    - [tapImage](vision/basic/function_property/tap_element/tap_image_ja.md)
    - [tapAppIcon](vision/basic/function_property/tap_element/tap_app_icon_ja.md)
- インストールとアプリの起動
    - [installApp, removeApp](vision/basic/function_property/install_and_launch_app/install_app_ja.md)
    - [launchApp, terminateApp](vision/basic/function_property/install_and_launch_app/launch_app_ja.md)
- 作業領域を指定する
    - [onTopRegion, onBottomRegion, onMiddleRegion, onRegion, onUpperHalfRegion, onLowerHalfRegion](vision/basic/function_property/setting_working_region/setting_working_region_ja.md)
- 画面をスワイプ/スクロールする
    - [swipe, flick](vision/basic/function_property/swipe_screen/swipe_flick_ja.md)
    - [scroll](vision/basic/function_property/swipe_screen/scroll_ja.md)
    - [スクロール可能領域の決定(onLine, onLineOf, onColum, onColumnOf)](vision/basic/function_property/swipe_screen/determining_scrollable_area_ja.md)
- 画像/テキストを相対的に取得する
    - [相対テキスト (leftText, rightText, belowText, aboveText)](vision/basic/function_property/getting_image/relative_text_ja.md)
    - [相対イメージ (leftItem, rightItem, belowItem, aboveItem)](vision/basic/function_property/getting_image/relative_image_ja.md)
- テキストを検証する
    - [テキストの検証](vision/basic/function_property/asserting_text/text_assertion_ja.md)
- 画像を検証する
    - [画像の検証](vision/basic/function_property/asserting_image/image_assertion_ja.md)
- 任意の値を検証する
    - [任意の値の検証](vision/basic/function_property/asserting_any_value/any_value_assertion_ja.md)
    - [文字列の検証](vision/basic/function_property/asserting_any_value/string_assertion_ja.md)
    - [真偽値の検証](vision/basic/function_property/asserting_any_value/boolean_assertion_ja.md)

- 存在することを検証する
    - [テキストが存在することの検証](vision/basic/function_property/asserting_existence/text_existance_assertion_ja.md)
    - [画像が存在することの検証](vision/basic/function_property/asserting_existence/image_existance_assertion_ja.md)
    - [画面が表示されていることの検証](vision/basic/function_property/asserting_existence/screen_assertion_ja.md)
- その他を検証する
    - [アプリが表示されていることの検証](vision/basic/function_property/asserting_others/app_assertion_ja.md)
    - [キーボードが表示されていることの検証](vision/basic/function_property/asserting_others/keyboard_assertion_ja.md)
    - [表示中のアプリのパッケージの検証](vision/basic/function_property/asserting_others/package_assertion_ja.md)
- 任意の内容を検証する
    - [任意の内容の検証 (verify)](vision/basic/function_property/asserting_anything/anything_assertion_ja.md)
- 分岐する
    - [分岐関数 (ifTrue, ifFalse)](vision/basic/function_property/branch/if_true_if_false_ja.md)
    - [分岐関数 (ifScreenIs, ifScreenIsNot)](vision/basic/function_property/branch/if_screen_is_ja.md)
    - [分岐関数 (ifCanDetect, ifCanDetectNot)](vision/basic/function_property/branch/if_can_detect_ja.md)
    - [分岐関数 (ifImageExist, ifImageExistNot)](vision/basic/function_property/branch/if_image_exist_ja.md)
    - [分岐関数 (ifImageIs, ifImageIsNot)](vision/basic/function_property/branch/if_image_is_ja.md)
    - [分岐関数 (ifStringIs, ifStartsWith, etc)](vision/basic/function_property/branch/if_string_ja.md)
    - [プラットフォーム関数 (android, ios, emulator, simulator, virtualDevice, realDevice)](vision/basic/function_property/branch/platform_branch_functions_ja.md)
    - [プラットフォームプロパティ (platformName, platformVersion, isAndroid, isiOS, isVirtualDevice, isRealDevice)](vision/basic/function_property/branch/platform_properties_ja.md)
- イベントハンドラー
    - [スクリーンハンドラー(onScreen)](vision/basic/function_property/event_handler/on_screen_ja.md)
- 編集とキーボード操作を行う
    - [sendKeys](vision/basic/function_property/editing_and_keyboard_operations/sendkeys_ja.md)
    - [clearInput](vision/basic/function_property/editing_and_keyboard_operations/clearinput_ja.md)
    - [press keys](vision/basic/function_property/editing_and_keyboard_operations/press_keys_ja.md)
    - [hideKeyboard](vision/basic/function_property/editing_and_keyboard_operations/hide_keyboard_ja.md)
- 同期する
    - [wait](vision/basic/syncing/wait_ja.md)
    - [waitScreen, waitScreenOf](vision/basic/syncing/wait_screen_ja.md)
    - [waitForClose](vision/basic/syncing/wait_for_close_ja.md)
    - [waitForDisplay](vision/basic/syncing/wait_for_display_ja.md)
- アクションを繰り返す
    - [doUntilTrue](vision/basic/repeating_action/do_until_true_ja.md)
- データストレージ
    - [writeMemo, readMemo, memoTextAs](vision/basic/function_property/data_storage/memo_ja.md)
    - [clipboard, readClipboard](vision/basic/function_property/data_storage/clipboard_ja.md)
    - [account](vision/basic/function_property/data_storage/account_ja.md)
    - [app](vision/basic/function_property/data_storage/app_ja.md)
    - [data](vision/basic/function_property/data_storage/data_ja.md)
- ディスクリプター
    - [describe, procedure, caption, comment, manual, knownIssue, target, output](vision/basic/function_property/descriptor/descriptors_ja.md)
    - [manual](vision/basic/function_property/descriptor/manual_ja.md)
    - [knownIssue](vision/basic/function_property/descriptor/known_issue_ja.md)
- 一時的なセレクターをオンデマンドで登録する
    - [tempSelector, tempValue](vision/basic/function_property/selector/temp_selector_ja.md)
- テストフロー制御
    - [SKIP/MANUAL/NOTIMPL](vision/basic/function_property/test_flow_control/skip_notimpl_ja.md)
- ロギング
    - [info, warn](vision/basic/function_property/logging/info_warn_ja.md)
    - [silent, procedure](vision/basic/function_property/logging/silent_and_procedure_function_ja.md)
- 外部コマンドを実行する
    - [shell](vision/basic/function_property/external_command/shell_ja.md)

[//]: # (    - [Scale and threshold]&#40;basic/function_property/find_image/scale_and_threshold.md&#41;)

### ヘルパー

- [言語ヘルパー](common/helper/language_helper/language_helper_ja.md)
- [Android用言語ヘルパー](common/helper/language_helper/language_helper_android_ja.md)

### レポート

- [HTMLレポート](common/report/test_result_files_ja.md)
- [Test Report Index](common/report/test_report_index_ja.md)
- [Spec-Report](common/report/spec_report_ja.md)
- [無負荷実行モード](common/report/no_load_run_mode_ja.md)
- [TestList](common/report/testlist_ja.md)

### バッチ処理を作成する

- [Summary-Report](common/creating_batch_tools/summary_report_execute_ja.md)
- [Code Generator](common/creating_batch_tools/code_generator_execute_ja.md)

## チュートリアル(In action)

- デバッグ
    - [recognizeText.jsonを確認する](vision/in_action/debugging/watching_recognize_text_json_ja.md)
    - [デバッガで画像をウォッチする](vision/in_action/debugging/watching_image_in_debugger_ja.md)
- classicモードとvisionモードの相互運用
    - [classic APIをVisionTestで使用する (Vision) (classicScope, visionScrope)](vision/in_action/hybrid_scripting/using_classic_apis_in_vision_test_ja.md)
- テストフィクスチャ
    - [テストフィクスチャ](common/test_fixture/test_fixture_ja.md)
- AppiumのAPIを使用する
    - [Appium ClientのAPIを使用する](common/using_appium_apis/using_appium_client_bare_apis_ja.md)
- 手動テストから移行する
    - [テストコードのテンプレートを生成する](common/migrating_from_manual_testing/generating_test_code_template_ja.md)
    - [Template Code Generatorを使用する](common/migrating_from_manual_testing/using_template_code_generator_ja.md)
- コードファーストでテストを設計する
    - [コードファーストでテストを設計する](common/designing_and_implementing_test/designing_test_in_code_first_ja.md)
- CIサーバーでテストを実行する
    - [テストをGradleで実行する](common/running_test_on_ci_server/running_with_gradle_ja.md)
- リモートのAppium Serverを利用する
    - [リモートのAppium Serverの構成](common/using_remote_appium_server/configuring_remote_appium_server_ja.md)
- テストの実行を安定化させる(フレーキーテストの改善)
    - [フレーキーテストの原因](in_action/stabilizing_test_execution/causes_of_flaky_test_ja.md)
    - [scenarioの再実行](in_action/stabilizing_test_execution/rerun_scenario_ja.md)

## チュートリアル(Advanced)

- [独自のデータ関数を作成する](common/advanced/creating_you_own_data_function_ja.md)
- [メッセージをカスタマイズする](common/advanced/customizing_message_ja.md)
- [スタブ(shirates-stub)を使用する](common/advanced/using_shirates_stub_ja.md)
- [ローカルへパブリッシュする](common/advanced/local_publishing_ja.md)
- [HTMLレポートのスタイルをカスタマイズする](common/advanced/customizing_html_report_style_ja.md)
- [独自のオペレーション関数を作成する](common/advanced/creating_you_own_operation_function_ja.md)
- [コード生成をカスタマイズする](common/advanced/customizing_code_generation_ja.md)

## ツールの設定

- IntelliJ IDEA
    - [JVMのバージョン, JDKのバージョン](common/tool_settings/jvm_version_ja.md)

## 仮想マシンの設定

- [Google Play Storeを有効にする](common/virtual_machine_settings/enabling_google_play_store_ja.md)

## パッケージのインストール/アップデート

- [nodeとnpmのインストール/アップデート](common/updating_packages/installing_updating_node_npm_ja.md)
- [Appiumとドライバーのインストール/アップデート](common/updating_packages/installing_updating_appium_drivers_ja.md)

## トラブルシューティング

- [エラーメッセージ/警告メッセージ](common/troubleshooting/error_warning_messages.md)
- [トラブルシューティング](common/troubleshooting/troubleshooting.md)

## Appendix

- [アノテーション](common/appendix/annotations_ja.md)

<br>
<br>