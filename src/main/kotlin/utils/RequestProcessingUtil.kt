package utils

import exceptions.RequestFilterInvalidException
import exceptions.RequiredBodyMissedException
import java.util.*

object RequestProcessingUtil {
    const val FILTER_DELIMITER = "-"

    fun <T> checkRequiredBodyEntity(bodyNameEntity: Pair<String, Optional<T>>): T = if (bodyNameEntity.second.isPresent) {
        bodyNameEntity.second.get()
    } else {
        throw RequiredBodyMissedException(bodyNameEntity.first)
    }

    fun parseFilterPair(filterBy: String) = try {
        filterBy.split(FILTER_DELIMITER)
            .let { parts -> Pair(parts[0], parts[1]) }
    } catch (ex: Exception) {
        throw RequestFilterInvalidException(filterBy, ex)
    }
}
