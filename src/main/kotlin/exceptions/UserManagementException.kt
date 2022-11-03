package exceptions

import entities.ListingParams
import model.user.User
import utils.RequestProcessingUtil

open class UserManagementException(message: String) : Exception(message)

class PassedUserInvalidException(user: User) : UserManagementException("The passed user ($user) is invalid.")

class RequiredParameterMissedException(paramName: String)
    : UserManagementException("The required '$paramName' parameter wasn't passed!")

class RequiredBodyMissedException(bodyDescription: String)
    : UserManagementException("The required '$bodyDescription' entity in body wasn't provided!")

class RequestFilterInvalidException(invalidParam: String, processingException: Exception)
    : UserManagementException("The passed filter params '$invalidParam' are invalid. " +
        "\nExample: '${ListingParams.filterBy}=<user_property>${RequestProcessingUtil.FILTER_DELIMITER}<your_value>'!" +
        "\n${processingException.message}")
