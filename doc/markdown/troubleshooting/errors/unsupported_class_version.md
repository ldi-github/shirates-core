# Unsupported class version

## Message

`Exception in thread "main" java.lang.UnsupportedClassVersionError: shirates/core/testcode/UITest has been compiled by a more recent version of the Java Runtime (class file version 61.0), this version of the Java Runtime only recognizes class file versions up to 59.0`

## Cause

The JVM version is too old to execute the library that built on newer Java version.

## Solution

Try update JVM to newer version.

See [JVM version](../../tool_settings/jvm_version.md)

### Link

- [Error messages / Warning messages](../error_warning_messages.md)

