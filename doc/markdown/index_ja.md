# Shirates (shirates-core) 日本語ドキュメント

[in English](index.md)

## リポジトリ

- [shirates-core](https://github.com/ldi-github/shirates-core)

## 概要

- [テスト済みの環境](environments.md)
- [クイックスタート](quick-start_ja.md)

## チュートリアル(Basic)

### サンプル

- [チュートリアルのサンプル](basic/tutorial_samples_ja.md)

### 推奨設定

- [Actions on Save (IntelliJ IDEA)](tool_settings/actions_on_save_ja.md)
- [右クリックによるテスト実行を有効にする (IntelliJ IDEA)](tool_settings/right_click_test_running_ja.md)
- [propertiesファイルのエンコーディング設定](tool_settings/properties_file_encoding_ja.md)
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

- [マクロ](basic/routine_work/macro_ja.md)

### セレクターとニックネーム

- [セレクター式](basic/selector_and_nickname/selector_expression_ja.md)
- [ニックネーム](basic/selector_and_nickname/nickname/nickname_ja.md)
    - [セレクターネックネーム](basic/selector_and_nickname/nickname/selector_nickname_ja.md)
    - [画面ニックネーム](basic/selector_and_nickname/nickname/screen_nickname_ja.md)
    - [データセットニックネーム](basic/selector_and_nickname/nickname/dataset_nickname_ja.md)
- [ウィジェット](basic/selector_and_nickname/widget/widget_ja.md)
- [相対セレクター](basic/selector_and_nickname/relative_selector/relative_selector_ja.md)
    - [相対セレクター(方向ベース)](basic/selector_and_nickname/relative_selector/relative_selector_direction_ja.md)
    - [相対セレクター(ウィジェットフローベース)](basic/selector_and_nickname/relative_selector/relative_selector_flow_ja.md)
    - [相対セレクター(XMLベース)](basic/selector_and_nickname/relative_selector/relative_selector_xml_ja.md)
- [スペシャルフィルター](basic/selector_and_nickname/special_filter/special_filter_ja.md)
    - [クラスエイリアス](basic/selector_and_nickname/special_filter/class_alias_ja.md)
- [スペシャルセレクター](basic/selector_and_nickname/special_selector/special_selector_ja.md)
    - [タイトルセレクター](basic/selector_and_nickname/special_selector/title_selector_ja.md)
    - [Webタイトルセレクター](basic/selector_and_nickname/special_selector/webtitle_selector_ja.md)

### パラメーター

- [パラメーター構成ファイル](basic/parameter/parameter_configuration_files_ja.md)
- [パラメーター](basic/parameter/parameters_ja.md)
- [プロファイルの構成](basic/parameter/profile_configuration_ja.md)
- [自動デバイス検出](basic/parameter/automatic_device_detection_ja.md)

### 関数/プロパティ

- 要素を選択する
    - [select](basic/function_property/select_element/select_ja.md)
    - [canSelect](basic/function_property/select_element/can_select_ja.md)
- 要素をタップする
    - [tap](basic/function_property/tap_element/tap_ja.md)
    - [tapAppIcon](basic/function_property/tap_element/tap_app_icon_ja.md)
- インストールとアプリの起動
    - [installApp, removeApp](basic/function_property/install_and_launch_app/install_app_ja.md)
    - [launchApp, terminateApp](basic/function_property/install_and_launch_app/launch_app_ja.md)
- 画面をスワイプ/スクロールする
    - [swipe, flick](basic/function_property/swipe_screen/swipe_flick_ja.md)
    - [scroll](basic/function_property/swipe_screen/scroll_ja.md)
- 属性を検証する
    - [text属性の検証](basic/function_property/asserting_attribute/text_assertion_ja.md)
    - [id属性の検証](basic/function_property/asserting_attribute/id_assertion_ja.md)
    - [access属性の検証](basic/function_property/asserting_attribute/access_assertion_ja.md)
    - [value属性の検証](basic/function_property/asserting_attribute/value_assertion_ja.md)
    - [クラス名の検証](basic/function_property/asserting_attribute/classname_assertion_ja.md)
    - [任意の属性の検証](basic/function_property/asserting_attribute/attribute_assertion_ja.md)
- 任意の値を検証する
    - [任意の値の検証](basic/function_property/asserting_any_value/any_value_assertion_ja.md)
    - [文字列の検証](basic/function_property/asserting_any_value/string_assertion_ja.md)
- 存在することを検証する
    - [要素が存在することの検証](basic/function_property/asserting_existence/exist_dontExist_ja.md)
    - [画面が表示されていることの検証](basic/function_property/asserting_existence/screen_is_ja.md)
- 画像を検証する
    - [画像の検証](basic/function_property/asserting_image/image_assertion_ja.md)
- その他を検証する
    - [アプリが表示されていることの検証](basic/function_property/asserting_others/app_assertion_ja.md)
    - [キーボードが表示されていることの検証](basic/function_property/asserting_others/keyboard_assertion_ja.md)
    - [表示中のアプリのパッケージの検証](basic/function_property/asserting_others/package_assertion_ja.md)
- 分岐する
    - [分岐関数 (ifTrue, ifFalse)](basic/function_property/branch/if_true_if_false_ja.md)
    - [分岐関数 (ifScreenIs, ifScreenIsNot)](basic/function_property/branch/ifscreenis_ja.md)
    - [分岐関数 (ifCanSelect, ifCanSelectNot)](basic/function_property/branch/ifcanselect_ja.md)
    - [分岐関数 (ifImageExist, ifImageExistNot)](basic/function_property/branch/ifimageexist_ja.md)
    - [分岐関数 (ifImageIs, ifImageIsNot)](basic/function_property/branch/ifimageis_ja.md)
    - [プラットフォーム関数 (android, ios, emulator, simulator, virtualDevice, realDevice)](basic/function_property/branch/platform_branch_functions_ja.md)
    - [プラットフォームプロパティ (platformName, platformVersion, isAndroid, isiOS, isVirtualDevice, isRealDevice)](basic/function_property/branch/platform_properties_ja.md)
    - [おサイフケータイ関数 (osaifuKeitai, osaifuKeitaiNot)](basic/function_property/branch/osaifu_keitai_branch_functions_ja.md)
    - [スペシャルタグ分岐関数 (specialTag)](basic/function_property/branch/special_branch_functions_ja.md)
    - [画面分岐関数 (onScreen)](basic/function_property/branch/screen_branch_functions_ja.md)
- 編集とキーボード操作を行う
    - [sendKeys](basic/function_property/editing_and_keyboard_operations/sendkeys_ja.md)
    - [clearInput](basic/function_property/editing_and_keyboard_operations/clearinput_ja.md)
    - [press keys](basic/function_property/editing_and_keyboard_operations/press_keys_ja.md)
    - [hideKeyboard](basic/function_property/editing_and_keyboard_operations/hide_keyboard_ja.md)
- 同期する
    - [wait](basic/syncing/wait_ja.md)
    - [waitScreen, waitScreenOf](basic/syncing/wait_screen_ja.md)
    - [waitForClose](basic/syncing/wait_for_close_ja.md)
    - [waitForDisplay](basic/syncing/wait_for_display_ja.md)
- アクションを繰り返す
    - [doUntilTrue](basic/repeating_action/do_until_true_ja.md)
- データストレージ
    - [writeMemo, readMemo, memoTextAs](basic/function_property/data_storage/memo_ja.md)
    - [clipboard, readClipboard](basic/function_property/data_storage/clipboard_ja.md)
    - [account](basic/function_property/data_storage/account_ja.md)
    - [app](basic/function_property/data_storage/app_ja.md)
    - [data](basic/function_property/data_storage/data_ja.md)
- ディスクリプター
    - [describe, procedure, caption, comment, manual, knownIssue, target, output](basic/function_property/descriptor/descriptors_ja.md)
    - [manual](basic/function_property/descriptor/manual_ja.md)
    - [knownIssue](basic/function_property/descriptor/known_issue_ja.md)
- テストフロー制御
    - [SKIP/NOTIMPL](basic/function_property/test_flow_control/skip_notimpl_ja.md)
- ロギング
    - [info, warn](basic/function_property/logging/info_warn_ja.md)
    - [silent, procedure](basic/function_property/logging/silent_and_procedure_function_ja.md)
- 画像を見つける
    - [find image](basic/function_property/find_image/find_image_ja.md)
- 外部コマンドを実行する
    - [shell](basic/function_property/external_command/shell_ja.md)

[//]: # (    - [Scale and threshold]&#40;basic/function_property/find_image/scale_and_threshold.md&#41;)

### ビヘイビアヘルパー

- 言語設定
    - [LanguageHelperAndroid](basic/behavior/language_helper/language_helper_android_ja.md)
    - [LanguageHelperIos](basic/behavior/language_helper/language_helper_ios_ja.md)

### レポート

- [HTMLレポート](basic/creating_testclass/test_result_files_ja.md)
- [Test Report Index](basic/report/test_report_index_ja.md)
- [Spec-Report](basic/report/spec_report_ja.md)
- [無負荷実行モード](basic/report/no_load_run_mode_ja.md)
- [TestList](basic/report/testlist_ja.md)

### バッチ処理を作成する

- [Summary-Report](basic/creating_batch_tools/summary_report_execute_ja.md)
- [Code Generator](basic/creating_batch_tools/code_generator_execute_ja.md)

## チュートリアル(In action)

- 画面ニックネームを作成する
    - [Appium Inspectorを使用する](in_action/creating_screen_nickname_file/using_appium_inspector_ja.md)
    - [画面ニックネームファイルを作成する](in_action/creating_screen_nickname_file/creating_screen_nickname_file_ja.md)
- メッセージと言語
    - [ログ出力の言語](in_action/message_and_language/log_language_ja.md)
    - [デバイスの言語](in_action/message_and_language/device_language_ja.md)
- ログとスクリーンショット
    - [ログ出力を構成する](in_action/log_and_screenshot/configuring_log_ja.md)
    - [スクリーンショットを構成する](in_action/log_and_screenshot/configuring_screenshot_ja.md)
- デバッグ
    - [ソースXMLをファイルで確認する](in_action/debugging/watching_source_xml_in_file_ja.md)
    - [ソースXMLをデバッガーで確認する](in_action/debugging/watching_source_xml_in_debugger_ja.md)
- さまざまな環境に適応する
    - [tapAppIcon関数を構成する](in_action/adapting_to_environments/configuring_tap_appIcon_function_ja.md)
    - [OSのメッセージの変更に適応する](in_action/adapting_to_environments/adapting_to_changes_in_os_messages_ja.md)
- イレギュラーを処理する
    - [イレギュラーハンドラー](in_action/handling_irregulars/irregular_handler_ja.md)
- テストフィクスチャ
    - [テストフィクスチャ](in_action/test_fixture/test_fixture_ja.md)
- 画像マッチング
    - [テンプレートマッチングのための画像を切り出す](in_action/image_matching/cropping_images_for_template_matching_ja.md)
- パフォーマンスとリソースの最適化
    - [スクロール終端位置の検出の最適化](in_action/performance_resource/end_of_scroll_ja.md)
    - [スクリーンショットの縮小率](in_action/performance_resource/screenshot_scale_ja.md)
- AppiumのAPIを使用する
    - [Appium ClientのAPIを使用する](in_action/using_appium_apis/using_appium_client_bare_apis_ja.md)
- テストを優先度でフィルターする
    - [must, should, want](in_action/filtering_tests_with_priority/must_should_want_ja.md)
- 手動テストから移行する
    - [テストコードのテンプレートを生成する](in_action/migrating_from_manual_testing/generating_test_code_template_ja.md)
- コードファーストでテストを設計する
    - [コードファーストでテストを設計する](in_action/designing_and_implementing_test/designing_test_in_code_first_ja.md)
- CIサーバーでテストを実行する
    - [テストをGradleで実行する](in_action/running_test_on_ci_server/running_with_gradle_ja.md)
- テストの実行を安定化させる(フレーキーテストの改善)
    - [フレーキーテストの原因](in_action/stabilizing_test_execution/causes_of_flaky_test_ja.md)
    - [scenarioの再実行](in_action/stabilizing_test_execution/rerun_scenario_ja.md)
- キャッシュの制御によるパフォーマンスチューニング
    - [iOSにおけるgetSource実行時のパフォーマンス問題](in_action/performance_tuning_with_cache_control/performance_problem_of_getpagesource_in_ios_ja.md)
    - [ダイレクトアクセスモード](in_action/performance_tuning_with_cache_control/direct_access_mode_ja.md)

## チュートリアル(Advanced)

- [独自のデータ関数を作成する](advanced/creating_you_own_data_function_ja.md)
- [メッセージをカスタマイズする](advanced/customizing_message_ja.md)
- [スタブ(shirates-stub)を使用する](advanced/using_shirates_stub_ja.md)
- [ローカルへパブリッシュする](advanced/local_publishing_ja.md)
- [HTMLレポートのスタイルをカスタマイズする](advanced/customizing_html_report_style_ja.md)
- [独自のオペレーション関数を作成する](advanced/creating_you_own_operation_function_ja.md)
- [コード生成をカスタマイズする](advanced/customizing_code_generation_ja.md)

## ツールの設定

- IntelliJ IDEA
    - [JVMのバージョン, JDKのバージョン](tool_settings/jvm_version_ja.md)

## 仮想マシンの設定

- [Google Play Storeを有効にする](virtual_machine_settings/enabling_google_play_store_ja.md)

## パッケージのインストール/アップデート

- [nodeとnpmのインストール/アップデート](updating_packages/installing_updating_node&npm_ja.md)
- [Appiumとドライバーのインストール/アップデート](updating_packages/installing_updating_appium&drivers_ja.md)

## トラブルシューティング

- [エラーメッセージ/警告メッセージ](troubleshooting/error_warning_messages.md)
- [トラブルシューティング](troubleshooting/troubleshooting.md)

## Appendix

- [アノテーション](appendix/annotations_ja.md)

<br>
<br>
