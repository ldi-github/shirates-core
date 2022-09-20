# (iOS)No response to swipe gesture

## Symptom

You get no response on swiping on the screen. This occurs on M1 Mac.

## Solution

Try run **Simulator.app** on **Rosetta**.

1. Open `Applications`folder in **Finder**.
2. Right click Xcode and select `Show Package Contents`.
3. Open `Contents/Developer/Applications/`.
4. Right click **Simulator.app** and select `Get Info`.
5. Check `Open using Rosetta` on.

### Link

- [Troubleshooting](../troubleshooting.md)
