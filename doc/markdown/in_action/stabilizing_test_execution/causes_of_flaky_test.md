# Causes of flaky test

Some time you can see that running the test that passed before fails this time, and after rerunning the test the problem
is resolved. This means the test is not reliable.

There are many causes of flaky test.

## Order dependent tests

Order dependent tests may be efficient in time on running in serial but may fail on running in parallel.

Write order-independent test as possible as you can if you want to run tests in parallel, or to rerun only failed test.

## Stale element error

Sync screen elements between device and local cache. **shirates-core**'s built in syncing mechanism supports syncing as
possible as it can.

## Session corruption

Appium server session may be corrupted on launching app/terminating app/restarting device and so on. Session corruption
causes many kinds of error.

Consider reconnecting to appium server.

## Poor quality of Virtual device

Virtual devices(Android emulator/iOS simulator) is often not stable.

Consider rebooting the device on trouble.

## High CPU/IO load

High CPU/IO load leads processes to time out error. Parallel-testing using many virtual devices may cause this problem.

Consider avoiding high loads. Booting virtual device consume resources intensively, so starting up virtual devices in
serial with interval is a good practice.

### Link

- [Rerun scenario](rerun_scenario.md)
- [index](../../index.md)
