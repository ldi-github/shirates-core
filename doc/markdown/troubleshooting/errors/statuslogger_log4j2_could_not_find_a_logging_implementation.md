# StatusLogger Log4j2 could not find a logging implementation

## Message

`ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...`

## Cause

Apache POI could not find a logging implementation.

## Solution

Add dependency to Log4j2, or just ignore this message.

```
// https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
implementation("org.apache.logging.log4j:log4j-core:2.18.0")
```

### Link

- [Error messages / Warning messages](../error_warning_messages.md)
