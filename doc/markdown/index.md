# Shirates/Vision

**Shirates/Vision** is new version of Shirates.<br>
[**Shirates/Classic**](classic/index.md) is old version.

## Repository

- [shirates-core](https://github.com/ldi-github/shirates-core)

## Overview

- [What is Shirates/Vision?](vision/about.md)
- [Environments](environments.md)
- [Quick start (Shirates/Vision)](quick-start.md)

## Tutorial(Basic)

### Samples

- [Gettings samples](vision/getting_samples.md)

### Recommended settings

- [Code format (IntelliJ IDEA)](common/tool_settings/code_format.md)
- [Enable right-click test running (IntelliJ IDEA)](common/tool_settings/right_click_test_running.md)
- [Encoding setting for properties file (IntelliJ IDEA)](common/tool_settings/properties_file_encoding.md)
- [Android Device settings](common/recommended_settings/recommended_settings_android.md)

### Creating project (Shirates/Vision)

- [Creating project](vision/basic/creating_project/creating_project.md)
- [Test Configuration template](vision/basic/creating_project/test_configuration_template.md)

### Creating TestClass (Shirates/Vision)

- [Creating TestClass](vision/basic/creating_testclass/creating_testclass.md)
- [Detect and assert](vision/basic/creating_testclass/detect_and_assert.md)
- [Test code structure](vision/basic/creating_testclass/testcode_structure.md)
- [Test result files](vision/basic/creating_testclass/test_result_files.md)

### Text and image recognition by Computer Vision

- [Text recognition by AI-OCR](vision/basic/text_and_image_recognition/text_recognition_by_ai_ocr.md)
- [Image classification by ML](vision/basic/text_and_image_recognition/image_recognition_by_ml.md)

### Macro

- [Macro](vision/basic/routine_work/macro.md)

### Selector and Nickname

- [Selector expression](vision/basic/selector_and_nickname/selector_expression.md)
- [Nickname](vision/basic/selector_and_nickname/nickname/nickname.md)
    - [Dataset nickname](classic/basic/selector_and_nickname/nickname/dataset_nickname.md)

### Parameter

- [Parameter configuration files](common/parameter/parameter_configuration_files.md)
- [Parameters](common/parameter/parameters.md)
- [Profile configuration](common/parameter/profile_configuration.md)
- [Automatic device detection](common/parameter/automatic_device_detection.md)

### Function/Property

- Detect text
    - [detect](vision/basic/function_property/detect_text/detect.md)
    - [canDetect](vision/basic/function_property/detect_text/can_detect.md)
- Tap element
    - [tap](vision/basic/function_property/tap_element/tap.md)
    - [tapAppIcon](vision/basic/function_property/tap_element/tap_app_icon.md)
- Install and launch app
    - [installApp, removeApp](vision/basic/function_property/install_and_launch_app/install_app.md)
    - [launchApp, terminateApp](vision/basic/function_property/install_and_launch_app/launch_app.md)
- Swipe/Scroll screen
    - [swipe, flick](vision/basic/function_property/swipe_screen/swipe_flick.md)
    - [scroll](vision/basic/function_property/swipe_screen/scroll.md)
    - [Determining scrollable area](vision/basic/function_property/swipe_screen/determining_scrollable_area.md)
- Asserting attribute
    - [text assertion](vision/basic/function_property/asserting_attribute/text_assertion.md)
- Asserting any value
    - [Any value assertion](vision/basic/function_property/asserting_any_value/any_value_assertion.md)
    - [String value assertion](vision/basic/function_property/asserting_any_value/string_assertion.md)
- Asserting existence
    - [Existence assertion](vision/basic/function_property/asserting_existence/existance_assertion.md)
    - [Existence assertion in cell](vision/basic/function_property/asserting_existence/in_cell_existance_assertion.md)
    - [Screen assertion](vision/basic/function_property/asserting_existence/screen_is.md)
- Find image
    - [find image](vision/basic/function_property/find_image/find_image.md)
- Asserting image
    - [Image assertion](vision/basic/function_property/asserting_image/image_assertion.md)
    - [Existence assertion of image (existImage, dontExistImage)](vision/basic/function_property/asserting_image/image_assertion.md)
- Asserting others
    - [App assertion](vision/basic/function_property/asserting_others/app_assertion.md)
    - [Keyboard assertion](vision/basic/function_property/asserting_others/keyboard_assertion.md)
    - [Package assertion](vision/basic/function_property/asserting_others/package_assertion.md)
- Asserting anyting
    - [Anyting assertion (verify)](vision/basic/function_property/asserting_anything/anything_assertion.md)
- Branch
    - [Branch function (ifTrue, ifFalse)](vision/basic/function_property/branch/if_true_if_false.md)
    - [Branch function (ifScreenIs, ifScreenIsNot)](vision/basic/function_property/branch/ifscreenis.md)
    - [Branch function (ifCanSelect, ifCanSelectNot)](vision/basic/function_property/branch/ifcanselect.md)
    - [Branch function (ifImageExist, ifImageExistNot)](vision/basic/function_property/branch/ifimageexist.md)
    - [Branch function (ifImageIs, ifImageIsNot)](vision/basic/function_property/branch/ifimageis.md)
    - [Branch function (ifStringIs, ifStartsWith, etc)](vision/basic/function_property/branch/ifString.md)
    - [Platform function (android, ios, emulator, simulator, virtualDevice, realDevice)](vision/basic/function_property/branch/platform_branch_functions.md)
    - [Platform property (platformName, platformVersion, isAndroid, isiOS, isVirtualDevice, isRealDevice)](vision/basic/function_property/branch/platform_properties.md)
    - [Osaifukeitai function (osaifuKeitai, osaifuKeitaiNot)](vision/basic/function_property/branch/osaifu_keitai_branch_functions.md)
    - [Special branch function (specialTag)](vision/basic/function_property/branch/special_branch_functions.md)
- Editing and Keyboard operations
    - [sendKeys](vision/basic/function_property/editing_and_keyboard_operations/sendkeys.md)
    - [clearInput](vision/basic/function_property/editing_and_keyboard_operations/clearinput.md)
    - [press keys](vision/basic/function_property/editing_and_keyboard_operations/press_keys.md)
    - [hideKeyboard](vision/basic/function_property/editing_and_keyboard_operations/hide_keyboard.md)
- Swich Apps
    - [goPreviousApp](vision/basic/function_property/navigation/go_previous_task.md)
- Syncing
    - [wait](vision/basic/syncing/wait.md)
    - [waitScreen, waitScreenOf](vision/basic/syncing/wait_screen.md)
    - [waitForClose](vision/basic/syncing/wait_for_close.md)
    - [waitForDisplay](vision/basic/syncing/wait_for_display.md)
- Repeating action
    - [doUntilTrue](vision/basic/repeating_action/do_until_true.md)
- Data storage
    - [writeMemo, readMemo, memoTextAs](vision/basic/function_property/data_storage/memo.md)
    - [clipboard, readClipboard](vision/basic/function_property/data_storage/clipboard.md)
    - [account](vision/basic/function_property/data_storage/account.md)
    - [app](vision/basic/function_property/data_storage/app.md)
    - [data](vision/basic/function_property/data_storage/data.md)
- Descriptor
    - [describe, procedure, caption, comment, manual, knownIssue, target, output](vision/basic/function_property/descriptor/descriptors.md)
    - [manual](vision/basic/function_property/descriptor/manual.md)
    - [knownIssue](vision/basic/function_property/descriptor/known_issue.md)
- Test flow control
    - [SKIP/MANUAL/NOTIMPL](vision/basic/function_property/test_flow_control/skip_notimpl.md)
- Registering selector on demand
    - [tempSelector](vision/basic/function_property/selector/temp_selector.md)
- Logging
    - [info, warn](vision/basic/function_property/logging/info_warn.md)
    - [silent, procedure](vision/basic/function_property/logging/silent_and_procedure_function.md)
- External command
    - [shell](vision/basic/function_property/external_command/shell.md)

[//]: # (    - [Scale and threshold]&#40;basic/function_property/find_image/scale_and_threshold.md&#41;)

### Behavior Helper

- Language
    - [LanguageHelperAndroid](vision/basic/behavior/language_helper/language_helper_android.md)
    - [LanguageHelperIos](vision/basic/behavior/language_helper/language_helper_ios.md)

### Report

- [HTML-Report](common/report/test_result_files.md)
- [Test Report Index](common/report/test_report_index.md)
- [Spec-Report](common/report/spec_report.md)
- [No-Load-Run mode](common/report/no_load_run_mode.md)
- [TestList](common/report/testlist.md)

### Creating batch tools

- [Summary-Report](vision/basic/creating_batch_tools/summary_report_execute.md)
- [Code Generator](vision/basic/creating_batch_tools/code_generator_execute.md)

## Tutorial(In action)

- Creating screen nickname file(classic)
    - [Introducing Appium Inspector](classic/in_action/creating_screen_nickname_file/using_appium_inspector.md)
    - [Creating screen nickname file](classic/in_action/creating_screen_nickname_file/creating_screen_nickname_file.md)
    - [Using Screen Builder](classic/in_action/creating_screen_nickname_file/using_screen_builder.md)
- Message and language
    - [Log language](common/message_and_language/log_language.md)
    - [Device language](common/message_and_language/device_language.md)
- Log and screenshot
    - [Configuring log](vision/in_action/log_and_screenshot/configuring_log.md)
    - [Configuring screenshot](vision/in_action/log_and_screenshot/configuring_screenshot.md)
- Debugging
    - [Watching sourceXML in file](vision/in_action/debugging/watching_source_xml_in_file.md)
    - [Watching sourceXML in debugger](vision/in_action/debugging/watching_source_xml_in_debugger.md)
    - [Tracing internal execution steps](vision/in_action/debugging/tracing_internal_execution_steps.md)
- Adapting to various environments
    - [Configuring tapAppIcon function](vision/in_action/adapting_to_environments/configuring_tap_appIcon_function.md)
    - [Adapting to changes in OS message](vision/in_action/adapting_to_environments/adapting_to_changes_in_os_messages.md)
- Handling irregulars
    - [Irregular Handler(Global Handler)](vision/in_action/handling_irregulars/irregular_handler.md)
    - [On Error Handler](vision/in_action/handling_irregulars/on_error_handler.md)
    - [Screen Handler(onScreen function)](vision/in_action/handling_irregulars/screen_handler.md)
- Test Fixture
    - [Test fixture](common/test_fixture/test_fixture.md)
- Image matching
    - [Cropping images for template matching](vision/in_action/image_matching/cropping_images_for_template_matching.md)
- Optimizing performance and resources
    - [end of scroll](vision/in_action/performance_resource/end_of_scroll.md)
    - [screenshot size](vision/in_action/performance_resource/screenshot_scale.md)
- Using Appium APIs
    - [Using Appium Client bare APIs](vision/in_action/using_appium_apis/using_appium_client_bare_apis.md)
- Filtering tests with priority
    - [must, should, want](vision/in_action/filtering_tests_with_priority/must_should_want.md)
- Migrating from manual testing
    - [Generating test code template](vision/in_action/migrating_from_manual_testing/generating_test_code_template.md)
    - [Using Template Code Generator](vision/in_action/migrating_from_manual_testing/using_template_code_generator.md)
- Designing test in code first
    - [Designing test in code first](vision/in_action/designing_and_implementing_test/designing_test_in_code_first.md)
- Running test on CI server
    - [Running with Gradle](vision/in_action/running_test_on_ci_server/running_with_gradle.md)
- Using remote Appium Server
    - [Configuring remote Appium Server](vision/in_action/using_remote_appium_server/configuring_remote_appium_server.md)
- Stabilizing test execution (Improve Flaky Test)
    - [Causes of flaky test](vision/in_action/stabilizing_test_execution/causes_of_flaky_test.md)
    - [Rerun scenario](vision/in_action/stabilizing_test_execution/rerun_scenario.md)
- Performance tuning with cache control
    - [Performance problem of getSource in iOS](vision/in_action/performance_tuning_with_cache_control/performance_problem_of_getpagesource_in_ios.md)
    - [Direct access mode](vision/in_action/performance_tuning_with_cache_control/direct_access_mode.md)

## Tutorial(Advanced)

- [Creating your own data function](common/advanced/creating_you_own_data_function.md)
- [Customizing Messages](common/advanced/customizing_message.md)
- [Using shirates-stub](common/advanced/using_shirates_stub.md)
- [Local publishing](common/advanced/local_publishing.md)
- [Customizing HTML-Report style](common/advanced/customizing_html_report_style.md)
- [Creating your own operation function](common/advanced/creating_you_own_operation_function.md)
- [Customizing code generation](common/advanced/customizing_code_generation.md)

## Tool settings

- IntelliJ IDEA
    - [JVM version, JDK version](common/tool_settings/jvm_version.md)

## Virtual machine settings

- [Enabling Google Play Store](vision/virtual_machine_settings/enabling_google_play_store.md)

## Installing/Updating packages

- [Installing/Updating node & npm](vision/updating_packages/installing_updating_node_npm.md)
- [Installing/Updating Appium & drivers](vision/updating_packages/installing_updating_appium_drivers.md)

## Troubleshooting

- [Error messages / Warning messages](vision/troubleshooting/error_warning_messages.md)
- [Troubleshooting](vision/troubleshooting/troubleshooting.md)

## Appendix

- [Annotations](vision/appendix/annotations.md)

<br>
<br>
