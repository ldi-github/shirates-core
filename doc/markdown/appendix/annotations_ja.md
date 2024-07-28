# アノテーション

## JUnit 5のアノテーション

ShiratesはJUnit 5のアノテーションを使用します。

| アノテーション      | 対象 | 説明                    | 
|:-------------|:---|:----------------------|
| @Test        | 関数 | テスト関数に付与する            |
| @Order       | 関数 | テストクラス内におけるテスト関数の実行順序 |
| @DisplayName | 関数 | JUnit runnerにおける表示    |

参照 [JUnit 5 User Guide / Annotations](https://junit.org/junit5/docs/current/user-guide/#writing-tests-annotations)

## Shiratesのアノテーション

Shiratesには以下のカスタムアノテーションがあります。

| アノテーション         | 対象      | 説明                                                                                                                |
|:----------------|:--------|:------------------------------------------------------------------------------------------------------------------|
| @Testrun        | クラス     | テストセッションを初期化するためのTestrunファイルを指定する([参照](../basic/creating_testclass/creating_testclass_ja.md))                     |
| @MacroObject    | クラス     | MacroObjectを宣言する ([参照](../basic/routine_work/macro_ja.md))                                                        |
| @Macro          | 関数      | Macro関数を宣言する ([参照](../basic/routine_work/macro_ja.md))                                                            |
| @CustomObject   | クラス     | CustomObjectを宣言する ([参照](../in_action/adapting_to_environments/configuring_tap_appIcon_function_ja.md))            |
| @CustomFunction | 関数      | Custom関数を宣言する ([参照](../in_action/adapting_to_environments/configuring_tap_appIcon_function_ja.md))                |
| @DisableCache   | クラス, 関数 | キャッシュを無効にする                                                                                                       |
| @NoLoadRun      | クラス, 関数 | (廃止予定。代わりに@Manualを使用してください。)                                                                                      |
| @Manual         | クラス, 関数 | テストを無負荷実行モード(NLRモード)で実行する ([参照](../in_action/designing_and_implementing_test/designing_test_in_code_first_ja.md)) |
| @Unstable       | クラス, 関数 | テストが不安定であることを宣言する                                                                                                 |
| @Deleted        | クラス, 関数 | テストが削除予定であることを宣言する                                                                                                |
| @Fail           | 関数      | テストがFailすることを宣言する                                                                                                 |
| @Must           | クラス, 関数 | 優先度アノテーション ([参照](../in_action/filtering_tests_with_priority/must_should_want_ja.md))                              |
| @Should         | クラス, 関数 | 優先度アノテーション ([参照](../in_action/filtering_tests_with_priority/must_should_want_ja.md))                              |
| @Want           | クラス, 関数 | 優先度アノテーション ([参照](../in_action/filtering_tests_with_priority/must_should_want_ja.md))                              |

### Link

- [index](../index_ja.md)
