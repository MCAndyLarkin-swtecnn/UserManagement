package exceptions

import entities.ListingParams
import model.user.NewUser
import model.user.UpdateUser
import utils.RequestProcessingUtil

open class UserManagementException(message: String) : Exception(message)

class RequiredBodyMissedException(bodyDescription: String)
    : UserManagementException("The required '$bodyDescription' entity in body wasn't provided!")

class RequestFilterInvalidException(invalidParam: String, processingException: Exception)
    : UserManagementException("The passed filter params '$invalidParam' are invalid. " +
        "\nExample: '${ListingParams.filterBy}=<user_property>${RequestProcessingUtil.FILTER_DELIMITER}<your_value>'!" +
        "\n${processingException.message}")

class EmailAlreadyUsedException(email: String)
    : UserManagementException("The user fwith same email '$email' is already exists!")

class UserWithIdNotFoundException(id: Int)
    : UserManagementException("The user with id: $id doesn't exists!")

class UserAdditionInterruptedException(user: NewUser)
    : UserManagementException("User: $user addition was unexpectedly interrupted!")

class UserUpdatingInterruptedException(user: UpdateUser)
    : UserManagementException("User: $user updating was unexpectedly interrupted!")
