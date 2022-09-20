package shirates.core.utility.misc

inline fun <reified T> Any?.toType(): T {

    if (this == null) {
        return null as T
    }

    val info = T::class
    if (info.simpleName == "String") {
        return this.toString() as T
    }

    return this as T
}
