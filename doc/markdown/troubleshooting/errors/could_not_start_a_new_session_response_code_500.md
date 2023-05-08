# Could not start a new session. Response code 500

### Message

`Could not start a new session. Response code 500. Message: An unknown server-side error occurred while processing the command. Original error: The instrumentation process cannot be initialized within 30000ms timeout. Make sure the application under test does not crash and investigate the logcat output. You could also try to increase the value of 'uiautomator2ServerLaunchTimeout' capability`

### Solution

Increase capability `uiautomator2ServerLaunchTimeout`
in [testConfig file](../../basic/parameter/parameter_configuration_files.md).

```
  "capabilities": {
    "uiautomator2ServerLaunchTimeout": "60000",
  },
```

### Link

- [Error messages / Warning messages](../error_warning_messages.md)

