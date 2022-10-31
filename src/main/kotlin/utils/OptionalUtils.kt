package utils

import java.util.*

object OptionalUtils {
    fun <T> getOrNull(optional: Optional<T>) = if (optional.isPresent) optional.get() else null
    fun getOrNull(optional: OptionalInt) = if (optional.isPresent) optional.asInt else null
}
