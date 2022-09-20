# WARNING: An illegal reflective access operation has occurred

## Message

```
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by boofcv.io.image.ConvertRaster (file:/Users/wave1008/.gradle/caches/modules-2/files-2.1/org.boofcv/boofcv-io/0.40.1/a99b6dbd3e07646ebaecc715aed48a0f03596db0/boofcv-io-0.40.1.jar) to method sun.awt.image.ByteInterleavedRaster.getDataOffset(int)
WARNING: Please consider reporting this to the maintainers of boofcv.io.image.ConvertRaster
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
```

## Cause

Dependent computer vision library **BoofCV** uses hidden API to improve performance.

See [An illegal reflective access operation has occurred #175](https://github.com/lessthanoptimal/BoofCV/issues/175)

## Solution

No solution for suppressing warning.

### Note

BoofCV may not available in the future release of JavaVM. In such cases, we could provide other alternatives.

## Why use BoofCV instead of OpenCV?

BoofCV is pure java library and easy to install. BoofCV is simple for our use of template matching.

OpenCV is highly functional but hard to install, and maintain versions.

### Link

- [Error messages / Warning messages](../error_warning_messages.md)

