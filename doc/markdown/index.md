# Shirates (shirates-core)

## Repository

- [shirates-core](https://github.com/ldi-github/shirates-core)

## Overview

- [Environments](environments.md)
- [Quick start](quick-start.md)

## Tutorial(Basic)

### Samples

- [Tutorial samples](basic/tutorial_samples.md)

### Recommended settings

- [Plugin settings (IntelliJ IDEA)](tool_settings/plugin_settings.md)
- [Actions on Save (IntelliJ IDEA)](tool_settings/actions_on_save.md) **(New! 4.2.0)**
- [Enable right-click test running (IntelliJ IDEA)](tool_settings/right_click_test_running.md)
- [Android Device settings](basic/recommended_settings/recommended_settings_android.md)

### Creating project

- [Creating project](basic/creating_project/creating_project.md)
- [Test Configuration template](basic/creating_project/test_configuration_template.md)

### Creating TestClass

- [Creating TestClass](basic/creating_testclass/creating_testclass.md)
- [Select and assert](basic/creating_testclass/select_and_assert.md)
- [Test code structure](basic/creating_testclass/testcode_structure.md)
- [Test result files](basic/creating_testclass/test_result_files.md)

### Macro

- [Macro](basic/routine_work/macro.md)

### Selector and Nickname

- [Selector expression](basic/selector_and_nickname/selector_expression.md)
- [Nickname](basic/selector_and_nickname/nickname/nickname.md)
    - [Selector nickname](basic/selector_and_nickname/nickname/selector_nickname.md)
    - [Screen nickname](basic/selector_and_nickname/nickname/screen_nickname.md)
    - [Dataset nickname](basic/selector_and_nickname/nickname/dataset_nickname.md)
- [Widget](basic/selector_and_nickname/widget/widget.md)
- [Relative selector](basic/selector_and_nickname/relative_selector/relative_selector.md)
    - [Relative selector(Direction based)](basic/selector_and_nickname/relative_selector/relative_selector_direction.md)
    - [Relative selector(Widget flow based)](basic/selector_and_nickname/relative_selector/relative_selector_flow.md)
    - [Relative selector(XML based)](basic/selector_and_nickname/relative_selector/relative_selector_xml.md)
- [Special filter](basic/selector_and_nickname/special_filter/special_filter.md)
    - [Class alias](basic/selector_and_nickname/special_filter/class_alias.md)
- [Special selector](basic/selector_and_nickname/special_selector/special_selector.md)
    - [title selector](basic/selector_and_nickname/special_selector/title_selector.md)
    - [webTitle selector](basic/selector_and_nickname/special_selector/webtitle_selector.md)

### Parameter

- [Parameter configuration files](basic/parameter/parameter_configuration_files.md)
- [Parameters](basic/parameter/parameters.md)
- [Profile configuration](basic/parameter/profile_configuration.md)
- [Automatic device detection](basic/parameter/automatic_device_detection.md)

### Function/Property

- Select element
    - [select](basic/function_property/select_element/select.md)
    - [canSelect](basic/function_property/select_element/can_select.md)
- Tap element
    - [tap](basic/function_property/tap_element/tap.md)
    - [tapAppIcon](basic/function_property/tap_element/tap_app_icon.md)
- Install and launch app
    - [installApp, removeApp](basic/function_property/install_and_launch_app/install_app.md)
    - [launchApp, terminateApp](basic/function_property/install_and_launch_app/launch_app.md)
- Swipe screen
    - [swipe, flick](basic/function_property/swipe_screen/swipe_flick.md)
    - [scroll](basic/function_property/swipe_screen/scroll.md)
- Asserting attribute
    - [text assertion](basic/function_property/asserting_attribute/text_assertion.md)
    - [id assertion](basic/function_property/asserting_attribute/id_assertion.md)
    - [access assertion](basic/function_property/asserting_attribute/access_assertion.md)
    - [value assertion](basic/function_property/asserting_attribute/value_assertion.md)
    - [className assertion](basic/function_property/asserting_attribute/classname_assertion.md)
    - [attribute assertion](basic/function_property/asserting_attribute/attribute_assertion.md)
- Asserting any value
    - [Any value assertion](basic/function_property/asserting_any_value/any_value_assertion.md)
    - [String value assertion](basic/function_property/asserting_any_value/string_assertion.md)
- Asserting existence
    - [Existence assertion](basic/function_property/asserting_existence/exist_dontExist.md)
    - [Screen assertion](basic/function_property/asserting_existence/screen_is.md)
- Asserting image
    - [Image assertion](basic/function_property/asserting_image/image_assertion.md)
- Asserting others
    - [App assertion](basic/function_property/asserting_others/app_assertion.md)
    - [Keyboard assertion](basic/function_property/asserting_others/keyboard_assertion.md)
    - [Package assertion](basic/function_property/asserting_others/package_assertion.md)
- Branch
    - [Branch function (ifTrue, ifFalse)](basic/function_property/branch/if_true_if_false.md)
    - [Branch function (ifScreenIs, ifScreenIsNot)](basic/function_property/branch/ifscreenis.md)
    - [Branch function (ifCanSelect, ifCanSelectNot)](basic/function_property/branch/ifcanselect.md)
    - [Branch function (ifImageExist, ifImageExistNot)](basic/function_property/branch/ifimageexist.md)
    - [Branch function (ifImageIs, ifImageIsNot)](basic/function_property/branch/ifimageis.md)
    - [Platform function (android, ios, emulator, simulator, virtualDevice, realDevice)](basic/function_property/branch/platform_branch_functions.md)
    - [Platform property (platformName, platformVersion, isAndroid, isiOS, isVirtualDevice, isRealDevice)](basic/function_property/branch/platform_properties.md)
    - [Osaifukeitai function (osaifuKeitai, osaifuKeitaiNot)](basic/function_property/branch/osaifu_keitai_branch_functions.md)
    - [Special branch function (specialTag)](basic/function_property/branch/special_branch_functions.md)
    - [Screen branch function (onScreen)](basic/function_property/branch/screen_branch_functions.md)
- Editing and Keyboard operations
    - [sendKeys](basic/function_property/editing_and_keyboard_operations/sendkeys.md)
    - [clearInput](basic/function_property/editing_and_keyboard_operations/clearinput.md)
    - [press keys](basic/function_property/editing_and_keyboard_operations/press_keys.md)
    - [hideKeyboard](basic/function_property/editing_and_keyboard_operations/hide_keyboard.md)
- Syncing
    - [wait](basic/syncing/wait.md)
    - [waitScreen, waitScreenOf](basic/syncing/wait_screen.md)
    - [waitForClose](basic/syncing/wait_for_close.md)
    - [waitForDisplay](basic/syncing/wait_for_display.md)
- Repeating action
    - [doUntilTrue](basic/repeating_action/do_until_true.md)
- Data storage
    - [writeMemo, readMemo, memoTextAs](basic/function_property/data_storage/memo.md)
    - [clipboard, readClipboard](basic/function_property/data_storage/clipboard.md)
    - [account](basic/function_property/data_storage/account.md)
    - [app](basic/function_property/data_storage/app.md)
    - [data](basic/function_property/data_storage/data.md)
- Descriptor
    - [describe, procedure, caption, comment, manual, knownIssue, target, output](basic/function_property/descriptor/descriptors.md)
    - [manual](basic/function_property/descriptor/manual.md)
    - [knownIssue](basic/function_property/descriptor/known_issue.md)
- Test flow control
    - [SKIP/NOTIMPL](basic/function_property/test_flow_control/skip_notimpl.md)
- Logging
    - [info, warn](basic/function_property/logging/info_warn.md)
    - [silent, procedure](basic/function_property/logging/silent_and_procedure_function.md)
- Find image
    - [find image](basic/function_property/find_image/find_image.md)
- External command
    - [shell](basic/function_property/external_command/shell.md)

[//]: # (    - [Scale and threshold]&#40;basic/function_property/find_image/scale_and_threshold.md&#41;)

### Behavior Helper

- Language
    - [LanguageHelperAndroid](basic/behavior/language_helper/language_helper_android.md)
    - [LanguageHelperIos](basic/behavior/language_helper/language_helper_ios.md)

### Report

- [HTML-Report](basic/creating_testclass/test_result_files.md)
- [Test Report Index](basic/report/test_report_index.md)
- [Spec-Report](basic/report/spec_report.md)
- [No-Load-Run mode](basic/report/no_load_run_mode.md)
- [TestList](basic/report/testlist.md)

### Creating batch tools

- [SummaryReportExecute](basic/creating_batch_tools/summary_report_execute.md)
- [CodeGeneratorExecute](basic/creating_batch_tools/code_generator_execute.md)

## Tutorial(In action)

- Creating screen nickname file
    - [Introducing Appium Inspector](in_action/creating_screen_nickname_file/using_appium_inspector.md)
    - [Creating screen nickname file](in_action/creating_screen_nickname_file/creating_screen_nickname_file.md)
- Message and language
    - [Log language](in_action/message_and_language/log_language.md)
    - [Device language](in_action/message_and_language/device_language.md)
- Log and screenshot
    - [Configuring log](in_action/log_and_screenshot/configuring_log.md)
    - [Configuring screenshot](in_action/log_and_screenshot/configuring_screenshot.md)
- Debugging
    - [Watching sourceXML in file](in_action/debugging/watching_source_xml_in_file.md)
    - [Watching sourceXML in debugger](in_action/debugging/watching_source_xml_in_debugger.md)
- Adapting to various environments
    - [Configuring tapAppIcon function](in_action/adapting_to_environments/configuring_tap_appIcon_function.md)
    - [Adapting to changes in OS message](in_action/adapting_to_environments/adapting_to_changes_in_os_messages.md)
- Handling irregulars
    - [Irregular Handler](in_action/handling_irregulars/irregular_handler.md)
- Test Fixture
    - [Test fixture](in_action/test_fixture/test_fixture.md) **(New! 4.1.0)**
- Image matching
    - [Cropping images for template matching](in_action/image_matching/cropping_images_for_template_matching.md)
- Optimizing performance and resources
    - [end of scroll](in_action/performance_resource/end_of_scroll.md)
    - [screenshot size](in_action/performance_resource/screenshot_scale.md)
- Using Appium APIs
    - [Using Appium Client bare APIs](in_action/using_appium_apis/using_appium_client_bare_apis.md)
- Filtering tests with priority
    - [must, should, want](in_action/filtering_tests_with_priority/must_should_want.md)
- Migrating from manual testing
    - [Generating test code template](in_action/migrating_from_manual_testing/generating_test_code_template.md)
- Designing test in code first
    - [Designing test in code first](in_action/designing_and_implementing_test/designing_test_in_code_first.md)
- Running test on CI server
    - [Running with Gradle](in_action/running_test_on_ci_server/running_with_gradle.md)
- Stabilizing test execution (Improve Flaky Test)
    - [Causes of flaky test](in_action/stabilizing_test_execution/causes_of_flaky_test.md)
    - [Rerun scenario](in_action/stabilizing_test_execution/rerun_scenario.md)
- Performance tuning with cache control **(New! 4.0.0)**
    - [Performance problem of getSource in iOS](in_action/performance_tuning_with_cache_control/performance_problem_of_getpagesource_in_ios.md)
    - [Direct access mode](in_action/performance_tuning_with_cache_control/direct_access_mode.md)

## Tutorial(Advanced)

- [Creating your own data function](advanced/creating_you_own_data_function.md)
- [Customizing Messages](advanced/customizing_message.md)
- [Using shirates-stub](advanced/using_shirates_stub.md)
- [Local publishing](advanced/local_publishing.md)
- [Customizing HTML-Report style](advanced/customizing_html_report_style.md)
- [Creating your own operation function](advanced/creating_you_own_operation_function.md)
- [Customizing code generation](advanced/customizing_code_generation.md)

## Tool settings

- IntelliJ IDEA
    - [JVM version, JDK version](tool_settings/jvm_version.md)

## Installing/Updating packages

- [Installing/Updating node & npm](updating_packages/installing_updating_node&npm.md) **(New! 4.2.0)**
- [Installing/Updating Appium & drivers](updating_packages/installing_updating_appium&drivers.md) **(New! 4.2.0)**

## Troubleshooting

- [Error messages / Warning messages](troubleshooting/error_warning_messages.md)
- [Troubleshooting](troubleshooting/troubleshooting.md)

## Appendix

- [Annotations](appendix/annotations.md)

<br>
<br>
