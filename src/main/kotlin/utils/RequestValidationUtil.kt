package utils

import exceptions.RequiredBodyMissed
import exceptions.RequiredParameterMissed
import java.util.*

object RequestValidationUtil {
    fun <T> checkRequiredParameter(parameterNameValue: Pair<String, Optional<T>>): T = if (parameterNameValue.second.isPresent) {
        parameterNameValue.second.get()
    } else {
        throw RequiredParameterMissed(parameterNameValue.first)
    }

    fun <T> checkRequiredBodyEntity(bodyNameEntity: Pair<String, Optional<T>>): T = if (bodyNameEntity.second.isPresent) {
        bodyNameEntity.second.get()
    } else {
        throw RequiredBodyMissed(bodyNameEntity.first)
    }
}