# Shirates/Vision [Beta]

[in Japanese(日本語)](index_ja.md)

### Shirates versions

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

- [Getting samples](vision/getting_samples.md)

### Recommended settings

- [Code format (IntelliJ IDEA)](common/tool_settings/code_format.md)
- [Enable right-click test running (IntelliJ IDEA)](common/tool_settings/right_click_test_running.md)
- [Encoding setting for properties file (IntelliJ IDEA)](common/tool_settings/properties_file_encoding.md)
- [Android Device settings](common/recommended_settings/recommended_settings_android.md)

### Switching environment

- [Switching Android/iOS](vision/basic/switching_environment/switching_android_ios.md)
- [Switching device](vision/basic/switching_environment/switching_device.md)
- [Switching device language](vision/basic/switching_environment/switching_device_language.md)
- [Switching log language](common/message_and_language/log_language.md)
- [Switching AI-OCR language](vision/basic/switching_environment/switching_ai_ocr_language.md)

### Creating project

- [Creating project](vision/basic/creating_project/creating_project.md)
- [Test Configuration template](vision/basic/creating_project/test_configuration_template.md)

### Creating TestClass

- [Creating TestClass](vision/basic/creating_testclass/creating_testclass.md)
- [Detect and assert](vision/basic/creating_testclass/detect_and_assert.md)
- [Test code structure](vision/basic/creating_testclass/testcode_structure.md)

[//]: # (- [Test result files]&#40;vision/basic/creating_testclass/test_result_files.md&#41;)

### Text and image recognition by Computer Vision

- [Text recognition by AI-OCR](vision/basic/text_and_image_recognition/text_recognition_by_ai_ocr.md)
- [Finding image by AI/ML](vision/basic/text_and_image_recognition/finding_image_by_ai_ml.md)
- [Learning images](vision/basic/text_and_image_recognition/learning_images.md)
- [Using CheckStateClassifier](vision/basic/text_and_image_recognition/using_check_state_classifier.md)
- [Using DefaultClassifier](vision/basic/text_and_image_recognition/using_default_classifier.md)
- [Screen identification using FeaturePrint and text recognition](vision/basic/text_and_image_recognition/screen_assertion_using_feature_print_and_text_recognition.md)
- [Setting up screen image_templates](vision/basic/text_and_image_recognition/setting_up_screen_image_templates.md)

### Macro

- [Macro](vision/basic/routine_work/macro.md)

### Selector and Nickname

- [Selector expression](vision/basic/selector_and_nickname/selector_expression.md)
- [Nickname](vision/basic/selector_and_nickname/nickname.md)
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
- Find image
    - [findImage](vision/basic/function_property/getting_image/find_image.md)
- Tap element
    - [tap](vision/basic/function_property/tap_element/tap.md)
    - [tapAppIcon](vision/basic/function_property/tap_element/tap_app_icon.md)
- Install and launch app
    - [installApp, removeApp](vision/basic/function_property/install_and_launch_app/install_app.md)
    - [launchApp, terminateApp](vision/basic/function_property/install_and_launch_app/launch_app.md)
- Swipe/Scroll screen
    - [swipe, flick](vision/basic/function_property/swipe_screen/swipe_flick.md)
    - [scroll](vision/basic/function_property/swipe_screen/scroll.md)
    - [Determining scrollable area(onLine, onLineOf, onColumn, onColumnOf)](vision/basic/function_property/swipe_screen/determining_scrollable_area.md)
- Getting image/text relatively
    - [Relative text (leftText, rightText, belowText, aboveText)](vision/basic/function_property/getting_image/relative_text.md)
    - [Relative image (leftItem, rightItem, belowItem, aboveItem)](vision/basic/function_property/getting_image/relative_image.md)
- Asserting text
    - [Text assertion](vision/basic/function_property/asserting_text/text_assertion.md)
- Asserting image
    - [Image assertion](vision/basic/function_property/asserting_image/image_assertion.md)
- Asserting any value
    - [Any value assertion](vision/basic/function_property/asserting_any_value/any_value_assertion.md)
    - [String value assertion](vision/basic/function_property/asserting_any_value/string_assertion.md)
    - [Boolean value assertion](vision/basic/function_property/asserting_any_value/boolean_assertion.md)
- Asserting existence
    - [Text existence assertion](vision/basic/function_property/asserting_existence/text_existance_assertion.md)
    - [Image existence assertion](vision/basic/function_property/asserting_existence/image_existance_assertion.md)
    - [Screen assertion](vision/basic/function_property/asserting_existence/screen_assertion.md)
- Asserting others
    - [App assertion](vision/basic/function_property/asserting_others/app_assertion.md)
    - [Keyboard assertion](vision/basic/function_property/asserting_others/keyboard_assertion.md)
    - [Package assertion](vision/basic/function_property/asserting_others/package_assertion.md)
- Asserting anyting
    - [Anyting assertion (verify)](vision/basic/function_property/asserting_anything/anything_assertion.md)
- Branch
    - [Branch function (ifTrue, ifFalse)](vision/basic/function_property/branch/if_true_if_false.md)
    - [Branch function (ifScreenIs, ifScreenIsNot)](vision/basic/function_property/branch/if_screen_is.md)
    - [Branch function (ifCanDetect, ifCanDetectNot)](vision/basic/function_property/branch/if_can_detect.md)
    - [Branch function (ifImageExist, ifImageExistNot)](vision/basic/function_property/branch/if_image_exist.md)
    - [Branch function (ifImageIs, ifImageIsNot)](vision/basic/function_property/branch/if_image_is.md)
    - [Branch function (ifStringIs, ifStartsWith, etc)](vision/basic/function_property/branch/if_string.md)
    - [Platform function (android, ios, emulator, simulator, virtualDevice, realDevice)](vision/basic/function_property/branch/platform_branch_functions.md)
    - [Platform property (platformName, platformVersion, isAndroid, isiOS, isVirtualDevice, isRealDevice)](vision/basic/function_property/branch/platform_properties.md)
- Editing and Keyboard operations
    - [sendKeys](vision/basic/function_property/editing_and_keyboard_operations/sendkeys.md)
    - [clearInput](vision/basic/function_property/editing_and_keyboard_operations/clearinput.md)
    - [press keys](vision/basic/function_property/editing_and_keyboard_operations/press_keys.md)
    - [hideKeyboard](vision/basic/function_property/editing_and_keyboard_operations/hide_keyboard.md)
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

### Helper

- [LanguageHelper](common/helper/language_helper/language_helper.md)
- [LanguageHelperAndroid](common/helper/language_helper/language_helper_android.md)

### Report

- [HTML-Report](common/report/test_result_files.md)
- [Test Report Index](common/report/test_report_index.md)
- [Spec-Report](common/report/spec_report.md)
- [No-Load-Run mode](common/report/no_load_run_mode.md)
- [TestList](common/report/testlist.md)

### Creating batch tools

- [Summary-Report](common/creating_batch_tools/summary_report_execute.md)
- [Code Generator](common/creating_batch_tools/code_generator_execute.md)

## Tutorial(In action)

- Debugging
    - [Watching recognizeText.json](vision/in_action/debugging/watching_recognize_text_json.md)
    - [Watching image in debugger](vision/in_action/debugging/watching_image_in_debugger.md)
- Interoperability between classic mode and vision mode
    - [Using classic APIs in VisionTest (classicScope, visionScrope)](vision/in_action/hybrid_scripting/using_classic_apis_in_vision_test.md)
- Test Fixture
    - [Test fixture](common/test_fixture/test_fixture.md)
- Using Appium APIs
    - [Using Appium Client bare APIs](common/using_appium_apis/using_appium_client_bare_apis.md)
- Migrating from manual testing
    - [Generating test code template](common/migrating_from_manual_testing/generating_test_code_template.md)
    - [Using Template Code Generator](common/migrating_from_manual_testing/using_template_code_generator.md)
- Designing test in code first
    - [Designing test in code first](common/designing_and_implementing_test/designing_test_in_code_first.md)
- Running test on CI server
    - [Running with Gradle](common/running_test_on_ci_server/running_with_gradle.md)
- Using remote Appium Server
    - [Configuring remote Appium Server](common/using_remote_appium_server/configuring_remote_appium_server.md)
- Stabilizing test execution (Improve Flaky Test)
    - [Causes of flaky test](common/stabilizing_test_execution/causes_of_flaky_test.md)
    - [Rerun scenario](common/stabilizing_test_execution/rerun_scenario.md)

## Tutorial(Advanced)

- [Creating your own data function](common/advanced/creating_you_own_data_function.md)
- [Customizing Messages](common/advanced/customizing_message.md)
- [Using shirates-stub](common/advanced/using_shirates_stub.md)
- [Customizing HTML-Report style](common/advanced/customizing_html_report_style.md)
- [Creating your own operation function](common/advanced/creating_you_own_operation_function.md)
- [Customizing code generation](common/advanced/customizing_code_generation.md)

## Tool settings

- IntelliJ IDEA
    - [JVM version, JDK version](common/tool_settings/jvm_version.md)

## Virtual machine settings

- [Enabling Google Play Store](common/virtual_machine_settings/enabling_google_play_store.md)

## Installing/Updating packages

- [Installing/Updating node & npm](common/updating_packages/installing_updating_node_npm.md)
- [Installing/Updating Appium & drivers](common/updating_packages/installing_updating_appium_drivers.md)

## Troubleshooting

- [Error messages / Warning messages](common/troubleshooting/error_warning_messages.md)
- [Troubleshooting](common/troubleshooting/troubleshooting.md)

## Appendix

- [Annotations](common/appendix/annotations.md)

<br>
<br>
