package shirates.core.utility

import java.time.Duration

val Duration.label: String
    get() {
        return "%02d:%02d:%02d.%03d".format(
            this.toHours(),
            this.toMinutesPart(),
            this.toSecondsPart(),
            this.toMillisPart()
        )
    }

val Duration.shortLabel: String
    get() {
        return "%02d:%02d:%02d".format(
            this.toHours(),
            this.toMinutesPart(),
            this.toSecondsPart()
        )
    }

val Duration.debugLabel: String
    get() {
        if (this.seconds > 60) {
            return shortLabel
        } else {
            return "${this.toSecondsPart()}.${this.toMillisPart()} sec"
        }
    }