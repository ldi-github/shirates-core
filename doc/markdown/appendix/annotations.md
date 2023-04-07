# Annotations

## JUnit 5 annotations

Shirates uses some JUnit 5 annotations.

| annotation   | target   | description                      | 
|:-------------|:---------|:---------------------------------|
| @Test        | Function | Annotate test function with this |
| @Order       | Function | Execution order in test class    |
| @DisplayName | Function | Display name in JUnit runner     |

See [JUnit 5 User Guide / Annotations](https://junit.org/junit5/docs/current/user-guide/#writing-tests-annotations)

## Shirates annotations

Shirates has these custom annotations.

| annotation      | target          | description                                                                                                                 |
|:----------------|:----------------|:----------------------------------------------------------------------------------------------------------------------------|
| @Testrun        | Class           | Testrun file to initialize test session ([see](../basic/creating_testclass/creating_testclass.md))                          |
| @MacroObject    | Class           | Declares it to be a MacroObject ([see](../basic/routine_work/macro.md))                                                     |
| @Macro          | Function        | Declares it to be a Macro function ([see](../basic/routine_work/macro.md))                                                  |
| @CustomObject   | Class           | Declares it to be a CustomObject ([see](../in_action/adapting_to_environments/configuring_tap_appIcon_function.md))         |
| @CustomFunction | Function        | Declares it to be a Custom function ([see](../in_action/adapting_to_environments/configuring_tap_appIcon_function.md))      |
| @DisableCache   | Class, Function | Disable cache                                                                                                               |
| @NoLoadRun      | Class, Function | Run the test as No-Load-Run(NLR) mode ([see](../in_action/designing_and_implementing_test/designing_test_in_code_first.md)) |
| @Unstable       | Class, Function | Describes the test is unstable                                                                                              |
| @Fail           | Function        | Describes that the test fails                                                                                               |
| @Must           | Class, Function | Priority annotation ([see](../in_action/filtering_tests_with_priority/must_should_want.md))                                 |
| @Should         | Class, Function | Priority annotation ([see](../in_action/filtering_tests_with_priority/must_should_want.md))                                 |
| @Want           | Class, Function | Priority annotation ([see](../in_action/filtering_tests_with_priority/must_should_want.md))                                 |

### Link

- [index](../index.md)
