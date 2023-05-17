# Shirates (shirates-core) 日本語ドキュメント

## リポジトリ

- [shirates-core](https://github.com/ldi-github/shirates-core)

## 概要

- [テスト済みの環境](environments.md)
- [クイックスタート](quick-start_ja.md)

## チュートリアル(Basic)

### サンプル

- [チュートリアルのサンプル](basic/tutorial_samples.md)

### 推奨設定

- [Save Actions (IntelliJ IDEA)](tool_settings/plugin_settings_ja.md) **(非推奨)**
- [Actions on Save (IntelliJ IDEA)](tool_settings/actions_on_save_ja.md)
- [右クリックによろテスト実行を有効にする (IntelliJ IDEA)](tool_settings/right_click_test_running_ja.md)
- [Androidデバイスの設定](basic/recommended_settings/recommended_settings_android_ja.md)

### プロジェクトの作成

- [プロジェクトの作成](basic/creating_project/creating_project_ja.md)
- [テスト構成テンプレート](basic/creating_project/test_configuration_template_ja.md)

### テストクラスの作成

- [テストクラスの作成](basic/creating_testclass/creating_testclass_ja.md)
- [要素の選択と検証](basic/creating_testclass/select_and_assert_ja.md)
- [テストコードの構造](basic/creating_testclass/testcode_structure_ja.md)
- [テスト結果ファイル](basic/creating_testclass/test_result_files_ja.md)

### マクロ

- [マクロ](basic/routine_work/macro.md)

### セレクターとニックネーム

- [セレクター式](basic/selector_and_nickname/selector_expression.md)
- [ニックネーム](basic/selector_and_nickname/nickname/nickname.md)
    - [セレクターネックネーム](basic/selector_and_nickname/nickname/selector_nickname.md)
    - [画面ニックネーム](basic/selector_and_nickname/nickname/screen_nickname.md)
    - [データセットニックネーム](basic/selector_and_nickname/nickname/dataset_nickname.md)
- [ウィジェット](basic/selector_and_nickname/widget/widget.md)
- [相対セレクター](basic/selector_and_nickname/relative_selector/relative_selector.md)
    - [相対セレクター(方向ベース)](basic/selector_and_nickname/relative_selector/relative_selector_direction.md)
    - [相対セレクター(ウィジェットフローベース)](basic/selector_and_nickname/relative_selector/relative_selector_flow.md)
    - [相対セレクター(XMLベース)](basic/selector_and_nickname/relative_selector/relative_selector_xml.md)
- [スペシャルフィルター](basic/selector_and_nickname/special_filter/special_filter.md)
    - [クラスエイリアス](basic/selector_and_nickname/special_filter/class_alias.md)
- [スペシャルセレクター](basic/selector_and_nickname/special_selector/special_selector.md)
    - [タイトルセレクター](basic/selector_and_nickname/special_selector/title_selector.md)
    - [Webタイトルセレクター](basic/selector_and_nickname/special_selector/webtitle_selector.md)

### パラメーター

- [パラメーター構成ファイル](basic/parameter/parameter_configuration_files.md)
- [パラメーター](basic/parameter/parameters.md)
- [プロファイルの構成](basic/parameter/profile_configuration.md)
- [自動デバイス検出](basic/parameter/automatic_device_detection.md)

### 関数/プロパティ

- 要素を選択する
    - [select](basic/function_property/select_element/select.md)
    - [canSelect](basic/function_property/select_element/can_select.md)
- 要素をタップする
    - [tap](basic/function_property/tap_element/tap.md)
    - [tapAppIcon](basic/function_property/tap_element/tap_app_icon.md)
- インストールとアプリの起動
    - [installApp, removeApp](basic/function_property/install_and_launch_app/install_app.md)
    - [launchApp, terminateApp](basic/function_property/install_and_launch_app/launch_app.md)
- 画面をスワイプスる
    - [swipe, flick](basic/function_property/swipe_screen/swipe_flick.md)
    - [scroll](basic/function_property/swipe_screen/scroll.md)
- 属性を検証する
    - [text属性の検証](basic/function_property/asserting_attribute/text_assertion.md)
    - [id属性の検証](basic/function_property/asserting_attribute/id_assertion.md)
    - [access属性の検証](basic/function_property/asserting_attribute/access_assertion.md)
    - [value属性の検証](basic/function_property/asserting_attribute/value_assertion.md)
    - [クラス名の検証](basic/function_property/asserting_attribute/classname_assertion.md)
    - [任意の属性の検証](basic/function_property/asserting_attribute/attribute_assertion.md)
- 任意の値を検証する
    - [任意型の検証](basic/function_property/asserting_any_value/any_value_assertion.md)
    - [文字列の検証](basic/function_property/asserting_any_value/string_assertion.md)
- 存在することを検証する
    - [要素が表示されていることの検証](basic/function_property/asserting_existence/exist_dontExist.md)
    - [画面が表示されていることの検証](basic/function_property/asserting_existence/screen_is.md)
- 画像を検証する
    - [画像の検証](basic/function_property/asserting_image/image_assertion.md)
- その他を検証する
    - [アプリが表示されていることの検証](basic/function_property/asserting_others/app_assertion.md)
    - [キーボードが表示されていることの検証](basic/function_property/asserting_others/keyboard_assertion.md)
    - [表示中のアプリのパッケージの検証](basic/function_property/asserting_others/package_assertion.md)
- 分岐する
    - [分岐関数 (ifTrue, ifFalse)](basic/function_property/branch/if_true_if_false.md)
    - [分岐関数 (ifScreenIs, ifScreenIsNot)](basic/function_property/branch/ifscreenis.md)
    - [分岐関数 (ifCanSelect, ifCanSelectNot)](basic/function_property/branch/ifcanselect.md)
    - [分岐関数 (ifImageExist, ifImageExistNot)](basic/function_property/branch/ifimageexist.md)
    - [分岐関数 (ifImageIs, ifImageIsNot)](basic/function_property/branch/ifimageis.md)
    - [プラットフォーム関数 (android, ios, emulator, simulator, virtualDevice, realDevice)](basic/function_property/branch/platform_branch_functions.md)
    - [プラットフォームプロパティ (platformName, platformVersion, isAndroid, isiOS, isVirtualDevice, isRealDevice)](basic/function_property/branch/platform_properties.md)
    - [おサイフケータイ関数 (osaifuKeitai, osaifuKeitaiNot)](basic/function_property/branch/osaifu_keitai_branch_functions.md)
    - [スペシャルタグ分岐関数 (specialTag)](basic/function_property/branch/special_branch_functions.md)
    - [画面分岐関数 (onScreen)](basic/function_property/branch/screen_branch_functions.md)
- 編集とキーボード操作を行う
    - [sendKeys](basic/function_property/editing_and_keyboard_operations/sendkeys.md)
    - [clearInput](basic/function_property/editing_and_keyboard_operations/clearinput.md)
    - [press keys](basic/function_property/editing_and_keyboard_operations/press_keys.md)
    - [hideKeyboard](basic/function_property/editing_and_keyboard_operations/hide_keyboard.md)
- 同期する
    - [wait](basic/syncing/wait.md)
    - [waitScreen, waitScreenOf](basic/syncing/wait_screen.md)
    - [waitForClose](basic/syncing/wait_for_close.md)
    - [waitForDisplay](basic/syncing/wait_for_display.md)
- アクションを繰り返す
    - [doUntilTrue](basic/repeating_action/do_until_true.md)
- データストレージ
    - [writeMemo, readMemo, memoTextAs](basic/function_property/data_storage/memo.md)
    - [clipboard, readClipboard](basic/function_property/data_storage/clipboard.md)
    - [account](basic/function_property/data_storage/account.md)
    - [app](basic/function_property/data_storage/app.md)
    - [data](basic/function_property/data_storage/data.md)
- ディスクリプター
    - [describe, procedure, caption, comment, manual, knownIssue, target, output](basic/function_property/descriptor/descriptors.md)
    - [manual](basic/function_property/descriptor/manual.md)
    - [knownIssue](basic/function_property/descriptor/known_issue.md)
- テストフロー制御
    - [SKIP/NOTIMPL](basic/function_property/test_flow_control/skip_notimpl.md)
- ロギング
    - [info, warn](basic/function_property/logging/info_warn.md)
    - [silent, procedure](basic/function_property/logging/silent_and_procedure_function.md)
- 画像を見つける
    - [find image](basic/function_property/find_image/find_image.md)
- 外部コマンドを実行する
    - [shell](basic/function_property/external_command/shell.md)

[//]: # (    - [Scale and threshold]&#40;basic/function_property/find_image/scale_and_threshold.md&#41;)

### ビヘイビアヘルパー

- 言語設定
    - [LanguageHelperAndroid](basic/behavior/language_helper/language_helper_android.md)
    - [LanguageHelperIos](basic/behavior/language_helper/language_helper_ios.md)

### レポート

- [HTMLレポート](basic/creating_testclass/test_result_files.md)
- [Test Report Index](basic/report/test_report_index.md)
- [Spec-Report](basic/report/spec_report.md)
- [無負荷実行モード](basic/report/no_load_run_mode.md)
- [TestList](basic/report/testlist.md)

### バッチ処理を作成する

- [SummaryReportExecute](basic/creating_batch_tools/summary_report_execute.md)
- [CodeGeneratorExecute](basic/creating_batch_tools/code_generator_execute.md)

## チュートリアル(In action)

- 画面ニックネームを作成する
    - [Appium Inspectorを導入する](in_action/creating_screen_nickname_file/using_appium_inspector.md)
    - [画面ニックネームを作成する](in_action/creating_screen_nickname_file/creating_screen_nickname_file.md)
- メッセージと言語
    - [ログ言語](in_action/message_and_language/log_language.md)
    - [デバイス言語](in_action/message_and_language/device_language.md)
- ログとスクリーンショット
    - [ログを構成する](in_action/log_and_screenshot/configuring_log.md)
    - [スクリーンショットを構成する](in_action/log_and_screenshot/configuring_screenshot.md)
- デバッグ
    - [ソースXMLをファイルで確認する](in_action/debugging/watching_source_xml_in_file.md)
    - [ソースXMLをデバッガーで確認する](in_action/debugging/watching_source_xml_in_debugger.md)
- さまざまな環境に適応する
    - [tapAppIcon関数を構成する](in_action/adapting_to_environments/configuring_tap_appIcon_function.md)
    - [OSのメッセージの変化に適応する](in_action/adapting_to_environments/adapting_to_changes_in_os_messages.md)
- イレギュラーを処理する
    - [イレギュラーハンドラー](in_action/handling_irregulars/irregular_handler.md)
- Test Fixture
    - [Test fixture](in_action/test_fixture/test_fixture.md)
- 画像マッチング
    - [テンプレートマッチングのための画像を切り出す](in_action/image_matching/cropping_images_for_template_matching.md)
- パフォーマンスとリソースの最適化
    - [スクロールの終端の検出](in_action/performance_resource/end_of_scroll.md)
    - [スクリーンショットのサイズ](in_action/performance_resource/screenshot_scale.md)
- AppiumのAPIを使用する
    - [AppiumClientのAPIsを使用する](in_action/using_appium_apis/using_appium_client_bare_apis.md)
- テストを優先順位でフィルターする
    - [must, should, want](in_action/filtering_tests_with_priority/must_should_want.md)
- 手動テストから移行する
    - [テストコードのテンプレートを生成する](in_action/migrating_from_manual_testing/generating_test_code_template.md)
- コードファーストでテストを設計する
    - [コードファーストでテストを設計する](in_action/designing_and_implementing_test/designing_test_in_code_first.md)
- CIサーバーでテストを実行する
    - [Gradleで実行する](in_action/running_test_on_ci_server/running_with_gradle.md)
- テストの実行を安定化させる(フレーキーテストの改善)
    - [フレーキーテストの原因](in_action/stabilizing_test_execution/causes_of_flaky_test.md)
    - [scenarioの再実行](in_action/stabilizing_test_execution/rerun_scenario.md)
- キャッシュの制御によるパフォーマンスチューニング
    - [iOSにおけるgetSource実行時のパフォーマンス問題](in_action/performance_tuning_with_cache_control/performance_problem_of_getpagesource_in_ios.md)
    - [ダイレクトアクセスモード](in_action/performance_tuning_with_cache_control/direct_access_mode.md)

## チュートリアル(Advanced)

- [独自のデータ関数を作成する](advanced/creating_you_own_data_function.md)
- [メッセージをカスタマイズする](advanced/customizing_message.md)
- [スタブ(shirates-stub)を使用する](advanced/using_shirates_stub.md)
- [ローカルへパブリッシュする](advanced/local_publishing.md)
- [HTMLレポートのスタイルをカスタマイズする](advanced/customizing_html_report_style.md)
- [独自のオペレーション関数を作成する](advanced/creating_you_own_operation_function.md)
- [コード生成をカスタマイズする](advanced/customizing_code_generation.md)

## ツールの設定

- IntelliJ IDEA
    - [JVMのバージョン, JDKのバージョン](tool_settings/jvm_version.md)

## パッケージのインストール/アップデート

- [nodeとnpmのインストール/アップデート](updating_packages/installing_updating_node&npm.md)
- [Appiumとドライバーのインストール/アップデート](updating_packages/installing_updating_appium&drivers.md)

## トラブルシューティング

- [エラーメッセージ/警告メッセージ](troubleshooting/error_warning_messages.md)
- [トラブルシューティング](troubleshooting/troubleshooting.md)

## Appendix

- [アノテーション](appendix/annotations.md)

<br>
<br>
