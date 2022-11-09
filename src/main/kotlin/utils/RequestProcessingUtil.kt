package utils

import exceptions.RequiredBodyMissedException
import java.util.*

object RequestProcessingUtil {
    fun <T> checkRequiredBodyEntity(bodyNameEntity: Pair<String, Optional<T>>): T = if (bodyNameEntity.second.isPresent) {
        bodyNameEntity.second.get()
    } else {
        throw RequiredBodyMissedException(bodyNameEntity.first)
    }
}
