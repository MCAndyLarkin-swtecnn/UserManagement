package exceptions

import gateway.model.user.NewUser
import gateway.model.user.UpdateUser

open class UserManagementException(message: String) : Exception(message)

class EmailAlreadyUsedException(email: String)
    : UserManagementException("The user with same email '$email' is already exists!")

class UserWithIdNotFoundException(id: Int)
    : UserManagementException("The user with id: $id doesn't exists!")

class UserAdditionInterruptedException(user: NewUser)
    : UserManagementException("User: $user addition was unexpectedly interrupted!")

class UserUpdatingInterruptedException(user: UpdateUser)
    : UserManagementException("User: $user updating was unexpectedly interrupted!")
