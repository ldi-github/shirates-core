package shirates.spec.report.entity

enum class Frame(val weight: Int) {

    SCENARIO(6),
    CASE(5),
    CONDITION(4),
    ACTION(3),

    //    TARGET(2),
    EXPECTATION(1),
}