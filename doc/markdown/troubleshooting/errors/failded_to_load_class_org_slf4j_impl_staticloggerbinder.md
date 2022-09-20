# SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder"

## Message

`SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".`

## Cause

Apache POI could not find a logging implementation.

## Solution

Add dependency to SLF4J, or just ignore this message.

You can choose SLF4J NOP Binding.

```
// https://mvnrepository.com/artifact/org.slf4j/slf4j-nop
testImplementation("org.slf4j:slf4j-nop:1.7.36")
```

### Link

- [Error messages / Warning messages](../error_warning_messages.md)

