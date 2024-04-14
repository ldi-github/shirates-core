package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.emulator
import shirates.core.driver.branchextension.realDevice
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class ExistTest : UITest() {

    @Test
    @Order(10)
    fun exist_existWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")

                }.action {

                }.expectation {
                    withScrollDown {
                        it
                            .exist("[Search Button]")
                            .exist("[Search settings]")
                            .exist("[Network & internet Icon]")
                            .exist("[Network & internet]")
                            .exist("{Network & internet}")
                            .exist("[Connected devices Icon]")
                            .exist("[Connected devices]")
                            .exist("{Connected devices}")
                            .exist("[Apps Icon]")
                            .exist("[Apps]")
                            .exist("{Apps}")
                            .exist("[Notifications Icon]")
                            .exist("[Notifications]")
                            .exist("{Notifications}")
                            .exist("[Battery Icon]")
                            .exist("[Battery]")
                            .exist("{Battery}")
                            .exist("[Storage Icon]")
                            .exist("[Storage]")
                            .exist("{Storage}")
                            .exist("[Sound & vibration Icon]")
                            .exist("[Sound & vibration]")
                            .exist("{Sound & vibration}")
                            .exist("[Display Icon]")
                            .exist("[Display]")
                            .exist("{Display}")
                            .exist("[Wallpaper & style Icon]")
                            .exist("[Wallpaper & style]")
                            .exist("{Wallpaper & style}")
                            .exist("[Accessibility Icon]")
                            .exist("[Accessibility]")
                            .exist("{Accessibility}")
                            .exist("[Security & privacy Icon]")
                            .exist("[Security & privacy]")
                            .exist("{Security & privacy}")
                            .exist("[Location Icon]")
                            .exist("[Location]")
                            .exist("{Location}")
                            .exist("[Safety & emergency Icon]")
                            .exist("[Safety & emergency]")
                            .exist("{Safety & emergency}")
                            .exist("[Passwords & accounts Icon]")
                            .exist("[Passwords & accounts]")
                            .exist("{Passwords & accounts}")
                            .exist("[Google Icon]")
                            .exist("[Google]")
                            .exist("{Google}")
                            .exist("[System Icon]")
                            .exist("[System]")
                            .exist("{System}")
                            .emulator {
                                it.exist("[About emulated device Icon]")
                                    .exist("[About emulated device]")
                                    .exist("{About emulated device}")
                            }
                            .realDevice {
                                it.exist("[About phone Icon]")
                                    .exist("[About phone]")
                                    .exist("{About phone}")
                            }
                            .exist("[Tips & support Icon]")
                            .exist("[Tips & support]")
                            .exist("{Tips & support}")
                    }
                }
            }
            case(2) {
                expectation {
                    it.dontExist("aaa")
                    it.dontExist("aaa", waitSeconds = 1.0)
                    it.dontExist("[Search Button]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existAllWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")

                }.action {

                }.expectation {
                    it.existAllWithScrollDown(
                        "[Search Button]",
                        "[Search settings]",
                        "[Network & internet Icon]",
                        "[Network & internet]",
                        "{Network & internet}",
                        "[Connected devices Icon]",
                        "[Connected devices]",
                        "{Connected devices}",
                        "[Apps Icon]",
                        "[Apps]",
                        "{Apps}",
                        "[Notifications Icon]",
                        "[Notifications]",
                        "{Notifications}",
                        "[Battery Icon]",
                        "[Battery]",
                        "{Battery}",
                        "[Storage Icon]",
                        "[Storage]",
                        "{Storage}",
                        "[Sound & vibration Icon]",
                        "[Sound & vibration]",
                        "{Sound & vibration}",
                        "[Display Icon]",
                        "[Display]",
                        "{Display}",
                        "[Wallpaper & style Icon]",
                        "[Wallpaper & style]",
                        "{Wallpaper & style}",
                        "[Accessibility Icon]",
                        "[Accessibility]",
                        "{Accessibility}",
                        "[Security & privacy Icon]",
                        "[Security & privacy]",
                        "{Security & privacy}",
                        "[Location Icon]",
                        "[Location]",
                        "{Location}",
                        "[Safety & emergency Icon]",
                        "[Safety & emergency]",
                        "{Safety & emergency}",
                        "[Passwords & accounts Icon]",
                        "[Passwords & accounts]",
                        "{Passwords & accounts}",
                        "[Google Icon]",
                        "[Google]",
                        "{Google}",
                        "[System Icon]",
                        "[System]",
                        "{System}",
                    ).emulator {
                        it.existAllWithScrollDown(
                            "[About emulated device Icon]",
                            "[About emulated device]",
                            "{About emulated device}"
                        )
                    }.realDevice {
                        it.existAllWithScrollDown(
                            "[About phone Icon]",
                            "[About phone]",
                            "{About phone}"
                        )
                    }.existAllWithScrollDown(
                        "[Tips & support Icon]",
                        "[Tips & support]",
                        "{Tips & support}"
                    )
                }
            }
        }

    }

    @Test
    @Order(30)
    fun existAllInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")

                }.action {

                }.expectation {
                    it.scanElements()
                        .existAllInScanResults(
                            "[Search Button]",
                            "[Search settings]",
                            "[Network & internet Icon]",
                            "[Network & internet]",
                            "{Network & internet}",
                            "[Connected devices Icon]",
                            "[Connected devices]",
                            "{Connected devices}",
                            "[Apps Icon]",
                            "[Apps]",
                            "{Apps}",
                            "[Notifications Icon]",
                            "[Notifications]",
                            "{Notifications}",
                            "[Battery Icon]",
                            "[Battery]",
                            "{Battery}",
                            "[Storage Icon]",
                            "[Storage]",
                            "{Storage}",
                            "[Sound & vibration Icon]",
                            "[Sound & vibration]",
                            "{Sound & vibration}",
                            "[Display Icon]",
                            "[Display]",
                            "{Display}",
                            "[Wallpaper & style Icon]",
                            "[Wallpaper & style]",
                            "{Wallpaper & style}",
                            "[Accessibility Icon]",
                            "[Accessibility]",
                            "{Accessibility}",
                            "[Security & privacy Icon]",
                            "[Security & privacy]",
                            "{Security & privacy}",
                            "[Location Icon]",
                            "[Location]",
                            "{Location}",
                            "[Safety & emergency Icon]",
                            "[Safety & emergency]",
                            "{Safety & emergency}",
                            "[Passwords & accounts Icon]",
                            "[Passwords & accounts]",
                            "{Passwords & accounts}",
                            "[Google Icon]",
                            "[Google]",
                            "{Google}",
                            "[System Icon]",
                            "[System]",
                            "{System}",
                        ).emulator {
                            it.existAllInScanResults(
                                "[About emulated device Icon]",
                                "[About emulated device]",
                                "{About emulated device}"
                            )
                        }.realDevice {
                            it.existAllInScanResults(
                                "[About phone Icon]",
                                "[About phone]",
                                "{About phone}"
                            )
                        }.existAllInScanResults(
                            "[Tips & support Icon]",
                            "[Tips & support]",
                            "{Tips & support}"
                        )
                }
            }
            case(2) {
                expectation {
                    it.dontExistAllInScanResult(
                        "aaa", "bbb", "ccc"
                    )
                }
            }
        }

    }

}