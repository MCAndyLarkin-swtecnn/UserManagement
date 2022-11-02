package utils

import java.util.*

object OptionalUtils {
    fun <T> getOrNull(optional: Optional<T>) = if (optional.isPresent) optional.get() else null
}
