package shirates.core.driver.commandextension

import io.appium.java_client.android.nativekey.AndroidKey
import shirates.core.driver.TestDriveObjectAndroid
import shirates.core.driver.TestMode
import shirates.core.driver.testDrive

/**
 * getAndroidKey
 */
internal fun TestDriveObjectAndroid.getAndroidKey(key: String): AndroidKey {

    when (key) {
        "0" -> return AndroidKey.DIGIT_0
        "1" -> return AndroidKey.DIGIT_1
        "2" -> return AndroidKey.DIGIT_2
        "3" -> return AndroidKey.DIGIT_3
        "4" -> return AndroidKey.DIGIT_4
        "5" -> return AndroidKey.DIGIT_5
        "6" -> return AndroidKey.DIGIT_6
        "7" -> return AndroidKey.DIGIT_7
        "8" -> return AndroidKey.DIGIT_8
        "9" -> return AndroidKey.DIGIT_9

        "*" -> return AndroidKey.STAR
        "#" -> return AndroidKey.POUND
        "" -> return AndroidKey.CLEAR

        "A" -> return AndroidKey.A
        "B" -> return AndroidKey.B
        "C" -> return AndroidKey.C
        "D" -> return AndroidKey.D
        "E" -> return AndroidKey.E
        "F" -> return AndroidKey.F
        "G" -> return AndroidKey.G
        "H" -> return AndroidKey.H
        "I" -> return AndroidKey.I
        "J" -> return AndroidKey.J
        "K" -> return AndroidKey.K
        "L" -> return AndroidKey.L
        "M" -> return AndroidKey.M
        "N" -> return AndroidKey.N
        "O" -> return AndroidKey.O
        "P" -> return AndroidKey.P
        "Q" -> return AndroidKey.Q
        "R" -> return AndroidKey.R
        "S" -> return AndroidKey.S
        "T" -> return AndroidKey.T
        "U" -> return AndroidKey.U
        "V" -> return AndroidKey.V
        "W" -> return AndroidKey.W
        "X" -> return AndroidKey.X
        "Y" -> return AndroidKey.Y
        "Z" -> return AndroidKey.Z

        "," -> return AndroidKey.COMMA
        "." -> return AndroidKey.PERIOD
        "\t" -> return AndroidKey.TAB
        " " -> return AndroidKey.SPACE
        "\n" -> return AndroidKey.ENTER
        "-" -> return AndroidKey.MINUS
        "=" -> return AndroidKey.EQUALS
        "[" -> return AndroidKey.LEFT_BRACKET
        "]" -> return AndroidKey.RIGHT_BRACKET
        "\\" -> return AndroidKey.BACKSLASH
        ";" -> return AndroidKey.SEMICOLON
        "'" -> return AndroidKey.APOSTROPHE
        "/" -> return AndroidKey.SLASH
        "@" -> return AndroidKey.AT
        "+" -> return AndroidKey.PLUS

        else -> throw NotImplementedError("key=$key")
    }
}

/**
 * pressKeys
 */
internal fun TestDriveObjectAndroid.pressKeys(keys: String) {

    if (TestMode.isAndroid.not()) {
        throw UnsupportedOperationException("pressKeys function is for Android.")
    }

    val list = keys.toList().map { "$it" }
    for (key in list) {
        val androidKey = getAndroidKey(key = key)
        testDrive.pressAndroid(key = androidKey)
    }
}