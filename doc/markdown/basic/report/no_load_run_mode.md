# No-Load-Run mode (NLR mode)

To generate [Spec-Report](../report/spec_report.md) for manual testing, use **"No-Load-Run mode"**.

In this mode, every command output logs without executing actual test. Every conditional branch (such as ifTrue,
ifFalse, android, ios, specialTag, etc) runs inner commands and outputs logs without interacting systems under test.

## Running tests in No-Load-Run mode

1. Set `noLoadRun` true in [testrun file](../parameter/parameter_configuration_files.md).

```
noLoadRun=true
```

2. Run test.
3. You can get Spec-Report in TestResults directory.

<br>

### Comparison

#### Normal mode

`noLoadRun=false`

![no-load-run](../_images/spec_report_calculator_normal.png)

#### No-Load-Run mode

`noLoadRun=true`

![no-load1](../_images/spec_report_calculator_no_load.png)

In NLR mode, both emulator block and non emulator block are output.

### Link

- [index](../../index.md)

