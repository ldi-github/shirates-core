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

val Duration.secondLabel: String
    get() {
        return "%d.%03d sec".format(
            this.toSecondsPart(),
            this.toMillisPart()
        )
    }

val Duration.debugLabel: String
    get() {
        if (this.toMinutes() < 1) {
            return secondLabel  // 1.234 sec
        } else {
            return label    // 01:23:45.678
        }
    }